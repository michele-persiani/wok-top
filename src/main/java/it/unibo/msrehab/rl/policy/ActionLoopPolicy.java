package it.unibo.msrehab.rl.policy;

import java.util.List;


/**
 * Policy that executes a list of actions in a loop. When the last action in the list is executed, the first action is
 * selected again
 *
 * @param <S>
 * @param <A>
 */
public class ActionLoopPolicy<S, A> implements IActionPolicy<S, A>
{
    private final List<A> actions;
    private int currentIndex = 0;

    public ActionLoopPolicy(List<A> actions)
    {
        this.actions = actions;
    }

    @Override
    public A selectAction(S state)
    {
        if (actions.isEmpty())
            return null;
        A selected = actions.get(currentIndex);
        currentIndex++;
        if (currentIndex >= actions.size())
            currentIndex = 0;
        return selected;
    }

    public void reset()
    {
        setCurrentIndex(0);
    }

    public int getCurrentIndex()
    {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex)
    {
        this.currentIndex = Math.max(0, Math.min(currentIndex, actions.size() - 1));
    }
}
