/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.entities;

/**
 *
 * @author sara
 */

import flexjson.JSON;
import flexjson.JSONSerializer;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

 @Entity
@Table(name = "performanceweinrome")
@NamedQueries({})
 @XmlRootElement
public class PerformanceWEInRome extends BaseEntity
 {
     @JSON(include = false)
    private static final long serialVersionUID = 1L;


    @Column(name = "historyid", table = "performanceweinrome")
    @Basic
    private Integer historyid;

    @Column(name = "goals", table = "performanceweinrome")
    @Basic
    private String goals;
            
    @Column(name = "actionuser", table = "performanceweinrome")
    @Basic
    private String actionuser;

    @Column(name = "problem", table = "performanceweinrome")
    @Basic
    private String problem;
    
    @Column(name = "domain", table = "performanceweinrome")
    @Basic
    private String domain;

     @Column(name = "initTime", table = "performanceweinrome")
    @Basic
    private Double initTime;

      @Column(name = "endTime", table = "performanceweinrome")
    @Basic
    private Double endTime;

       @Column(name = "nClickTarget", table = "performanceweinrome")
    @Basic
    private Integer nClickTarget;

        @Column(name = "noGoalsCompleted", table = "performanceweinrome")
    @Basic
    private String noGoalsCompleted;
       
        
    @Column(name = "nClickPrenotazioni", table = "performanceweinrome")
    @Basic
    private Integer nClickPrenotazioni;

    public Integer getnClickPrenotazioni() {
        return nClickPrenotazioni;
    }

    public void setnClickPrenotazioni(Integer nClickPrenotazioni) {
        this.nClickPrenotazioni = nClickPrenotazioni;
    }

    public Double getInitTime() {
        return initTime;
    }

    public void setInitTime(Double initTime) {
        this.initTime = initTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public Integer getnClickTarget() {
        return nClickTarget;
    }

    public void setnClickTarget(Integer nClickTarget) {
        this.nClickTarget = nClickTarget;
    }

    public String getNoGoalsCompleted() {
        return noGoalsCompleted;
    }

    public void setNoGoalsCompleted(String noGoalsCompleted) {
        this.noGoalsCompleted = noGoalsCompleted;
    }



    public Integer getHistoryid() {
        return historyid;
    }

    public void setHistoryid(Integer historyid) {
        this.historyid = historyid;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getActionuser() {
        return actionuser;
    }

    public void setActionuser(String actionuser) {
        this.actionuser = actionuser;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


}

