/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.controller;

import it.unibo.msrehab.model.entities.Problem;

import java.util.List;
import java.util.Objects;



public class PDDLProblemController extends BaseEntityController<Problem>
{
    
    public PDDLProblemController(){
        super(Problem.class);
    }
    
    public List<Problem> getRecordFromDifficulty(int difficulty)
    {
        return getAllEntities(pr -> Objects.equals(pr.getDifficulty(), difficulty));
    }
}
