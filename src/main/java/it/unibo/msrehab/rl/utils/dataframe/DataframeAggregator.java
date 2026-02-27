package it.unibo.msrehab.rl.utils.dataframe;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.UnaryOperator;


/**
 * Class to transform a dataframe into another one with a single row, resulting from applying aggregator functions
 * to different columns
 */
public class DataframeAggregator implements UnaryOperator<Dataframe>
{

    /**
     * Function aggregating values from a column into a single value
     */
    public interface IAggregateFunction extends Function<ISeries<Object>, Object>
    {
        static IAggregateFunction of(Function<ISeries<Object>, Object> aggregator)
        {
            return aggregator::apply;
        }

        static IAggregateFunction average()
        {
            return s -> s.mapToDouble().average(v -> v);
        }

        static IAggregateFunction min()
        {
            return s -> s.mapToDouble().min(v -> v);
        }

        static IAggregateFunction max()
        {
            return s -> s.mapToDouble().max(v -> v);
        }

        static IAggregateFunction std()
        {
            return s -> s.mapToDouble().standardDeviation(v -> v);
        }

        static IAggregateFunction takeFirst()
        {
            return s -> s.size() == 0? null : s.get(0);
        }

        static IAggregateFunction takeLast()
        {
            return s -> s.size() == 0? null : s.get(s.size() - 1);
        }

        static IAggregateFunction takeMedian()
        {
            return s -> {
                if (s.size() == 0)
                    return null;

                List<Object> sortedList = s.sortBy(new ObjectComparator()).toList();

                int size = sortedList.size();
                int middle = size / 2;

                return sortedList.get(size % 2 != 0? middle : middle - 1);
            };
        }

    }

    private final Map<Object, BiPredicate<Dataframe, String>> tests = new LinkedHashMap<>();
    private final Map<Object, IAggregateFunction> aggregators = new LinkedHashMap<>();
    private final Map<Object, Function<String, String>> translations = new LinkedHashMap<>();

    public DataframeAggregator aggregateColumn(String col, IAggregateFunction aggregator)
    {
        return aggregateColumns((df, c) -> c.equals(col), c -> c, aggregator);
    }

    public DataframeAggregator aggregateColumn(String col, Function<String, String> columnTranslate, IAggregateFunction aggregator)
    {
        return aggregateColumns((df, c) -> c.equals(col), columnTranslate, aggregator);
    }

    public <T> DataframeAggregator aggregateAssignableColumns(Class<T> cls, IAggregateFunction aggregator)
    {
        return aggregateColumns((df, c) -> df.isAssignableFrom(c, cls), c -> c, aggregator);
    }

    public <T> DataframeAggregator aggregateAssignableColumns(Class<T> cls, Function<String, String> columnTranslation, IAggregateFunction aggregator)
    {
        return aggregateColumns((df, c) -> df.isAssignableFrom(c, cls), columnTranslation, aggregator);
    }

    /**
     * Aggregates ALL columns in the dataframe using the specified aggregator function.
     *
     * @param aggregator A function that defines the aggregation logic to be applied
     *                   to each column in the dataframe.
     * @return An updated instance of {@code AggregateBuilder<T>} with the specified
     *         aggregations applied to all columns in the dataframe.
     */
    public DataframeAggregator aggregateAllColumns(IAggregateFunction aggregator)
    {
        return aggregateColumns((df, c) -> true, col -> col, aggregator);
    }

    /**
     * Aggregates ALL columns passing the provided test using the specified aggregator function
     * @param columnTest test to select which columns to aggregate
     * @param newColumnName function to define the column in the aggregated dataframe
     * @param aggregator aggregator function to transform column values into a single one
     * @return An updated instance of {@code AggregateBuilder<T>} with the specified
     *         aggregations applied to the selected columns.
     */
    public DataframeAggregator aggregateColumns(BiPredicate<Dataframe, String> columnTest, Function<String, String> newColumnName, IAggregateFunction aggregator)
    {
        Object key = new Object();
        tests.put(key, columnTest);
        aggregators.put(key, aggregator);
        translations.put(key, newColumnName);
        return this;
    }

    /**
     * Aggregates all columns of class Number into statistics of mean value, standard deviation, min value and value
     * @return An updated instance of {@code AggregateBuilder<T>} with the specified
     *         aggregations applied to the numeric columns.
     */
    public DataframeAggregator aggregateStatistics()
    {
        return aggregateAssignableColumns(Number.class, col -> col + " (avg)", IAggregateFunction.average())
                .aggregateAssignableColumns(Number.class, col -> col + " (std)", IAggregateFunction.std())
                .aggregateAssignableColumns(Number.class, col -> col + " (min)", IAggregateFunction.min())
                .aggregateAssignableColumns(Number.class, col -> col + " (max)", IAggregateFunction.max());
    }

    /**
     * Aggregates a single column into its statistics of mean value, standard deviation, min value and value
     * @param column
     * @return An updated instance of {@code AggregateBuilder<T>} with the specified
     *      *         aggregations applied to the selected column.
     */
    public DataframeAggregator aggregateStatistics(String column)
    {
        return aggregateColumn(column, col -> col + " (avg)", IAggregateFunction.average())
                .aggregateColumn(column, col -> col + " (std)", IAggregateFunction.std())
                .aggregateColumn(column, col -> col + " (min)", IAggregateFunction.min())
                .aggregateColumn(column, col -> col + " (max)", IAggregateFunction.max());
    }

    @Override
    public Dataframe apply(Dataframe df)
    {
        Dataframe result = new Dataframe();
        for(Object key : tests.keySet())
        {
            BiPredicate<Dataframe, String> test = tests.get(key);
            Function<ISeries<Object>, Object> aggregator = aggregators.get(key);
            Function<String, String> translation = translations.get(key);
            for (String column : df.getColumnNames())
            {
                if (!test.test(df, column))
                    continue;
                String newColumn = translation.apply(column);
                Object aggregation = aggregator.apply(df.getColumn(column));

                result.setColumn(newColumn, ISeries.fromValues(Collections.singletonList(aggregation)));
            }
        }

        return result;
    }
}
