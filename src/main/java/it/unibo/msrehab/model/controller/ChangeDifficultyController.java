/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;



import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.ChangeDifficulty;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;




/**
 * Controller for ChangeDifficulty entities
 *
 * @author danger
 */
public class ChangeDifficultyController extends DecoratorEntityController<ChangeDifficulty>
{
    private final HistoryController historyController;

    public ChangeDifficultyController(Model model)
    {
        super(model, ChangeDifficulty.class);
        historyController = new HistoryController(model);
    }



    /**
     * Gets all the ChangeDifficulty associated to the given historyid, sorted by id. The first in the result is the last
     * recorded (with higher id)
     *
     * @param historyid
     * @return
     */
    public List<ChangeDifficulty> findFromHistory(Integer historyid)
    {
        return getAllEntities()
                .stream()
                .filter(ch -> Objects.equals(historyid, ch.getHistoryid()))
                .sorted(Comparator.comparing(ChangeDifficulty::getId).reversed())
                .collect(Collectors.toList());
    }



    /**
     * Gets the last ChangeDifficulty associated with the given historyid.
     *
     * @param historyid
     * @return
     */
    public Optional<ChangeDifficulty> findLastFromHistory(Integer historyid)
    {
        return findFromHistory(historyid)
                .stream()
                .max(Comparator.comparing(ChangeDifficulty::getId));
    }




    /**
     * Gets the last ChangeDifficulty associated with the given exerciseId, userId, sessionId.
     * exerciseId, userId, sessionId are first used to find the last history associated with the given exerciseId, userId, sessionId
     * (solved or not). Then, the last ChangeDifficulty associated with the given historyid is returned.
     *
     * @param exerciseId
     * @param userId
     * @param sessionId
     * @return
     */
    public Optional<ChangeDifficulty> findLastFromExerciseUserSession(int userId, int exerciseId, int sessionId)
    {
        return historyController.findLastByUserAndExerciseAndSessid(userId, exerciseId, sessionId)
                .flatMap(h -> findLastFromHistory(h.getId()))
                ;
    }



    /**
     * Gets the level of the last ChangeDifficulty associated with the given exerciseId, userId, sessionId. If no such
     * ChangeDifficulty is found, the defaultReturnedLevel is returned.
     *
     * @param exerciseId
     * @param userId
     * @param sessionId
     * @param defaultReturnedLevel
     * @return
     */
    public int findChangedLevel(int userId, int exerciseId, int sessionId, int defaultReturnedLevel)
    {
        return findLastFromExerciseUserSession(userId, exerciseId, sessionId)
                .map(ChangeDifficulty::getLevel)
                .orElse(defaultReturnedLevel);
    }
}
