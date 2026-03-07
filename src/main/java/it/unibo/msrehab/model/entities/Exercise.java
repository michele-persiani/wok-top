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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class modeling an exercise of the multiple sclerosis rehabilitation system
 */
@Entity
@Table(name = "exercise")
@NamedQueries({
    @NamedQuery(name = "Exercise.findAll", query = "SELECT l FROM Exercise l ORDER BY l.id ASC")
    ,
    @NamedQuery(name = "Exercise.findAllByCategory", query = "SELECT l FROM Exercise l WHERE l.category = :category ORDER BY l.id ASC")
    ,
    @NamedQuery(name = "Exercise.findAllByCategoryLike", query = "SELECT l FROM Exercise l WHERE l.type = :giorgio ORDER BY l.id ASC")
    ,
    @NamedQuery(name = "Exercise.findMinIdFromType", query = "SELECT min(l.id) FROM Exercise l WHERE l.type = :type ")
    ,
    @NamedQuery(name = "Exercise.findMaxIdFromType", query = "SELECT max(l.id) FROM Exercise l WHERE l.type = :type ")
    ,
    @NamedQuery(name = "Exercise.findMinIdFromCategory", query = "SELECT min(l.id) FROM Exercise l WHERE l.category = :category ")
    ,
    @NamedQuery(name = "Exercise.findMaxIdFromCategory", query = "SELECT max(l.id) FROM Exercise l WHERE l.category = :category ")
    ,
    @NamedQuery(name = "Exercise.findAllType", query = "SELECT l.type FROM Exercise l GROUP BY l.type")
    ,
    @NamedQuery(name = "Exercise.findAllCategory", query = "SELECT l FROM Exercise l WHERE l.type = :type GROUP BY l.category")
    ,
    @NamedQuery(name = "Exercise.findFromCategory", query = "SELECT l FROM Exercise l WHERE l.category = :category")
    ,
    @NamedQuery(name = "Exercise.delete", query = "DELETE FROM Exercise l WHERE l.id= :exerciseid")
    ,
    @NamedQuery(name = "Exercise.findById", query = "SELECT l FROM Exercise l WHERE l.id = :exerciseid")})

@XmlRootElement
public class Exercise extends BaseEntity
{

    /**
     * Enumeration of the element categories
     */
    public enum ExerciseCategoryValue {
        // attention
        ATT_SEL_STD, ATT_SEL_FLW, ATT_ALT, ATT_DIV,
        // attention faces
        ATT_SEL_STD_FAC, ATT_SEL_FLW_FAC, ATT_ALT_FAC, ATT_DIV_FAC,
        // attention orienteering
        ATT_SEL_STD_ORI, ATT_SEL_FLW_ORI, ATT_ALT_ORI, ATT_DIV_ORI,
        // memory
        MEM_VIS_1, MEM_VIS_2, NBACK, MEM_VIS_5,
        // memory faces
        MEM_VIS_1_FAC, MEM_VIS_2_FAC, NBACK_FAC, MEM_LONG_1,
        // memory orienteering
        MEM_VIS_1_ORI, NBACK_ORI,
        // ex funct
        RES_INH, PLAN, PLAN_1, PLAN_2, PLAN_3,
        // reflexes
        ATT_RFLXS;
    };

    /**
     * Enumeration of the exercise categories
     */
    public enum ExerciseNameValue
    {
        // att sel std
        ATT_SEL_STD_ANM, ATT_SEL_STD_ARR, ATT_SEL_STD_CHS, ATT_SEL_STD_CMP,
        ATT_SEL_STD_FAC, ATT_SEL_STD_FRT, ATT_SEL_STD_VEG,
        ATT_SEL_STD_ANM_RL, ATT_SEL_STD_CHS_RL, ATT_SEL_STD_FRT_RL, ATT_SEL_STD_VEG_RL,     // Names for RL versions
        // att sel flw
        ATT_SEL_FLW_ANM, ATT_SEL_FLW_ARR, ATT_SEL_FLW_CHS, ATT_SEL_FLW_CMP,
        ATT_SEL_FLW_FAC, ATT_SEL_FLW_FRT, ATT_SEL_FLW_VEG,
        // att alt
        ATT_ALT_ANM, ATT_ALT_ARR, ATT_ALT_CHS, ATT_ALT_CMP,
        ATT_ALT_FAC, ATT_ALT_FRT, ATT_ALT_VEG,
        // att div
        ATT_DIV_ANM, ATT_DIV_ARR, ATT_DIV_CHS, ATT_DIV_CMP,
        ATT_DIV_FAC, ATT_DIV_FRT, ATT_DIV_VEG,
        ATT_DIV_FRT_RL, ATT_DIV_CHS_RL, ATT_DIV_ANM_RL, ATT_DIV_VEG_RL,                     // Names for RL versions
        // mem vis 1
        MEM_VIS_1_ANM, MEM_VIS_1_ARR, MEM_VIS_1_CHS, MEM_VIS_1_CMP,
        MEM_VIS_1_FAC, MEM_VIS_1_FRT, MEM_VIS_1_VEG,
        // mem vis 2
        MEM_VIS_2_ANM, MEM_VIS_2_CHS, MEM_VIS_2_FRT, MEM_VIS_2_VEG,
        MEM_VIS_2_ANM_RL, MEM_VIS_2_CHS_RL, MEM_VIS_2_FRT_RL, MEM_VIS_2_VEG_RL,             // Names for RL versions
        // mem vis 5
        MEM_VIS_5_ANM, MEM_VIS_5_CHS, MEM_VIS_5_FRT, MEM_VIS_5_VEG,
        MEM_LONG_1_FAC,
        // nback
        NBACK_ANM, NBACK_ARR, NBACK_CHS, NBACK_FAC, NBACK_FRT, NBACK_VEG,
        NBACK_FRT_RL, NBACK_VEG_RL, NBACK_ANM_RL,                                           // Names for RL versions
        // fun exec
        RES_INH_1, RES_INH_2, RES_INH_3,
        PLAN_1, PLAN_2, PLAN_3,
        //att reflxs
        ATT_RFLXS_MOTORBIKE;
    };

    @JSON(include = false)
    private static final long serialVersionUID = 1L;


    @Column(name = "name", table = "exercise")
    @Enumerated(EnumType.STRING)
    private ExerciseNameValue name;

    @Column(name = "fullname", table = "exercise")
    @Basic
    private String fullname;

    @Column(name = "globalcategory", table = "exercise")
    @Basic
    private String globalcategory;

    @Column(name = "category", table = "exercise")
    @Enumerated(EnumType.STRING)
    private ExerciseCategoryValue category;

    @Column(name = "description", table = "exercise")
    @Basic
    private String description;

    @Column(name = "type", table = "exercise")
    @Basic
    private String type;

    @Column(name = "maxlevel", table = "exercise")
    @Basic
    private int maxlevel;

    @Column(name = "exelementcat", table = "exercise")
    @Enumerated(EnumType.STRING)
    private ExElement.CategoryValue exelementcat;

    @Column(name = "enabled", table = "exercise")
    @Basic
    private boolean enabled;

    @Column(name = "medium_level", table = "exercise")
    @Basic
    private int mediumLevel;

    @Column(name = "difficult_level", table = "exercise")
    @Basic
    private int difficultLevel;

    @Transient
    private String status;



    public ExerciseNameValue getName() {
        return name;
    }

    public void setName(ExerciseNameValue name) {
        this.name = name;
    }

    public String getFullName() {
        return fullname;
    }

    public void setGlobalCategory(String globalcategory) {
        this.globalcategory = globalcategory;
    }

    public String getGlobalCategory() {
        return globalcategory;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }

    public ExerciseCategoryValue getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategoryValue category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxLevel() {
        return maxlevel;
    }

    public void setMaxLevel(Integer maxlevel) {
        this.maxlevel = maxlevel;
    }

    public ExElement.CategoryValue getExelementcat() {
        return exelementcat;
    }

    public void setExelementcat(ExElement.CategoryValue exelementcat) {
        this.exelementcat = exelementcat;
    }


    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }


    public void setStatus(boolean exerciseInHistory, boolean done) {
        if (done) {
            this.status = "done";
        } else if (exerciseInHistory) {
            this.status = "interrupted";
        } else {
            this.status = "todo";
        }
    }

    public String getStatus() {
        return this.status;
    }


    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    public int getLevel(String difficulty)
    {
        switch (difficulty)
        {
            case "training":
            case "demo":
                return -1;
            case "medium":
                return getMediumLevel();
            case "difficult":
            case "hard":
                return getDifficultLevel();
            case "easy":
            default:
                return 1;
        }
    }


    public String getDifficulty(int level)
    {
        if(level >= getDifficultLevel())
            return "difficult";
        if(level >= getMediumLevel())
            return "medium";
        if(level >= 1)
            return "easy";
        return "training";
    }


    public int clampLevel(int level)
    {
        return Math.max(-1, Math.min(level, getMaxLevel()));
    }

    public int getDifficultLevel()
    {
        return difficultLevel;
    }

    public void setDifficultLevel(int difficultLevel)
    {
        this.difficultLevel = difficultLevel;
    }

    public int getMediumLevel()
    {
        return mediumLevel;
    }

    public void setMediumLevel(int mediumLevel)
    {
        this.mediumLevel = mediumLevel;
    }
}
