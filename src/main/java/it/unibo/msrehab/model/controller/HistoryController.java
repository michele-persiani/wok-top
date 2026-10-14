/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;
import com.sun.istack.Nullable;
import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.History;

import java.util.*;
import java.util.stream.Collectors;


/**
 * TODO: add documentation
 *
 * @author floriano
 *
 */
public class HistoryController extends DecoratorEntityController<History>
{

    public HistoryController(Model model)
    {
        super(model, History.class);
    }



    public List<History> findAllSolvedByUserAndExerciseAndSessidAndAgent(Integer userid, Integer exid, Integer sessid, @Nullable History.LevelStrategy agentStrategy)
    {
        return getAllEntities()
                .stream()
                .filter(History::isSolved)
                .filter(h -> Objects.equals(h.getUserid(), userid))
                .filter(h -> Objects.equals(h.getExid(), exid))
                .filter(h -> Objects.equals(h.getSessid(), sessid))
                .filter(h -> agentStrategy == null || Objects.equals(h.getLevelStrategy(), agentStrategy))
                .sorted(Comparator.comparing(History::getCreationTimestamp).reversed())
                .collect(Collectors.toList());
    }



    public Optional<History> findLastUnsolvedByUserAndExerciseAndSession(Integer userid, Integer exid, Integer sessid)
    {
        return findAllUnsolvedByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getCreationTimestamp).reversed()));
    }

    public List<History> findAllUnsolvedByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return getAllEntities()
                .stream()
                .filter(h -> Objects.equals(h.getUserid(), userid))
                .filter(h -> Objects.equals(h.getExid(), exid))
                .filter(h -> Objects.equals(h.getSessid(), sessid))
                .filter(h -> !h.isSolved())
                .sorted(Comparator.comparing(History::getCreationTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public Optional<History> findLastByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return findAllByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getCreationTimestamp).reversed()))
                ;
    }


    public List<History> findAllByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return getAllEntities()
                .stream()
                .filter(h -> Objects.equals(h.getUserid(), userid))
                .filter(h -> Objects.equals(h.getExid(), exid))
                .filter(h -> Objects.equals(h.getSessid(), sessid))
                .sorted(Comparator.comparing(History::getCreationTimestamp).reversed())
                .collect(Collectors.toList());
    }


    public List<History> findAllByUser(Integer userid)
    {
        return getAllEntities()
                .stream()
                .filter(h -> Objects.equals(h.getUserid(), userid))
                .sorted(Comparator.comparing(History::getCreationTimestamp).reversed())
                .collect(Collectors.toList());
    }


    public boolean isExerciseInHistory(Integer exid, Integer sessid)
    {
        return getAllEntities()
                .stream()
                .anyMatch(h -> Objects.equals(h.getExid(), exid) && Objects.equals(h.getSessid(), sessid));
    }

    /**
     * Gets exercise with max timestamp
     *
     * @param interruptedExs
     * @param sessid
     * @param fromSessionId
     * @return exid of exercise with max timestamp
     */
    public Integer getLastInterruptedExercise(List<Integer> interruptedExs, Integer sessid, Integer fromSessionId)
    {
        return getAllEntities()
                .stream()
                .filter(h -> interruptedExs.contains(h.getExid()))
                .filter(h -> Objects.equals(h.getSessid(), sessid))
                .sorted(Comparator.comparing(History::getCreationTimestamp).reversed())
                .map(History::getExid)
                .findFirst()
                .orElse(0);
    }
}
