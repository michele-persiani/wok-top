package it.unibo.msrehab.rl.rewardmodel;

import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.utils.regression.IRegressor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.DoubleStream;


/**
 * Class to implement a reward model storing (from-state, action, to-state) -> (reward) mappings.
 *
 * @param <S> class type for states
 * @param <A> class type for actions
 */
public abstract class RewardModel<S, A>
{
    /**
     * Predicts the reward of a state transition.
     *
     * @param fromState current state
     * @param action    action taken
     * @param toState   next state
     * @return reward of the transition
     */
    public abstract double getReward(S fromState, A action, S toState);

    /**
     * Resets the reward model.
     */
    public abstract void reset();


    /**
     * Incrementally fit the reward model with a list of state transitions. The previous internal state is to be
     * retained. Call reset() before fit() if the internal state should be discarded before fitting
     *
     * @param transitions
     */
    public abstract void fit(List<StateTransition<S, A>> transitions);

    /**
     * Utility method to predict the reward of a state transition.
     * The reward is computed by calling getReward(fromState, action, toState).
     *
     * @param transition
     * @return
     */
    public final double getReward(StateTransition<S, A> transition)
    {
        return getReward(transition.getFromState(), transition.getAction(), transition.getToState());
    }


    /**
     * Returns a new reward model applying a function to the rewards.
     *
     * @param rewardWrapper function to apply to the rewards of the original reward model.
     * @return reward model instance
     */
    public final RewardModel<S, A> applyToRewards(Function<Double, Double> rewardWrapper)
    {
        RewardModel<S, A> instance = this;

        return new RewardModel<S, A>()
        {
            @Override
            public double getReward(S fromState, A action, S toState)
            {
                return rewardWrapper.apply(instance.getReward(fromState, action, toState));
            }

            @Override
            public void reset()
            {
                instance.reset();
            }

            @Override
            public void fit(List<StateTransition<S, A>> transitions)
            {
                instance.fit(transitions);
            }
        };
    }


    /**
     * Returns a new reward model clamping the rewards between min and max.
     *
     * @param min min reward value
     * @param max max reward value
     * @return a new reward model instance, wrapping the current one
     */
    public final RewardModel<S, A> clampReward(double min, double max)
    {
        return applyToRewards(r -> Math.min(max, Math.max(min, r)));
    }


    /**
     * Returns a new reward model filtering the transitions before fitting.
     *
     * @param filter filter to apply to state transitions before fitting
     * @return a new reward model instance, wrapping the current one
     */
    public final RewardModel<S, A> setFitFilter(Function<List<StateTransition<S, A>>, List<StateTransition<S, A>>> filter)
    {
        RewardModel<S, A> instance = this;
        return new RewardModel<S, A>()
        {
            @Override
            public double getReward(S fromState, A action, S toState)
            {
                return instance.getReward(fromState, action, toState);
            }

            @Override
            public void reset()
            {
                instance.reset();
            }

            @Override
            public void fit(List<StateTransition<S, A>> transitions)
            {
                instance.fit(filter.apply(transitions));
            }
        };
    }









    // Factory methods


    public static <S, A> RewardModel<S, A> createLambdaRewardModel(TransitionFeatureFunction<S, A> defaultRewardModel)
    {
        return new LambdaRewardModel<>(defaultRewardModel);
    }

    /**
     * Creates a reward model that assigns a constant reward value to all transitions,
     * regardless of the states or actions involved. This model always returns the
     * specified reward value for any given transition.
     *
     * @param <S>    the type representing states.
     * @param <A>    the type representing actions.
     * @param reward the fixed reward value to be assigned for all transitions.
     * @return a constant reward model that provides the specified reward for all transitions.
     */
    public static <S, A> RewardModel<S, A> createConstantRewardModel(double reward)
    {
        return new LambdaRewardModel<>((from, action, to) -> reward);
    }


    /**
     * Creates a reward model that assigns rewards to transitions based on the current state state, action taken, and next state.
     * The reward is computed by encoding the from and to states and the action taken into keys, and storing
     * keys-rewards pairs in a table.
     *
     * @param priorWeight        weight deciding the strength of the prior when computing the average reward for a transition.
     *                           The prior is the mean of ALL seen rewards
     * @param fromKeyExtractor   function to extract the key identifying the current state
     * @param actionKeyExtractor function to extract the key identifying an action
     * @param toKeyExtractor     function to extract the key identifying the next state
     * @param <S>                class of states
     * @param <A>                class of actions
     * @return reward model that assigns rewards to transitions based on the current state, action taken, and next state.
     */
    public static <S, A> RewardModel<S, A> createTabularRewardModel(
            double priorWeight,
            Function<S, Object> fromKeyExtractor,
            Function<A, Object> actionKeyExtractor,
            Function<S, Object> toKeyExtractor
    )
    {
        return new RewardModel<S, A>()
        {
            private final Map<Object, List<Double>> rewardModel = new HashMap<>();

            private final Object nullKey = "null";

            private Double averageReward = null;

            private Object getTransitionKey(S fromState, A action, S toState)
            {
                Object fromKey = fromState == null ? nullKey : fromKeyExtractor.apply(fromState);
                Object toKey = toState == null ? nullKey : toKeyExtractor.apply(toState);
                Object actionKey = action == null ? nullKey : actionKeyExtractor.apply(action);
                return Arrays.asList(fromKey, toKey, actionKey);
            }

            @Override
            public double getReward(S fromState, A action, S toState)
            {
                Object key = getTransitionKey(fromState, action, toState);
                List<Double> rewards = rewardModel.getOrDefault(key, new ArrayList<>());

                averageReward = Optional.ofNullable(averageReward).orElseGet(this::computePriorReward);

                double w = priorWeight / (rewards.size() + 1);
                double rewardsSum = rewards.stream().mapToDouble(Double::doubleValue).sum();

                return (w * averageReward + rewardsSum) / Math.max(1e-12, w + rewards.size());
            }


            public void fit(S fromState, A action, S toState, double reward)
            {
                Object key = getTransitionKey(fromState, action, toState);
                rewardModel.computeIfAbsent(key, k -> new ArrayList<>());
                rewardModel.get(key).add(reward);
                averageReward = null;
            }

            @Override
            public void reset()
            {
                rewardModel.clear();
                averageReward = null;
            }

            @Override
            public void fit(List<StateTransition<S, A>> stateTransitions)
            {
                stateTransitions.forEach(st -> fit(st.getFromState(), st.getAction(), st.getToState(), st.getReward()));
            }

            private double computePriorReward()
            {
                return rewardModel.values().stream()
                        .map(r -> r.stream()
                                .filter(v -> v != Double.NEGATIVE_INFINITY && v != Double.POSITIVE_INFINITY)
                                .mapToDouble(Double::doubleValue)
                        )
                        .flatMap(DoubleStream::boxed)
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(0.0);
            }
        };
    }


    /**
     * Creates a reward model that assigns rewards to transitions based on a regression of the state transition features.
     *
     * @param featureFunction         function computing the feature of a state transition.
     * @param regressor regressor that from the transition feature predicts the transition reward
     * @param <S>
     * @param <A>
     * @return
     */
    public static <S, A> RewardModel<S, A> createRegressionRewardModel(
            TransitionFeatureFunction<S, A> featureFunction,
            IRegressor regressor
    )
    {
        return new RewardModel<S, A>()
        {

            @Override
            public double getReward(S fromState, A action, S toState)
            {
                return regressor.predict(
                        featureFunction.getFeatureValue(fromState, action, toState)
                );
            }
            @Override
            public void reset()
            {
                regressor.reset();
            }

            @Override
            public void fit(List<StateTransition<S, A>> stateTransitions)
            {
                double[] x = stateTransitions.stream()
                        .mapToDouble(st -> featureFunction.getFeatureValue(st.getFromState(), st.getAction(), st.getToState()))
                        .toArray();
                double[] y = stateTransitions.stream()
                        .mapToDouble(StateTransition::getReward)
                        .toArray();
                if (x.length > 0 && y.length > 0)
                    regressor.fit(x, y);
            }
        };
    }

    /**
     * Creates a reward model that computes the weighted average of other reward models.
     *
     * @param rewardModelsWeights averaged reward model and their corresponding weights
     * @param <S> class type of states
     * @param <A> class type of actions
     * @return reward model instance
     */
    public static <S, A> RewardModel<S, A> createMixtureRewardModel(Map<RewardModel<S, A>, Double> rewardModelsWeights)
    {
        return new RewardModel<S, A>()
        {
            @Override
            public double getReward(S fromState, A action, S toState)
            {
                double weightsSum = rewardModelsWeights.values().stream().mapToDouble(Double::doubleValue).sum();
                return rewardModelsWeights.keySet()
                        .stream()
                        .map(m -> rewardModelsWeights.get(m) / weightsSum * m.getReward(fromState, action, toState))
                        .reduce(0d, Double::sum)
                        ;
            }

            @Override
            public void reset()
            {
                rewardModelsWeights.forEach((k, v) -> k.reset());
            }

            @Override
            public void fit(List<StateTransition<S, A>> stateTransitions)
            {
                rewardModelsWeights.forEach((k, v) -> k.fit(stateTransitions));
            }
        };
    }

}
