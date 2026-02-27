package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class modeling a patient profile of the
 * multiple sclerosis rehabilitation system
 */


@Entity
@Table(name = "personaldata")
@NamedQueries({
    @NamedQuery(name = "PersonalData.findAll", query = "SELECT l FROM PersonalData l ORDER BY l.id ASC"),
    @NamedQuery(name = "PersonalData.delete", query = "DELETE FROM PersonalData l WHERE l.id= :userid"),
    @NamedQuery(name = "PersonalData.findAllByUser", query = "SELECT l FROM PersonalData l WHERE l.userid= :userid ORDER BY l.timestamp DESC")})

public class PersonalData extends BaseEntity
{
    
    private final static int MIN_BIRTHYEAR = 1900;
    private final static int MAX_BIRTHYEAR = 2017;

    private final static int MIN_DIAGNOSYSYEAR = 1900;
    private final static int MAX_DIAGNOSYSYEAR = 2020;

    private final static int MIN_EDUCATIONYEARS = 0;
    private final static int MAX_EDUCATIONYEARS = 30;
    
    public enum Gender {
        F, M
    };
    
    private static final Logger logger=LoggerFactory.getLogger(PersonalData.class);
    
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    // Patient id
    @Column(name = "userid", table = "personaldata")
    @Basic
    private Integer userid;
    
    @Column(name = "timestamp", table = "personaldata")
    @Basic
    private Long timestamp;
    
    // Gender
    @Column(name = "gender", table = "personaldata")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    // Year of birth
    @Column(name = "birthyear", table = "personaldata")
    @Basic
    @NotNull(message = "Anno di nascita non valido, non può essere vuoto")
    @Min(value=MIN_BIRTHYEAR,
            message="Anno di nascita non valido, il minimo è " + MIN_BIRTHYEAR)
    @Max(value=MAX_BIRTHYEAR,
            message="Anno di nascita non valido, il massimo è " + MAX_BIRTHYEAR)
    private Integer birthyear;
    
    // Education years
    @Column(name = "educationyears", table = "personaldata")
    @Basic
    @NotNull(message = "Anni di educazione non valido, non può essere vuoto")
    @Min(value=MIN_EDUCATIONYEARS,
            message="Anni di educazione non valido, il minimo è " + MIN_EDUCATIONYEARS)
    @Max(value=MAX_EDUCATIONYEARS,
            message="Anni di educazione non valido, il massimo è " + MAX_EDUCATIONYEARS)
    private Integer educationyears;    
    
    // Job
    @Column(name = "job", table = "personaldata")
    @Basic    
    private String job;    

    // Marital status
    @Column(name = "maritalstatus", table = "personaldata")
    @Basic    
    private String maritalstatus;
    
    // Number of sons 
    @Column(name = "sonnumber", table = "personaldata")
    @Basic    
    private Integer  sonnumber;

    
    public static boolean isBirthyearValid(Integer by) {
        return (by==null || MIN_BIRTHYEAR <= by && by <= MAX_BIRTHYEAR);
    }
    
    public static boolean isDignosysyearValid(Integer dy) {
        return (dy == null || MIN_DIAGNOSYSYEAR <= dy && dy <= MAX_DIAGNOSYSYEAR);
        
    }
        
    public static boolean isEducationYearsValid(Integer eys) {
        return (eys == null || MIN_EDUCATIONYEARS <= eys && eys <= MAX_EDUCATIONYEARS);        
    }
        
    public static boolean isUseridValid(Integer userid) {
        return (userid == null || userid > 0);
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Integer birthyear) {
        this.birthyear = birthyear;
    }

    public Integer getEducationyears() {
        return educationyears;
    }

    public void setEducationyears(Integer educationyears) {
        this.educationyears = educationyears;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }
    
    public Integer getSonnumber() {
        return sonnumber;
    }

    public void setSonnumber(Integer sonnumber) {
        this.sonnumber = sonnumber;
    }    

}