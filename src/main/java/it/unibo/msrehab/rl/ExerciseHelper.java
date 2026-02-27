package it.unibo.msrehab.rl;

import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.utils.Lists;
import it.unibo.msrehab.services.ExerciseService;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.rl.model.JPAController;
import it.unibo.msrehab.rl.utils.ObjectComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.*;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_CHS_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_DIV_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_RFLXS_MOTORBIKE;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.ATT_SEL_STD_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_CHS_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.MEM_VIS_2_VEG_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_ANM_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_FRT_RL;
import static it.unibo.msrehab.model.entities.Exercise.ExerciseNameValue.NBACK_VEG_RL;


public class ExerciseHelper
{
    private static final IModel model = JPAController.getInstance();
    private Exercise exercise;

    private ExerciseHelper() {}




    public static ExerciseHelper create(Exercise.ExerciseCategoryValue exCategory)
    {
        ExerciseHelper h = new ExerciseHelper();
        h.exercise = model.getEntityController(Exercise.class)
                .findEntity(ex -> ex.getCategory().equals(exCategory))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Exercise with category %s not found", exCategory)));
        return h;
    }



    public static ExerciseHelper create(Exercise exercise)
    {
        ExerciseHelper h = new ExerciseHelper();
        h.exercise = exercise;
        return h;
    }



    public static ExerciseHelper create(int exerciseId)
    {
        ExerciseHelper h = new ExerciseHelper();
        h.exercise = model
                .getEntityController(Exercise.class)
                .findEntity(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Exercise with id %d not found", exerciseId)));
        return h;
    }


    public double getRelativePerformance(int level, double absolutePerformance)
    {
        double percLvl = (double) level / getMaxLevel();
        if(percLvl < 0.33)
            return absolutePerformance * 0.33;
        if(percLvl < 0.66)
            return absolutePerformance * 0.66;
        return absolutePerformance;
    }

    public List<Integer> getLevelsForDifficulties()
    {
        return Stream.of("easy", "medium", "hard")
                .map(this::getLevel)
                .collect(Collectors.toList());
    }

    public int getLevel(String difficultyLevel)
    {
        switch (difficultyLevel)
        {
            case "training":
            case "demo":
                return -1;
            case "medium":
                return (int)Math.floor(getMaxLevel() * 0.33);
            case "difficult":
            case "hard":
                return (int)Math.floor(getMaxLevel() * 0.66);
            case "easy":
            default:
                return 1;
        }
    }

    public String getDifficultyString(int level)
    {
        double percLvl = (double) level / getMaxLevel();
        if(percLvl < 0)
            return "training";
        if(percLvl < 0.33)
            return "easy";
        if(percLvl < 0.66)
            return "medium";
        return "hard";
    }

    public boolean isRLDriven()
    {
        return Lists.of(
                        ATT_SEL_STD_ANM_RL,
                        ATT_SEL_STD_CHS_RL,
                        ATT_SEL_STD_FRT_RL,
                        ATT_SEL_STD_VEG_RL,
                        MEM_VIS_2_ANM_RL,
                        MEM_VIS_2_FRT_RL,
                        MEM_VIS_2_VEG_RL,
                        MEM_VIS_2_CHS_RL,
                        NBACK_ANM_RL,
                        NBACK_FRT_RL,
                        NBACK_VEG_RL,
                        ATT_DIV_FRT_RL,
                        ATT_DIV_ANM_RL,
                        ATT_DIV_CHS_RL,
                        ATT_DIV_VEG_RL,
                        ATT_RFLXS_MOTORBIKE
                )
                .contains(exercise.getName());
    }


    public boolean isSupported()
    {
        switch (exercise.getCategory())
        {
            case ATT_SEL_STD:
            case NBACK:
            case MEM_VIS_2:
            case ATT_DIV:
            case ATT_DIV_ORI:
            case ATT_DIV_FAC:
                return true;
            default:
                return false;
        }
    }


    public int getMaxLevel()
    {
        return exercise.getMaxLevel();
    }

    public int clampLevel(int level)
    {
        return Math.max(0, Math.min(level, getMaxLevel()));
    }

    public Map<String, Object> getParameters(int level)
    {
        switch (exercise.getCategory())
        {
            case ATT_SEL_STD:
                return ExerciseService.createParametersAttention1(level, new Integer[10], Exercise.ExerciseNameValue.ATT_SEL_STD_FRT);
            case NBACK:
                return ExerciseService.createParametersNback(level, new Integer[10], Exercise.ExerciseNameValue.NBACK_ANM);
            case MEM_VIS_2:
                return ExerciseService.createParametersMemory2(level, new Integer[10], Exercise.ExerciseNameValue.MEM_VIS_2_ANM);
            case ATT_DIV:
            case ATT_DIV_ORI:
            case ATT_DIV_FAC:
                return ExerciseService.createParametersAttention4(level, new Integer[10]);
            default:
                throw new IllegalArgumentException(String.format("Exercise %s not supported", exercise.getName()));
        }
    }

    /**
     * Gets the parameters configuration for increasing levels of the exercise
     * @return
     */
    public Map<String, Object[]> getParametersConfig()
    {
        if(!isSupported())
            throw new IllegalArgumentException(String.format("Exercise %s not supported", exercise.getName()));

        int maxLevel = getMaxLevel();

        Map<String, List<Object>> paramsAccum = new HashMap<>();

        for(int i = 0; i < maxLevel; i++)
        {
            Map<String, Object> params = getParameters(i);
            for(String p : params.keySet())
                paramsAccum.computeIfAbsent(p, k -> new ArrayList<>()).add(params.get(p));
        }

        paramsAccum.replaceAll((p, v) -> v.stream().distinct().sorted(new ObjectComparator()).collect(Collectors.toList()));

        return paramsAccum
                .keySet()
                .stream()
                .collect(Collectors.toMap(p -> p, p -> paramsAccum.get(p).toArray(new Object[0])));
    }
}
