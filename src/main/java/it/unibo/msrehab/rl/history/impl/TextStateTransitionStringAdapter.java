package it.unibo.msrehab.rl.history.impl;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import it.unibo.msrehab.rl.common.IAdapter;
import it.unibo.msrehab.rl.common.StateTransition;



public class TextStateTransitionStringAdapter<S, A> implements IAdapter<StateTransition<S, A>, String>
{
    private final String delimiter = ";";

    private final Class<S> stateClass;

    private final Class<A> actionClass;

    private final JSONSerializer serializer = new JSONSerializer().exclude("*.class");

    private final JSONDeserializer<S> stateDeserializer = new JSONDeserializer<>();

    private final JSONDeserializer<A> actionDeserializer = new JSONDeserializer<>();

    public TextStateTransitionStringAdapter(Class<S> styateClass, Class<A> actionClass)
    {
        this.stateClass = styateClass;
        this.actionClass = actionClass;
    }

    @Override
    public StateTransition<S, A> inverseApply(String value)
    {
        String[] tokens = value.split(delimiter);

        if(tokens.length != 10)
            throw new IllegalArgumentException("Invalid number of tokens in the state transition string");

        return new StateTransition.Builder<S, A>()
                .setEnvironmentUid(tokens[0])
                .setEpisodeUid(tokens[1])
                .setTransitionUid(tokens[2])
                .setFromState(stateDeserializer.deserialize(tokens[3], stateClass))
                .setAction(actionDeserializer.deserialize(tokens[4], actionClass))
                .setToState(stateDeserializer.deserialize(tokens[5], stateClass))
                .setReward(Double.parseDouble(tokens[6]))
                .setDone(Boolean.parseBoolean(tokens[7]))
                .setReset(Boolean.parseBoolean(tokens[8]))
                .setTimestampEpochMillis(Long.parseLong(tokens[9]))
                .build();
    }

    @Override
    public String apply(StateTransition<S, A> stateTransition)
    {
        return new StringBuilder()
                .append(stateTransition.getEnvironmentUid())
                .append(delimiter)
                .append(stateTransition.getEpisodeUid())
                .append(delimiter)
                .append(stateTransition.getTransitionUid())
                .append(delimiter)
                .append(serializer.deepSerialize(stateTransition.getFromState()))
                .append(delimiter)
                .append(serializer.deepSerialize(stateTransition.getAction()))
                .append(delimiter)
                .append(serializer.deepSerialize(stateTransition.getToState()))
                .append(delimiter)
                .append(stateTransition.getReward())
                .append(delimiter)
                .append(stateTransition.isDone())
                .append(delimiter)
                .append(stateTransition.isReset())
                .append(delimiter)
                .append(stateTransition.getTimestampEpochMillis())
                .toString();
    }
}
