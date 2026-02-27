package it.unibo.msrehab.rl.policy;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;


/**
 * Policy that selects actions based on their probability values
 * @param <S>
 * @param <A>
 */
public abstract class StochasticPolicy<S, A> implements IActionPolicy<S, A>
{
    private final Random random = new Random();

    @Override
    public A selectAction(S state)
    {
        Map<A, Double> proba = getActionProbabilities(state);

        double v = random.nextDouble();
        double cum = 0;
        for(Map.Entry<A, Double> a : proba.entrySet())
        {
            cum += a.getValue();
            if(cum >= v)
                return a.getKey();
        }
        throw new IllegalStateException("At least one action should have a non-zero probability, but none was found.");
    }


    public Map<A, Double> getActionProbabilities(S state)
    {
        Map<A, Double> proba = getActionsWeights(state);

        if(proba.isEmpty())
            return proba;

        normalizeWeights(proba);

        if(proba.values().stream().reduce(0.0, Double::sum) != 1)
            throw new IllegalStateException("Sum of probabilities should be 1");

        return proba;
    }


    /**
     * Normalize weights into probabilities
     * @param weights weights associated to the actions
     */
    protected abstract void normalizeWeights(Map<A, Double> weights);


    /**
     * Gets a dictionary of actions and associated weight
     * @param state current state
     * @return dictionary of actions and associated weights
     */
    protected abstract Map<A, Double> getActionsWeights(S state);
}
