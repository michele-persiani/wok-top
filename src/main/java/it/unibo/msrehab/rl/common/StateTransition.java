package it.unibo.msrehab.rl.common;

import it.unibo.msrehab.rl.utils.AbstractBuilder;
import java.util.Objects;
import java.util.UUID;




public class StateTransition<S, A>
{

    public static class Builder<S, A> extends AbstractBuilder<StateTransition<S, A>>
    {
        public Builder<S, A> setTransitionUid(String transitionUid)
        {
            return setValue(this, s -> s.transitionUid = transitionUid);
        }

        public Builder<S, A> setEnvironmentUid(String environmentUid)
        {
            return setValue(this, s -> s.environmentUid = environmentUid);
        }

        public Builder<S, A> setEpisodeUid(String episodeUid)
        {
            return setValue(this, s -> s.episodeUid = episodeUid);
        }

        public Builder<S, A> setFromState(S fromState)
        {
            return setValue(this, s -> s.fromState = fromState);
        }

        public Builder<S, A> setToState(S toState)
        {
            return setValue(this, s -> s.toState = toState);
        }

        public Builder<S, A> setAction(A action)
        {
            return setValue(this, s -> s.action = action);
        }

        public Builder<S, A> setReward(Double reward)
        {
            return setValue(this, s -> s.reward = reward);
        }

        public Builder<S, A> setDone(boolean isDone)
        {
            return setValue(this, s -> s.isDone = isDone);
        }

        public Builder<S, A> setReset(boolean isReset)
        {
            return setValue(this, s -> s.isReset = isReset);
        }

        public Builder<S, A> setTimestampEpochMillis(long timestampEpochMillis)
        {
            return setValue(this, s -> s.timestampEpochMillis = timestampEpochMillis);
        }

        public Builder<S, A> copyValues(StateTransition<S, A> other)
        {
            return copyValuesWithoutStateAction(other)
                    .setValue(this, tr -> {
                        tr.fromState = other.fromState;
                        tr.action = other.action;
                        tr.toState = other.toState;
                    });
        }

        public Builder<S, A> copyValuesWithoutStateAction(StateTransition<?, ?> other)
        {
            return setValue(this, tr -> {
                tr.transitionUid = other.transitionUid;
                tr.episodeUid = other.episodeUid;
                tr.environmentUid = other.environmentUid;
                tr.reward = other.reward;
                tr.isDone = other.isDone;
                tr.isReset = other.isReset;
                tr.timestampEpochMillis = other.timestampEpochMillis;
            });
        }

        @Override
        protected StateTransition<S, A> newInstance()
        {
            return new StateTransition<>();
        }
    }
    private String environmentUid = "Env-" + UUID.randomUUID();

    private String transitionUid = UUID.randomUUID().toString();

    private String episodeUid = UUID.randomUUID().toString();


    private S fromState;
    private S toState;
    private A action;
    private Double reward = 0d;
    private boolean isDone = false;
    private boolean isReset = false;
    private long timestampEpochMillis = System.currentTimeMillis();

    public String getEnvironmentUid()
    {
        return environmentUid;
    }

    public String getTransitionUid()
    {
        return transitionUid;
    }

    public String getEpisodeUid()
    {
        return episodeUid;
    }

    public S getFromState()
    {
        return fromState;
    }

    public S getToState()
    {
        return toState;
    }

    public A getAction()
    {
        return action;
    }

    public Double getReward()
    {
        return reward;
    }

    public boolean isDone()
    {
        return isDone;
    }

    public boolean isReset()
    {
        return isReset;
    }

    public long getTimestampEpochMillis()
    {
        return timestampEpochMillis;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!StateTransition.class.isAssignableFrom(getClass()))
            return false;
        StateTransition<?,?> other = (StateTransition<?,?>)obj;
        return Objects.equals(transitionUid, other.transitionUid);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(transitionUid);
    }

    public String toString()
    {
        return String.format("FROM: %s ACTION: %s TO: %s REWARD: %s", getFromState(), getAction(), getToState(), getReward());
    }
}
