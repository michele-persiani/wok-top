package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class modeling a group of users of the
 * multiple sclerosis rehabilitation system
 */

@Entity
@Table(name = "msrgroup")
@NamedQueries({
    @NamedQuery(name = "Group.findAll", query = "SELECT l FROM MSRGroup l ORDER BY l.id ASC"),
    @NamedQuery(name = "Group.delete", query = "DELETE FROM MSRGroup l WHERE l.id= :msrgroupid"),
    @NamedQuery(name = "Group.findAllGroupsInCenter", query = "SELECT l FROM MSRGroup l WHERE l.cid= :cid ORDER BY l.id ASC")})

public class MSRGroup extends BaseEntity
{
    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    
    @Column(name = "name", table = "msrgroup")
    @Basic
    @NotNull
    @Size(min=2,
            message="Nome gruppo non valido. Deve essere di almeno due caratteri")    
    private String name;
    
    @Column(name = "cid", table = "msrgroup")
    @Basic
    private Integer cid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

}