package it.unibo.msrehab.rl.utils;

import java.util.function.Consumer;

/**
 * Base class for builders
 * @param <T> class of built objects
 */
public abstract class AbstractBuilder<T>
{
    private final T object;

    public AbstractBuilder()
    {
        this.object = newInstance();
    }

    public AbstractBuilder(T object)
    {
        this.object = newInstance();
        initializeFrom(object);
    }

    /**
     * Sets the instance being built
     * @param instance instance to build
     */
    public void initializeFrom(T instance)
    {
    }

    /**
     * Create new instance of the object to be built;
     * @return
     */
    protected abstract T newInstance();

    /**
     * Utility method setting the object's fields and returning this instance. The returned type is the builder subclass
     * @param builder this instance
     * @param setter setter of the built object fields
     * @return this instance
     * @param <C> class for the builder's subclass
     */
    protected <C extends AbstractBuilder<T>> C setValue(C builder, Consumer<T> setter)
    {
        setter.accept(object);
        return builder;
    }

    /**
     * Returns the built object and reset the builder's state
     * @return the built objec
     */
    public T build()
    {
        return object;
    }

}