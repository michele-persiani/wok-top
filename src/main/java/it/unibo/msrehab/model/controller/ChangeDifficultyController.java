/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.ChangeDifficulty;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author danger
 */
public class ChangeDifficultyController extends BaseEntityController<ChangeDifficulty>
{
    public ChangeDifficultyController()
    {
        super(ChangeDifficulty.class);
    }

    /**
     * Gets all the ChangeDifficulty associated to the given historyid, sorted by id. The first in the result is the last
     * recorded (with higher id)
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

    public ChangeDifficulty findLastFromHistory(Integer historyid)
    {
        return findFromHistory(historyid)
                .stream()
                .max(Comparator.comparing(ChangeDifficulty::getId))
                .orElse(null);
    }
}
