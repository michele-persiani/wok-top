package it.unibo.msrehab.rl.utils.rv;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class RandomVariable<T> implements IRandomVariable<T>
{

    public Collection<T> values()
    {
        return weights().keySet();
    }

    public List<T> sample(int n)
    {
        return IntStream.range(0, n)
                .mapToObj(x -> sample())
                .collect(Collectors.toList());
    }

    public T sample()
    {
        double p = Math.random();
        for(Map.Entry<T, Double> entry : pmf().entrySet())
        {
            p -= entry.getValue();
            if(p <= 0)
                return entry.getKey();
        }
        throw new IllegalStateException("Couldn't generate sample. Random variable size = " + weights().size());
    }

    public T mode()
    {
        Map<T, Double> pmf = pmf();
        return pmf.keySet()
                .stream()
                .max(Comparator.comparingDouble(pmf::get))
                .orElseThrow(() -> new IllegalStateException("Couldn't find mode. Random variable size = " + weights().size()))
                ;
    }

    public double cdf(Comparator<T> comparator, T value)
    {
        Map<T, Double> cdf = cdf(comparator);
        if(!cdf.containsKey(value))
            throw new IllegalArgumentException("Value not in distribution");
        return cdf.get(value);
    }

    public Map<T, Double> cdf(Comparator<T> comparator)
    {
        Map<T, Double> pmf = pmf();
        return pmf.keySet()
                .stream()
                .collect(Collectors.toMap(
                        v -> v,
                        v0 -> pmf.keySet()
                                .stream()
                                .filter(v1 -> comparator.compare(v0, v1) >= 0)
                                .map(pmf::get)
                                .mapToDouble(p -> p)
                                .sum()
                ))
                ;
    }

    public double pmf(T value)
    {
        Map<T, Double> pmf = pmf();
        if(!pmf.containsKey(value))
            throw new IllegalArgumentException("Value not in distribution");
        return pmf.get(value);
    }


    /**
     * Computes and returns a probability mass function (PMF) of the random variable.
     * The PMF represents the normalized weights of the values, ensuring the sum
     * of probabilities equals 1. The returned map contains each value as a key and
     * its corresponding probability as the value.
     *
     * @return a map where keys are the values of the random variable and values are their respective probabilities.
     */
    public Map<T, Double> pmf()
    {
        double sum = weights().values().stream().mapToDouble(Double::doubleValue).sum();
        Map<T, Double> weights = new HashMap<>(weights());
        weights.replaceAll((k, v) -> v / sum);
        return weights;
    }

    public abstract Map<T, Double> weights();
}
