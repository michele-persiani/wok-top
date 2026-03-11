/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.History;

import java.util.*;


/**
 * TODO: add documentation
 *
 * @author floriano
 *
 */
public class HistoryController extends BaseEntityController<History>
{

    public HistoryController()
    {
        super(History.class);
    }

    public List<Object[]> findAllByType(Integer userid, String type, String difficulty, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByType", q -> {
                            q.setParameter("userid", userid)
                            .setParameter("type", type)
                            .setParameter("difficulty", difficulty)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());

    }


    public List<Object[]> findAllByCategory(Integer userid, Exercise.ExerciseCategoryValue category, String difficulty, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByCategory", q -> {
                    q.setParameter("userid", userid)
                            .setParameter("category", category)
                            .setParameter("difficulty", difficulty)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());
    }

    public List<Object[]> findAllByExIdRangeNoDifficulty(Integer userid, Integer exIdInit, Integer exIdFinish, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByExIdRangeNoDifficulty", q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exIdInit", exIdInit)
                            .setParameter("exIdFinish", exIdFinish)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());
    }

    public List<Object[]> findAllByTypeNoDifficulty(Integer userid, String type, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByTypeNoDifficulty", q -> {
                    q.setParameter("userid", userid)
                            .setParameter("type", type)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());

    }

    public List<Object[]> findAllByCategoryNoDifficulty(Integer userid, Exercise.ExerciseCategoryValue category, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByCategoryNoDifficulty", q -> {
                    q.setParameter("userid", userid)
                            .setParameter("category", category)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());
    }

    public List<Object[]> findAllByExIdSessIdRangeNoDifficulty(Integer userid, Integer sessid, Integer exIdInit, Integer exIdFinish, Integer group, Long start, Long end)
    {
        return getTransactionManager()
                .<Object[]>executeResultListNamedQuery("History.findAllByExIdSessIdRangeNoDifficulty", q -> {
                    q.setParameter("userid", userid)
                            .setParameter("sessid", sessid)
                            .setParameter("exIdInit", exIdInit)
                            .setParameter("exIdFinish", exIdFinish)
                            .setParameter("group", group)
                            .setParameter("start", start)
                            .setParameter("end", end);
                })
                .orElse(new ArrayList<>());
    }

    public List<History> findAllByUserAndExercise(Integer userid, Integer exid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllByUserAndExercise", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid);
                })
                .orElse(new ArrayList<>());
    }

    public Optional<History> findLastSolvedByUserAndExerciseAndSession(Integer userid, Integer exid, Integer sessid)
    {

        return findAllSolvedByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getTimestamp)))
                ;
    }

    public List<History> findAllSolvedByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllSolvedByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                }).get();
    }

    public Optional<History> findLastUnsolvedByUserAndExerciseAndSession(Integer userid, Integer exid, Integer sessid)
    {
        return findAllUnsolvedByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getTimestamp)))
                ;
    }

    public List<History> findAllUnsolvedByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllUnsolvedByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                }).get();
    }

    public Optional<History> findLastByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return findAllByUserAndExerciseAndSessid(userid, exid, sessid)
                .stream()
                .max(Comparator.nullsLast(Comparator.comparing(History::getTimestamp)))
                ;
    }


    public List<History> findAllByUserAndExerciseAndSessid(Integer userid, Integer exid, Integer sessid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllSolvedByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                }).get();
    }

    @Override
    public History getEntityOrThrow(Object id)
    {
        return getTransactionManager()
                .executeSingleResultNamedQuery("History.findAssignmentById", History.class, q ->
                {
                    q.setParameter("id", id);
                }).orElseThrow(() -> new IllegalArgumentException("History not found"));
    }

    public History findAssignmentById(Integer assignmentId)
    {
        return getTransactionManager()
                .executeSingleResultNamedQuery("History.findAssignmentById", History.class, q ->
                {
                    q.setParameter("id", assignmentId);
                }).orElse(null);
    }

    public List<History> findAllByUserAndExerciseAndSessidUnsolved(Integer userid, Integer exid, Integer sessid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllByUserAndExerciseAndSessid", History.class,q -> {
                    q.setParameter("userid", userid)
                            .setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                }).get();
    }

    public List<History> findAllByExerciseAndSessid(Integer exid, Integer sessid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllByExerciseAndSessid", History.class,q -> {
                    q.setParameter("exid", exid)
                            .setParameter("sessid", sessid);
                })
                .orElse(new ArrayList<>());
    }


    public List<History> findAllByUser(Integer userid)
    {
        return getTransactionManager()
                .executeResultListNamedQuery("History.findAllByUser", History.class,q -> {
                    q.setParameter("userid", userid);
                })
                .orElse(new ArrayList<>());
    }


    public boolean isExerciseInHistory(Integer exid, Integer sessid)
    {
        return !getAllEntities(h -> Objects.equals(h.getExid(), exid) && Objects.equals(h.getSessid(), sessid))
                .isEmpty();
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
        resultList = getTransactionManager()
                .<Integer>executeResultListNamedQuery("History.findLastInterruptedExercise", q ->
                        q.setParameter("interruptedExs", interruptedExs)
                                .setParameter("sessid", sessid))
                .orElse(new ArrayList<>());

        if (resultList.isEmpty())
        {
            hospitalSessResultList = getTransactionManager()
                    .<Integer>executeResultListNamedQuery("History.findLastInterruptedExercise", q ->
                    {
                        q.setParameter("interruptedExs", interruptedExs)
                                .setParameter("sessid", fromSessionId);
                    })
                    .orElse(new ArrayList<>());
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
