package it.unibo.msrehab.rl.utils;

import java.util.function.Function;

public interface XYFunction extends Function<Double, Double>
{
    default XYFunction scaled(double c)
    {
        return x -> c * apply(x);
    }

    default XYFunction multiply(Function<Double, Double> other)
    {
        return x -> apply(x) * other.apply(x);
    }

    default XYFunction sum(Function<Double, Double> other)
    {
        return x -> apply(x) + other.apply(x);
    }

    default XYFunction weightedSum(double wOther, Function<Double, Double> other)
    {
        if(wOther < 0 || wOther > 1)
            throw new IllegalArgumentException("Weight must be between 0 and 1");
        return x -> (1 - wOther) * apply(x) + wOther * other.apply(x);
    }

    default XYFunction max(double value)
    {
        return x -> Math.max(apply(x), value);
    }

    default XYFunction min(double value)
    {
        return x -> Math.min(apply(x), value);
    }

    default XYFunction clamp(double min, double max)
    {
        if(min > max)
            throw new IllegalArgumentException("Min must be less than max");
        return x -> Math.max(min, Math.min(apply(x), max));
    }




    // Factory methods

    static XYFunction identity()
    {
        return x -> x;
    }

    static XYFunction constant(double c)
    {
        return x -> c;
    }


    static XYFunction linear(double a, double b)
    {
        return x -> x * a + b;
    }

    static XYFunction sigmoid(double a, double b)
    {
        return x -> 1 / (1 + Math.exp(a * (x - b)));
    }


}
