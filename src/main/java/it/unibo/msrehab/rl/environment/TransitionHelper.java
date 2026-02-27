package it.unibo.msrehab.rl.environment;

import it.unibo.msrehab.rl.common.StateTransition;

import java.util.UUID;
import java.util.function.Supplier;


/**
 * Helper class to create transitions for an environment.
 *
 * @param <S>
 * @param <A>
 */
public class TransitionHelper<S, A>
{
    private String environmentUid = UUID.randomUUID().toString();

    private String episodeUid = UUID.randomUUID().toString();

    private Supplier<String> episodeUidSupplier = () -> UUID.randomUUID().toString();

    private Supplier<String> transitionUidSupplier = () -> UUID.randomUUID().toString();


    private S currentState = null;

    public TransitionHelper() {}

    public TransitionHelper(String environmentUid)
    {
        this.environmentUid = environmentUid;
    }


    /**
     * Called when the environment is reset to prepare for a new episode.
     *
     * @param state the state to which the environment should be reset
     * @return a new {@code StateTransition} representing the reset operation, marking the
     * beginning of a new episode
     */
    public StateTransition<S, A> onReset(S state)
    {
        currentState = state;
        episodeUid = episodeUidSupplier.get();
        return createTransition(null, null, state, 0d, true, false);
    }

    /**
     * Handles a step in the environment by transitioning to the specified state.
     *
     * @param action the action performed in the current state
     * @param to the state resulting from the action
     * @param reward the reward received for taking the action
     * @param done whether the step represents the end of an episode
     * @return a {@code StateTransition} object representing the transition,
     *         which includes details about the previous state, the action,
     *         the resulting state, the reward, and whether the episode has ended
     */
    public StateTransition<S, A> onStep(A action, S to, Double reward, boolean done)
    {
        S prevState = currentState;
        currentState = to;
        return createTransition(prevState, action, to, reward, false, done);
    }

    /**
     * Handles an invalid step in the environment by creating a state transition
     * back to the current state with a negative infinite reward, marking the transition
     * as "done" to indicate termination of the episode.
     *
     * @param action the action that resulted in an invalid step
     * @return a {@code StateTransition} object representing the invalid step,
     *         with the current state as both the source and destination,
     *         a reward of negative infinity, and "done" set to true
     */
    public StateTransition<S, A> onInvalidStep(A action)
    {
        return createTransition(currentState, action, currentState, Double.NEGATIVE_INFINITY, false, true);
    }

    /**
     * Creates a state transition with the specified parameters.
     *
     * @param from the state before the transition
     * @param action the action that causes the transition
     * @param to the state after the transition
     * @param reward the reward associated with the transition
     * @param reset whether the transition represents a reset operation
     * @param done whether the transition represents the end of an episode
     * @return a {@code StateTransition} object representing the specified transition
     */
    private StateTransition<S, A> createTransition(S from, A action, S to, Double reward, boolean reset, boolean done)
    {
        return new StateTransition.Builder<S, A>()
                .setEnvironmentUid(environmentUid)
                .setEpisodeUid(episodeUid)
                .setTransitionUid(transitionUidSupplier.get())
                .setTimestampEpochMillis(System.currentTimeMillis())
                .setFromState(from)
                .setAction(action)
                .setToState(to)
                .setReward(reward)
                .setReset(reset)
                .setDone(done)
                .build();
    }

    /**
     * Sets the supplier responsible for generating unique transition identifiers for state transitions.
     * Default generates random UUIDs.
     *
     * @param transitionUidSupplier the supplier instance to generate unique identifiers for transitions
     * @return the current instance of {@code TransitionHelper}, allowing for method chaining
     */
    public TransitionHelper<S, A> setTransitionUidSupplier(Supplier<String> transitionUidSupplier)
    {
        this.transitionUidSupplier = transitionUidSupplier;
        return this;
    }

    /**
     * Sets the unique identifier for the environment.
     * Default generates random UUIDs.
     * @param environmentUid the unique identifier to be assigned to the environment
     * @return the updated {@code TransitionHelper} instance, allowing for method chaining
     */
    public TransitionHelper<S, A> setEnvironmentUid(String environmentUid)
    {
        this.environmentUid = environmentUid;
        return this;
    }

    /**
     * Sets the supplier responsible for generating unique identifiers (UIDs) for episodes.
     * The episode UID is updated immediately using the provided supplier.
     *
     * @param episodeUidSupplier a {@code Supplier<String>} that provides unique identifiers for episodes
     * @return the current instance of {@code TransitionHelper<S, A>} to allow method chaining
     */
    public TransitionHelper<S, A> setEpisodeUidSupplier(Supplier<String> episodeUidSupplier)
    {
        this.episodeUidSupplier = episodeUidSupplier;
        episodeUid = episodeUidSupplier.get();
        return this;
    }

    /**
     * Retrieves the current state of the environment. This method returns the
     * state that is presently stored. The current state is updated after each call to {@code onStep()} and {@code onReset()}.
     *
     * @return the current state of the environment.
     */
    public S getCurrentState()
    {
        return this.currentState;
    }
}
