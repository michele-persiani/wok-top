package it.unibo.msrehab.rl.policy;

import java.util.Map;


public abstract class EGreedyPolicy<S, A> extends StochasticPolicy<S, A>
{
    public double epsilon;

    public EGreedyPolicy(double epsilon)
    {
        this.epsilon = epsilon;
    }

    /**
     * Set the epsilon value i.e. the percentage of actions to be explored randomly. Common value is 0.05
     *
     * @param epsilon epsilon value between 0 and 1, inclusive
     */
    public void setEpsilon(double epsilon)
    {
        this.epsilon = Math.max(0, Math.min(1, epsilon));
    }

    /**
     * Gets the epsilon value i.e. percentage of actions to be explored randomly
     *
     */
    public double getEpsilon()
    {
        return epsilon;
    }

    @Override
    protected void normalizeWeights(Map<A, Double> weights)
    {
        A max = weights.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if(weights.size() == 1)
        {
            weights.put(max, 1d);
            return;
        }

        double pMax = 1 - epsilon;
        double pOthers = epsilon / (weights.size() - 1);

        weights.replaceAll((a, p) -> a == max ? pMax : pOthers);
    }
}
