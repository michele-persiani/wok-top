package it.unibo.msrehab.rl.mdp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class CollectionAdapter<T> implements ICollectionAdapter<T>
{
    private final Map<Integer, T> forwardMap = new HashMap<>();

    private final Map<T, Integer> backwardMap = new HashMap<>();

    public CollectionAdapter(Iterator<T> elements)
    {
        int index = 0;
        while (elements.hasNext())
        {
            T element = elements.next();
            forwardMap.put(index, element);
            backwardMap.put(element, index);
            index++;
        }
    }

    @Override
    public T inverseApply(Integer index)
    {
        return forwardMap.get(index);
    }

    @Override
    public Integer apply(T element)
    {
        return backwardMap.get(element);
    }

    @Override
    public int countElements()
    {
        return forwardMap.size();
    }
}
