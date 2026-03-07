/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.Exercise;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for Exercise entities
 * 
 * @author floriano
 * 
 */

public class ExerciseController extends BaseEntityController<Exercise>
{

    public ExerciseController()
    {
        super(Exercise.class);
    }

    
    public List<Exercise> findAllExercisesByCategory(Exercise.ExerciseCategoryValue category)
    {
        return getAllEntities(ex -> Objects.equals(ex.getCategory(), category));
    }

    public List<Exercise> findAllEnabledExercisesByCategory(Exercise.ExerciseCategoryValue category)
    {
        return getAllEntities(ex -> Objects.equals(ex.getCategory(), category) && ex.isEnabled());
    }

    public Integer findMinIdFromType(String exerciseType)
    {
        return getAllEntities(ex -> Objects.equals(ex.getType(), exerciseType))
                .stream()
                .mapToInt(Exercise::getId)
                .min()
                .orElse(0);
    }

    public Integer findMaxIdFromType(String exerciseType)
    {
        return getAllEntities(ex -> Objects.equals(ex.getType(), exerciseType))
                .stream()
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
        return getAllEntities(ex -> Objects.equals(ex.getType(), exerciseType));
    }
    
    public String getExerciseFullName(Exercise.ExerciseCategoryValue category)
    {
        return findAllExercisesByCategory(category)
                .stream()
                .findFirst()
                .map(Exercise::getFullName)
                .orElse("");
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
