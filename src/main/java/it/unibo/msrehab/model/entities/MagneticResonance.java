package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.joda.time.LocalDateTime;

/**
 * Class modeling a magnetic resonance of the
 * multiple sclerosis rehabilitation system
 */


@Entity
@Table(name = "magneticresonance")
@NamedQueries({
    @NamedQuery(name = "MagneticResonance.findAll", query = "SELECT l FROM MagneticResonance l ORDER BY l.id ASC"),
    @NamedQuery(name = "MagneticResonance.delete", query = "DELETE FROM MagneticResonance l WHERE l.id= :id"),
    @NamedQuery(name = "MagneticResonance.findAllByClinicalprofileid", query = "SELECT l FROM MagneticResonance l WHERE l.clinicalprofileid= :clinicalprofileid ORDER BY l.timestamp DESC")})

public class MagneticResonance extends BaseEntity
{
    
    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    
    // Patient id
    @Column(name = "clinicalprofileid", table = "magneticresonance")
    @Basic
    private Integer clinicalprofileid;
        
    // Report
    @Column(name = "report", table = "magneticresonance")
    @Basic
    private String report;
    
    // Report
    @Column(name = "date", table = "magneticresonance")
    @Basic
    private LocalDateTime date;

    @Column(name = "timestamp", table = "magneticresonance")
    @Basic
    private Long timestamp;     


    public Integer getClinicalprofileid() {
        return clinicalprofileid;
    }

    public void setClinicalprofileid(Integer clinicalprofileid) {
        this.clinicalprofileid = clinicalprofileid;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}