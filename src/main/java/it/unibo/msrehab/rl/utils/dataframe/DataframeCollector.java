package it.unibo.msrehab.rl.utils.dataframe;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


public class DataframeCollector<T> implements Collector<T, List<T>, Dataframe>
{
    private final Function<T, Map<String, Object>> rowFunction;

    public DataframeCollector(Function<T, Map<String, Object>> rowFunction)
    {
        this.rowFunction = rowFunction;
    }

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
    public Function<List<T>, Dataframe> finisher()
    {
        return data ->
        {
            Dataframe df = new Dataframe();
            data
                    .stream()
                    .map(rowFunction)
                    .forEach(df::appendRow);
            return df;
        };
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return new HashSet<>();
    }
}
