package it.unibo.msrehab.rl.utils.dataframe;

import flexjson.JSONSerializer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Dataframe
{
    public static class Builder<T>
    {
        private final List<Function<T, Map<String, Object>>> valueGetters = new ArrayList<>();

        private final List<T> data = new ArrayList<>();

        public Builder<T> setData(Iterable<T> data)
        {
            this.data.clear();
            data.forEach(this.data::add);
            return this;
        }

        public Builder<T> addColumn(String column, Function<T, Object> valueGetter)
        {
            return addColumns((e) -> Collections.singletonMap(column, valueGetter.apply(e)));
        }

        public Builder<T> addColumns(Function<T, Map<String, ?>> valueGetter)
        {
            valueGetters.add(e -> (Map<String, Object>)valueGetter.apply(e));
            return this;
        }


        public Dataframe build()
        {
            return data.stream()
                    .collect(collector(item -> valueGetters
                            .stream()
                            .map(f -> f.apply(item))
                            .reduce(new LinkedHashMap<>(), (m0, m1) -> {
                                m0.putAll(m1);
                                return m0;
                            })));
        }
    }


    private Map<String, ISeries<Object>> columns = new LinkedHashMap<>();
    private final JSONSerializer serializer = new JSONSerializer().exclude("*.class");

    private int size = 0;

    public Dataframe()
    {
    }

    public Dataframe(Dataframe other)
    {
        other.columns.forEach((k, v) -> this.columns.put(k, ISeries.fromValues(v)));
        this.size = other.size;
    }

    public List<String> getColumnNames()
    {
        return new ArrayList<>(columns.keySet());
    }

    public ISeries<ISeries<Object>> getColumns()
    {
        return ISeries.fromValues(columns.values());
    }

    public ISeries<Object> getColumn(String columnName)
    {
        return columns.get(columnName);
    }

    public Dataframe setColumn(String name, ISeries<Object> series)
    {
        if(!columns.isEmpty() && series.size() != size())
            throw new IllegalArgumentException("Series must have the same number of elements as the dataframe");
        columns.put(name, series);
        size = series.size();
        return this;
    }

    public int size()
    {
        return size;
    }

    public void clear()
    {
        columns.clear();
        size = 0;
    }

    public Dataframe transform(Consumer<Dataframe> transformer)
    {
        Dataframe df = new Dataframe(this);
        transformer.accept(df);
        return df;
    }

    public Dataframe setRows(Iterable<Map<String, Object>> rows)
    {
        clear();
        rows.forEach(this::appendRow);
        return this;
    }

    public Dataframe appendRow(Map<String, Object> row)
    {
        Set<String> cols = new HashSet<>(columns.keySet());
        cols.addAll(row.keySet());

        cols.forEach(col -> {
            if(columns.containsKey(col) && row.containsKey(col))
                columns.computeIfPresent(col, (c, s) -> s.append(row.get(col)));
            else if (columns.containsKey(col) && !row.containsKey(col))
                columns.computeIfPresent(col, (c, s) -> s.append((Object) null));
            else if(!columns.containsKey(col) && row.containsKey(col))
            {
                setColumn(col, ISeries.repeatedValue(null, size()));
                columns.computeIfPresent(col, (c, s) -> s.append(row.get(col)));
            }
            else
                throw new IllegalStateException("This shouldn't happen");

        });
        this.size += 1;
        return this;
    }

    public Dataframe append(Dataframe other)
    {
        assert other != null : "Dataframe cannot be null";
        other.getRows().forEach(this::appendRow);
        return this;
    }

    public Dataframe retainColumns(List<String> columns)
    {
        this.columns = this.columns.keySet()
                .stream().filter(columns::contains)
                .collect(Collectors.toMap(col -> col, this.columns::get));
        return this;
    }

    public Map<String, Object> getRow(int n)
    {
        if(n < 0 || n >= size())
            throw new IndexOutOfBoundsException("Index out of bounds");

        Map<String, Object> row = new LinkedHashMap<>();
        for(String col : columns.keySet())
            row.put(col, columns.get(col).get(n));
        return row;
    }

    public Dataframe filterRows(Predicate<Map<String, Object>> predicate)
    {
        setRows(getRows().filter(predicate));
        return this;
    }

    public Dataframe removeRow(int n)
    {
        if(n < 0 || n >= size())
            throw new IndexOutOfBoundsException("Index out of bounds");

        columns.forEach((col, series) -> columns.computeIfPresent(col, (c, s) -> s.remove(n)));
        return this;
    }


    public ISeries<Map<String, Object>> getRows()
    {
        return IntStream.range(0, size())
                .mapToObj(this::getRow)
                .collect(ISeries.collector());
    }


    /**
     * Checks whether all non-null values in the specified column are assignable to the provided class type.
     * <p>
     * This method verifies that for the given column name, all non-null values can be assigned
     * to instances of the specified class type using {@code Class.isAssignableFrom()}.
     *
     * @param columnName the name of the column to validate; must exist in the dataframe
     * @param cls the class type to check assignability against
     * @param <T> the type represented by the class parameter
     * @return {@code true} if all non-null values in the specified column are assignable from the provided class;
     *         {@code false} otherwise
     * @throws AssertionError if the specified column does not exist in the dataframe
     */
    public <T> boolean isAssignableFrom(String columnName, Class<T> cls)
    {
        assert getColumnNames().contains(columnName) : "Column " + columnName + " not found in dataframe";
        assert cls != null : "Class cannot be null";
        return getRows()
                .stream()
                .map(row -> row.getOrDefault(columnName, null))
                .filter(Objects::nonNull)
                .allMatch(v -> cls.isAssignableFrom(v.getClass()));
    }


    public Map<List<Object>, Dataframe> groupBy(List<String> columns)
    {
        return getRows()
                .stream()
                .collect(Collectors.groupingBy(
                        row -> columns
                                .stream()
                                .map(row::get)
                                .collect(Collectors.toList()),
                        collector(r -> r)
                ));

    }

    public Map<Object, Dataframe> groupBy(String column)
    {
        return getRows()
                .stream()
                .collect(Collectors.groupingBy(
                        row -> row.get(column),
                        collector(r -> r)
                ));

    }

    public Dataframe groupAggregate(List<String> columns, DataframeAggregator aggregator)
    {
        return groupBy(columns)
                .values()
                .stream()
                .map(aggregator)
                .reduce(new Dataframe(), Dataframe::append);
    }



    public Dataframe transform(
            List<String> columns,
            List<String> groupBy,
            List<String> sortBy
    )
    {
        DataframeAggregator aggr = new DataframeAggregator()
                .aggregateAllColumns(DataframeAggregator.IAggregateFunction.takeMedian())
                .aggregateStatistics();


        Dataframe df = new Dataframe(this);

        if(columns != null && !columns.isEmpty())
            df = df.retainColumns(columns);

        if(groupBy != null && !groupBy.isEmpty())
            df = df.groupBy(groupBy)
                    .values()
                    .stream()
                    .map(aggr)
                    .reduce(new Dataframe(), Dataframe::append);

        if (sortBy != null && !sortBy.isEmpty())
            df = df.sortBy(sortBy);

        return df;
    }

    /**
     * Sorts the dataframe rows by the natural order of columns (if they are comparable, otherwise they are compared as equal).
     * The order of columns in the list is used to determine the sort order.
     *
     * @param columns columns to sort the dataframe by
     * @return
     */
    public Dataframe sortBy(List<String> columns)
    {
        Comparator<Map<String, Object>> cmp = Comparator.nullsLast(new MapComparator(columns));
        setRows(getRows().sortBy(cmp));
        return this;
    }


    public String toJSON()
    {
        return serializer.deepSerialize(getRows().toList());
    }

    public String toString()
    {
        return toJSON();
    }


    public static <T> DataframeCollector<T> collector(Function<T, Map<String, Object>> rowFunction)
    {
        return new DataframeCollector<>(rowFunction);
    }




}
