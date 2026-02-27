package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Class modeling a group of users of the
 * multiple sclerosis rehabilitation system
 */

@Entity
@Table(name = "msrsession")
@NamedQueries({
    @NamedQuery(name = "MSRSession.findAll", query = "SELECT l FROM MSRSession l ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllForGroups", query = "SELECT l FROM MSRSession l where l.forgroup = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllHospitalActiveForGroups", query = "SELECT l FROM MSRSession l where l.forgroup = true AND l.active = true AND l.hospital = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllHomeActiveForGroups", query = "SELECT l FROM MSRSession l where l.forgroup = true AND l.active = true AND l.hospital = false ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllForPatients", query = "SELECT l FROM MSRSession l where l.forgroup = false ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllActiveForPatients", query = "SELECT l FROM MSRSession l where l.forgroup = false AND l.active = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllByUserOrGroup", query = "SELECT l FROM MSRSession l WHERE l.usrgrpid= :usrgrpid ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllActiveByUserOrGroup", query = "SELECT l FROM MSRSession l WHERE l.usrgrpid= :usrgrpid AND l.active = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllInactiveByUserOrGroup", query = "SELECT l FROM MSRSession l WHERE l.usrgrpid= :usrgrpid AND l.active = false ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllHospitalByUserOrGroup", query = "SELECT l FROM MSRSession l WHERE l.usrgrpid= :usrgrpid AND l.hospital = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllForPatientsInGroup", query = "SELECT l FROM MSRSession l where l.forgroup = false AND l.grpid= :grpid ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllHospitalActiveForPatients", query = "SELECT l FROM MSRSession l where l.forgroup = false AND l.active = true AND l.hospital = true ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.findAllHomeActiveForPatients", query = "SELECT l FROM MSRSession l where l.forgroup = false AND l.active = true AND l.hospital = false ORDER BY l.id DESC"),
    @NamedQuery(name = "MSRSession.delete", query = "DELETE FROM MSRSession l WHERE l.id= :sessionid")})

public class MSRSession extends BaseEntity
{
    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    @Column(name = "usrgrpid", table = "msrsession")
    @Basic
    @NotNull
    private Integer usrgrpid;
    
    @Column(name = "exercises", table = "msrsession")
    @Basic
    @NotNull
    private String exercises;
    
    @Column(name = "active", table = "msrsession")
    @Basic
    @NotNull
    private Boolean active;
    
    @Column(name = "hospital", table = "msrsession")
    @Basic
    @NotNull
    private Boolean hospital;
    
    @Column(name = "difficulty", table = "msrsession")
    @Basic
    private String difficulty;

    @Column(name = "forgroup", table = "msrsession")
    @Basic
    @NotNull    
    private Boolean forgroup;
    
    @Column(name = "grpid", table = "msrsession")
    @Basic
    private Integer grpid;
    
    @Column(name = "fromSessionId", table = "msrsession")
    @Basic   
    private Integer fromSessionId;



    public Integer getUsrgrpid() {
        return usrgrpid;
    }

    public void setUsrgrpid(Integer usrgrpid) {
        this.usrgrpid = usrgrpid;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Boolean getHospital() {
        return hospital;
    }

    public void setHospital(Boolean hospital) {
        this.hospital = hospital;
    }
    
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public Boolean getForgroup() {
        return forgroup;
    }

    public void setForgroup(Boolean forgroup) {
        this.forgroup = forgroup;
    }

    public Integer getGrpid() {
        return grpid;
    }

    public void setGrpid(Integer grpid) {
        this.grpid = grpid;
    }
    
    public Integer getFromSessionId() {
		return fromSessionId;
	}

	public void setFromSessionId(Integer fromSessionId) {
		this.fromSessionId = fromSessionId;
	}


}