package it.unibo.msrehab.rl.environment;

import it.unibo.msrehab.rl.common.IAdapter;
import it.unibo.msrehab.rl.environment.wrappers.EnvironmentAdapterWrapper;
import it.unibo.msrehab.rl.environment.wrappers.EnvironmentHistoryWrapper;
import it.unibo.msrehab.rl.history.HistoryManager;
import it.unibo.msrehab.rl.mdp.MDPAdapter;

import java.util.function.Function;


public class EnvironmentBuilder<S, A>
{
    private final IEnvironment<S, A> env;

    private EnvironmentBuilder(IEnvironment<S, A> env)
    {
        this.env = env;
    }


    public EnvironmentBuilder<S, A> setHistoryWrapper(HistoryManager<S, A> historyManager)
    {
        EnvironmentHistoryWrapper<S, A> historyWrapper = new EnvironmentHistoryWrapper<>(historyManager);
        historyWrapper.setWrappedEnvironment(env);
        return new EnvironmentBuilder<>(historyWrapper);
    }

    public <S0> EnvironmentBuilder<S0, A> setStateAdapter(Function<S, S0> stateAdapter)
    {
        EnvironmentAdapterWrapper<S, A, S0, A> adapterEnvironment = new EnvironmentAdapterWrapper<>(
                IAdapter.from(stateAdapter, x -> null),
                IAdapter.identity()
        );
        adapterEnvironment.setWrappedEnvironment(env);
        return new EnvironmentBuilder<>(adapterEnvironment);
    }

    public <A0> EnvironmentBuilder<S, A0> setActionAdapter(Function<A, A0> actionAdapter, Function<A0, A> actionInvAdapter)
    {
        EnvironmentAdapterWrapper<S, A, S, A0> adapterEnvironment = new EnvironmentAdapterWrapper<>(
                IAdapter.identity(),
                IAdapter.from(actionAdapter, actionInvAdapter)
        );
        adapterEnvironment.setWrappedEnvironment(env);
        return new EnvironmentBuilder<>(adapterEnvironment);
    }

    public EnvironmentBuilder<Integer, Integer> setMdpAdapter(MDPAdapter<S, A> mdpAdapter)
    {
        return setStateAdapter(
                s -> mdpAdapter.getStateAdapter().apply(s)
        )
                .setActionAdapter(
                        a -> mdpAdapter.getActionAdapter().apply(a),
                        idx -> mdpAdapter.getActionAdapter().inverseApply(idx)
                );
    }

    public IEnvironment<S, A> build()
    {
        return env;
    }


    public static <S, A> EnvironmentBuilder<S, A> from(IEnvironment<S, A> env)
    {
        return new EnvironmentBuilder<>(env);
    }
}
