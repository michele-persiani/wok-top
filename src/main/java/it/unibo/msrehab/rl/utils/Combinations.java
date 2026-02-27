package it.unibo.msrehab.rl.utils;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;



/**
 * The Combinations utility class provides a mechanism to create combinations of elements
 * from multiple lists. It supports adding lists of objects, ranges of integers,
 * and streams of combinations, and implements the Iterable interface to allow
 * iteration over all possible combinations of the added lists.
 */
public class Combinations implements Iterable<List<Object>>
{
    private final List<List<Object>> values = new ArrayList<>();


    public Combinations add(List<Object> values)
    {
        this.values.add(values);
        return this;
    }


    public Combinations add(Object... values)
    {
        this.values.add(Arrays.asList(values));
        return this;
    }


    public Combinations addIntegerRange(int fromInclusive, int toExclusive)
    {
        this.values.add(IntStream.range(fromInclusive, toExclusive).boxed().collect(Collectors.toList()));
        return this;
    }


    @Override
    public Iterator<List<Object>> iterator()
    {
        return computeCombinations(values).iterator();
    }


    public Stream<List<Object>> stream()
    {
        return StreamSupport.stream(spliterator(), false);
    }


    /**
     * Computes all possible combinations of elements, where each combination includes one element
     * from each list in the provided collection of lists. The order of elements within a list
     * is preserved in the resulting combinations.
     *
     * @param collections a list of lists, where each list contains elements from which combinations are created.
     *                    Each inner list must contain elements to include in combinations. If the input list
     *                    is empty or null, a single empty combination is returned.
     * @return a list of lists, where each inner list represents one unique combination of elements from the input lists.
     *         If the input list is empty or null, the result contains a single empty list.
     */
    public static List<List<Object>> computeCombinations(List<List<Object>> collections) {
        List<List<Object>> result = new ArrayList<>();

        // Caso base: se la lista è vuota, restituisce una lista con una lista vuota
        if (collections == null || collections.isEmpty()) {
            result.add(new ArrayList<>());
            return result;
        }

        // Prendi la prima collezione
        Collection<Object> firstCollection = collections.get(0);
        // Crea una sottolista con le collezioni rimanenti
        List<List<Object>> remainingCollections = collections.subList(1, collections.size());

        // Calcola ricorsivamente le combinazioni delle collezioni rimanenti
        List<List<Object>> subCombinations = computeCombinations(remainingCollections);

        // Per ogni elemento nella prima collezione
        for (Object item : firstCollection) {
            // Per ogni combinazione già calcolata
            for (List<Object> subCombination : subCombinations) {
                List<Object> newCombination = new ArrayList<>();
                newCombination.add(item);
                newCombination.addAll(subCombination);
                result.add(newCombination);
            }
        }

        return result;
    }
}
