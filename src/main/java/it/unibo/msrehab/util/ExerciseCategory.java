package it.unibo.msrehab.util;

import java.io.Serializable;
import java.util.List;

import it.unibo.msrehab.model.entities.Exercise;

public class ExerciseCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Exercise> allExercises;
    private List<Exercise> toDoExercises;
    private List<Exercise> interruptedExercises;
    private List<Exercise> completedExercises;
    private Integer progress;
    private String id;
    private String categoryNameForPatient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExerciseCategory(List<Exercise> exercises, List<Exercise> tmpToDoExercises, List<Exercise> tmpCompletedExercises, List<Exercise> tmpInterruptedExercises, Integer progress, String id) {
        super();
        this.allExercises = exercises;
        this.toDoExercises = tmpToDoExercises;
        this.completedExercises = tmpCompletedExercises;
        this.interruptedExercises = tmpInterruptedExercises;
        this.progress = progress;
        this.id = id;
        this.categoryNameForPatient = this.getCategoryNameForPatient(id);
    }

    public List<Exercise> getExercises() {
        return allExercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.allExercises = exercises;
    }

    public List<Exercise> getToDoExercises() {
        return toDoExercises;
    }

    public void setToDoExercises(List<Exercise> toDoExercises) {
        this.toDoExercises = toDoExercises;
    }

    public List<Exercise> getInterruptedExercises() {
        return interruptedExercises;
    }

    public void setInterruptedExercises(List<Exercise> interruptedExercises) {
        this.interruptedExercises = interruptedExercises;
    }

    public List<Exercise> getCompletedExercises() {
        return completedExercises;
    }

    public void setCompletedExercises(List<Exercise> completedExercises) {
        this.completedExercises = completedExercises;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getCategoryNameForPatient() {
        return categoryNameForPatient;
    }

    public void setCategoryNameForPatient(String name) {
        this.categoryNameForPatient = name;
    }

    @Override
    public String toString() {
        return "ExerciseCategory [exercises=" + allExercises.toString() + ", progress=" + progress + "]";
    }

    private String getCategoryNameForPatient(String id) {
        switch (id) {
            case "ATT_SEL_STD":
            case "ATT_SEL_STD_FAC":
            case "ATT_SEL_STD_ORI":
                return "Cogli gli oggetti";
            case "ATT_SEL_FLW":
            case "ATT_SEL_FLW_FAC":
            case "ATT_SEL_FLW_ORI":
                return "Beccali al volo!";
            case "ATT_DIV":
            case "ATT_DIV_FAC":
            case "ATT_DIV_ORI":
                return "Su 2 fronti";
            case "ATT_ALT":
            case "ATT_ALT_FAC":
            case "ATT_ALT_ORI":
                return "Prima uno e poi l'altro";
            case "MEM_VIS_1":
            case "MEM_VIS_1_FAC":
            case "MEM_VIS_1_ORI":
                return "Quali erano?";
            case "MEM_VIS_2":
            case "MEM_VIS_2_FAC":
                return "Nel posto giusto";
            case "MEM_VIS_5":
                return "Nel posto giusto griglia";
            case "NBACK":
            case "NBACK_FAC":
            case "NBACK_ORI":
                return "Un occhio alle spalle";
            case "MEM_LONG_1":
                return "Chi sarà?"; // Chi sar&agrave;?
            case "RES_INH":
                return "Cataloga gli oggetti";
            case "PLAN_1":
                return "Pianificazione Giornata";
            case "PLAN_2":
                return "Pianificazione allo zoo";   
             case "PLAN_3":
                return "Pianificazione week-end a Roma";
            case "ATT_RFLXS":
                return "Riflessi";
            default:
                return "";
        }
    }

}
