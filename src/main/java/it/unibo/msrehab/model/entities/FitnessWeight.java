/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unibo.msrehab.model.entities;


import flexjson.JSON;

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
@Table(name = "fitnessweight")
@NamedQueries({
    @NamedQuery(name = "FitnessWeight.findAll", query = "SELECT l FROM FitnessWeight l"),
    @NamedQuery(name = "FitnessWeight.findAllByExercise", query = "SELECT l FROM FitnessWeight l WHERE l.exid= :exid"),
    @NamedQuery(name = "FitnessWeight.findAllByExerciseAndStatus", query = "SELECT l FROM FitnessWeight l WHERE l.exid= :exid AND l.status= :status"),
    @NamedQuery(name = "FitnessWeight.delete", query = "DELETE FROM FitnessWeight l WHERE l.id= :id")})
@XmlRootElement
public class FitnessWeight extends BaseEntity
{

    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    
    @Column(name = "exid", table = "fitnessweight", nullable = false)
    @Basic
    private Integer exid;
    
    
    @Column(name = "beta", table = "fitnessweight", nullable = false)
    @Basic
    private Double beta;
    
    @Column(name = "thr", table = "fitnessweight", nullable = false)
    @Basic
    private Double thr;
    
    @Column(name = "timeflag", table = "fitnessweight", nullable = false)
    @Basic
    private Boolean timeflag;
        
    //Rappresenta la difficoltà
    @Column(name = "status", table = "fitnessweight", nullable = false)
    @Basic
    private Integer status;

    
    /**
     * @return the exid
     */
    public Integer getExid() {
        return exid;
    }

    /**
     * @param exid the exid to set
     */
    public void setExid(Integer exid) {
        this.exid = exid;
    }
    
    /**
     * @return the beta
     */
    public Double getBeta() {
        return beta;
    }

    /**
     * @param beta the beta to set
     */
    public void setBeta(Double beta) {
        this.beta = beta;
    }

    /**
     * @return the thr
     */
    public Double getThr() {
        return thr;
    }

    /**
     * @param thr the thr to set
     */
    public void setThr(Double thr) {
        this.thr = thr;
    }

    /**
     * @return the timeflag
     */
    public Boolean getTimeflag() {
        return timeflag;
    }

    /**
     * @param timeflag the timeflag to set
     */
    public void setTimeflag(Boolean timeflag) {
        this.timeflag = timeflag;
    }


    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
}
