package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.EntityController;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.util.SendMail;



public class NotificationsController
{
    private final EntityController<MSRUser> userController;
    private final EntityController<Exercise> exerciseController;


    public NotificationsController(Model model)
    {
        userController = model.getEntityController(MSRUser.class);
        exerciseController = model.getEntityController(Exercise.class);
    }

    /**
     * Notifies that an exercise has been completed by a patient
     *
     * @param patientid id of the patient that completed the exercise
     * @param exid id of the exercise that was completed
     */
    public void notifyExerciseCompleted(int patientid, int exid)
    {
        MSRUser patient = userController.getEntity(patientid);
        Exercise ex = exerciseController.getEntity(exid);
        String subject = "Esercizio completato";
        String message = "Esercizio completato" +
                "\n\nUtente: " + patient.getUsername() +
                "\nEsercizio: " + ex.getFullName();

        userController.getAllEntities()
                .stream()
                .filter(MSRUser::getNotificationsEnabled)
                .forEach(n -> SendMail.sendMail(n.getEmail(), subject, message));
    }
}
