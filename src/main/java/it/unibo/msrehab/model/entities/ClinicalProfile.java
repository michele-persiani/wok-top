package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Class modeling a patient profile of the
 * multiple sclerosis rehabilitation system
 */


@Entity
@Table(name = "clinicalprofile")
@NamedQueries({
    @NamedQuery(name = "ClinicalProfile.findAll", query = "SELECT l FROM ClinicalProfile l ORDER BY l.id ASC"),
    @NamedQuery(name = "ClinicalProfile.delete", query = "DELETE FROM ClinicalProfile l WHERE l.id= :userid"),
    @NamedQuery(name = "ClinicalProfile.findAllByUser", query = "SELECT l FROM ClinicalProfile l WHERE l.userid= :userid ORDER BY l.timestamp DESC")})

public class ClinicalProfile extends BaseEntity
{

    private final static int MIN_DIAGNOSYSYEAR = 1900;
    private final static int MAX_DIAGNOSYSYEAR = 2033;


    /**
     * Types of multiple sclerosis
     */
     public enum SMType {
        // relapsing-remitting
        RRMS,
        // secondary progressive
        SPMS,
        // primary progressive
        PPMS,
        // progressive relapsing
        PRMS;
    };


    
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    // Patient id
    @Column(name = "userid", table = "clinicalprofile")
    @Basic
    private Integer userid;
    
    @Column(name = "timestamp", table = "clinicalprofile")
    @Basic
    private Long timestamp;
            
    // Year of diagnosys
    @Column(name = "diagnosysyear", table = "clinicalprofile")
    @Basic
    @NotNull(message = "Anni di diagnosi non valido, non può essere vuoto")
    @Min(value=MIN_DIAGNOSYSYEAR,
            message="Anno di diagnosi non valido, il minimo è " + MIN_DIAGNOSYSYEAR)
    @Max(value=MAX_DIAGNOSYSYEAR,
            message="Anno di diagnosi non valido, il massimo è " + MAX_DIAGNOSYSYEAR)
    private Integer diagnosysyear;

    // Disease tipology
    @Column(name = "mstype", table = "clinicalprofile")
    @Enumerated(EnumType.STRING)
    private SMType mstype;    

    // Disease modifying therapy
    @Column(name = "dmt", table = "clinicalprofile")
    @Basic
    @NotNull
    @Size(min=2, message = "Terapia non valida")
    private String dmt;
    
    // Disease modifying therapy start date
    @Column(name = "dmtdate", table = "clinicalprofile")
    @Basic
    @NotNull
    @Past(message = "La data inizio deve essere nel passato")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dmtdate;
    
    // Symptomatic therapy
    @Column(name = "st", table = "clinicalprofile")
    @Basic
    @NotNull
    @Size(min=2, message = "Terapia non valida")
    private String st;
    
    // Disease modifying therapy start date
    @Column(name = "stdate", table = "clinicalprofile")
    @Basic
    @NotNull
    @Past(message = "La data inizio deve essere nel passato")
    @Temporal(javax.persistence.TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date stdate;

    // Profilo funzionale neurologico
    // Motor system
    @Column(name = "motsys", table = "clinicalprofile")
    @Basic    
    private Boolean motsys;

    // Profilo funzionale neurologico
    // Sensorial system
    @Column(name = "sensys", table = "clinicalprofile")
    @Basic    
    private Boolean sensys;

    // Profilo funzionale neurologico
    // Visual system
    @Column(name = "vissys", table = "clinicalprofile")
    @Basic    
    private Boolean vissys;

    // Profilo funzionale neurologico
    // Sphinteric system
    @Column(name = "sphsys", table = "clinicalprofile")
    @Basic    
    private Boolean sphsys;
    
    // Profilo funzionale neurologico
    // Cerebellar system
    @Column(name = "cersys", table = "clinicalprofile")
    @Basic    
    private Boolean cersys;

    // Profilo funzionale neurologico
    // Brainstem system
    @Column(name = "brasys", table = "clinicalprofile")
    @Basic    
    private Boolean brasys;
    
    //EDSS value
    @Column(name = "edssval", table = "clinicalprofile")
    @Basic    
    private Double edssval;
        
    // Notes
    @Column(name = "notes", table = "clinicalprofile")
    @Basic
    private String notes;
    
    public static boolean isDignosysyearValid(Integer dy) {
        return (dy == null || MIN_DIAGNOSYSYEAR <= dy && dy <= MAX_DIAGNOSYSYEAR);
        
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public Integer getDiagnosysyear() {
        return diagnosysyear;
    }

    public void setDiagnosysyear(Integer diagnosysyear) {
        this.diagnosysyear = diagnosysyear;
    }

    public SMType getMstype() {
        return mstype;
    }

    public void setMstype(SMType mstype) {
        this.mstype = mstype;
    }
    
    public String getDmt() {
        return this.dmt;
    }

    public void setDmt(String dmt) {
        this.dmt = dmt;
    }

    public String getSt() {
        return this.st;
    }

    public void setSt(String st) {
        this.st = st;
    }
    
    public Boolean getMotsys() {
        return motsys;
    }

    public void setMotsys(Boolean motsys) {
        this.motsys = motsys;
    }

    public Boolean getSensys() {
        return sensys;
    }

    public void setSensys(Boolean sensys) {
        this.sensys = sensys;
    }

    public Boolean getVissys() {
        return vissys;
    }

    public void setVissys(Boolean vissys) {
        this.vissys = vissys;
    }

    public Boolean getSphsys() {
        return sphsys;
    }

    public void setSphsys(Boolean sphsys) {
        this.sphsys = sphsys;
    }

    public Boolean getCersys() {
        return cersys;
    }

    public void setCersys(Boolean cersys) {
        this.cersys = cersys;
    }

    public Boolean getBrasys() {
        return brasys;
    }

    public void setBrasys(Boolean brasys) {
        this.brasys = brasys;
    }

    public Date getDmtdate() {
        return dmtdate;
    }

    public void setDmtdate(Date dmtdate) {
        this.dmtdate = dmtdate;
    }

    public Date getStdate() {
        return stdate;
    }

    public void setStdate(Date stdate) {
        this.stdate = stdate;
    }

    public Double getEdssval() {
        return edssval;
    }

    public void setEdssval(Double edssval) {
        this.edssval = edssval;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}