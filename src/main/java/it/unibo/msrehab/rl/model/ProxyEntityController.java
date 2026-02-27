package it.unibo.msrehab.rl.model;

import java.util.Collection;
import java.util.Optional;

public class ProxyEntityController<T> extends EntityController<T>
{
    private final IEntityController<T> controller;

    public ProxyEntityController(IEntityController<T> controller)
    {
        this.controller = controller;
    }

    @Override
    public T newEntity()
    {
        return controller.newEntity();
    }

    @Override
    public Optional<T> findEntity(Object id)
    {
        return controller.findEntity(id);
    }

    @Override
    public Collection<T> getAllEntities()
    {
        return controller.getAllEntities();
    }

    @Override
    public boolean insertEntities(Collection<T> entities)
    {
        return controller.insertEntities(entities);
    }

    @Override
    public boolean updateEntities(Collection<T> entities)
    {
        return controller.updateEntities(entities);
    }

    @Override
    public boolean removeEntities(Collection<T> entities)
    {
        return controller.removeEntities(entities);
    }
}
