package it.unibo.msrehab.rl.environment.wrappers;

import it.unibo.msrehab.rl.environment.IEnvironment;


/**
 * Base class for
 * @param <S>
 * @param <A>
 */
public class WrapperEnvironment<S, A>
{
    protected IEnvironment<S, A> env;

    public IEnvironment<S, A> getWrappedEnvironment()
    {
        return env;
    }

    public void setWrappedEnvironment(IEnvironment<S, A> env)
    {
        this.env = env;
    }
}
