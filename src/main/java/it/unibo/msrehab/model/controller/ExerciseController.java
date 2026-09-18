package it.unibo.msrehab.model.controller;



import it.unibo.msrehab.model.JPAEntityController;
import it.unibo.msrehab.model.PersistenceUnit;
import it.unibo.msrehab.model.entities.ExElement;
import it.unibo.msrehab.model.entities.Exercise;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;







/**
 * Controller for Exercise entities
 * 
 * @author floriano
 * 
 */

public class ExerciseController extends JPAEntityController<Exercise>
{
    public ExerciseController()
    {
        super(Exercise.class, PersistenceUnit.DEFAULT_PU);
    }

    
    public List<Exercise> findAllExercisesByCategory(Exercise.ExerciseCategoryValue category)
    {
        return getAllEntities()
                .stream()
                .filter(ex -> Objects.equals(ex.getCategory(), category))
                .collect(Collectors.toList());
    }

    public List<Exercise> findAllEnabledExercisesByCategory(Exercise.ExerciseCategoryValue category)
    {
        return getAllEntities()
                .stream()
                .filter(ex -> Objects.equals(ex.getCategory(), category) && ex.isEnabled())
                .collect(Collectors.toList());
    }

    public Integer findMinIdFromType(String exerciseType)
    {
        return getAllEntities()
                .stream()
                .filter(ex -> Objects.equals(ex.getType(), exerciseType))
                .mapToInt(Exercise::getId)
                .min()
                .orElse(0);
    }

    public Integer findMaxIdFromType(String exerciseType)
    {
        return getAllEntities()
                .stream()
                .filter(ex -> Objects.equals(ex.getType(), exerciseType))
                .mapToInt(Exercise::getId)
                .max()
                .orElse(0);
    }


    public List<String> getAllExerciseTypes()
    {
        return getAllEntities()
                .stream()
                .map(Exercise::getType)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<Exercise> findAllExercisesByType(String exerciseType)
    {
        return getAllEntities()
                .stream()
                .filter(ex -> Objects.equals(ex.getType(), exerciseType))
                .collect(Collectors.toList());
    }


    public String getExerciseFullName(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getFullName)
                .orElse("");
    }

    public String getDisplayName(Integer exId)
    {
        return findEntity(exId)
                .map(this::getDisplayName)
                .orElse("");
    }

    public String getDisplayName(Exercise ex)
    {
        return getDisplayName(ex.getCategory()) + Optional.ofNullable(ex.getExelementcat()).map(c -> " - " + c.getDisplayName()).orElse("");
    }

    public String getDisplayName(Exercise.ExerciseCategoryValue category)
    {
        switch (category) {
            case ATT_SEL_STD:
            case ATT_SEL_STD_FAC:
            case ATT_SEL_STD_ORI:
                return "Cogli gli oggetti";
            case ATT_SEL_FLW:
            case ATT_SEL_FLW_FAC:
            case ATT_SEL_FLW_ORI:
                return "Beccali al volo!";
            case ATT_DIV:
            case ATT_DIV_FAC:
            case ATT_DIV_ORI:
                return "Su 2 fronti";
            case ATT_ALT:
            case ATT_ALT_FAC:
            case ATT_ALT_ORI:
                return "Prima uno e poi l'altro";
            case MEM_VIS_1:
            case MEM_VIS_1_FAC:
            case MEM_VIS_1_ORI:
                return "Quali erano?";
            case MEM_VIS_2:
            case MEM_VIS_2_FAC:
                return "Nel posto giusto";
            case MEM_VIS_5:
                return "Nel posto giusto griglia";
            case NBACK:
            case NBACK_FAC:
            case NBACK_ORI:
                return "Un occhio alle spalle";
            case MEM_LONG_1:
                return "Chi sarà?"; // Chi sar&agrave;?
            case RES_INH:
                return "Cataloga gli oggetti";
            case PLAN_1:
                return "Pianificazione Giornata";
            case PLAN_2:
                return "Pianificazione allo zoo";
            case PLAN_3:
                return "Pianificazione week-end a Roma";
            case ATT_RFLXS:
                return "Moto";
            default:
                return "";
        }
    }

    public String findGlobalCategory(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getGlobalCategory)
                .orElse("");
    }


    public String findExerciseTypeFromCategory(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getType)
                .orElse("");
    }
}
