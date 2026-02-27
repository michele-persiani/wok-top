package it.unibo.msrehab.rl.history.impl;

import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.history.HistoryManager;
import it.unibo.msrehab.rl.history.IHistoryPersistence;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHistoryPersistence<S, A> implements IHistoryPersistence<S, A>
{
    @Override
    public boolean save(HistoryManager<S, A> history, boolean append)
    {
        try
        {
            List<StateTransition<S, A>> currentHistory = history.getHistory();
            List<StateTransition<S, A>> persistedHistory = new ArrayList<>();

            if(append)
            {
                List<StateTransition<S, A>> previousHistory = loadHistory();
                currentHistory.addAll(previousHistory);
            }

            currentHistory.stream()
                    .filter(st -> !persistedHistory.contains(st))
                    .forEach(persistedHistory::add);

            saveHistory(persistedHistory);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean load(HistoryManager<S, A> history, boolean append)
    {
        try
        {
            List<StateTransition<S, A>> loadedHistory = loadHistory();
            if(!append)
                history.clearHistory();
            history.addTransitions(loadedHistory);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    protected abstract List<StateTransition<S, A>> loadHistory() throws Exception;

    protected abstract void saveHistory(List<StateTransition<S, A>> history) throws Exception;
}
