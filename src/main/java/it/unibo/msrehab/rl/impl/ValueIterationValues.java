package it.unibo.msrehab.rl.impl;

import it.unibo.msrehab.rl.utils.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to store the values of the value iteration algorithm. Used to display values in RLService
 */
public class ValueIterationValues
{
    private ValueIterationValues()
    {
    }

    private static List<Double> values = Lists.of();

    public static List<Double> getValues()
    {
        return values;
    }

    public static void setValues(List<Double> values)
    {
        ValueIterationValues.values = values;
    }

    public static String getValuesString()
    {
        return getValues()
                .stream()
                .map(v -> String.format("%,.4f", v))
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
