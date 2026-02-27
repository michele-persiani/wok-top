package it.unibo.msrehab.rl.utils;


import java.util.Comparator;


/**
 * Comparator for Objects. If two objects are comparable, then they are compared by their natural order, otherwise they are
 * treated as equal.
 */
public class ObjectComparator implements Comparator<Object>
{
    private final Comparator<Object> comparator = Comparator.<Object, Comparable<Object>>comparing(
                    Comparable.class::cast,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );


    @Override
    public int compare(Object o1, Object o2)
    {
        return ExceptionUtils.attempt(() -> comparator.compare(o1, o2), 0);
    }
}
