package it.unibo.msrehab.rl.impl;

import it.unibo.msrehab.rl.agents.IAgent;



public interface NextLevelAgent extends IAgent<Integer, Integer>
{

    /**
     * Gets the action given the current level, that is, the delta level to apply to obtain the next level
     * @param currentLevel the current environment level
     * @return delta level to apply to obtain the next level
     */
    Integer selectAction(Integer currentLevel);


    /**
     * Finds the next level given the current one.
     * This method is equivalent to calling selectAction(currentLevel) and adding the result to the current level.
     *
     * @param currentLevel
     * @return
     */
    default Integer getNextLevel(Integer currentLevel)
    {
        int deltaLevel = selectAction(currentLevel);
        return currentLevel + deltaLevel;
    }

}
