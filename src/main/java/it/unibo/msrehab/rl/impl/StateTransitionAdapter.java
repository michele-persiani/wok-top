package it.unibo.msrehab.rl.impl;


import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.utils.dataframe.ISeries;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Adapter to transform a list of History objects into a list of StateTransition objects.
 */
public interface StateTransitionAdapter extends Function<List<History>, List<StateTransition<Integer, Integer>>>
{

    /**
     * Creates one transition per history, setting the from-state and to-state to the same level of the history. As such, all actions
     * are 0.
     *
     * @return An adapter converting lists of History objects into lists of StateTransition objects.
     */
    static StateTransitionAdapter onlyArrivalState(Function<History, Double> rewardFunction)
    {
        return episode ->
        {
            String episodeUUid = UUID.randomUUID().toString();
            return episode.stream()
                    .map(h -> new StateTransition.Builder<Integer, Integer>()
                            .setEnvironmentUid("exid-" + h.getExid())
                            .setEpisodeUid(episodeUUid)
                            .setFromState(h.getLevel())
                            .setToState(h.getLevel())
                            .setAction(0)
                            .setTimestampEpochMillis(h.getTimestamp())
                            .setReward(rewardFunction.apply(h))
                            .setDone(false)
                            .setReset(false)
                            .build()
                    )
                    .collect(Collectors.toList());
        };
    }


    /**
     * Creates a transition for each consecutive pair of histories, setting the from-state to the level of the first history
     * and to-state to the level of the second history. The action is the difference between the levels.
     *
     * @param rewardFunction function to determine the transition reward
     * @return An adapter converting lists of History objects into lists of StateTransition objects.
     */
    static StateTransitionAdapter pairwiseTransitions(BiFunction<History, History, Double> rewardFunction)
    {
        return episode ->
        {
            String episodeUUid = UUID.randomUUID().toString();
            return ISeries.fromValues(episode)
                    .slidingWindow(1, 2)
                    .map(pair ->
                    {
                        History h0 = pair.get(0);
                        History h1 = pair.get(1);
                        return new StateTransition.Builder<Integer, Integer>()
                                .setEnvironmentUid("exid-" + h0.getExid() + "-" + h1.getExid())
                                .setEpisodeUid(episodeUUid)
                                .setFromState(h0.getLevel())
                                .setToState(h1.getLevel())
                                .setAction(h1.getLevel() - h0.getLevel())
                                .setReward(rewardFunction.apply(h0, h1))
                                .setTimestampEpochMillis(h1.getTimestamp())
                                .setDone(false)
                                .setReset(false)
                                .build();
                    })
                    .toList()
                    ;
        };
    }

    /**
     * Adapter that ignores its input and supplies a fixed list of transitions
     *
     * @param transitions list of fixed supplied transitions
     * @return An adapter converting lists of History objects into lists of StateTransition objects.
     */
    static StateTransitionAdapter fixedTransitions(List<StateTransition<Integer, Integer>> transitions)
    {
        return hist -> transitions;
    }
}
