package it.unibo.msrehab.rl.utils.dataframe;


import it.unibo.msrehab.rl.utils.ExceptionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;


/**
 * Represents an interface for a Series, which is a collection of elements supporting various
 * operations such as transformation, aggregation, and statistical computations.
 *
 * @param <T> the type of elements in this series
 */
public interface ISeries<T> extends Iterable<T>
{

    @Override
    default Iterator<T> iterator()
    {
        return toList().iterator();
    }


    /**
     * Returns the number of elements in this series.
     *
     * @return the size of the series as an integer
     */
    default int size()
    {
        return toList().size();
    }


    /**
     * Retrieves the element at the specified index in the series.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     */
    default T get(int index)
    {
        return toList().get(index);
    }


    /**
     * Finds the index of the first occurrence of the specified value in the series.
     * If the value is not found, the method returns -1.
     *
     * @param value the value to search for in the series
     * @return the index of the first occurrence of the specified value,
     * or -1 if the value is not present
     */
    default int indexOf(T value)
    {
        return toList().indexOf(value);
    }


    /**
     * Checks if the series contains the specified value.
     * This operation converts the series to a list and checks for the presence of the value.
     *
     * @param value the value to search for in the series
     * @return {@code true} if the value is present in the series, {@code false} otherwise
     */
    default boolean contains(T value)
    {
        return toList().contains(value);
    }


    /**
     * Appends all elements of the given series to this series and returns a new series
     * containing the combined elements from both series.
     *
     * @param other the series to append to this series
     * @return a new series containing elements from the current series followed by elements
     * from the provided series
     */
    default ISeries<T> append(ISeries<T> other)
    {
        List<T> list = new ArrayList<>(toList());
        list.addAll(other.toList());
        return fromValues(list);
    }


    /**
     * Adds the specified value at the given index in the series.
     * If the index is less than 0, the value is added at the beginning of the series.
     * If the index is greater than the size of the series, the value is added at the end.
     *
     * @param index the position at which the value should be added
     * @param value the value to be added at the specified index
     * @return a new series with the value added at the specified index
     */
    default ISeries<T> add(int index, T value)
    {
        List<T> list = new ArrayList<>(toList());
        index = Math.max(0, Math.min(index, list.size()));
        list.add(index, value);
        return fromValues(list);
    }


    /**
     * Appends the specified elements to the end of this series and returns a new series
     * containing the combined elements from both series.
     *
     * @param values the elements to append to the end of this series
     * @return a new series containing elements from the current series followed by the specified elements
     */
    default ISeries<T> append(T... values)
    {
        return append(ISeries.fromArray(values));
    }


    /**
     * Removes all instances of the specified value from the series and returns a new series with the remaining values.
     * If the specified value is not present in the series, the original series remains unchanged.
     *
     * @param value The value to be removed from the series.
     * @return A new series with the specified value removed.
     */
    default ISeries<T> removeAll(T value)
    {
        return filter(v -> !v.equals(value));
    }


    /**
     * Removes the element at the specified position in this series.
     *
     * @param index the index of the element to be removed
     * @return a new series with the specified element removed
     */
    default ISeries<T> remove(int index)
    {
        List<T> list = new ArrayList<>(toList());
        list.remove(index);
        return fromValues(list);
    }


    /**
     * Returns a new series containing the first 'n' elements from this series.
     * If 'n' is greater than the size of the series, all elements will be returned.
     *
     * @param n the number of elements to include in the resulting series
     * @return a new series containing up to the first 'n' elements from this series
     */
    default ISeries<T> head(int n)
    {
        return stream()
                .limit(n)
                .collect(collector());
    }


    /**
     * Returns an {@code Optional} containing the first element of the stream,
     * or an empty {@code Optional} if the stream is empty.
     *
     * @return an {@code Optional} with the first element of the stream, or an empty {@code Optional} if the stream has no elements
     */
    default Optional<T> first()
    {
        return stream().findFirst();
    }

    /**
     * Returns a new series consisting of the last `n` elements in the series.
     *
     * @param n the number of elements to include from the end of the series
     * @return a new series containing the last `n` elements of this series
     */
    default ISeries<T> tail(int n)
    {
        List<T> values = toList();
        values = values.subList(values.size() - n, values.size());
        return fromValues(values);
    }


    /**
     * Computes the average of the elements in the series based on the provided score function.
     * If the series is empty, returns Double.NaN.
     *
     * @param scoreFunction a function that extracts a double value from each element in the series
     * @return the average of the extracted values as a double, or Double.NaN if the series is empty
     */
    default double average(Function<T, Double> scoreFunction)
    {
        return stream()
                .mapToDouble(scoreFunction::apply)
                .average()
                .orElse(Double.NaN);
    }


    /**
     * Computes the maximum value in the series based on the specified scoring function.
     *
     * @param scoreFunction a function that maps each element of the series to a double value,
     *                      which is used as the score for determining the maximum.
     * @return the maximum score in the series, or Double.NaN if the series is empty.
     */
    default double max(Function<T, Double> scoreFunction)
    {
        return stream()
                .mapToDouble(scoreFunction::apply)
                .max()
                .orElse(Double.NaN);
    }


    /**
     * Calculates the minimum value from a series of elements based on the given scoring function.
     *
     * @param scoreFunction a function that computes a score (as a double) for each element in the series
     * @return the smallest score computed by the score function, or {@code Double.NaN} if the series is empty
     */
    default double min(Function<T, Double> scoreFunction)
    {
        return stream()
                .mapToDouble(scoreFunction::apply)
                .min()
                .orElse(Double.NaN);
    }


    /**
     * Computes the sum of the values obtained by applying the given value function to each element in the series.
     *
     * @param valueFunction a function that extracts a Double value from elements of type T
     * @return the total sum of the Double values derived from the elements of the series
     */
    default double sum(Function<T, Double> valueFunction)
    {
        int sum = 0;
        for (Double v : map(valueFunction))
            sum += v;
        return sum;
    }


    /**
     * Computes the cumulative sum of the mapped values produced by the provided function.
     * Iterates through the series, applies the function to each element to get a numeric value,
     * and accumulates the sum progressively.
     *
     * @param valueFunction the function to map series elements to their numeric values
     * @return a new series containing the cumulative sum of the mapped values
     */
    default ISeries<Double> cumsum(Function<T, Double> valueFunction)
    {
        double sum = 0;
        List<Double> values = new ArrayList<>();
        for (Double v : map(valueFunction))
        {
            sum += v;
            values.add(sum);
        }
        return ISeries.fromValues(values);
    }


    /**
     * Calculates the variance of the elements in the series based on the values
     * derived from applying the provided value function. Variance is a measure
     * of how much the values in the series deviate from the mean.
     *
     * @param valueFunction a function that maps each element of the series to a double value
     * @return the variance of the series as a double
     */
    default double variance(Function<T, Double> valueFunction)
    {
        double mean = average(valueFunction);
        double sumSquares = sum(v -> Math.pow(valueFunction.apply(v) - mean, 2));
        double size = size();
        return sumSquares / size;
    }


    /**
     * Computes the standard deviation of the values in the series as determined
     * by the given value function. The standard deviation measures the
     * dispersion of the values around their mean.
     *
     * @param valueFunction a function that extracts a numeric value from each
     *                      element for computation
     * @return the standard deviation of the extracted values as a double
     */
    default double standardDeviation(Function<T, Double> valueFunction)
    {
        return Math.sqrt(variance(valueFunction));
    }


    /**
     * Normalizes the elements in the series to a scale between 0 and 1 based on the values
     * retrieved by the provided valueFunction. The minimum value in the series is mapped to 0,
     * and the maximum value is mapped to 1, with other values scaled proportionally.
     *
     * @param valueFunction a function to extract numeric values from the elements of the series
     * @return a series of normalized values as instances of Double
     */
    default ISeries<Double> normalize(Function<T, Double> valueFunction)
    {
        double max = max(valueFunction);
        double min = min(valueFunction);

        return stream()
                .map(v -> (valueFunction.apply(v) - min) / (max - min))
                .collect(collector());
    }


    /**
     * Calculates the z-scores of the elements in this series based on the provided value function.
     * A z-score represents the number of standard deviations a data point is from the mean.
     *
     * @param valueFunction a function that extracts numerical values from the elements of the series for computation
     * @return a new series containing the z-scores corresponding to each element of this series
     */
    default ISeries<Double> zscores(Function<T, Double> valueFunction)
    {
        double mean = average(valueFunction);
        double std = standardDeviation(valueFunction);
        return map(v -> (valueFunction.apply(v) - mean) / std);
    }

    /**
     * Scales the series values beteween 0 and 1 based on the provided value function.
     *
     * @param valueFunction a function that extracts numerical values from the elements of the series for computation
     * @return a series with scaled values between 0 and 1
     */
    default ISeries<Double> scaled(Function<T, Double> valueFunction)
    {
        double min = min(valueFunction);
        double max = max(valueFunction);
        return map(v -> (valueFunction.apply(v) - min) / (max - min));
    }


    /**
     * Calculates the cosine similarity between the current series and another series.
     * Cosine similarity measures the cosine of the angle between two vectors, providing
     * a metric of similarity between the two series based on their patterns.
     *
     * @param other         the other series to compute the cosine similarity against
     * @param valueFunction a function to extract numerical values from the series elements
     * @return the cosine similarity as a double value, where 1 indicates identical series and 0 indicates orthogonality
     */
    default double cosine(ISeries<T> other, Function<T, Double> valueFunction)
    {
        return dot(other, valueFunction) / (magnitude(valueFunction) * other.magnitude(valueFunction));
    }


    /**
     * Computes the dot product of the current series with another series.
     * Each pair of elements is transformed using the provided function, and their
     * products are summed to calculate the dot product.
     *
     * @param other         the series to compute the dot product with
     * @param valueFunction a function to extract a numerical value from each element of the series
     * @return the computed dot product as a double
     */
    default double dot(ISeries<T> other, Function<T, Double> valueFunction)
    {
        return zip(other, (v0, v1) -> valueFunction.apply(v0) * valueFunction.apply(v1))
                .sum(v -> v);
    }


    /**
     * Calculates the magnitude of a series using the provided value function.
     * The magnitude is computed as the square root of the sum of the squares
     * of all values derived using the value function.
     *
     * @param valueFunction a function that maps each element in the series to a double value
     * @return the magnitude of the series as a double
     */
    default double magnitude(Function<T, Double> valueFunction)
    {
        return Math.sqrt(sum(v -> Math.pow(valueFunction.apply(v), 2)));
    }


    /**
     * Computes the numerical derivative of a series based on a provided value function and time step.
     * This method calculates the rate of change at each point in the series using the provided
     * mapping and sliding window functions.
     *
     * @param dt            the time step between consecutive points in the series
     * @param valueFunction a function that maps the elements of the series to a numerical value
     * @return a new series containing the computed derivatives as doubles
     */
    default ISeries<Double> derivative(double dt, Function<T, Double> valueFunction)
    {
        return map(valueFunction)
                .slidingWindow(1, 2)
                .map(v -> (v.get(1) - v.get(0) / dt));

    }


    /**
     * Samples a specified number of elements from the series, either with or without replacement.
     *
     * @param n           the number of elements to sample
     * @param replacement a boolean indicating whether sampling is with replacement (true) or without replacement (false)
     * @return a new series containing the sampled elements
     */
    default ISeries<T> sample(int n, boolean replacement)
    {
        Random rand = new Random();

        List<T> values = new ArrayList<>(toList());
        List<T> samples = new ArrayList<>();

        while (samples.size() < n && !values.isEmpty())
        {
            int idx = rand.nextInt(values.size());
            T sampledValue = values.remove(idx);
            samples.add(sampledValue);
            if (replacement)
                values.add(idx, sampledValue);
        }
        return ISeries.fromValues(samples);
    }


    /**
     * Shuffles the elements of the current series in random order and returns
     * a new series with the shuffled elements.
     *
     * @return a new series containing the elements of the current series in a randomly shuffled order
     */
    default ISeries<T> shuffle()
    {
        List<T> values = new ArrayList<>(toList());
        Collections.shuffle(values);
        return ISeries.fromValues(values);
    }


    /**
     * Divides the elements in the series into a specified number of bins based on a given value function.
     * Each bin represents a range of values, and elements are grouped into these ranges.
     *
     * @param n             the number of bins to divide the elements into
     * @param valueFunction the function used to compute numerical values from the elements of the series
     * @return a series of sub-series, where each sub-series contains the elements within a specific bin
     */
    default ISeries<ISeries<T>> bins(int n, Function<T, Double> valueFunction)
    {
        double min = min(valueFunction);
        double max = max(valueFunction) + 1e-6;
        double step = (max - min) / n;

        return IntStream.range(0, n)
                .mapToObj(x -> new Double[]{min + x * step, min + (x + 1) * step})
                .map(minMax -> filter(elem ->
                {
                    double elemValue = valueFunction.apply(elem);
                    return elemValue >= minMax[0] && elemValue < minMax[1];
                }))
                .collect(collector());

    }


    /**
     * Returns a series of boolean values indicating whether the elements in the series
     * are null. Each boolean value corresponds to an element in the series: true if the
     * element is null, false otherwise.
     *
     * @return a Series<Boolean> where each element is true if the corresponding element
     * in the original series is null, false otherwise.
     */
    default ISeries<Boolean> isNull()
    {
        return test(Objects::isNull);
    }


    /**
     * Evaluates whether the values derived from the provided function are NaN (Not a Number) for each element
     * in the series and returns a series of booleans indicating the result.
     *
     * @param valueFunction a function that maps the elements of the series to their corresponding double values
     *                      which will be checked for NaN.
     * @return a series of booleans where each element is true if the corresponding value in the series is NaN,
     * and false otherwise.
     */
    default ISeries<Boolean> isNaN(Function<T, Double> valueFunction)
    {
        return mapToDouble(valueFunction).test(v -> Double.isNaN(v));
    }


    /**
     * Finds and returns the element from the series that has the maximum value based on the provided score function.
     *
     * @param scoreFunction a function that calculates a score (as a Double) for each element of the series
     *                      to determine the comparison criteria for finding the maximum.
     * @return the element with the highest score according to the provided score function, or null if the series is empty.
     */
    default T findMax(Function<T, Double> scoreFunction)
    {
        return stream()
                .max(Comparator.comparing(scoreFunction))
                .orElse(null);
    }


    /**
     * Finds and returns the element from the series with the minimum value as determined
     * by the provided scoring function.
     *
     * @param scoreFunction a function that assigns a score (double value) to each element
     *                      in the series for comparison purposes.
     * @return the element in the series with the minimum value according to the score function,
     * or null if the series is empty.
     */
    default T findMin(Function<T, Double> scoreFunction)
    {
        return stream()
                .min(Comparator.comparing(scoreFunction))
                .orElse(null);
    }


    /**
     * Finds and returns the first element in the series that matches the given predicate.
     *
     * @param predicate a functional interface representing the condition to test elements in the series
     * @return the first element that satisfies the predicate; if no such element is found, returns null
     */
    default Optional<T> findFirst(Predicate<T> predicate)
    {
        return stream()
                .filter(predicate)
                .findFirst();
    }


    /**
     * Merges two Series objects by applying a specified element-wise combining function
     * to corresponding elements in each series. The returned series contains
     * the results of applying the function to pairs of elements from the two series.
     *
     * @param <V>           the type of elements in the other series
     * @param <U>           the type of elements in the resulting series
     * @param other         the other series to merge with this series
     * @param valueFunction a function that takes an element from this series and
     *                      an element from the other series, and produces a result
     *                      to be included in the resulting series
     * @return a new series containing the results of merging the two series
     * @throws IllegalArgumentException if the size of the two series does not match
     */
    default <V, U> ISeries<U> zip(ISeries<V> other, BiFunction<T, V, U> valueFunction)
    {
        if (size() != other.size())
            throw new IllegalArgumentException("Series size mismatch");
        return ISeries.fromValues(
                IntStream.range(0, size())
                        .mapToObj(idx -> valueFunction.apply(get(idx), other.get(idx)))
                        .collect(Collectors.toList())
        );
    }


    /**
     * Replaces elements in the series that satisfy the given test with the specified value.
     *
     * @param value the value to replace matching elements with
     * @param test  a predicate that determines which elements should be replaced
     * @return a new Series where elements satisfying the test predicate are replaced with the specified value
     */
    default ISeries<T> replace(T value, Predicate<T> test)
    {
        return stream()
                .map(v -> test.test(v) ? value : v)
                .collect(collector());
    }


    /**
     * Replaces all `null` values in the series with the specified value.
     *
     * @param value the value to replace `null` elements with
     * @return a new series with `null` values replaced by the specified value
     */
    default ISeries<T> replaceNull(T value)
    {
        return replace(value, Objects::isNull);
    }


    /**
     * Replaces all occurrences of NaN (Not a Number) values in the series with a specified value.
     *
     * @param value         the value to replace NaN occurrences with
     * @param valueFunction a function that maps an element of the series to a Double,
     *                      used to identify NaN values
     * @return a new series where all NaN values (determined by the valueFunction) have been replaced
     */
    default ISeries<T> replaceNaN(T value, Function<T, Double> valueFunction)
    {
        return zip(isNaN(valueFunction), (v, isnan) -> isnan ? value : v);
    }


    /**
     * Tests each element in the series against the given condition.
     *
     * @param testFunction a function that takes an element of type T and returns a Boolean
     *                     indicating whether the condition is met for that element
     * @return a new series of Boolean values where each value represents the result
     * of applying the testFunction to the corresponding element in the original series
     */
    default ISeries<Boolean> test(Function<T, Boolean> testFunction)
    {
        return map(testFunction);
    }


    /**
     * Filters the elements of the series based on the given predicate. Returns a new series
     * containing only the elements that satisfy the specified condition.
     *
     * @param predicate the condition to test each element in the series
     * @return a new series containing elements that match the predicate
     */
    default ISeries<T> filter(Predicate<T> predicate)
    {
        return stream()
                .filter(predicate)
                .collect(collector());

    }


    /**
     * Filters the series based on a provided predicate series.
     * Each element in the resulting series is included only if the corresponding
     * element in the predicate series is true.
     *
     * @param predicate the series of boolean values acting as a filter.
     *                  Each value determines if the corresponding element
     *                  in the series is included or excluded.
     * @return a new series containing only the elements where the predicate is true.
     * @throws IllegalArgumentException if the size of the predicate series
     *                                  does not match the size of the series.
     */
    default ISeries<T> filter(ISeries<Boolean> predicate)
    {
        if(predicate.size() != size())
            throw new IllegalArgumentException("Predicate size mismatch");

        return zip(predicate, (v, pred) -> pred ? v : null)
                .filter(Objects::nonNull);

    }


    /**
     * Transforms each element in the series using the specified function and returns a new series
     * containing the results of the transformation.
     *
     * @param <U>       the type of elements in the resulting series
     * @param transform the function to apply to each element in the series
     * @return a new series containing the transformed elements
     */
    default <U> ISeries<U> map(Function<T, U> transform)
    {
        return stream()
                .map(transform)
                .collect(collector());
    }


    /**
     * Casts the non-null elements of the series to the specified class type. Null elements are mapped to null
     *
     * @param <U>     the target type to cast elements to
     * @param toClass the class representing the target type to which elements will be cast
     * @return a new series containing the elements cast to the specified type
     */
    default <U> ISeries<U> cast(Class<U> toClass)
    {
        return map(toClass::cast);
    }


    /**
     * Aggregates to a single value the elements of the series by applying the provided transformation function.
     *
     * @param <U>       The type of the aggregated result.
     * @param transform A function that takes an iterable representation of the series
     *                  and produces the aggregated result of type U.
     * @return The result of applying the transformation function to the elements of the series.
     */
    default <U> U transform(Function<Iterable<T>, U> transform)
    {
        return transform.apply(toList());
    }


    /**
     * Aggregates to a single value the elements of the series by applying the provided transformation function.
     *
     * @param <U>       The type of the aggregated result.
     * @param transform A function that takes an iterable representation of the series
     *                  and produces the aggregated result of type U.
     * @return The result of applying the transformation function to the elements of the series.
     */
    default <U> ISeries<U> reduce(BiFunction<ISeries<U>, T, ISeries<U>> transform)
    {
        ISeries<U> result = ISeries.emptySeries();
        for (T v : this)
            result = transform.apply(result, v);
        return result;
    }


    /**
     * Returns a new series containing only distinct elements from the original series
     * based on the provided key function. The key function is applied to each element
     * to compute a key, and elements with duplicate keys are excluded from the result.
     * I.e., no elements with the same key are present in the resulting series.
     *
     * @param keyFunction a function that computes a key for each element in the series.
     *                    The resulting keys are used to determine uniqueness.
     * @return a new series containing only the elements with unique keys as determined by the key function.
     */
    default ISeries<T> distinct(Function<T, Object> keyFunction)
    {
        Map<Object, T> dict = new LinkedHashMap<>();
        stream()
                .forEach(v ->
                {
                    Object key = keyFunction.apply(v);
                    if (!dict.containsKey(key))
                        dict.put(key, v);
                });
        return fromValues(dict.values());
    }


    /**
     * Groups the elements of the series based on the result of applying the provided key function.
     * Each group corresponds to a unique key produced by the key function,
     * and contains all elements that map to that key.
     *
     * @param keyFunction a function that computes a key for each element in the series
     * @return a series of series, where each inner series contains elements grouped by a common key
     */
    default ISeries<ISeries<T>> groupBy(Function<T, ?> keyFunction)
    {
        Map<Object, List<T>> groups = new LinkedHashMap<>();

        for (T v : toList())
        {
            Object group = keyFunction.apply(v);
            if (!groups.containsKey(group))
                groups.put(group, new ArrayList<>());
            groups.get(group).add(v);
        }

        return groups
                .values()
                .stream()
                .map(ISeries::fromValues)
                .collect(collector());
    }


    /**
     * Sorts the elements in the current series based on the scores provided
     * by the given scoring function. The scores are calculated for each element,
     * and the series is sorted in ascending order of these scores.
     *
     * @param scoreFunction a function that computes a score (as a double)
     *                      for each element in the series to determine the order.
     * @return a new Series instance where elements are sorted by the computed scores.
     */
    default ISeries<T> sortBy(Function<T, Double> scoreFunction)
    {
        return sortBy(Comparator.comparing(scoreFunction));
    }


    /**
     * Sorts the series based on the provided comparator.
     *
     * @param comparator the comparator defining the order of the elements in the series
     * @return a new series with elements sorted according to the specified comparator
     */
    default ISeries<T> sortBy(Comparator<T> comparator)
    {
        return stream()
                .sorted(comparator)
                .collect(collector());
    }


    /**
     * Creates a sliding window view of the series, where each window contains a subset
     * of elements from the original series. Sliding windows have a fixed size, and their
     * starting positions are determined by the specified step. If a window contains fewer
     * elements than the specified size, the last element of the series is repeated to fill
     * the window.
     *
     * @param step the step size between the starting points of consecutive sliding windows
     * @param size the size of each sliding window
     * @return a series containing the sliding windows, where each sliding window is itself
     * represented as a series
     */
    default ISeries<ISeries<T>> slidingWindow(int step, int size)
    {
        List<T> values = toList();
        int maxN = (int) Math.floor(((double) values.size()) / step);
        return IntStream.range(0, maxN)
                .map(n -> n * step)
                .mapToObj(n ->
                {
                    List<T> window = values.subList(n, Math.min(n + size, values.size()));
                    for (int i = 0; i < size - window.size(); i++)
                        window.add(window.get(window.size() - 1));
                    return window;
                })
                .map(ISeries::fromValues)
                .collect(collector());
    }

    /**
     * Concatenates the given values to the current series.
     *
     * @param values the values to be concatenated to the series
     * @return a new series containing the original elements and the concatenated values
     */
    default ISeries<T> concatenate(T... values)
    {
        return concatenate(Arrays.asList(values));
    }


    /**
     * Concatenates the current series with the provided iterable of values and returns a new series.
     *
     * @param values the iterable of values to be appended to the current series
     * @return a new series containing elements from the current series followed by the provided values
     */
    default ISeries<T> concatenate(Iterable<T> values)
    {
        return ISeries.fromValues(
                Stream.concat(
                                stream(),
                                StreamSupport
                                        .stream(values.spliterator(), false))
                        .collect(Collectors.toList()
                        )
        );
    }



    default ISeries<T> fill(int toSize, T value)
    {
        List<T> values = toList();
        while(values.size() < toSize)
            values.add(value);
        return ISeries.fromValues(values);
    }


    /**
     * Enumerates over the elements of a collection and provides each element along with its index to the specified BiConsumer.
     *
     * @param consumer a BiConsumer that takes an integer index and an element of type T as input, which will process each index-element pair
     */
    default void enumerate(BiConsumer<Integer, T> consumer)
    {
        List<T> elements = toList();
        for (T e : elements)
            consumer.accept(elements.indexOf(e), e);
    }


    /**
     * Transforms the elements of the series to a series of doubles based on the provided cast function.
     * If the cast function produces a null or throws an exception, the resulting value will be Double.NaN.
     *
     * @param castFunction the function to convert elements of type T to Double
     * @return a new series with elements of type Double resulting from the transformation
     */
    default ISeries<Double> mapToDouble(Function<T, Double> castFunction)
    {
        return stream()
                .map(v -> ExceptionUtils.attempt(() -> castFunction.apply(v), Double.NaN))
                .collect(collector());
    }


    /**
     * Transforms the elements of the series into a new series
     * where each element is converted to a Double value.
     * Elements for which no double value is available are mapped to Double.NaN.
     *
     * @return a new ISeries containing Double values obtained
     * from the transformation of the elements in the original series.
     */
    default ISeries<Double> mapToDouble()
    {
        return mapToDouble(o ->
        {
            if (Number.class.isAssignableFrom(o.getClass()))
                return ((Number)o).doubleValue();
            if (Boolean.class.isAssignableFrom(o.getClass()))
                return ((Boolean)o) ? 1d : 0d;
            return Double.parseDouble(o.toString());
        });
    }


    /**
     * Converts the elements in the series to an array of doubles using the given mapping function.
     *
     * @param valueFunction a function that maps each element in the series to a double value
     * @return an array of doubles containing the mapped values from the series
     */
    default double[] toDoubleArray(Function<T, Double> valueFunction)
    {
        return mapToDouble(valueFunction)
                .stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }


    /**
     * Attempt to convert the elements of the collection to a double array.
     * Elements for which no double value is available are mapped to Double.NaN.
     *
     * @return a double array containing the converted elements of the collection.
     */
    default double[] toDoubleArray()
    {
        return mapToDouble()
                .stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }


    /**
     * Joins the elements of a collection into a single string separated by the specified delimiter.
     * Each element is transformed into a string using the provided toStringFunction.
     *
     * @param delimiter the string that separates each element in the resulting string
     * @param toStringFunction a function to convert each element to a string
     * @return a string consisting of the elements separated by the delimiter
     */
    default String join(String delimiter, Function<T, String> toStringFunction)
    {
        return String.join(delimiter, map(toStringFunction));
    }

    /**
     * Converts the elements of this series into an array.
     *
     * @return an array containing all elements of this series
     */
    default T[] toArray()
    {
        return (T[]) toList().toArray();
    }


    /**
     * Converts the elements in the series into a sequential {@code Stream}.
     * <p>
     * The method utilizes the {@code toList()} function to first retrieve the elements
     * of the series as a {@code List}, and subsequently converts the list into a stream.
     *
     * @return a {@code Stream} containing the elements of the series
     */
    default Stream<T> stream()
    {
        return toList().stream();
    }


    /**
     * Converts the elements of the stream into a 2D matrix representation using the provided transformer function.
     *
     * @param transformer a function that transforms elements of type T into an array of doubles
     * @return a 2D array of doubles where each element is a transformed representation of the input
     */
    default double[][] toMatrix(Function<T, double[]> transformer)
    {
        return map(transformer)
                .transform(rows ->
                {
                    List<double[]> rowArrays = new ArrayList<>();
                    rows.forEach(rowArrays::add);
                    double[][] matrix = new double[rowArrays.size()][];
                    for (int i = 0; i < rowArrays.size(); i++)
                        matrix[i] = rowArrays.get(i);
                    return matrix;
                });

    }

    /**
     * Converts the elements of the stream into a map by applying the specified functions
     * to generate keys and values.
     *
     * @param <K>           the type of keys in the resulting map
     * @param <V>           the type of values in the resulting map
     * @param keyFunction   the function to transform the elements into keys
     * @param valueFunction the function to transform the elements into values
     * @return a map containing the keys and values generated by the specified functions
     */
    default <K, V> Map<K, V> toMap(Function<T, K> keyFunction, Function<T, V> valueFunction)
    {
        return stream()
                .collect(Collectors.toMap(keyFunction, valueFunction));
    }

    /**
     * Converts the elements of the series into a list.
     *
     * @return a List containing all elements of the series in order.
     */
    List<T> toList();


    /**
     * Creates a new Series from the provided iterable collection of values.
     *
     * @param <U>    the type of elements in the series
     * @param values an Iterable containing the values to include in the series
     * @return a new Series containing all elements in the provided Iterable, preserving their order
     */
    static <U> ISeries<U> fromValues(Iterable<U> values)
    {
        List<U> list = new ArrayList<>();
        for(U v : values)
            list.add(v);

        return new ISeries<U>()
        {
            @Override
            public List<U> toList()
            {
                return list;
            }

            @Override
            public String toString()
            {
                String type = size() > 0 ? get(0).getClass().getSimpleName() : "unknown";
                return String.format(
                        "Series[class=%s, size=%s]",
                        type,
                        list.size()
                );
            }
        };
    }


    /**
     * Creates a new series from an array of values.
     *
     * @param values an array of values to initialize the series
     * @param <U>    the type of elements in the series
     * @return a series containing the specified values
     */
    static <U> ISeries<U> fromArray(U[] values)
    {
        return fromValues(Arrays.asList(values));
    }


    /**
     * Creates a new Series of Double objects from an array of primitive double values.
     *
     * @param values an array of primitive double values to be converted into a Series
     * @return a Series containing the Double objects corresponding to the provided array
     */
    static ISeries<Double> fromArray(double[] values)
    {
        return fromValues(DoubleStream.of(values)
                .boxed()
                .collect(Collectors.toList())
        );
    }


    /**
     * Creates and returns an empty Series.
     *
     * @param <U> the type of elements in the Series
     * @return an empty Series instance
     */
    static <U> ISeries<U> emptySeries()
    {
        return fromValues(Collections.emptyList());
    }


    /**
     * Creates a series containing a single element.
     *
     * @param value the single element to include in the series
     * @param <U>   the type of the element in the series
     * @return a series containing only the specified element
     */
    static <U> ISeries<U> singletonSeries(U value)
    {
        return fromValues(Collections.singletonList(value));
    }


    /**
     * Concatenates multiple iterable series into a single series.
     *
     * @param series an array of iterable series to be concatenated
     * @return a new series containing all elements from the provided series in order
     */
    static <U> ISeries<U> concatenate(Iterable<U>... series)
    {
        ISeries<U> result = emptySeries();
        for (Iterable<U> s : series)
            result = result.concatenate(s);
        return result;
    }


    /**
     * Creates a series containing a specified value repeated a given number of times.
     *
     * @param <U>   the type of the value
     * @param value the value to be repeated in the series
     * @param n     the number of times the value should be repeated
     * @return a series with the specified value repeated the given number of times
     */
    static <U> ISeries<U> repeatedValue(U value, int n)
    {
        return fromValues(Collections.nCopies(n, value));
    }



    /**
     * Generates a sequential series of integers within the specified range.
     *
     * @param start the starting value of the range (inclusive)
     * @param end the ending value of the range (exclusive)
     * @return an ISeries*/
    static ISeries<Integer> range(int start, int end)
    {
        return fromValues(IntStream.range(start, end).boxed().collect(Collectors.toList()));
    }

    /**
     * Creates and returns a new instance of SeriesCollector.
     *
     * @param <T> the type of elements handled by the SeriesCollector
     * @return a new SeriesCollector instance
     */
    static <T> SeriesCollector<T> collector()
    {
        return new SeriesCollector<>();
    }
}
