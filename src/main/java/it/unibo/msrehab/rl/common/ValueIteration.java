
package it.unibo.msrehab.rl.common;

import it.unibo.msrehab.rl.mdp.MDPAdapter;
import it.unibo.msrehab.rl.rewardmodel.RewardModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueIteration<S, A>
{

    private final RewardModel<S, A> rewardModel;
    private final MDPAdapter<S, A> mdpAdapter;
    private final Map<S, Double> stateValues;
    private final double discountFactor;
    private final double tolerance;
    private final int maxIterations;

    public ValueIteration(
            RewardModel<S, A> rewardModel,
            MDPAdapter<S, A> mdpAdapter,
            double discountFactor,
            double tolerance,
            int maxIterations
    ) {
        this.mdpAdapter = mdpAdapter;
        this.rewardModel = rewardModel;
        this.stateValues = new HashMap<>();
        this.discountFactor = discountFactor;
        this.tolerance = tolerance;
        this.maxIterations = maxIterations;
    }

    public Map<S, Double> getStateValues()
    {
        return Collections.unmodifiableMap(stateValues);
    }

    public Map<A, Double> getActionValues(S state)
    {
        Map<A, Double> actionValues = new HashMap<>();
        for (int a = 0; a < mdpAdapter.getActionAdapter().countElements(); a++)
        {
            A action = mdpAdapter.getActionAdapter().inverseApply(a);
            double actionValue = computeActionValue(state, action);
            if(actionValue > Double.NEGATIVE_INFINITY)
                actionValues.put(action, actionValue);
        }
        return actionValues;
    }


    public void update(List<StateTransition<S, A>> history)
    {
        // Aggiorna il modello delle ricompense
        rewardModel.fit(history);

        // Esegui Value Iteration
        performValueIteration();
    }


    public void reset()
    {
        rewardModel.reset();
        stateValues.clear();
    }


    private void performValueIteration()
    {
        int iteration = 0;
        double delta;

        do {
            delta = 0.0;
            Map<S, Double> newValues = new HashMap<>();

            // Itera su tutti gli stati possibili
            for (S state : mdpAdapter.getStates())
            {
                double v = stateValues.getOrDefault(state, 0.0);
                double maxActionValue = Double.NEGATIVE_INFINITY;


                // Trova il valore massimo tra tutte le azioni
                for (A action : mdpAdapter.getActions(state))
                {
                    double actionValue = computeActionValue(state, action);
                    maxActionValue = Math.max(maxActionValue, actionValue);
                }

                newValues.put(state, maxActionValue);
                delta = Math.max(delta, Math.abs(v - maxActionValue));
            }

            stateValues.clear();
            stateValues.putAll(newValues);
            iteration++;

        } while (delta > tolerance && iteration < maxIterations);
    }

    private double computeActionValue(S state, A action)
    {
        Map<S, Double> transitionProbs = mdpAdapter.getTransitionProbabilities(state, action);
        if(transitionProbs.isEmpty())
            return Double.NEGATIVE_INFINITY;

        double actionValue = 0.0;

        for (S nextState : transitionProbs.keySet())
        {
            double prob = transitionProbs.get(nextState);
            double reward = rewardModel.getReward(state, action, nextState);

            actionValue += prob * (reward + discountFactor * stateValues.getOrDefault(nextState, 0.0));
        }

        return actionValue;
    }
}