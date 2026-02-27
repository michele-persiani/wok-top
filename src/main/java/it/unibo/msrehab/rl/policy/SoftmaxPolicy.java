package it.unibo.msrehab.rl.policy;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;


/**
 * Policy assigning probabilities to actions based on the softmax function: P proportional to Exp(Wa / temp)
 * @param <S>
 * @param <A>
 */
public abstract class SoftmaxPolicy<S, A> extends StochasticPolicy<S, A>
{
    private double temperature;


    public SoftmaxPolicy(double tau)
    {
        this.temperature = tau;
    }


    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }


    public double getTemperature()
    {
        return temperature;
    }


    @Override
    protected void normalizeWeights(Map<A, Double> weights)
    {
        weights.forEach((a, b) -> weights.put(a, Math.exp(b / temperature)));
        double sum = weights.values().stream().reduce(0d, Double::sum);
        weights.replaceAll((a, b) -> b / sum);
    }
}
