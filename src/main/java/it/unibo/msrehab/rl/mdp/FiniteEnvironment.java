package it.unibo.msrehab.rl.mdp;

import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.environment.IEnvironment;
import it.unibo.msrehab.rl.environment.TransitionHelper;
import it.unibo.msrehab.rl.utils.ScoreComparator;
import it.unibo.msrehab.rl.utils.rv.IRandomVariable;
import it.unibo.msrehab.rl.utils.rv.RandomVariableFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class FiniteEnvironment<S, A> implements IEnvironment<S, A>
{
    private final TransitionHelper<S, A> transitionHelper = new TransitionHelper<>(getEnvironmentName());

    private boolean isReset = false;
    private List<Object> states;
    private List<Object> actions;
    private boolean initialized = false;

    public abstract String getEnvironmentName();

    public abstract Iterator<S> states();

    public abstract Iterator<A> actions();

    public abstract Map<S, Double> getTransitionProbabilities(S state, A action);

    protected abstract boolean isDone(S state);

    protected abstract double getReward(S fromState, A action, S toState);

    protected abstract boolean isStartState(S state);

    protected abstract Object getStateKey(S state);

    protected abstract Object getActionKey(A action);

    private void initialize()
    {
        states = StreamSupport.stream(Spliterators.spliteratorUnknownSize(states(), 0), false)
                .map(this::getStateKey)
                .collect(Collectors.toList());
        actions = StreamSupport.stream(Spliterators.spliteratorUnknownSize(actions(), 0), false)
                .map(this::getActionKey)
                .collect(Collectors.toList());

        if (states.isEmpty())
            throw new IllegalStateException("No states found");
        if (actions.isEmpty())
            throw new IllegalStateException("No actions found");

        if (states.stream().distinct().count() != states.size())
            throw new IllegalStateException("States must be unique");
        if (actions.stream().distinct().count() != actions.size())
            throw new IllegalStateException("Actions must be unique");
    }

    public MDPAdapter<S, A> getMDPAdapter()
    {
        return new MDPAdapter<>(
                new CollectionAdapter<>(states()),
                new CollectionAdapter<>(actions()),
                this::getTransitionProbabilities
        );
    }

    @Override
    public StateTransition<S, A> reset()
    {
        isReset = true;
        if(!initialized)
            initialize();
        initialized = true;

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(states(), 0), false)
                .filter(this::isStartState)
                .min(ScoreComparator.randomComparator())
                .map(transitionHelper::onReset)
                .orElseThrow(() -> new IllegalStateException("No start state found"));
    }

    @Override
    public S getState()
    {
        return transitionHelper.getCurrentState();
    }

    @Override
    public boolean isDone()
    {
        return isDone(transitionHelper.getCurrentState());
    }

    @Override
    public StateTransition<S, A> step(A action)
    {
        if(!isReset)
            throw new IllegalStateException("Environment must be reset before stepping");

        if(!actions.contains(getActionKey(action)))
            throw new IllegalStateException("Invalid action. Action not found in actions()");

        IRandomVariable<S> rv = RandomVariableFactory.from(
                getTransitionProbabilities(getState(), action)
        );


        if(!rv.values().stream().allMatch(s -> states.contains(getStateKey(s))))
            throw new IllegalStateException("Invalid transition probabilities. Some states were not supplied by states()");

        if(rv.values().isEmpty())
            return transitionHelper.onInvalidStep(action);

        S nextState = rv.sample();
        return transitionHelper.onStep(
                action,
                nextState,
                getReward(transitionHelper.getCurrentState(), action, nextState),
                isDone(nextState)
        );
    }
}
