package it.unibo.msrehab.rl.rewardmodel;

import it.unibo.msrehab.rl.common.StateTransition;

import java.util.List;


/*

attention1 no
attention2 no
attention3 no
attention4 no
reflexes1 si
memory1 no
memory2 no
nback no
memory4 no
memory5 no





 */


public class LambdaRewardModel<S, A> extends RewardModel<S, A>
{
    private final IRewardFunction<S, A> rewardFunction;

    public LambdaRewardModel(IRewardFunction<S, A> rewardModel)
    {
        this.rewardFunction = rewardModel;
    }

    @Override
    public double getReward(S fromState, A action, S toState)
    {
        return rewardFunction.getReward(fromState, action, toState);
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void fit(List<StateTransition<S, A>> stateTransitions)
    {

    }
}
