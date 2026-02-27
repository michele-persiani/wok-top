package it.unibo.msrehab.rl.environment.wrappers;

import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.environment.IEnvironment;
import it.unibo.msrehab.rl.history.HistoryManager;


public class EnvironmentHistoryWrapper<S, A> extends WrapperEnvironment<S, A> implements IEnvironment<S, A>
{
    private final HistoryManager<S, A> historyManager;

    public EnvironmentHistoryWrapper(HistoryManager<S, A> historyManager)
    {
        this.historyManager = historyManager;
    }

    @Override
    public StateTransition<S, A> reset()
    {
        StateTransition<S, A> tr = env.reset();
        historyManager.addTransition(tr);
        return tr;
    }

    @Override
    public StateTransition<S, A> step(A action)
    {
        StateTransition<S, A> tr = env.step(action);
        historyManager.addTransition(tr);
        return tr;
    }

    @Override
    public boolean isDone()
    {
        return env.isDone();
    }

    @Override
    public S getState()
    {
        return env.getState();
    }
}
