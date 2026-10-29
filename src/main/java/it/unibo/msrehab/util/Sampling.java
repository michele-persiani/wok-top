package it.unibo.msrehab.util;




import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Provides methods to sample elements from a collections.
 */
public class Sampling
{
    private static final Random random = new Random();

    private Sampling()
    {
    }

    /**
     * Increases the frequency of elements
     *
     * @param elements    collection where the frequency is to be increased
     * @param targets     elements of which the frequency is to be increased
     * @param probability probability of increasing the frequency of an element from targets in elements
     * @param <T>
     */
    public static <T> void increaseFrequencyOfElements(List<T> elements, List<T> targets, double probability)
    {

        for (int i = 0; i < elements.size(); i++)
            if (Math.random() < probability)
                elements.set(i, targets.get(random.nextInt(targets.size())));

        // Make sure all elements in target are present
        targets.stream()
                .filter(trg -> !elements.contains(trg))
                .forEach(trg -> elements.set(random.nextInt(elements.size()), trg));
    }


    /**
     * Randomly samples with replacement elements from a collection, ensuring that all collection elements are present if possible.
     *
     * @param n num elements to sample
     * @param items collection to sample from
     * @param <T> type of elements
     * @return
     */
    public static <T> List<T> sampleElementsAllPresent(int n, Collection<T> items)
    {
        return sampleElementsAllPresent(n, items, item -> item);
    }

    /**
     * Randomly samples with replacement elements from a collection, ensuring that all unique collection elements are present if possible.
     *
     * @param n num elements to sample
     * @param items collection to sample from
     * @param uniqueKeyExtractor key extractor to associate unique keys to elements
     * @param <T> type of elements
     * @return list with the sampled elements
     */
    public static <T> List<T> sampleElementsAllPresent(int n, Collection<T> items, Function<T, Object> uniqueKeyExtractor)
    {
        List<T> uniqueElements = new ArrayList<>(items.stream()
                .collect(Collectors.toMap(
                        uniqueKeyExtractor,
                        item -> item,
                        (previous, replacement) -> random.nextDouble() > 0.5 ? previous : replacement
                ))
                .values());

        Collections.shuffle(uniqueElements);

        List<T> sampled = new ArrayList<>(uniqueElements.subList(0, Math.min(n, uniqueElements.size())));

        while (sampled.size() < n)
            sampled.add(uniqueElements.get(random.nextInt(uniqueElements.size())));
        return sampled;
    }

    /**
     * Samples elements from a collection with or without replacement
     *
     * @param n           num elements to sample
     * @param items       collection to sample from
     * @param replacement true if elements can be sampled more than once, false otherwise
     * @param <T>
     * @return list with the sampled elements
     */
    public static <T> List<T> sampleElements(int n, Collection<T> items, boolean replacement)
    {
        List<T> fromList = new ArrayList<>(items);
        List<T> resultList = new ArrayList<>();

        while (n > 0 && !items.isEmpty())
        {
            int index = random.nextInt(fromList.size());
            resultList.add(fromList.get(index));
            if (!replacement)
                fromList.remove(index);
            n--;
        }
        return resultList;
    }
}
