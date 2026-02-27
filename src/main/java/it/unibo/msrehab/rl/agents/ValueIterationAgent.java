
package it.unibo.msrehab.rl.agents;


import it.unibo.msrehab.rl.rewardmodel.RewardModel;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.common.ValueIteration;
import it.unibo.msrehab.rl.mdp.MDPAdapter;
import it.unibo.msrehab.rl.policy.IActionPolicy;

import java.util.*;
import java.util.function.Function;


/**
 * Agent leveraging the value-iteration algorithm
 * @param <S>
 * @param <A>
 */
public class ValueIterationAgent<S, A> implements IAgent<S, A>
{
    private final ValueIteration<S, A> valueIteration;


    private final Function<ValueIteration<S, A>, IActionPolicy<S, A>> actionPolicyFactory;


    public ValueIterationAgent(
            RewardModel<S, A> rewardModel,
            MDPAdapter<S, A> mdpAdapter,
            double discountFactor,
            double tolerance,
            int maxIterations,
            Function<ValueIteration<S, A>, IActionPolicy<S, A>> actionPolicyFactory
    ) {
        this.valueIteration = new ValueIteration<>(
                rewardModel,
                mdpAdapter,
                discountFactor,
                tolerance,
                maxIterations
        );
        this.actionPolicyFactory = actionPolicyFactory;
    }

    @Override
    public void fit(List<StateTransition<S, A>> history)
    {
        valueIteration.reset();
        valueIteration.update(history);
    }

    @Override
    public A selectAction(S state)
    {
        return actionPolicyFactory.apply(valueIteration).selectAction(state);
    }

    public Map<S, Double> getStateValues()
    {
        return valueIteration.getStateValues();
    }
}