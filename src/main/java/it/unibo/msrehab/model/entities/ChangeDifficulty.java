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
@Table(name = "changedifficulty")
@NamedQueries({
    @NamedQuery(name = "ChangeDifficulty.findAll", query = "SELECT l FROM ChangeDifficulty l"),
    @NamedQuery(name = "ChangeDifficulty.findFromHistory", query = "SELECT l FROM ChangeDifficulty l WHERE l.historyid = :historyid")
    })

@XmlRootElement
public class ChangeDifficulty extends BaseEntity
{
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    @Column(name = "historyid", table = "changedifficulty")
    @Basic
    private Integer historyid;
    
    @Column(name = "level", table = "changedifficulty")
    @Basic
    private Integer level;


    /**
     * @return the historyid
     */
    public Integer getHistoryid() {
        return historyid;
    }

    /**
     * @param historyid the historyid to set
     */
    public void setHistoryid(Integer historyid) {
        this.historyid = historyid;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }
}
