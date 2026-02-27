package it.unibo.msrehab.rl.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for working with lists.
 */
public class Lists
{
    private Lists()
    {
    }

    public static <T> List<T> empty()
    {
        return new ArrayList<>();
    }

    public static <T> List<T> of(T... elements)
    {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <T> List<T> merge(List<T>... lists)
    {
        return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Get a list of integers from the specified range, upper bound exclusive.
     *
     * @param fromInclusive inclusive from index
     * @param toExclusive   exclusive to index
     * @return A list of integers from, from + 1, ..., to - 2, to - 1
     */
    public static List<Integer> range(int fromInclusive, int toExclusive)
    {
        if (toExclusive < fromInclusive)
            throw new IllegalArgumentException("to must be greater than from");
        return IntStream.range(fromInclusive, toExclusive).boxed().collect(Collectors.toList());
    }

    /**
     * Get a list of integers from 0 to the specified value, exclusive.
     *
     * @param toExclusive exclusive to index
     * @return A list of integers 0, 1, ..., to - 1
     */
    public static List<Integer> range(int toExclusive)
    {
        return range(0, toExclusive);
    }
}
