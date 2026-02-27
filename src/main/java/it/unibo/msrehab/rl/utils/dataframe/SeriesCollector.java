package it.unibo.msrehab.rl.utils.dataframe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * Collector to collect a stream of objects into a Series
 *
 * @param <T>
 */
public class SeriesCollector<T> implements Collector<T, List<T>, ISeries<T>>
{
    @Override
    public Supplier<List<T>> supplier()
    {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator()
    {
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner()
    {
        return (l0, l1) ->
        {
            List<T> l = new ArrayList<>(l0);
            l.addAll(l1);
            return l;
        };
    }

    @Override
    public Function<List<T>, ISeries<T>> finisher()
    {
        return ISeries::fromValues;
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return new HashSet<>();
    }
}
