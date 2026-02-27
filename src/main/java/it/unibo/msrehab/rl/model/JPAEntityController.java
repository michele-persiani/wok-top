package it.unibo.msrehab.rl.model;



import it.unibo.msrehab.rl.utils.MethodLogger;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class JPAEntityController<T> extends EntityController<T>
{
    private final String persistenceUnit;
    private final Class<T> entityClass;
    private final EntityManagerFactory entitymanagerFactory;

    private final MethodLogger logger = new MethodLogger(getClass());

    public JPAEntityController(Class<T> entityClass, String persistenceUnit)
    {
        this.entityClass = entityClass;
        this.persistenceUnit = persistenceUnit;
        this.entitymanagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnit);
    }

    protected final EntityManager getEntityManager()
    {
        return entitymanagerFactory.createEntityManager();
    }


    /**
     * Creates a new entity. Subclasses of EntityManager can extend this method to provide additional
     * initialization
     * @return a newly created entity. The entity is not inserted in the database
     */
    public T newEntity()
    {
        try
        {
            return entityClass.getConstructor().newInstance();
        } catch (Exception e)
        {
            logger.error(e.getMessage());
            logger.flush();
            return null;
        }
    }

    protected String getSchemaName()
    {
        return entityClass.getSimpleName();
    }



    /**
     * Inserts new entities in the model. After insertion the entity id is updated with its new id
     * @param entities
     * @return
     */
    public boolean insertEntities(Collection<T> entities)
    {
        logger.info("insert of %s (%s)", entityClass.getSimpleName(), entities.size());
        if(!entities.stream().allMatch(this::isEntityValid))
        {
            logger.error("one or more entities are invalid").flush();
            return false;
        }
        EntityManager em = getEntityManager();

        try
        {
            em.getTransaction().begin();
            entities.forEach(em::persist);
            boolean success = onEntityInserted(entities);
            if(!success)
                throw new Exception("callback returned false");
            em.getTransaction().commit();
            logger.info("success");
            return true;
        }
        catch (Exception e)
        {
            logger.error("Exception: %s", e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        finally
        {
            em.close();
            logger.flush();
        }
    }


    /**

     * Finds an entity with the given id
     * @param id id of the entity
     * @return the found entity, or null
    */
    public Optional<T> findEntity(Object id)
    {
        logger.info("id %s", id);
        if(id == null)
            return Optional.empty();
        EntityManager em = getEntityManager();
        T entity = null;
        try
        {
            entity = em.find(entityClass, id);
            logger.info("entity fetched");
        }
        catch (Exception e)
        {
            logger.error("exception %s", e.getMessage());
        }
        finally
        {
            em.close();
        }
        logger.flush();
        return Optional.ofNullable(entity);
    }


    /**
     * Returns all entities managed by this controller
     * @return all managed entities
     */
    public Collection<T> getAllEntities()
    {
        String tableName = getSchemaName();
        EntityManager em = getEntityManager();

        try
        {
            List<T> entities = new ArrayList<>(
                    em.createQuery("SELECT e FROM " + tableName + " e", entityClass).getResultList()
            );
            logger.info("num entities %s", entities.size()).flush();
            return entities;
        }
        catch (Exception e)
        {
            logger.warn("exception thrown %s", e.getMessage());
            logger.warn("returning empty list").flush();
            return new ArrayList<>();
        }
        finally
        {
            em.close();
        }
    }


    @Override
    public boolean updateEntities(Collection<T> entities)
    {
        logger.info("num entities %s", entities.size());
        if(!entities.stream().allMatch(this::isEntityValid))
        {
            logger.error("one or more entities are invalid").flush();
            return false;
        }
        EntityManager em = getEntityManager();

        try
        {
            em.getTransaction().begin();

            Collection<T> merged = entities.stream().map(em::merge).collect(Collectors.toList());

            boolean success = onEntitiesUpdated(merged);
            if(!success)
                throw new Exception("callback returned false");
            em.getTransaction().commit();
            logger.info("updated %s entities", merged.size());
            return true;
        }
        catch (Exception e)
        {
            logger.error("exception: %s", e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        finally
        {
            em.close();
            logger.flush();
        }
    }


    /**
     * Removes a set of entities. If at least one entitity is failed to be removed the whole transaction is rolled back
     *
     * @param entities entitites to be removed from the stored ones
     * @return true if all entities were removed, false otherwise
     */
    public boolean removeEntities(Collection<T> entities)
    {
        logger.info("%s entities", entities.size());
        EntityManager em = getEntityManager();
        try
        {
            em.getTransaction().begin();
            entities.forEach(e -> {
                if(!em.contains(e))
                    e = em.merge(e);
                em.remove(e);
            });
            boolean success = onEntitiesRemoved(entities);
            if(!success)
                throw new Exception("callback returned false");
            em.getTransaction().commit();
            logger.info("removed %s entities", entities.size());
            return true;
        }
        catch (Exception e)
        {
            em.getTransaction().rollback();
            logger.error("exception %s", e.getMessage());
            return false;
        }
        finally
        {
            logger.flush();
            em.close();
        }
    }


    /**
     * Executes a select named query (need to be defined inside a @NamedQuery annotation of the entity) returning a result list.
     * @param namedQueryId
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the list of entities resulting from applying the query, or null if an error occurred
     */
    public final Collection<T> executeResultListNamedQuery(String namedQueryId, Consumer<TypedQuery<T>> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);
        List<T> userList;
        EntityManager em = getEntityManager();
        try
        {
            TypedQuery<T> query = em.createNamedQuery(namedQueryId, entityClass);
            queryConfig.accept(query);
            userList = query.getResultList();
            return userList;

        } catch (Exception ex)
        {
            logger.error("Error while executing named query", ex);
            return null;
        } finally
        {
            em.close();
        }
    }

    /**
     * Executes a named query returning a single result (for example, SELECT count(x) from X). The result
     * is casted to the requested type
     * @param namedQueryId name of the named query
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the query's single result, casted to the requested type. Cast is unchecked
     * @param <S>
     */
    public final <S> S executeSingleResultNamedQuery(String namedQueryId, Consumer<Query> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);

        EntityManager em = getEntityManager();
        try
        {
            Query query = em.createNamedQuery(namedQueryId);
            queryConfig.accept(query);
            return (S) query.getSingleResult();

        } catch (Exception ex)
        {
            logger.error("Error while executing named query", ex);
            return null;
        } finally
        {
            em.close();
        }
    }


    /**
     * Called each time one or more entities are inserted
     *
     * @param entities entities being inserted
     * @return whether the operation was successful. If false, the transaction is rolled back.
     */
    protected boolean onEntityInserted(Collection<T> entities)
    {
        return true;
    }



    /**
     * Called each time one or more entities are updated
     *
     * @param entities entities being updated
     * @return whether the operation was successful. If false, the transaction is rolled back.
     */
    public boolean onEntitiesUpdated(Collection<T> entities)
    {
        return true;
    }



    /**
     * Called each time one or more entities are removed
     *
     * @param entities entities being removed
     * @return whether the operation was successful. If false, the transaction is rolled back.
     */
    public boolean onEntitiesRemoved(Collection<T> entities)
    {
        return true;
    }
}
