package it.unibo.msrehab.rl.policy;

import it.unibo.msrehab.rl.utils.rv.RandomVariableFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ActionPolicyFactory
{
    private ActionPolicyFactory() {}

    public static <S, A> IActionPolicy<S, A> createStochasticPolicy(Iterator<A> actions)
    {
        return createStochasticPolicy(
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(actions, 0), false)
                        .collect(Collectors.toList())
        );
    }

    public static <S, A> IActionPolicy<S, A> createStochasticPolicy(Collection<A> actions)
    {
        return createStochasticPolicy(actions.stream().collect(Collectors.toMap(a -> a, a -> 1d)));
    }

    public static <S, A> IActionPolicy<S, A> createStochasticPolicy(Map<A, Double> actions)
    {
        return createStochasticPolicy(s -> actions);
    }

    public static <S, A> IActionPolicy<S, A> createStochasticPolicy(Function<S, Map<A, Double>> actions)
    {
        return new LinearRandomPolicy<S, A>()
        {
            @Override
            protected Map<A, Double> getActionsWeights(S state)
            {
                return actions.apply(state);
            }
        };
    }

    public static <S, A> SoftmaxPolicy<S, A> createSoftmaxPolicy(double temperature, Function<S, Map<A, Double>> actions)
    {
        return new SoftmaxPolicy<S, A>(temperature)
        {
            @Override
            protected Map<A, Double> getActionsWeights(S state)
            {
                return actions.apply(state);
            }
        };
    }

    public static <S, A> EGreedyPolicy<S, A> createEpsilonGreedyPolicy(double epsilon, Function<S, Map<A, Double>> actions)
    {
        return new EGreedyPolicy<S, A>(epsilon)
        {
            @Override
            protected Map<A, Double> getActionsWeights(S state)
            {
                return actions.apply(state);
            }
        };
    }

    public static <S, A> ActionLoopPolicy<S, A> createActionLoopPolicy(List<A> actions)
    {
        return new ActionLoopPolicy<>(actions);
    }

    public static <S, A> IActionPolicy<S, A> createFixedPolicy(A action)
    {
        return s -> action;
    }

    public static <S, A> IActionPolicy<S, A> createMixturePolicy(Map<IActionPolicy<S, A>, Double> policiesWeights)
    {
        return s -> RandomVariableFactory.from(policiesWeights).sample().selectAction(s);
    }
}
