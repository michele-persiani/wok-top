package it.unibo.msrehab.rl.policy;

import java.util.*;


public abstract class LinearRandomPolicy<S, A> extends StochasticPolicy<S, A>
{
    @Override
    protected void normalizeWeights(Map<A, Double> weights)
    {
        if(weights.isEmpty())
            return;
        double sum = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        weights.replaceAll((a, b) -> b / sum);
    }
}
