package it.unibo.msrehab.rl.model;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Singleton class to manage all entities
 */
public class JPAController implements IModel
{
    public static final String persistenceUnit = "DEFAULT_PU";

    private static JPAController instance;

    private final Map<Class<?>, IEntityController<?>> controllers = new HashMap<>();

    private static final Lock lock = new ReentrantLock();

    private JPAController() {}

    /**
     * Gets the singleton instance of the JPAController
     * @return singleton instance of the JPAController
     */
    public static JPAController getInstance()
    {
        if(instance == null)
            instance = new JPAController();
        return instance;
    }

    /**
     * Gets a IEntityController for the given entity class.
     * @param entityClass class of entities for which get the controller
     * @return the entity controller
     * @param <T>
     */
    public <T> IEntityController<T> getEntityController(Class<T> entityClass)
    {
        if(!controllers.containsKey(entityClass))
            controllers.put(entityClass, createController(entityClass));
        return (IEntityController<T>)controllers.get(entityClass);
    }


    private <T> IEntityController<T> createController(Class<T> entityClass)
    {
        IEntityController<T> controller = new JPAEntityController<>(entityClass, persistenceUnit);
        controller = new SynchronizedEntityController<>(lock, controller);
        return controller;
    }

}
