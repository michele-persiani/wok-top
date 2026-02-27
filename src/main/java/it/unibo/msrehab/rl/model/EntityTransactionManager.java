package it.unibo.msrehab.rl.model;

import it.unibo.msrehab.rl.utils.MethodLogger;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;





public class EntityTransactionManager
{
    private final MethodLogger logger = new MethodLogger(getClass());

    private final EntityManagerFactory entityManagerFactory;

    public EntityTransactionManager(String persistenceUnit)
    {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    /**
     * Use the entity manager to perform some kind of transaction with it, specified by {@code function}.
     *
     * @param function
     * @param <T>
     * @return an optional with the transaction result, or empty if an exception occurred
     */
    private <T> Optional<T> performTransaction(Function<EntityManager, T> function)
    {
        logger.info("Transaction begin");

        EntityManager em = entityManagerFactory.createEntityManager();

        try
        {
            em.getTransaction().begin();
            T result = function.apply(em);
            em.getTransaction().commit();
            logger.info("success");
            return Optional.ofNullable(result);
        } catch (Exception e)
        {
            logger.error("Exception: %s", e.getMessage());
            em.getTransaction().rollback();
            return Optional.empty();
        } finally
        {
            em.close();
            logger.flush();
        }
    }


    /**
     * Executes a select named query (need to be defined inside a @NamedQuery annotation of the entity) returning a result list.
     * @param namedQueryId
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the list of entities resulting from applying the query, or null if an error occurred
     */
    public final <T> Optional<List<T>> executeResultListNamedQuery(String namedQueryId, Class<T> entityClass, Consumer<TypedQuery<T>> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);

        return performTransaction(em -> {
            TypedQuery<T> query = em.createNamedQuery(namedQueryId, entityClass);
            queryConfig.accept(query);
            return query.getResultList();
        });
    }



    /**
     * Executes a select named query (need to be defined inside a @NamedQuery annotation of the entity) returning a result list.
     * @param namedQueryId
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the list of entities resulting from applying the query, or null if an error occurred
     */
    public final <T> Optional<List<T>> executeResultListNamedQuery(String namedQueryId, Consumer<Query> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);

        return performTransaction(em -> {
            Query query = em.createNamedQuery(namedQueryId);
            queryConfig.accept(query);
            return (List<T>) query.getResultList();
        });
    }


    /**
     * Executes a named query returning a single result (for example, SELECT count(x) from X). The result
     * is casted to the requested type
     * @param namedQueryId name of the named query
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the query's single result, casted to the requested type. Cast is unchecked
     */
    public final Optional<Object> executeSingleResultNamedQuery(String namedQueryId, Consumer<Query> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);

        return performTransaction(em -> {

            Query query = em.createNamedQuery(namedQueryId);
            queryConfig.accept(query);
            return query.getSingleResult();
        });
    }


    /**
     * Executes a named query returning a single result (for example, SELECT count(x) from X). The result
     * is casted to the requested type
     * @param namedQueryId name of the named query
     * @param resultType type of the result
     * @param queryConfig consumer to configure the query. e.g. to invoke .setParameter() for all query parameters
     * @return the query's single result, casted to the requested type. Cast is unchecked
     * @param <S>
     */
    public final <S> Optional<S> executeSingleResultNamedQuery(String namedQueryId, Class<S> resultType, Consumer<Query> queryConfig)
    {
        logger.info("Executing named query: " + namedQueryId);

        return performTransaction(em -> {
            TypedQuery<S> query = em.createNamedQuery(namedQueryId, resultType);
            queryConfig.accept(query);
            return query.getSingleResult();
        });
    }
}
