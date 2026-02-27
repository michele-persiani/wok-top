/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.entities;

import flexjson.JSON;
import it.unibo.msrehab.model.pddl.PDDLProblem;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author danger
 */

@Entity
@Table(name = "problem")
@NamedQueries({
                @NamedQuery(name = "Problem.findAllByDifficulty", query = "SELECT l FROM Problem l WHERE l.difficulty = :difficulty")
             })

@XmlRootElement
public class Problem extends BaseEntity
{
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    @Column(name = "problemHead", table = "problem")
    @Basic
    private String problemHead;
    
    @Column(name = "idenvironment", table = "problem")
    @Basic
    private int idenvironment;
    
    @Column(name = "belief", table = "problem")
    @Basic
    private String belief;
    
    @Column(name = "goals", table = "problem")
    @Basic
    private String goals;
    
    @Column(name = "solutions", table = "problem")
    @Basic
    private String solutions;
    
    @Column(name = "difficulty", table = "problem")
    @Basic
    private int difficulty;
    
    @Column(name = "mapjson", table = "problem")
    @Basic
    private String mapjson;

    public String getMapjson() {
        return mapjson;
    }

    public void setMapjson(String mapjson) {
        this.mapjson = mapjson;
    }

    public String getProblemHead() {
        return problemHead;
    }

    public void setProblemHead(String problemHead) {
        this.problemHead = problemHead;
    }

    public int getIdenvironment() {
        return idenvironment;
    }

    public void setIdenvironment(int idenvironment) {
        this.idenvironment = idenvironment;
    }

    public String getBelief() {
        return belief;
    }

    public void setBelief(String belief) {
        this.belief = belief;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    
    public PDDLProblem getPDDLproblem(){
        return new PDDLProblem(problemHead, goals, belief, mapjson);
    }
}
