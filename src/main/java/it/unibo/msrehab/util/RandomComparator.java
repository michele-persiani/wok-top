package it.unibo.msrehab.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * A comparator implementation that provides randomized ordering .
 *
 *
 * @param <T> the type of objects
 */
public class RandomComparator<T> implements Comparator<T>
{

    private final Random random = new Random();

    @Override
    public int compare(T t0, T t1)
    {
        return (int) Math.signum(random.nextDouble() - 0.5);
    }

}
