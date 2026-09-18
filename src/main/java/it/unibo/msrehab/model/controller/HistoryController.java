/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;
import com.sun.istack.Nullable;
import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.History;

import java.util.*;
import java.util.stream.Collectors;


/**
 * TODO: add documentation
 *
 * @author floriano
 *
 */
public class HistoryController extends JPAEntityController<History>
{

    public HistoryController()
    {
        super(History.class, PersistenceUnit.DEFAULT_PU);
    }



    public Optional<History> findLastSolvedByUserAndExerciseAndSessionAndAgent(Integer userid, Integer exid, Integer sessid, History.LevelStrategy agentStrategy)
    {
        return findAllSolvedByUserAndExerciseAndSessidAndAgent(userid, exid, sessid, agentStrategy)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getCreationTimestamp)));
    }



    public List<History> findAllSolvedByUserAndExerciseAndSessidAndAgent(Integer userid, Integer exid, Integer sessid, @Nullable History.LevelStrategy agentStrategy)
    {
        return executeResultListNamedQuery("History.findAllByUserAndExerciseAndSessidSolvedAgent", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid)
                            .setParameter("rlagent", agentStrategy)
                    ;
                });
    }



    public Optional<History> findLastUnsolvedByUserAndExerciseAndSession(Integer userid, Integer exid, Integer sessid)
    {
        return findAllUnsolvedByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getCreationTimestamp)));
    }

    public List<History> findAllUnsolvedByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return executeResultListNamedQuery("History.findAllUnsolvedByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                });
    }

    public Optional<History> findLastByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return findAllByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getCreationTimestamp)))
                ;
    }


    public List<History> findAllByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return executeResultListNamedQuery("History.findAllSolvedByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                });
    }


    @Override
    public History getEntity(Object id)
    {
        return executeSingleResultNamedQuery("History.findAssignmentById", History.class, q ->
                {
                    q.setParameter("id", id);
                });
    }

    public History findAssignmentById(Integer assignmentId)
    {
        return executeSingleResultNamedQuery("History.findAssignmentById", History.class, q ->
                {
                    q.setParameter("id", assignmentId);
                });
    }

    public List<History> findAllByUserAndExerciseAndSessidUnsolved(Integer userid, Integer exid, Integer sessid)
    {
        return executeResultListNamedQuery("History.findAllByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                });
    }

    public List<History> findAllByExerciseAndSessid(Integer exid, Integer sessid)
    {
        return executeResultListNamedQuery("History.findAllByExerciseAndSessid", History.class,q -> {
                    q.setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                });
    }


    public List<History> findAllByUser(Integer userid)
    {
        return executeResultListNamedQuery("History.findAllByUser", History.class,q -> {
                    q.setParameter("userid", userid);
                });
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
        List<Integer> resultList;
        List<Integer> hospitalSessResultList;
        Integer lastInterruptedEx = 0;
        resultList = executeResultListNamedQuery("History.findLastInterruptedExercise", q ->
                        q.setParameter("interruptedExs", interruptedExs)
                                .setParameter("sessid", sessid));

        if (resultList.isEmpty())
        {
            hospitalSessResultList = executeResultListNamedQuery("History.findLastInterruptedExercise", q ->
                    {
                        q.setParameter("interruptedExs", interruptedExs)
                                .setParameter("sessid", fromSessionId);
                    });
            if (hospitalSessResultList.isEmpty())
                lastInterruptedEx = 0;
            else
                lastInterruptedEx = hospitalSessResultList.get(0);
        }
        else
            lastInterruptedEx = resultList.get(resultList.size() - 1);

        return lastInterruptedEx;
    }
}
