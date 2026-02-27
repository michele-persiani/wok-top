package it.unibo.msrehab.rl.agents;


import it.unibo.msrehab.rl.policy.IActionPolicy;
import it.unibo.msrehab.rl.common.StateTransition;

import java.util.List;


/**
 * Base interface for RL agents.
 *
 * @param <S> type of states
 * @param <A> type of actions
 */
public interface IAgent<S, A> extends IActionPolicy<S, A>
{
    /**
     * Fits the agent to the given history of state transitions.
     *
     * @param history A list of {@code StateTransition<S, A>} objects to fit the agent with
     */
    void fit(List<StateTransition<S, A>> history);


    /**
     * Resets the policy's internal state. This method is called before each episode, for example, to reset LSTM
     * internal states.
     */
    default void reset()
    {
    }


    /**
     * Creates an agent from the given policy.
     *
     * @param policy wrapped policy
     * @param <S>    type of states
     * @param <A>    type of actions
     * @return an agent using the provided policy to select actions. Its fit() method will do nothing.
     */
    static <S, A> IAgent<S, A> fromPolicy(IActionPolicy<S, A> policy)
    {
        return new IAgent<S, A>()
        {

            @Override
            public A selectAction(S state)
            {
                return policy.selectAction(state);
            }

            @Override
            public void fit(List<StateTransition<S, A>> history)
            {

            }
        };
    }
}
