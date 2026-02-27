package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.AssignedExercise;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AssignedExerciseController extends BaseEntityController<AssignedExercise>
{
    public AssignedExerciseController()
    {
        super(AssignedExercise.class);
    }


    public AssignedExercise findLastAssignedExercise(int userId, int exerciseId, int sessionId)
    {
        return getAllEntities(ae -> Objects.equals(ae.getUserId(), userId) && Objects.equals(ae.getExerciseId(), exerciseId) && Objects.equals(ae.getSessionId(), sessionId))
                .stream()
                .max(Comparator.comparing(AssignedExercise::getTimestampMillis))
                .orElse(null)
                ;
    }


    public List<AssignedExercise> findAllAssignedExercises(int userId, int exerciseId)
    {
        return getAllEntities(ae -> ae.getUserId() == userId && ae.getExerciseId() == exerciseId);
    }


    public List<AssignedExercise> findAllAssignedExercises(int userId)
    {
        return getAllEntities(ae -> ae.getUserId() == userId);
    }
}
