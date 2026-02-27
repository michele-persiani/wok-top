package it.unibo.msrehab.rl.impl;

import it.unibo.msrehab.rl.common.StateTransition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Adaptive threshold agent (agente a soglie)
 */
public class ThresholdAgent implements NextLevelAgent
{
    private class State
    {
        private int currentLevel;
        private double currentThreshold;

        State(int currentLevel, double startThreshold)
        {
            this.currentLevel = currentLevel;
            this.currentThreshold = startThreshold;
        }

        public void onSuccessTransition()
        {
            currentThreshold += deltaThresholdPassed;
            currentThreshold = Math.min(currentThreshold, 1);
            currentLevel = Math.min(currentLevel + 1, maxLevel);
        }

        public void onFailTransition()
        {
            currentThreshold += deltaThresholdNotPassed;
            if(currentThreshold < lowerThreshold)
            {
                currentLevel = Math.max(currentLevel - 1, minLevel);
                currentThreshold = startThreshold;
            }
        }

        public int getCurrentLevel()
        {
            return currentLevel;
        }

        public double getCurrentThreshold()
        {
            return currentThreshold;
        }
    }

    private final double lowerThreshold;
    private final double startThreshold;
    private final double deltaThresholdPassed;
    private final double deltaThresholdNotPassed;
    private final int minLevel;
    private final int maxLevel;
    private List<StateTransition<Integer, Integer>> history = new ArrayList<>();


    public ThresholdAgent(double lowerThreshold, double startThreshold, double thresholdIncrement, double deltaThresholdNotPassed, int minLevel, int maxLevel)
    {
        this.lowerThreshold = lowerThreshold;
        this.startThreshold = startThreshold;
        this.deltaThresholdPassed = thresholdIncrement;
        this.deltaThresholdNotPassed = deltaThresholdNotPassed;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }


    @Override
    public Integer selectAction(Integer currentLevel) // , Double currentThreshold, double performance, boolean exerciseIsPassed
    {
        State state = new State(
                history.stream()
                        .min(Comparator.comparing(StateTransition::getTimestampEpochMillis))
                        .map(StateTransition::getFromState)
                        .orElse(minLevel)
                ,
                startThreshold
        );
        /*
        if(Math.round(performance * 1e3) >= Math.round(state.getCurrentThreshold() * 1e3))
            state.onSuccessTransition();
        else
            state.onFailTransition();
            */





        List<Double> thresholds = history.stream()
                .sorted(Comparator.comparing(StateTransition::getTimestampEpochMillis))
                .map(h -> {
                    if(Math.round(h.getReward() * 1e3) >= Math.round(state.getCurrentThreshold() * 1e3))
                        state.onSuccessTransition();
                    else
                        state.onFailTransition();
                    return state.getCurrentThreshold();
                })
                .collect(Collectors.toList());
        ValueIterationValues.setValues(thresholds);
        return state.getCurrentLevel() - currentLevel;
    }

    @Override
    public void fit(List<StateTransition<Integer, Integer>> history)
    {
        this.history = history;
    }
}
