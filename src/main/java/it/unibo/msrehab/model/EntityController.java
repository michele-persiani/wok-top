package it.unibo.msrehab.model;

import it.unibo.msrehab.model.entities.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Controller to manage a collection of entities
 * @param <T> type of entities
 */
public interface EntityController<T extends BaseEntity>
{
    /**
     * Gets all entities
     *
     * @return all entities
     */
    List<T> getAllEntities();

    /**
     * Finds an entity with the given id
     * @param id entity id
     * @return an optional with the found entity
     */
    Optional<T> findEntity(Object id);

    /**
     * Inserts an entity
     *
     * @param entity entity to insert
     * @return true if the entity was inserted, false if the entity already exists or an error occurred
     */
    boolean insertEntity(T entity);

    /**
     * Updates an entity
     * @param entity entity to update
     * @return true if the entity was updated, false if the entity was not found (is new or with invalid id) or an error occurred
     */
    boolean updateEntity(T entity);

    /**
     * Removes an entity
     *
     * @param entity entity to remove
     * @return true if the entity was removed, false if the entity was not found or an error occurred
     */
    boolean removeEntity(T entity);

    /**
     * Puts an entity, namely it inserts it if it is new, or updates it if it is not new.
     *
     * @param entity entity to put
     * @return whether the operation was successful
     */
    default boolean putEntity(T entity)
    {
        if (entity.isNew())
            return insertEntity(entity);
        else
            return updateEntity(entity);
    }

    /**
     * Gets the entity with the given id or throws an exception if not found.
     *
     * @param id entity id
     * @return the entity with id {@code id}
     */
    default T getEntity(Object id)
    {
        return findEntity(id).orElseThrow(() -> new IllegalArgumentException(String.format("Entity with id %s not found", id.toString())));
    }
}
