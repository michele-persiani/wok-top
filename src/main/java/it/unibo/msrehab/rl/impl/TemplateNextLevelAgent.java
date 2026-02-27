package it.unibo.msrehab.rl.impl;


import it.unibo.msrehab.rl.common.StateTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder to construct NextLevelAgent instances.
 */
public class TemplateNextLevelAgent implements NextLevelAgent
{

    private EpisodesSupplier episodeSupplier = ArrayList::new;

    private StateTransitionAdapter episodeTransitionAdapter = games ->
            games.stream()
                    .map(h -> new StateTransition.Builder<Integer, Integer>()
                            .build()
                    )
                    .collect(Collectors.toList());

    private boolean fitOnActionSelection = true;
    private NextLevelAgent agent = NextLevelAgentFactory.sameLevel();

    private Integer minLevel = null;
    private Integer maxLevel = null;





    /**
     * Sets the supplier for episodes that will be used to fit the agent.
     * Default returns an empty history
     *
     * @param episodeSupplier episode history supplier.
     * @return this instance
     */
    public TemplateNextLevelAgent setEpisodeSupplier(EpisodesSupplier episodeSupplier)
    {
        this.episodeSupplier = episodeSupplier;
        return this;
    }

    /**
     * Bypass episode supplier and transition adapter and set the history directly.
     *
     * NB. Overrides currently present episode supplier and transition adapter.
     *
     * @param transitionsHistory history of state transitions to set
     * @return this instance
     */
    public TemplateNextLevelAgent setTransitionsHistory(List<StateTransition<Integer, Integer>> transitionsHistory)
    {
        return setEpisodeSupplier(EpisodesSupplier.emptyEpisodes(1))
                .setEpisodeTransitionAdapter(StateTransitionAdapter.fixedTransitions(transitionsHistory));
    }

    /**
     * Set the adapter to transform episodes into state transitions.
     * Default returns null transitions, with from and to state equal to null, and reward equal to 0
     *
     * @param episodeTransitionAdapter state transition adapter to use
     * @return this instance
     */
    public TemplateNextLevelAgent setEpisodeTransitionAdapter(StateTransitionAdapter episodeTransitionAdapter)
    {
        this.episodeTransitionAdapter = episodeTransitionAdapter;
        return this;
    }

    /**
     * Set the NextLevelAgent to use. Default is an agent that always selects a delta level of 0.
     *
     * @param agent agent
     * @return this instance
     */
    public TemplateNextLevelAgent setAgent(NextLevelAgent agent)
    {
        this.agent = agent;
        return this;
    }

    /**
     * Selects whether the agent should be fitted on the episodes every time it selects an action.
     * Default is true
     *
     * @param fitOnActionSelection whether the agent should be fitted every time it selects an action.
     * @return this instance
     */
    public TemplateNextLevelAgent setFitOnActionSelection(boolean fitOnActionSelection)
    {
        this.fitOnActionSelection = fitOnActionSelection;
        return this;
    }

    /**
     * Selects the action to perform given the current level. If fitOnActionSelection is true, the agent is fitted on the
     * history
     *
     * @param currentLevel the current state of the environment
     * @return
     */
    @Override
    public Integer selectAction(Integer currentLevel)
    {
        if (fitOnActionSelection)
            fit();
        return agent.selectAction(currentLevel);
    }

    @Override
    public Integer getNextLevel(Integer currentLevel)
    {
        Integer nextLevel = NextLevelAgent.super.getNextLevel(currentLevel);
        if(minLevel != null)
            nextLevel = Math.max(nextLevel, minLevel);
        if(maxLevel != null)
            nextLevel = Math.min(nextLevel, maxLevel);
        return nextLevel;
    }


    private void fit()
    {
        List<StateTransition<Integer, Integer>> history = episodeSupplier.get()
                .stream()
                .map(episodeTransitionAdapter)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        fit(history);
    }

    /**
     * Fits the agent on the history
     *
     * @param history A list of {@code StateTransition<S, A>} objects to fit the agent with
     */
    @Override
    public void fit(List<StateTransition<Integer, Integer>> history)
    {
        agent.fit(history);
        agent.reset();
    }


    /**
     * Forces the agent to return a level with minLevel lower bound
     *
     * @param minLevel
     * @return
     */
    public TemplateNextLevelAgent setMinLevel(Integer minLevel)
    {
        this.minLevel = minLevel;
        return this;
    }

    /**
     * Forces the agent to return a level with maxLevel upper bound
     *
     * @param maxLevel
     * @return
     */
    public TemplateNextLevelAgent setMaxLevel(Integer maxLevel)
    {
        this.maxLevel = maxLevel;
        return this;
    }

    /**
     * Forces the agent to return a level between minLevel and maxLevel
     * @param minLevel
     * @param maxLevel
     * @return
     */
    public TemplateNextLevelAgent setMinMaxLevel(Integer minLevel, Integer maxLevel)
    {
        return setMinLevel(minLevel).setMaxLevel(maxLevel);
    }
}
