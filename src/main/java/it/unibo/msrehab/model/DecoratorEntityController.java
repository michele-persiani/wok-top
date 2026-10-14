package it.unibo.msrehab.model;

import it.unibo.msrehab.model.entities.BaseEntity;

import java.util.List;
import java.util.Optional;

public class DecoratorEntityController<T extends BaseEntity> implements EntityController<T>
{
    private final EntityController<T> controller;

    public DecoratorEntityController(Model model, Class<T> entityClass)
    {
        this.controller = model.getEntityController(entityClass);
    }

    public DecoratorEntityController(EntityController<T> controller)
    {
        this.controller = controller;
    }

    @Override
    public List<T> getAllEntities()
    {
        return controller.getAllEntities();
    }

    @Override
    public Optional<T> findEntity(Object id)
    {
        return controller.findEntity(id);
    }

    @Override
    public void insertEntity(T entity)
    {
        controller.insertEntity(entity);
    }

    @Override
    public void updateEntity(T entity)
    {
        controller.updateEntity(entity);
    }

    @Override
    public void removeEntity(T entity)
    {
        controller.removeEntity(entity);
    }

    @Override
    public void putEntity(T entity)
    {
        controller.putEntity(entity);
    }

    @Override
    public T getEntity(Object id)
    {
        return controller.getEntity(id);
    }
}
