package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.PassThreshold;

public class PassThresholdController extends BaseEntityController<PassThreshold>
{
    private final AssignedExerciseController assignedExerciseController = new AssignedExerciseController();

    public PassThresholdController()
    {
        super(PassThreshold.class);
    }


    public PassThreshold findPassThreshold(int assignedExerciseId)
    {
        return assignedExerciseController.findEntity(assignedExerciseId)
                .flatMap(ae -> findEntity(th -> th.getAssignmentId() == assignedExerciseId))
                .orElse(null);
    }
}
