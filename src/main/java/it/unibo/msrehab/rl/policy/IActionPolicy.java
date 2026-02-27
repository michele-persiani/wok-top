package it.unibo.msrehab.rl.policy;





/**
 * Interface representing an action selection policy for an environment. It provides methods
 * to determine actions based on the current state.
 *
 * @param <S> the type representing the state of the environment
 * @param <A> the type representing the actions that can be selected
 */
public interface IActionPolicy<S, A>
{
    /**
     * Selects an action based on the current state of the environment.
     *
     * @param state the current state of the environment
     * @return the selected action
     */
    A selectAction(S state);
}
