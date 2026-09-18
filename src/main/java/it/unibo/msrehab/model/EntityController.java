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
    List<T> getAllEntities();

    Optional<T> findEntity(Object id);

    boolean insertEntity(T entity);

    boolean updateEntity(T entity);

    boolean removeEntity(T entity);

    default boolean putEntity(T entity)
    {
        if(entity.isNew())
            return insertEntity(entity);
        else
            return updateEntity(entity);
    }

    default T getEntity(Object id)
    {
        return findEntity(id).orElseThrow(() -> new IllegalArgumentException(String.format("Entity with id %s not found", id.toString())));
    }
}
