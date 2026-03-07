/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.ChangeDifficulty;
import it.unibo.msrehab.model.entities.History;

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
public class ChangeDifficultyController extends BaseEntityController<ChangeDifficulty>
{
    private final HistoryController historyController = new HistoryController();

    public ChangeDifficultyController()
    {
        super(ChangeDifficulty.class);
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
        return getAllEntities(ch -> Objects.equals(historyid, ch.getHistoryid()))
                .stream()
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
    public Optional<ChangeDifficulty> findLastFromExerciseUserSession(int exerciseId, int userId, int sessionId)
    {
        return historyController.findLastByUserAndExerciseAndSession(exerciseId, userId, sessionId, null)
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
    public int findChangedLevel(int exerciseId, int userId, int sessionId, int defaultReturnedLevel)
    {
        return findLastFromExerciseUserSession(exerciseId, userId, sessionId)
                .map(ChangeDifficulty::getLevel)
                .orElse(defaultReturnedLevel);
    }
}
