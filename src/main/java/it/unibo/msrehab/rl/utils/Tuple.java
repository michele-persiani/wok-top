package it.unibo.msrehab.rl.utils;


import java.util.Objects;

/**
 * A generic immutable class representing a tuple with two elements.
 * This class is useful for pairing two values together where a more specific class is not necessary.
 *
 * @param <T0> the type of the first element in the tuple
 * @param <T1> the type of the second element in the tuple
 */
public class Tuple<T0, T1>
{
    public final T0 first;
    public final T1 second;

    public Tuple(T0 first, T1 second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode()
    {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!Tuple.class.isAssignableFrom(getClass()))
            return false;
        Tuple<?,?> other = (Tuple<?,?>)obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

    @Override
    public String toString()
    {
        return String.format("Tuple[%s, %s]", first, second);
    }


    public static <T0, T1> Tuple<T0, T1> of(T0 first, T1 second)
    {
        return new Tuple<>(first, second);
    }
}
