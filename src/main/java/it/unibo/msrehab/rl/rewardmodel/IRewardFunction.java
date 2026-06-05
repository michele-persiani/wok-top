package it.unibo.msrehab.rl.rewardmodel;




/**
 * Class to extract a feature from a state transition
 * @param <S>
 * @param <A>
 */
public interface IRewardFunction<S, A>
{
    double getReward(S fromState, A action, S toState);
}
