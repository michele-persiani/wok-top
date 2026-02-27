package it.unibo.msrehab.rl.history;

import it.unibo.msrehab.rl.common.StateTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HistoryManager<S, A>
{
    private final List<StateTransition<S, A>> history = new ArrayList<>();

    private final IHistoryPersistence<S, A> persistence;

    public HistoryManager(IHistoryPersistence<S, A> persistence)
    {
        this.persistence = persistence;
    }


    public List<StateTransition<S, A>> getHistory()
    {
        return history;
    }

    public void clearHistory()
    {
        history.clear();
    }

    public boolean persist(boolean append)
    {
        return persistence.save(this, append);
    }

    public boolean loadPersisted(boolean append)
    {
        return persistence.load(this, append);
    }



    // History manipulation

    public void addTransition(StateTransition<S, A> transition)
    {
        int idx = history.stream()
                .filter(tr -> tr.getTransitionUid().equals(transition.getTransitionUid()))
                .findFirst()
                .map(history::indexOf)
                .orElse(-1);
        if(idx >= 0)
            history.remove(idx);
        history.add(transition);
    }

    public void addTransitions(List<StateTransition<S, A>> transitions)
    {
        transitions.forEach(this::addTransition);
    }

    public void modifyTransition(int index, Consumer<StateTransition.Builder<S, A>> builder)
    {
        if(index < 0)
            index = history.size() + index; // negative index identifies elements from the tail
        if(index < 0 || index >= history.size())
            return;
        int finalIndex = index;
        modifyTransitions(tr -> history.indexOf(tr) == finalIndex, builder);
    }

    public void modifyTransitions(Predicate<StateTransition<S, A>> filter, Consumer<StateTransition.Builder<S, A>> builder)
    {
        for(int i = 0; i < history.size(); i++)
        {
            StateTransition<S, A> tr = history.get(i);
            if(filter.test(tr))
            {
                StateTransition.Builder<S, A> b = new StateTransition.Builder<S, A>()
                        .copyValues(tr);
                builder.accept(b);
                history.set(i, b.build());
            }
        }
    }

    public void removeTransitions(Predicate<StateTransition<S, A>> filter)
    {
        history.removeIf(filter);
    }

    public void removeTransitions(int index)
    {
        if(index < 0)
            index = history.size() + index; // index identifies elements from the tail
        if(index < 0 || index >= history.size())
            return;
        history.remove(index);
    }
}
