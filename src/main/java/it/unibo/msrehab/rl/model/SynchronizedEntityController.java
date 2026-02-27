package it.unibo.msrehab.rl.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SynchronizedEntityController<T> extends EntityController<T>
{
    private final IEntityController<T> controller;
    private final Lock lock;

    public SynchronizedEntityController(Lock lock, IEntityController<T> entityController)
    {
        this.lock = lock;
        this.controller = entityController;
    }

    public SynchronizedEntityController(EntityController<T> entityController)
    {
        this(new ReentrantLock(), entityController);
    }

    public Lock getLock()
    {
        return lock;
    }

    private <V> V lockExecute(Callable<V> fcn, V defaultValue)
    {
        lock.lock();
        V result = null;
        try
        {
            result = fcn.call();
        }
        catch (Exception e)
        {
            result = defaultValue;
        }
        finally
        {
            lock.unlock();
        }
        return result;
    }

    @Override
    public T newEntity()
    {
        return lockExecute(controller::newEntity, null);
    }

    @Override
    public Optional<T> findEntity(Object id)
    {
        return lockExecute(() -> controller.findEntity(id), Optional.empty());
    }

    @Override
    public Collection<T> getAllEntities()
    {
        return lockExecute(controller::getAllEntities, new ArrayList<>());
    }

    @Override
    public boolean insertEntities(Collection<T> entities)
    {
        return lockExecute(() -> controller.insertEntities(entities), false);
    }

    @Override
    public boolean updateEntities(Collection<T> entities)
    {
        return lockExecute(() -> controller.updateEntities(entities), false);
    }

    @Override
    public boolean removeEntities(Collection<T> entities)
    {
        return lockExecute(() -> controller.removeEntities(entities), false);
    }


}
