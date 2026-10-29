package it.unibo.msrehab.util;

import flexjson.JSONSerializer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * Collector to gather data into a json object
 * {
 *     fields : [fields],
 *     displayNames : {display names for each field},
 *     data : [list of data]
 * }
 * @param <T>
 */
public class DataJsonCollector<T> implements Collector<T, List<Map<String, Object>>, String>
{
    public static final String FIELDS = "fields";
    public static final String DISPLAY_NAMES = "displayNames";
    public static final String DATA = "data";

    public static final Object NULL_VALUE = "null"; // Used to avoid null pointer exceptions

    private final Map<String, Function<T, Object>> suppliers = new LinkedHashMap<>();
    private final Map<String, String> displayNames = new LinkedHashMap<>();
    private final JSONSerializer serializer = new  JSONSerializer();


    public DataJsonCollector<T> addColumn(String columnName, Function<T, Object> supplier)
    {
        suppliers.put(columnName, supplier);
        return this;
    }

    public DataJsonCollector<T> setDisplayName(String columnName, String displayName)
    {
        displayNames.put(columnName, displayName);
        return this;
    }


    @Override
    public Supplier<List<Map<String, Object>>> supplier()
    {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Map<String, Object>>, T> accumulator()
    {
        return (accum, item) -> {
            Map<String, Object> map = suppliers.keySet()
                    .stream()
                    .collect(Collectors.toMap(
                            col -> col,
                            col -> {
                                Object obj = suppliers.get(col).apply(item);
                                return obj == null ? NULL_VALUE : obj.toString();
                            },
                            (i0, i1) -> i1,
                            LinkedHashMap::new
                    ));
            accum.add(map);
        };
    }

    @Override
    public BinaryOperator<List<Map<String, Object>>> combiner()
    {
        return (l0, l1) -> {
            l0.addAll(l1);
            return l0;
        };
    }


    @Override
    public Function<List<Map<String, Object>>, String> finisher()
    {
        return data -> {
            Map<String, String> displayNames = suppliers.keySet()
                    .stream()
                    .collect(Collectors.toMap(
                            col -> col,
                            col -> this.displayNames.getOrDefault(col, col)
                    ));

            Map<String, Object> result = new LinkedHashMap<>();
            result.put(FIELDS, suppliers.keySet());
            result.put(DISPLAY_NAMES, displayNames);
            result.put(DATA, data);
            return serializer.deepSerialize(result);
        };
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return Collections.emptySet();
    }
}
