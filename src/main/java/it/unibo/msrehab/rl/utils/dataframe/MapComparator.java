package it.unibo.msrehab.rl.utils.dataframe;


import it.unibo.msrehab.rl.utils.ExceptionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * Comparator for map objects
 */
public class MapComparator implements Comparator<Map<String, ?>>
{
    private final List<String> columns;

    private final Comparator<Object> comparator = new ObjectComparator();

    public MapComparator(List<String> columns)
    {
        this.columns = columns;
    }

    @Override
    public int compare(Map<String, ?> o1, Map<String, ?> o2)
    {
        double score = 0;
        Comparator<Object> comparator = this.comparator;
        for(String column : columns)
        {
            Object value0 = o1.getOrDefault(column, null);
            Object value1 = o2.getOrDefault(column, null);

            score += ExceptionUtils.attempt(
                    () -> Math.pow(10, columns.size() - columns.indexOf(column)) * comparator.compare(value0, value1),
                    0d)
            ;
        }
        return (int) Math.signum(score);
    }
}
