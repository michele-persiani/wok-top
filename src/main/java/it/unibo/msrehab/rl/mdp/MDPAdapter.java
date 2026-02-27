package it.unibo.msrehab.rl.mdp;


import it.unibo.msrehab.rl.common.IAdapter;
import it.unibo.msrehab.rl.common.StateTransition;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


public class MDPAdapter<S, A>
{
    private final ICollectionAdapter<S> stateAdapter;
    private final ICollectionAdapter<A> actionAdapter;
    private final BiFunction<S, A, Map<S, Double>> transitionProbabilityFunction;

    public MDPAdapter(
            ICollectionAdapter<S> stateAdapter,
            ICollectionAdapter<A> actionAdapter,
            BiFunction<S, A, Map<S, Double>> transitionProbabilityFunction
    )
    {
        this.stateAdapter = stateAdapter;
        this.actionAdapter = actionAdapter;
        this.transitionProbabilityFunction = transitionProbabilityFunction;
        if (!isConsistent(this))
            throw new IllegalArgumentException("The provided adapters are not consistent");
    }

    public ICollectionAdapter<S> getStateAdapter()
    {
        return stateAdapter;
    }


    public ICollectionAdapter<A> getActionAdapter()
    {
        return actionAdapter;
    }


    public IAdapter<StateTransition<S, A>, StateTransition<Integer, Integer>> getTransitionAdapter()
    {
        return IAdapter.from(
                st -> new StateTransition.Builder<Integer, Integer>()
                        .copyValuesWithoutStateAction(st)
                        .setFromState(stateAdapter.apply(st.getFromState()))
                        .setAction(actionAdapter.apply(st.getAction()))
                        .setToState(stateAdapter.apply(st.getToState()))
                        .build(),
                st -> new StateTransition.Builder<S, A>()
                        .copyValuesWithoutStateAction(st)
                        .setFromState(stateAdapter.inverseApply(st.getFromState()))
                        .setAction(actionAdapter.inverseApply(st.getAction()))
                        .setToState(stateAdapter.inverseApply(st.getToState()))
                        .build()
        );
    }


    public Map<S, Double> getTransitionProbabilities(S state, A action)
    {
        return transitionProbabilityFunction.apply(state, action);
    }

    public Map<Integer, Double> getTransitionProbabilities(Integer state, Integer action)
    {
        return getTransitionProbabilities(
                stateAdapter.inverseApply(state),
                actionAdapter.inverseApply(action)
        )
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> stateAdapter.apply(e.getKey()), Map.Entry::getValue));
    }


    public Collection<A> getActions()
    {
        return actionAdapter.getElements();
    }

    public Collection<S> getStates()
    {
        return stateAdapter.getElements();
    }


    public Collection<A> getActions(S state)
    {
        return getActions()
                .stream()
                .filter(a -> getTransitionProbabilities(state, a)
                        .values()
                        .stream()
                        .anyMatch(p -> p > 0))
                .collect(Collectors.toList());
    }


    /**
     * Checks if mdp is consistent, namely if the adapters are consistent with respect to the provided equality predicates.
     * Adapters are consistent if transforming an action or state to its index and then back, the same action or state is obtained.
     * Here object equal() method is used to compare the objects.
     *
     * @param mdpAdapter
     * @param <S>
     * @param <A>
     * @return
     */
    public static <S, A> boolean isConsistent(MDPAdapter<S, A> mdpAdapter)
    {
        return isConsistent(mdpAdapter, Objects::equals, Objects::equals);
    }

    /**
     * Checks if mdp is consistent, namely if the adapters are consistent with respect to the provided equality predicates.
     * Adapters are consistent if transforming an action or state to its index and then back, the same action or state is obtained.
     * Also the transition function must return a valid probability distribution over the states.
     *
     * @param adapter
     * @param stateEquality
     * @param actionEquality
     * @param <S>
     * @param <A>
     * @return
     */
    public static <S, A> boolean isConsistent(MDPAdapter<S, A> adapter, BiPredicate<S, S> stateEquality, BiPredicate<A, A> actionEquality)
    {
        for (int i = 0; i < adapter.stateAdapter.countElements(); i++)
        {
            S s0 = adapter.stateAdapter.inverseApply(i);
            int idx = adapter.stateAdapter.apply(s0);
            S s1 = adapter.stateAdapter.inverseApply(idx);
            if (idx != i || !stateEquality.test(s0, s1))
                return false;
        }

        for (int i = 0; i < adapter.actionAdapter.countElements(); i++)
        {
            A a0 = adapter.actionAdapter.inverseApply(i);
            int idx = adapter.actionAdapter.apply(a0);
            A a1 = adapter.actionAdapter.inverseApply(idx);
            if (idx != i || !actionEquality.test(a0, a1))
                return false;

            for (int j = 0; j < adapter.stateAdapter.countElements(); j++)
            {
                S s0 = adapter.stateAdapter.inverseApply(j);

                boolean transitionConsistent = adapter.transitionProbabilityFunction.apply(s0, a1)
                        .keySet()
                        .stream()
                        .allMatch(s1 ->
                        {
                            int sIdx = adapter.stateAdapter.apply(s1);
                            return 0 <= sIdx && sIdx < adapter.stateAdapter.countElements();
                        });

                if (!transitionConsistent)
                    return false;

            }
        }
        return true;
    }
}
