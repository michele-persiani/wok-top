package it.unibo.msrehab.rl.common;

import java.util.function.Function;


/**
 * Interface to transform elements of type T0 into elements of type T1, as well as inverse transform them.
 *
 * @param <T0>
 * @param <T1>
 */
public interface IAdapter<T0, T1> extends Function<T0, T1>
{
    T0 inverseApply(T1 value);


    default  <T2> IAdapter<T0, T2> compose(IAdapter<T1, T2> other)
    {
        return from(
                x -> other.apply(this.apply(x)),
                y -> this.inverseApply(other.inverseApply(y))
        );
    }


    default IAdapter<T1, T0> reverse()
    {
        return from(this::inverseApply, this);
    }



    static <T0, T1> IAdapter<T0, T1> from(Function<T0, T1> forward, Function<T1, T0> backward)
    {
        return new IAdapter<T0, T1>()
        {
            @Override
            public T0 inverseApply(T1 value)
            {
                return backward.apply(value);
            }

            @Override
            public T1 apply(T0 t0)
            {
                return forward.apply(t0);
            }
        };
    }


    static <T0> IAdapter<T0, T0> identity()
    {
        return from(
                x -> x,
                y -> y
        );
    }

}
