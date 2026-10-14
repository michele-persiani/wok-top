package it.unibo.msrehab.model.controller;



import it.unibo.msrehab.model.DecoratorEntityController;
import it.unibo.msrehab.model.Model;
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

public class ExerciseController extends DecoratorEntityController<Exercise>
{
    public ExerciseController(Model model)
    {
        super(model, Exercise.class);
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

    public String getDisplayName(Integer exid)
    {
        Exercise ex = getEntity(exid);
        return getDisplayName(ex);
    }

    public String getDisplayName(Exercise ex)
    {
        String displayName;
        switch (ex.getCategory()) {
            case ATT_SEL_STD:
            case ATT_SEL_STD_FAC:
            case ATT_SEL_STD_ORI:
                displayName = "Cogli gli oggetti";
                break;
            case ATT_SEL_FLW:
            case ATT_SEL_FLW_FAC:
            case ATT_SEL_FLW_ORI:
                displayName = "Beccali al volo!";
                break;
            case ATT_DIV:
            case ATT_DIV_FAC:
            case ATT_DIV_ORI:
                displayName = "Su 2 fronti";
                break;
            case ATT_ALT:
            case ATT_ALT_FAC:
            case ATT_ALT_ORI:
                displayName = "Prima uno e poi l'altro";
                break;
            case MEM_VIS_1:
            case MEM_VIS_1_FAC:
            case MEM_VIS_1_ORI:
                displayName = "Quali erano?";
                break;
            case MEM_VIS_2:
            case MEM_VIS_2_FAC:
                displayName = "Nel posto giusto";
                break;
            case MEM_VIS_5:
                displayName = "Nel posto giusto griglia";
                break;
            case NBACK:
            case NBACK_FAC:
            case NBACK_ORI:
                displayName = "Un occhio alle spalle";
                break;
            case MEM_LONG_1:
                displayName = "Chi sarà?"; // Chi sar&agrave;?
                break;
            case RES_INH:
                displayName = "Cataloga gli oggetti";
                break;
            case PLAN_1:
                displayName = "Pianificazione Giornata";
                break;
            case PLAN_2:
                displayName = "Pianificazione allo zoo";
                break;
            case PLAN_3:
                displayName = "Pianificazione week-end a Roma";
                break;
            case ATT_RFLXS:
                displayName = "Moto";
                break;
            default:
                displayName = "";
        }
        displayName += ex.isRL() ? " (RL)" : "";

        String[] fullNameParts = ex.getFullName().split("-");
        String stimulusCategory = fullNameParts[fullNameParts.length - 1];
        stimulusCategory = stimulusCategory.trim();

        displayName += " - " + stimulusCategory;
        return displayName;
    }


    public String findGlobalCategory(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getGlobalCategory)
                .orElseThrow(() -> new IllegalArgumentException("No exercises found for category " + category));
    }


    public String findExerciseType(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getType)
                .orElseThrow(() -> new IllegalArgumentException("No exercises found for category " + category));
    }
}
