package it.unibo.msrehab.rl;

import it.unibo.msrehab.rl.agents.IAgent;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.environment.EnvironmentBuilder;
import it.unibo.msrehab.rl.environment.IEnvironment;
import it.unibo.msrehab.rl.environment.wrappers.EnvironmentHistoryWrapper;
import it.unibo.msrehab.rl.history.HistoryManager;
import it.unibo.msrehab.rl.history.IHistoryPersistence;
import it.unibo.msrehab.rl.policy.IActionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * Represents a reinforcement learning agent that interacts with an environment, manages its history,
 * and performs action and planning operations using defined policies.
 *
 * @param <S> the state type of the environment
 * @param <A> the action type that the agent can perform in the environment
 */
public class RLAgent<S, A>
{
    private final IAgent<S, A> agent;

    private final HistoryManager<S, A> historyManager;

    private final IEnvironment<S, A> environment;

    public RLAgent(
            IEnvironment<S, A> environment,
            IAgent<S, A> agent,
            IHistoryPersistence<S, A> historyPersistence
    )
    {
        this.agent = agent;
        this.historyManager = new HistoryManager<>(historyPersistence);
        this.environment = EnvironmentBuilder.from(environment)
                .setHistoryWrapper(historyManager)
                .build();
    }

    /**
     * Retrieves the agent associated with the RLAgent.
     *
     * @return the instance of {@code IAgent<S, A>} representing the agent used in the reinforcement learning process.
     */
    public IAgent<S, A> getAgent()
    {
        return agent;
    }


    /**
     * Retrieves the environment managed by the RLAgent.
     *
     * @return the instance of {@code IEnvironment<S, A>} associated with the agent, which represents
     *         the environment in which the agent operates.
     */
    public IEnvironment<S, A> getEnvironment()
    {
        return environment;
    }

    /**
     * Retrieves the {@code HistoryManager} instance associated with the agent.
     * This manager records each action performed on the environment and
     * is responsible for storing, managing, and persisting the history
     * of state transitions.
     *
     *
     * @return the {@code HistoryManager} instance used by the agent.
     */
    public HistoryManager<S, A> getHistoryManager()
    {
        return historyManager;
    }


    /**
     * Resets the agent and the environment to their initial states.
     * The agent's state and environment's state are reset through their respective reset methods.
     *
     * @return the initial {@code StateTransition<S, A>} of the environment after the reset.
     */
    public StateTransition<S, A> reset()
    {
        agent.reset();
        return environment.reset();
    }


    // Step functions

    /**
     * Executes a step in the environment by applying the given action and returns the resulting state transition.
     *
     * @param action the action to be applied to the environment.
     * @return the corresponding state transition
     */
    public StateTransition<S, A> step(A action)
    {
        return environment.step(action);
    }


    /**
     * Executes one step in the environment by selecting an action using the given policy.
     *
     * @param policy the policy used to select an action.
     * @return the state transition resulting from executing the selected action in the environment.
     */
    public StateTransition<S, A> step(IActionPolicy<S, A> policy)
    {
        A action = policy.selectAction(environment.getState());
        return step(action);
    }


    // Plan functions

    /**
     * Executes a sequence of steps from the current environment state, using the given policy.
     *
     * @param maxSteps maximum number of steps to perform
     * @param stopOnDone stop executing actions once the environment is done
     * @param policy policy to sample actions from
     * @return list of state transitions resulting from the execution of the policy. The number of transitions will be
     * lesser or equal to the number of actions provided.
     */
    public List<StateTransition<S, A>> plan(int maxSteps, boolean stopOnDone, IActionPolicy<S, A> policy)
    {
        return IntStream.range(0, maxSteps)
                .mapToObj(i -> stopOnDone && environment.isDone() ? null : step(policy))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Executes a list of actions from the current environment state.
     *
     * @param stopOnDone stop executing actions once the environment is done
     * @param actions    list of actions to perform
     * @return list of state transitions resulting from the execution of the actions. The number of transitions will be
     * lesser or equal to the number of actions provided.
     */
    public List<StateTransition<S, A>> plan(boolean stopOnDone, List<A> actions)
    {
        AtomicInteger counter = new AtomicInteger(0);
        return plan(actions.size(), stopOnDone, s -> actions.get(counter.getAndIncrement()));
    }


    // Episode sampling functions

    /**
     * Sample one episode from the environment using the given policy.
     * The environment is reset before the episode starts.
     *
     * @param maxSteps max episode lengths (executions end when environment is done)
     * @param policy   policy to sampel actions from
     * @return episode
     */
    public List<StateTransition<S, A>> sampleEpisode(int maxSteps, IActionPolicy<S, A> policy)
    {
        List<StateTransition<S, A>> trajectory = new ArrayList<>();
        trajectory.add(reset());
        trajectory.addAll(plan(maxSteps, true, policy));
        return trajectory;
    }

    /**
     * Sample {@code numEpisodes} episodes from the environment using the given policy.}
     * The environment is reset before each episode starts.
     *
     * @param numEpisodes     number of episodes to sample
     * @param maxEpisodeSteps max episode lengths (executions end when environment is done)
     * @param policy          policy to sampel actions from
     * @return list of sampled episodes.
     */
    public List<StateTransition<S, A>> sampleEpisodes(int numEpisodes, int maxEpisodeSteps, IActionPolicy<S, A> policy)
    {
        return IntStream.range(0, numEpisodes)
                .mapToObj(i -> sampleEpisode(maxEpisodeSteps, policy))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    // Agent-based functions


    /**
     * Fits the agent on the history of state transitions currently stored in the history manager.
     * @return number of transitions the agent was fitted on
     */
    public int agentFitHistory()
    {
        List<StateTransition<S, A>> history = historyManager.getHistory();
        agent.fit(history);
        return history.size();
    }


    /**
     * Executes a single step in the environment using the agent's policy.
     *
     * @return the resulting state transition
     */
    public StateTransition<S, A> agentStep()
    {
        return step(agent);
    }

    /**
     * Executes a sequence of steps in the environment, starting from the current state, using the agent's policy.
     *
     * @param numSteps the maximum number of steps to perform
     * @param stopOnDone whether to stop executing actions if the environment indicates it is done
     * @return a list of state transitions resulting from the execution of the agent's policy.
     *         The number of transitions will be less than or equal to the specified number of steps.
     */
    public List<StateTransition<S, A>> agentPlan(int numSteps, boolean stopOnDone)
    {
        return plan(numSteps, stopOnDone, agent);
    }

    /**
     * Samples one episode from the environment using the agent's default policy.
     * The environment is reset before the episode starts. The number of steps in the episode
     * will be limited by the specified maximum.
     *
     * @param maxSteps the maximum number of steps to perform during the episode,
     *                 or the limit for episode length.
     * @return a list of {@code StateTransition<S, A>} objects representing the state transitions
     *         that occurred during the sampled episode.
     */
    public List<StateTransition<S, A>> agentSampleEpisode(int maxSteps)
    {
        return sampleEpisode(maxSteps, agent);
    }

    /**
     * Samples multiple episodes using the agent's default action-selection policy.
     * Each episode will execute up to the provided maximum number of steps, or terminate earlier if the environment is done.
     * The environment is reset prior to the start of each episode.
     *
     * @param numEpisodes     the number of episodes to sample
     * @param maxEpisodeSteps the maximum number of steps to execute per episode
     * @return a list of state transitions representing all sampled episodes
     */
    public List<StateTransition<S, A>> agentSampleEpisodes(int numEpisodes, int maxEpisodeSteps)
    {
        return sampleEpisodes(numEpisodes, maxEpisodeSteps, agent);
    }
}
