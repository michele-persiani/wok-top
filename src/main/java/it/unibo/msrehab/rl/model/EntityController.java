package it.unibo.msrehab.rl.model;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Abstract controller to manage a set of entities
 * @param <T> entity class
 */
public abstract class EntityController<T> implements IEntityController<T>
{

    /* Query Methods */

    /**
     * Checks whether an entity is contained in the controller
     *
     * @param predicate entity predicate to check for existence
     * @return whether an entity is contained in the controller
     */
    @Override
    public boolean containsEntity(Predicate<T> predicate)
    {
        return !getAllEntities(predicate).isEmpty();
    }


    /**
     * Checks whether an entity with the given id exists within the controller.
     *
     * @param id the identifier of the entity to check for existence
     * @return true if an entity with the given id exists, false otherwise
     */
    @Override
    public boolean containsEntity(Object id)
    {
        return findEntity(id).isPresent();
    }


    /**
     * Count number of managed entities
     * @return number of entities
     */
    @Override
    public int countEntities()
    {
        return getAllEntities().size();
    }


    /**
     * Returns the first found entity fulfilling the predicate
     *
     * @param predicate predicate to filter entities
     * @return optional with found entity, or empty optional if no entity was found.
     */
    @Override
    public Optional<T> findEntity(Predicate<T> predicate)
    {
        return getAllEntities()
                .stream()
                .filter(predicate)
                .findFirst();
    }


    /**
     * Returns all entities managed by this controller. The table from which entities are fetched has name equal to the
     * entities' base class name, that is the table naming common convention
     * @param filter filter to select whi entities to return
     * @return the list of found entities, or null if an error occurred
     */
    @Override
    public Collection<T> getAllEntities(Predicate<T> filter)
    {
        return getAllEntities()
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    /**
     * Tests whether an entity is valid and can be inserted or updated
     * @param entity entity to test
     * @return true if the entity is valid and can be inserted or updated
     */
    @Override
    public boolean isEntityValid(T entity)
    {
        return true;
    }


    /**
     * Groups entities managed by this controller based on the provided grouping function and applying an optional filter.
     *
     * @param <G> the type of the grouping key
     * @param filter a predicate to filter the entities to be grouped
     * @param groupingFunction a function that determines the grouping key for each entity
     * @return a map where the keys are the grouping keys determined by the grouping function,
     *         and the values are lists of entities belonging to each group
     */
    @Override
    public <G> Map<G, List<T>> groupEntitiesBy(Predicate<T> filter, Function<T, G> groupingFunction)
    {
        Collection<T> entities = getAllEntities(filter);
        return entities
                .stream()
                .collect(Collectors.groupingBy(groupingFunction));
    }


    /**
     * Groups entities managed by this controller based on the provided grouping function.
     *
     * @param <G> the type of the grouping key
     * @param groupingFunction a function that determines the grouping key for each entity
     * @return a map where the keys are the grouping keys determined by the grouping function,
     *         and the values are lists of entities belonging to each group
     */
    @Override
    public <G> Map<G, List<T>> groupEntitiesBy(Function<T, G> groupingFunction)
    {
        return groupEntitiesBy(s -> true, groupingFunction);
    }

    /* Insert Methods */


    /**
     * Inserts a new entity in the model. After insertion the entity id is updated with its new id
     *
     * @param entity entity to insert
     * @return true if the entity was successfully inserted, false otherwise. If the entity is not valid, false is returned
     */
    @Override
    public boolean insertEntity(T entity)
    {
        if (!isEntityValid(entity))
            return false;
        return insertEntities(Collections.singletonList(entity));
    }

    /**
     * Inserts a new entity in the model
     * @param entityBuilder builder to set the new entity fields
     * @return the entity inserted, or null if an error occurred
     */
    @Override
    public T insertEntity(Consumer<T> entityBuilder)
    {
        T entity = newEntity();
        entityBuilder.accept(entity);
        boolean success = insertEntity(entity);
        return success ? entity : null;
    }

    /* Update Methods */

    /**
     * Updates a single entitity
     *
     * @param id      id of the entity
     * @param updater strategy applied to the entity to update it
     * @return true id update was successful, false otherwise. If after update the entity is not valid, false is returned
     */
    @Override
    public boolean updateEntity(Object id, Consumer<T> updater)
    {
        Optional<T> entity = findEntity(id);
        if (!entity.isPresent())
            return false;
        T e = entity.get();
        updater.accept(e);
        if (!isEntityValid(e))
            return false;
        return updateEntity(e);
    }


    /**
     * Updates a single entity. The entity must be among those managed by this controller
     * @param entity the entity to update
     * @return true if the entity was successfully updated, false otherwise
     */
    @Override
    public boolean updateEntity(T entity)
    {
        return updateEntities(Collections.singletonList(entity));
    }


    /**
     * Updates a set of entities using a common criteria.
     * @param filter filter to select which entities to update
     * @param updater strategy applied to all entities to update them
     * @return true if all entities were updated, false otherwise
     */
    @Override
    public boolean updateEntities(Predicate<T> filter, Consumer<T> updater)
    {
        Collection<T> entities = getAllEntities(filter);
        return updateEntities(entities, updater);
    }

    /**
     * Update a set of entity using a common criteria. All entities must be managed by this controller. If at least one entitity
     * is failed to be updated the whole transaction is rolled back
     * @param entities entities to update
     * @param updater strategy applied to all entities to update them
     * @return true if all entities were updated, false otherwise
     */
    @Override
    public boolean updateEntities(Collection<T> entities, Consumer<T> updater)
    {
        boolean valid = entities
                .stream()
                .anyMatch(e ->{
                    updater.accept(e);
                    return !isEntityValid(e);
                });
        if(!valid)
            return false;

        return updateEntities(entities);
    }


    /**
     * Inserts or updates an entity depending on whether it already exists
     * @param entity entity to insert or update
     * @return operation success
     */
    @Override
    public boolean putEntity(T entity)
    {
        return putEntities(Collections.singletonList(entity));
    }


    /**
     * Inserts or updates entities depending on whether it already exists
     * @param entities entities to insert or update
     * @return operation success
     */
    @Override
    public boolean putEntities(Collection<T> entities)
    {
        if(entities.stream().anyMatch(entity -> !isEntityValid(entity)))
            return false;

        List<T> toInsert = entities
                .stream()
                .filter(entity -> !containsEntity(entity::equals))
                .collect(Collectors.toList());
        List<T> toUpdate = entities
                .stream()
                .filter(entity -> !toInsert.contains(entity))
                .collect(Collectors.toList());

        boolean success = insertEntities(toInsert);
        if(!success)
            return false;
        success = updateEntities(toUpdate);

        if(!success)
            removeEntities(toInsert);
        return success;
    }


    /* Remove Methods */

    /**
     * Removes the entity with the given id
     * @param id id of the entity to remove
     * @return true if removal was successful, false otherwise
     */
    @Override
    public boolean removeEntityWithId(Object id)
    {
        if(id == null)
            return false;
        return findEntity(id)
                .map(this::removeEntity)
                .orElse(false);
    }

    /**
     * Remove an entity from those being managed by the controller
     * @param entity entity to remove
     * @return true if the entity was removed, false if an error occurred
     */
    @Override
    public boolean removeEntity(T entity)
    {
        return removeEntities(Collections.singletonList(entity));
    }

    /**
     * Removes entities fulfilling a given criteria. Entities are removed as a single transaction. If at least one entitity
     * is failed to be removed the whole transaction is rolled back
     * @param toRemovePredicate predicate to specify which entities are to be removed
     * @return true if all entities were removed, false otherwise.
     */
    @Override
    public boolean removeEntities(Predicate<T> toRemovePredicate)
    {
        Collection<T> entities = getAllEntities(toRemovePredicate);
        return removeEntities(entities);
    }


}
