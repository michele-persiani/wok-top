package it.unibo.msrehab.rl.rewardmodel;




/**
 * Class to extract a feature from a state transition
 * @param <S>
 * @param <A>
 */
public interface TransitionFeatureFunction<S, A>
{
    double getFeatureValue(S fromState, A action, S toState);
}
