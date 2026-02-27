package it.unibo.msrehab.rl.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateBuilder<T> implements Predicate<T>
{
    private final List<Predicate<T>> predicates = new ArrayList<>();

    public PredicateBuilder<T> add(Predicate<T> predicate)
    {
        predicates.add(predicate);
        return this;
    }

    public PredicateBuilder<T> isNull()
    {
        return add(Objects::isNull);
    }

    public PredicateBuilder<T> isNotNull()
    {
        return add(Objects::nonNull);
    }

    public <V> PredicateBuilder<T> equals(Function<T, V> getter, V other)
    {
        return add(t -> Objects.equals(getter.apply(t), other));
    }

    public <V> PredicateBuilder<T> notEquals(Function<T, V> getter, V other)
    {
        return add(t -> !Objects.equals(getter.apply(t), other));
    }

    public PredicateBuilder<T> isContained(Collection<T> coll)
    {
        return add(coll::contains);
    }

    public PredicateBuilder<T> isNotContained(Collection<T> coll)
    {
        return add(t -> !coll.contains(t));
    }

    public <V> PredicateBuilder<T> isContained(Function<T, V> getter, Collection<V> coll)
    {
        return add(t -> coll.contains(getter.apply(t)));
    }

    public <V> PredicateBuilder<T> isNotContained(Function<T, V> getter, Collection<V> coll)
    {
        return add(t -> !coll.contains(getter.apply(t)));
    }


    @Override
    public boolean test(T t)
    {
        return predicates.stream().allMatch(p -> p.test(t));
    }
}
