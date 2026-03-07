package it.unibo.msrehab.rl.model;


import it.unibo.msrehab.rl.utils.Tuple;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A utility class for handling JPA-based queries such as joins.
 * This class provides methods to facilitate querying operations.
 */
public class JPAQueries implements IModel
{
    private final IModel model;

    private JPAQueries(IModel model)
    {
        this.model = model;
    }

    public static JPAQueries create(IModel forModel)
    {
        return new JPAQueries(forModel);
    }

    public <T> IEntityController<T> getEntityController(Class<T> entityClass)
    {
        return model.getEntityController(entityClass);
    }

    public <T0, T1> List<Tuple<T0, T1>> joinEntities(Class<T0> entity0, Class<T1> entity1, BiPredicate<T0, T1> joinCondition)
    {
        List<Tuple<T0, T1>> result = new ArrayList<>();
        Collection<T0> coll0 = getEntityController(entity0).getAllEntities();
        Collection<T1> coll1 = getEntityController(entity1).getAllEntities();

        for (T0 e0 : coll0)
            for (T1 e1 : coll1)
                if (joinCondition.test(e0, e1))
                    result.add(new Tuple<>(e0, e1));
        return result;
    }

    public <T0, T1> List<Tuple<T0, T1>> joinEntities(Class<T0> entity0, Class<T1> entity1, Function<T0, Object> key0Function, Function<T1, Object> key1Function)
    {
        return joinEntities(entity0, entity1, (e0, e1) -> Objects.equals(key0Function.apply(e0), key1Function.apply(e1)));
    }
}
