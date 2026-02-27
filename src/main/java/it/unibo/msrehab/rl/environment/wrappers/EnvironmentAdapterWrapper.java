package it.unibo.msrehab.rl.environment.wrappers;

import it.unibo.msrehab.rl.common.IAdapter;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.environment.IEnvironment;


/**
 * Adapter to convert an environment state/action types from (S0, A0) to (S1, A1)
 *
 * @param <S0>
 * @param <A0>
 * @param <S1>
 * @param <A1>
 */
public class EnvironmentAdapterWrapper<S0, A0, S1, A1> extends WrapperEnvironment<S0, A0> implements IEnvironment<S1, A1>
{
    private final IAdapter<S0, S1> stateAdapter;
    private final IAdapter<A0, A1> actionAdapter;

    public EnvironmentAdapterWrapper(IAdapter<S0, S1> stateAdapter, IAdapter<A0, A1> actionAdapter)
    {
        this.stateAdapter = stateAdapter;
        this.actionAdapter = actionAdapter;
    }

    @Override
    public StateTransition<S1, A1> reset()
    {
        StateTransition<S0, A0> st = env.reset();
        return convertTransition(st);
    }

    @Override
    public StateTransition<S1, A1> step(A1 action)
    {
        StateTransition<S0, A0> st = env.step(actionAdapter.inverseApply(action));
        return convertTransition(st);
    }

    private StateTransition<S1, A1> convertTransition(StateTransition<S0, A0> st)
    {
        S0 fromState = st.getFromState();
        S0 toState = st.getToState();
        A0 action = st.getAction();
        return new StateTransition.Builder<S1, A1>()
                .copyValuesWithoutStateAction(st)
                .setFromState(fromState != null ? stateAdapter.apply(fromState) : null)
                .setToState(toState != null ? stateAdapter.apply(toState) : null)
                .setAction(action != null ? actionAdapter.apply(action) : null)
                .build();
    }

    @Override
    public boolean isDone()
    {
        return env.isDone();
    }

    @Override
    public S1 getState()
    {
        return stateAdapter.apply(env.getState());
    }
}
