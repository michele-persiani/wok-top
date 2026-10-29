package it.unibo.msrehab.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CsvCollector<T> implements Collector<T, List<String>, String>
{
    private final Map<String, Function<T, String>> columnsMap = new LinkedHashMap<>();

    private String nullString = "";

    private String separator = ",";

    public CsvCollector<T> setSeparator(String separator)
    {
        this.separator = separator;
        return this;
    }

    /**
     * Sets the cell string for null values. Default is the empty string.
     *
     * @param nullString
     * @return
     */
    public CsvCollector<T> setNullString(String nullString)
    {
        this.nullString = nullString;
        return this;
    }

    public CsvCollector<T> addColumn(String columnName, Function<T, Object> columnValueExtractor)
    {
        columnsMap.put(columnName, item -> {
            Object value = columnValueExtractor.apply(item);
            return Objects.toString(value == null ? nullString : value);
        });
        return this;
    }

    @Override
    public Supplier<List<String>> supplier()
    {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<String>, T> accumulator()
    {
        return (accum, item) -> {
            String row = columnsMap.keySet()
                    .stream()
                    .map(col -> columnsMap.get(col).apply(item))
                    .collect(Collectors.joining(separator));
            accum.add(row);
        };
    }

    @Override
    public BinaryOperator<List<String>> combiner()
    {
        return (l0, l1) -> {
            l0.addAll(l1);
            return l0;
        };
    }

    @Override
    public Function<List<String>, String> finisher()
    {
        return accum -> {
            String header = String.join(separator, columnsMap.keySet());
            accum.add(0, header);
            return String.join("\n", accum);
        };
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return Collections.emptySet();
    }
}
