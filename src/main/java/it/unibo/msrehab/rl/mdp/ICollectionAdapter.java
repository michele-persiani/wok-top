package it.unibo.msrehab.rl.mdp;

import it.unibo.msrehab.rl.common.IAdapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Adapter for collections of elements of type T. Provides methods to convert between elements and indexes.
 * Elements must map to values between 0 and the size of the collection
 *
 * @param <T>
 */
public interface ICollectionAdapter<T> extends IAdapter<T, Integer>
{

    /**
     * Converts an element to its index in the collection.
     *
     * @param t the function argument
     * @return the index in the collection of the specified element, between 0 and the size of the countElements() - 1
     */
    @Override
    Integer apply(T t);

    /**
     * Converts a collection index into its corresponding element.
     *
     * @param value collection index to transform, between 0 and countElements() - 1
     * @return the element corresponding to the specified collection index
     */
    @Override
    T inverseApply(Integer value);

    /**
     * Count the elements of the collection
     * @return number of elements
     */
    int countElements();

    /**
     * Returns all the elements of the collection
     *
     * @return
     */
    default Collection<T> getElements()
    {
        return IntStream.range(0, countElements())
                .mapToObj(this::inverseApply)
                .collect(Collectors.toList());
    }

    /**
     * Creates a ICollectionAdapter from a list.
     *
     * @param collection
     * @param <T>
     * @return
     */
    static <T> ICollectionAdapter<T> fromList(List<T> collection)
    {
        return new ICollectionAdapter<T>()
        {
            @Override
            public int countElements()
            {
                return collection.size();
            }

            @Override
            public T inverseApply(Integer value)
            {
                return collection.get(value);
            }

            @Override
            public Integer apply(T t)
            {
                return collection.indexOf(t);
            }
        };
    }
}
