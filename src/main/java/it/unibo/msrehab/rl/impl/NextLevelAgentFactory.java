package it.unibo.msrehab.rl.impl;



import it.unibo.msrehab.rl.agents.ValueIterationAgent;
import it.unibo.msrehab.rl.rewardmodel.RewardModel;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.mdp.ICollectionAdapter;
import it.unibo.msrehab.rl.mdp.MDPAdapter;
import it.unibo.msrehab.rl.policy.ActionPolicyFactory;
import it.unibo.msrehab.rl.utils.dataframe.ISeries;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NextLevelAgentFactory
{
    private NextLevelAgentFactory()
    {
    }

    /**
     * Agent that always supplies the same level as
     * @return
     */
    public static NextLevelAgent sameLevel()
    {
        return new NextLevelAgent()
        {
            @Override
            public Integer selectAction(Integer currentLevel) { return 0; }

            @Override
            public void fit(List<StateTransition<Integer, Integer>> history) {}
        };
    }

    /**
     * <p>Agent that computes the next level using the incremental strategy. i.e., advances of level only if the last two
     * levels are of the same level as the one passed to selectAction(), and are both passed=true.</p>
     * <p>Considers only arrival states for transitions passed through fit()</p>
     *
     *
     * @param passThreshold mininimum transition reward to consider it a pass
     * @return
     */
    public static NextLevelAgent incrementalAgent(double passThreshold)
    {
        return new NextLevelAgent()
        {

            private List<StateTransition<Integer, Integer>> history = new ArrayList<>();


            @Override
            public Integer selectAction(Integer currentLevel)
            {
                boolean canPass = history.size() >= 2 &&
                        ISeries.fromValues(history)
                                .sortBy(Comparator.comparing(StateTransition::getTimestampEpochMillis))
                                .tail(2)
                                .stream()
                                .allMatch(t -> Objects.equals(t.getToState(), currentLevel) && t.getReward() >= passThreshold);

                return canPass ? 1 : 0;
            }

            @Override
            public void fit(List<StateTransition<Integer, Integer>> history)
            {
                this.history = history;
            }
        };
    }

    public static NextLevelAgent thresholdAgent(double lowerThreshold, double startThreshold, double deltaThresholdPassed, double deltaThresholdNotPassed, int minLevel, int maxLevel)
    {
        return new ThresholdAgent(lowerThreshold, startThreshold, deltaThresholdPassed, deltaThresholdNotPassed, minLevel, maxLevel);
    }

    /**
     * <p>Agent that uses value iteration to compute its actions.</p>
     * <p>Considers only arrival states for transitions passed through fit()</p>
     * @param minLevel min level
     * @param maxLevel
     * @param rewardModel rewardModel to use in value iteration
     * @return
     */
    public static NextLevelAgent valueIterationAgent(
            int minLevel,
            int maxLevel,
            double discountFactor,
            double epsilon,
            RewardModel<Integer, Integer> rewardModel
    )
    {
        List<Integer> levels = IntStream.range(minLevel, maxLevel + 1).boxed().collect(Collectors.toList());
        List<Integer> actions = Arrays.asList(-1, 0, 1);

        MDPAdapter<Integer, Integer> mdp = new MDPAdapter<>(
                ICollectionAdapter.fromList(levels),
                ICollectionAdapter.fromList(actions),
                (level, action) -> {
                    Map<Integer, Double> proba = new HashMap<>();
                    int nextLevel = level + action;
                    if(nextLevel >= minLevel && nextLevel <= maxLevel)
                        proba.put(nextLevel, 1d);
                    return proba;
                }
        );

        if(!MDPAdapter.isConsistent(mdp))
            throw new IllegalArgumentException("MDP is not consistent");


        ValueIterationAgent<Integer, Integer> viAgent = new ValueIterationAgent<>(
                rewardModel,
                mdp,
                discountFactor,
                0.0005,
                300,
                vi -> ActionPolicyFactory.createEpsilonGreedyPolicy(epsilon, vi::getActionValues)
        );

        return new NextLevelAgent()
        {
            @Override
            public void fit(List<StateTransition<Integer, Integer>> history)
            {
                boolean consistent = history.stream()
                        .allMatch(
                                st -> levels.contains(st.getFromState()) &&
                                        actions.contains(st.getAction()) &&
                                        levels.contains(st.getToState())
                        );
                if(!consistent)
                    throw new IllegalStateException("Some state or action from the history is not defined in the MDP");
                viAgent.fit(history);
                storeStateValues();
            }

            private void storeStateValues()
            {
                Map<Integer, Double> stateValues = viAgent.getStateValues();
                ValueIterationValues.setValues(
                        stateValues.keySet()
                                .stream()
                                .sorted()
                                .map(stateValues::get)
                                .collect(Collectors.toList())
                );
            }

            @Override
            public Integer selectAction(Integer currentLevel)
            {
                if(currentLevel == null || currentLevel < minLevel || currentLevel > maxLevel)
                    throw new IllegalArgumentException("Current level is not in the defined range");
                return viAgent.selectAction(currentLevel);
            }

            @Override
            public Integer getNextLevel(Integer currentLevel)
            {
                Integer original = NextLevelAgent.super.getNextLevel(currentLevel);
                return Math.max(minLevel, Math.min(maxLevel, original));
            }
        };
    }


    /**
     * <p>
     * Agent wrapper that will change the agent behavior if the fitted history doesn't contain enough data.
     * In particular, if for each level in 'targetLevels' at least 'n' records are not present in the history, then the
     * agent will supply those levels to get sufficient data.
     * </p>
     * <p>
     *     Once enough data is present the {@code original} agent is used
     * </p>
     *
     * @param original original agent of which the behavior is changed
     * @param targetLevels levels for which there must be at least 'n' records in the history
     * @param n number of attemps for each level in targetLevels
     * @return factory method for creating agents
     */
    public static NextLevelAgent requireEnoughDataAgentWrapper(NextLevelAgent original, Collection<Integer> targetLevels, int n)
    {
        final Map<Integer, Integer> performed = new HashMap<>();

        return new NextLevelAgent()
        {
            private final List<StateTransition<Integer, Integer>> history = new ArrayList<>();

            @Override
            public Integer selectAction(Integer currentLevel)
            {
                return targetLevels.stream()
                        .filter(lvl -> performed.getOrDefault(lvl, 0) < n)
                        .min(Integer::compareTo)
                        .map(lvl -> lvl - currentLevel)
                        .orElseGet(
                                () -> new TemplateNextLevelAgent()
                                        .setAgent(original)
                                        .setFitOnActionSelection(true)
                                        .setTransitionsHistory(new ArrayList<>(history))
                                        .selectAction(currentLevel)
                        );
            }

            @Override
            public void fit(List<StateTransition<Integer, Integer>> history)
            {
                performed.clear();
                history.stream()
                        .filter(h -> h.getToState() != null)
                        .forEach(h -> performed.merge(h.getToState(), 1, Integer::sum));
                this.history.clear();
                this.history.addAll(history);
            }
        };
    }

}
