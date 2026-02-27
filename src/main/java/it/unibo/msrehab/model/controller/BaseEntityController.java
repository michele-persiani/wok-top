package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.BaseEntity;
import it.unibo.msrehab.rl.model.EntityTransactionManager;
import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.model.JPAController;
import it.unibo.msrehab.rl.model.ProxyEntityController;
import it.unibo.msrehab.rl.utils.ScoreComparator;

import java.util.*;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class BaseEntityController<T extends BaseEntity> extends ProxyEntityController<T>
{
    public static final IModel model = JPAController.getInstance();

    protected final Random random = new Random();

    public BaseEntityController(Class<T> entityClass)
    {
        super(model.getEntityController(entityClass));
    }

    public EntityTransactionManager getTransactionManager()
    {
        return new EntityTransactionManager(
                JPAController.persistenceUnit
        );
    }

    // Sometimes findEntity(id) returns null, so we use this method instead.
    @Override
    public Optional<T> findEntity(Object id)
    {
        return super.getAllEntities()
                .stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();
    }

    public List<T> sampleElements(int n, Collection<T> items)
    {
        return sampleElements(n, items, item -> item);
    }

    /**
     * Sample n elements from the collection of items, possibly without duplicates (if n < len(items)).
     *
     * @param n            number of elements to sample
     * @param items        collection to sample from
     * @param uniqueKeyExtractor function that extracts a key from each element of the collection, used to determine equal elements
     * @param <K>
     * @return a collection of {@code n} elements, possibly without duplicates, sampled from the collection of items.
     */
    public <K> List<T> sampleElements(int n, Collection<T> items, Function<T, K> uniqueKeyExtractor)
    {
        List<T> uniqueElements = items.stream()
                .collect(Collectors.toMap(
                        uniqueKeyExtractor,
                        item -> item,
                        (previous, replacement) -> random.nextDouble() > 0.5 ? previous : replacement
                ))
                .values()
                .stream()
                .sorted(ScoreComparator.randomComparator())
                .limit(n)
                .collect(Collectors.toList());

        List<T> sampled = new ArrayList<>(uniqueElements.subList(0, Math.min(n, uniqueElements.size())));

        while(sampled.size() < n)
            sampled.add(uniqueElements.get(random.nextInt(uniqueElements.size())));
        return sampled;

    }


    public List<T> getAllEntities()
    {
        return super.getAllEntities()
                .stream()
                .sorted(Comparator.comparing(BaseEntity::getId))
                .collect(Collectors.toList());
    }


    public List<T> getAllEntities(Predicate<T> filter)
    {
        return super.getAllEntities(filter)
                .stream()
                .sorted(Comparator.comparing(BaseEntity::getId))
                .collect(Collectors.toList());
    }

}
