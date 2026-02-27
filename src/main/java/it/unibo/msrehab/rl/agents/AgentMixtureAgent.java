package it.unibo.msrehab.rl.agents;

import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.utils.rv.RandomVariableFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Mixture of agents. Each agent has a weight associated to it. When selecting an action an agent is randomly selected
 * based on its weight.
 *
 * @param <S>
 * @param <A>
 */
public class AgentMixtureAgent<S, A> implements IAgent<S, A>
{
    private final Map<IAgent<S, A>, Double> agentsWeights = new HashMap<>();


    public AgentMixtureAgent<S, A> setAgentWeight(IAgent<S, A> agent, double weight)
    {
        agentsWeights.put(agent, weight);
        return this;
    }

    @Override
    public void fit(List<StateTransition<S, A>> history)
    {
        agentsWeights.keySet().forEach(agent -> agent.fit(history));
    }

    @Override
    public void reset()
    {
        IAgent.super.reset();
        agentsWeights.forEach((a, w) -> a.reset());
    }

    @Override
    public A selectAction(S state)
    {
        if(agentsWeights.isEmpty())
            throw new IllegalStateException("No agent has been set");
        return RandomVariableFactory.from(agentsWeights).sample().selectAction(state);
    }
}
