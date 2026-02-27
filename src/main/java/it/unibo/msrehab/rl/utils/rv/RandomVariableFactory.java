package it.unibo.msrehab.rl.utils.rv;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class RandomVariableFactory
{
    private RandomVariableFactory()
    {
    }

    public static <T> IRandomVariable<T> singleton(T value)
    {
        return from(Collections.singletonMap(value, 1d));
    }



    public static <T> IRandomVariable<T> from(Map<T, Double> weights)
    {
        return new RandomVariable<T>()
        {
            @Override
            public Map<T, Double> weights()
            {
                return weights;
            }
        };
    }


    public static <T> IRandomVariable<T> from(Collection<T> values)
    {
        return new RandomVariable<T>()
        {
            @Override
            public Map<T, Double> weights()
            {
                return values.stream().collect(Collectors.toMap(k -> k, v -> 1d / values.size(), Double::sum));
            }
        };
    }

    public static IRandomVariable<Double> fromRange(double minValue, double maxValue, int steps, Function<Double, Double> weightFunction)
    {
        if(minValue > maxValue)
            throw new IllegalArgumentException("minXValue must be lesser or equal than maxXValue");
        if(steps <= 0)
            throw new IllegalArgumentException("steps must be greater than 0");
        return from(
                IntStream.range(0, steps)
                .mapToDouble(i -> minValue + i * (maxValue - minValue) / steps)
                .boxed()
                .collect(Collectors.toMap(x -> x, weightFunction))
        );
    }

    public static IRandomVariable<Double> uniform(double min, double max, int steps)
    {
        return fromRange(min, max, steps, x -> 1d / steps);
    }

    public static IRandomVariable<Boolean> bernoulli(double p)
    {
        if(p < 0 || p > 1)
            throw new IllegalArgumentException("Invalid supplied probability");
        return from(Stream.of(true, false)
                .collect(Collectors.toMap(Function.identity(), v -> v ? p : 1 - p))
        );
    }

    /**
     * Bernoulli random variable with probability calculated as p = 1 / (1 + exp(a * (x - b)))
     *
     * @param a
     * @param b
     * @param x
     * @return
     */
    public static IRandomVariable<Boolean> sigmoidBernoulli(double a, double b, double x)
    {
        return bernoulli(1d / (1d + Math.exp(a * (x - b))));
    }


    public static <T, V> IRandomVariable<V> map(IRandomVariable<T> rv, Function<T, V> mapper)
    {
        return new IRandomVariable<V>()
        {
            @Override
            public Collection<V> values()
            {
                return rv.values().stream().map(mapper).collect(Collectors.toList());
            }

            @Override
            public List<V> sample(int n)
            {
                return rv.sample(n).stream().map(mapper).collect(Collectors.toList());
            }

            @Override
            public V sample()
            {
                return mapper.apply(rv.sample());
            }

            @Override
            public V mode()
            {
                return mapper.apply(rv.mode());
            }

            @Override
            public double cdf(Comparator<V> comparator, V value)
            {
                return Double.NaN;
            }

            @Override
            public Map<V, Double> cdf(Comparator<V> comparator)
            {
                return Collections.emptyMap();
            }

            @Override
            public double pmf(V value)
            {
                return Double.NaN;
            }

            @Override
            public Map<V, Double> pmf()
            {
                return Collections.emptyMap();
            }
        };
    }
}
