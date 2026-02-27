package it.unibo.msrehab.rl.rewardmodel;

import it.unibo.msrehab.rl.common.StateTransition;

import java.util.List;

public class LambdaRewardModel<S, A> extends RewardModel<S, A>
{

    private final TransitionFeatureFunction<S, A> rewardFunction;

    public LambdaRewardModel(TransitionFeatureFunction<S, A> rewardModel)
    {
        this.rewardFunction = rewardModel;
    }

    @Override
    public double getReward(S fromState, A action, S toState)
    {
        return rewardFunction.getFeatureValue(fromState, action, toState);
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
