package it.unibo.msrehab.rl.history.impl;


import it.unibo.msrehab.rl.common.IAdapter;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.utils.Files;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TextHistoryPersistence<S, A> extends AbstractHistoryPersistence<S, A>
{

    private final String filepath;
    private final IAdapter<StateTransition<S, A>, String> adapter;


    public TextHistoryPersistence(String filepath, Class<S> stateClass, Class<A> actionClass)
    {
        this.filepath = filepath;
        this.adapter = new TextStateTransitionStringAdapter<>(stateClass, actionClass);
    }

    @Override
    protected List<StateTransition<S, A>> loadHistory() throws Exception
    {
        String fileContent = Files.readTextFile(filepath);

        return Arrays.stream(fileContent.split(System.lineSeparator()))
                .map(adapter::inverseApply)
                .collect(Collectors.toList());
    }

    @Override
    protected void saveHistory(List<StateTransition<S, A>> history) throws Exception
    {
        String fileContent = history.stream()
                .map(adapter)
                .collect(Collectors.joining(System.lineSeparator()));
        Files.writeTextFile(filepath, fileContent);
    }

}
