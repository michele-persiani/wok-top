package it.unibo.msrehab.rl.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;


/**
 * Compares elements based on a score function. Provides caching and inverse scoring mechanisms.
 *
 * @param <T>
 */
public class ScoreComparator<T> implements Comparator<T>
{
    private final Function<T, Number> scoreFunction;
    private final Map<Integer, Number> scoreMap = new HashMap<>();
    private final boolean cached;

    /**
     * Constructs a ScoreComparator that compares elements based on their scores as computed by the provided score function.
     *
     * @param scoreFunction the function used to calculate the score for each element
     */
    public ScoreComparator(Function<T, Number> scoreFunction)
    {
        this(scoreFunction, true);
    }


    /**
     * Constructs a ScoreComparator that compares elements based on their scores computed using the provided score function.
     *
     * @param scoreFunction the function used to compute the score for each element
     * @param cached        whether to cache score computations for elements to improve efficiency. Set to true if the scores
     *                      don't change between a comparison and the other (should be 99.99% of the cases). If false, at each
     *                      comparison the scores are recomputed.
     */
    public ScoreComparator(Function<T, Number> scoreFunction, boolean cached)
    {
        this.scoreFunction = scoreFunction;
        this.cached = cached;
    }

    @Override
    public int compare(T t0, T t1)
    {
        Number score0 = scoreMap.computeIfAbsent(System.identityHashCode(t0), hash -> scoreFunction.apply(t0));
        Number score1 = scoreMap.computeIfAbsent(System.identityHashCode(t1), hash -> scoreFunction.apply(t1));
        if (!cached)
            scoreMap.clear();
        return (int) Math.signum(score0.doubleValue() - score1.doubleValue());
    }

    /**
     * Gets a comparator assigning a random score to each item
     *
     * @param <T> type of items to compare
     * @return a comparator assigning a random score to each item
     */
    public static <T> Comparator<T> randomComparator()
    {
        return randomComparator(new Random());
    }

    /**
     * Gets a comparator assigning a random score to each item
     *
     * @param random random number generator to use for generating random scores
     * @param <T>    type of items to compare
     * @return a comparator assigning a random score to each item
     */
    public static <T> Comparator<T> randomComparator(Random random)
    {
        return new ScoreComparator<>(
                x -> random.nextDouble(),
                true
        );
    }
}
