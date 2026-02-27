package it.unibo.msrehab.rl.utils.rv;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a random variable. A random variable encapsulates
 * a probability distribution, allowing for operations such as sampling,
 * calculation of probabilities, and retrieval of statistical properties.
 *
 * @param <T> the type of values that the random variable can take
 */
public interface IRandomVariable<T>
{
    /**
     * Retrieves the collection of all possible values that the random variable can take.
     * These values correspond to the keys of the probability distribution defined by the random variable.
     *
     * @return a collection of all possible values of the random variable.
     */
    Collection<T> values();

    /**
     * Generates a list of samples from the random variable based on its probability distribution.
     * Each sample is drawn independently, and the likelihood of selecting each value corresponds
     * to the probability mass function (PMF) of the random variable.
     *
     * @param n the number of samples to generate
     * @return a list containing the generated samples
     * @throws IllegalStateException if sampling cannot be completed successfully
     */
    List<T> sample(int n);

    /**
     * Generates a single sample from the random variable based on its probability distribution.
     * The sampling is performed according to the probability mass function (PMF), where the
     * likelihood of each value being selected corresponds to its associated probability.
     *
     * @return a randomly generated value from the random variable.
     * @throws IllegalStateException if the sampling process cannot yield a valid value due to an error
     *                               in the distribution or its weights.
     */
    T sample();

    /**
     * Retrieves the mode (the most frequent value) of the random variable.
     * The mode is the value associated with the highest
     * probability in the distribution.
     *
     * @return the most probable value (mode) from the random variable.
     * @throws IllegalStateException if the random variable is empty or mode cannot be determined.
     */
    T mode();

    /**
     * Calculates the cumulative distribution function (CDF) for a given value of the random variable
     * by utilizing a comparator to determine the ordering. The CDF represents the probability that
     * the random variable will take a value less than or equal to the given value.
     *
     * @param comparator the comparator used to determine the ordering of the values in the distribution
     * @param value the value for which to calculate the CDF
     * @return the cumulative probability for the given value
     * @throws IllegalArgumentException if the given value is not part of the probability distribution
     */
    double cdf(Comparator<T> comparator, T value);

    /**
     * Computes and returns the cumulative distribution function (CDF) of the random variable.
     * The CDF represents the cumulative probabilities for each value in the random variable,
     * calculated by summing the probabilities of all values less than or equal to the given value
     * according to the provided comparator.
     *
     * @param comparator the comparator to determine the ordering of the values in the random variable.
     * @return a map where keys are the values of the random variable and values are their cumulative probabilities.
     */
    Map<T, Double> cdf(Comparator<T> comparator);


    /**
     * Computes the probability mass function (PMF) for a specific value of the random variable.
     * The PMF gives the probability of the random*/
    double pmf(T value);


    /**
     * Computes and returns a probability mass function (PMF) for the random variable.
     * The PMF represents the probabilities associated with each possible value of the random variable,
     * where these probabilities are normalized to ensure their sum equals 1. The resulting map associates
     * each value of the random variable with its corresponding probability.
     *
     * @return a map where keys are the possible values of the random variable and values are their probabilities.
     */
    Map<T, Double> pmf();
}
