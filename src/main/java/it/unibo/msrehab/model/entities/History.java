package it.unibo.msrehab.model.entities;

import flexjson.JSON;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class modeling an exercise of the multiple sclerosis rehabilitation
 * system
 */
@Entity
@Table(name = "history")
@NamedQueries({
    @NamedQuery(name = "History.maxIdForUserIdSessIdExId", query = "SELECT max(l.id) FROM History l  WHERE l.userid = :userid AND l.exid = :exid AND l.sessid=:sessid"),
    
    @NamedQuery(name = "History.findAll", query = "SELECT l FROM History l ORDER BY l.timestamp DESC"),
    //@NamedQuery(name = "History.findAllByExCategory", query = "SELECT l FROM History l WHERE l.excat= :excat ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByExercise", query = "SELECT l FROM History l WHERE l.exid= :exid ORDER BY l.timestamp DESC"),
    //@NamedQuery(name = "History.getAllRecordsByExIdRange", query = "SELECT l FROM History l WHERE l.userid = :userid AND l.exid>= :exIdInit AND l.exid<= :exIdFinish AND l.difficulty = :difficulty ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByExIdRange", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        "AVG(l.relperformance) as performance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t, " +
                                                                        "AVG(l.pCorrect) as avgpasswd, "+
                                                                        "AVG(l.pMissed) as avgmissed, "+
                                                                        "AVG(l.pWrong) as avgwrong, "+
                                                                        "AVG(l.pTime) as avgtime "+
                                                                        "FROM History AS l " +
                                                                        "WHERE " +
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	l.exid>= :exIdInit " +
                                                                        "	AND " +
                                                                        "	l.exid<= :exIdFinish " +
                                                                        "	AND " +
                                                                        "	l.difficulty = :difficulty " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByType", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        //"AVG(l.relperformance) as performance, " +
                                                                        "AVG(l.absperformance) as absperformance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t, " +
                                                                        "AVG(l.pCorrect) as avgpasswd, "+
                                                                        "AVG(l.pMissed) as avgmissed, "+
                                                                        "AVG(l.pWrong) as avgwrong, "+
                                                                        "AVG(l.pTime) as avgtime, "+
                                                                        "AVG(l.maxtime) as maxtime "+
                                                                        "FROM History AS l " +
                                                                        "JOIN Exercise AS e "+
                                                                        "WHERE " +
                                                                        "       l.exid = e.id AND "+
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	e.type= :type " +
                                                                        "	AND " +
                                                                        "	l.difficulty = :difficulty " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByCategory", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        //"AVG(l.relperformance) as performance, " +
                                                                        "AVG(l.absperformance) as absperformance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t, " +
                                                                        "AVG(l.pCorrect) as avgpasswd, "+
                                                                        "AVG(l.pMissed) as avgmissed, "+
                                                                        "AVG(l.pWrong) as avgwrong, "+
                                                                        "AVG(l.pTime) as avgtime, "+
                                                                        "AVG(l.maxtime) as maxtime "+
                                                                        "FROM History AS l " +
                                                                        "JOIN Exercise AS e "+
                                                                        "WHERE " +
                                                                        "       l.exid = e.id AND "+
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	e.category= :category " +
                                                                        "	AND " +
                                                                        "	l.difficulty = :difficulty " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByTypeNoDifficulty", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        "AVG(l.relperformance) as relperformance, " +
                                                                        //"AVG(l.absperformance) as absperformance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t, " +
                                                                        "AVG(l.pCorrect) as avgpasswd, "+
                                                                        "AVG(l.pMissed) as avgmissed, "+
                                                                        "AVG(l.pWrong) as avgwrong, "+
                                                                        "AVG(l.pTime) as avgtime, "+
                                                                        "AVG(l.maxtime) as maxtime "+
                                                                        "FROM History AS l " +
                                                                        "JOIN Exercise AS e "+
                                                                        "WHERE " +
                                                                        "       l.exid = e.id AND "+
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	e.type= :type " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByCategoryNoDifficulty", query = "SELECT " +
            									   "SUM(l.passed) as passed, " +
            									 "COUNT(l.passed) as total, " +
            									 "AVG(l.relperformance) as relperformance, " +
            																//"AVG(l.absperformance) as absperformance, " +
            																//"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
            									"l.timestamp/1000/:group as d, "+
            									"l.timestamp as t, " +
            									"AVG(l.pCorrect) as avgpasswd, "+
                                                                                "AVG(l.pMissed) as avgmissed, "+
            									"AVG(l.pWrong) as avgwrong, "+
            									"AVG(l.pTime) as avgtime, "+
            									"AVG(l.maxtime) as maxtime "+
            									"FROM History AS l " +
            									"JOIN Exercise AS e "+
            									"WHERE " +
            									"       l.exid = e.id AND "+
            									"	l.userid = :userid " +
            									"	AND " +
            									"	e.category= :category " +
            									"	AND " +
            									"	l.timestamp >= :start " +
            									"	AND " +
            									"	l.timestamp <= :end " +
            									"GROUP BY d  " +
            									"ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByExIdRangeNoDifficulty", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        "AVG(l.relperformance) as performance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t, " +
                                                                        "AVG(l.pCorrect) as avgpasswd, "+
                                                                        "AVG(l.pMissed) as avgmissed, "+
                                                                        "AVG(l.pWrong) as avgwrong, "+
                                                                        "AVG(l.pTime) as avgtime "+
                                                                        "FROM History AS l " +
                                                                        "WHERE " +
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	l.exid>= :exIdInit " +
                                                                        "	AND " +
                                                                        "	l.exid<= :exIdFinish " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByExIdSessIdRangeNoDifficulty", query = "SELECT " +
                                                                        "SUM(l.passed) as passed, " +
                                                                        "COUNT(l.passed) as total, " +
                                                                        "AVG(l.relperformance) as performance, " +
                                                                        //"concat((l.timestamp/1000/31556926+1970),\" \",l.timestamp/1000/2629743 -(l.timestamp/1000/31556926)*12 +1,\" \",l.timestamp/1000/86400 - ((l.timestamp/1000/86400)/365)*365 - 10*30.44 - 10) as d " +
                                                                        "l.timestamp/1000/:group as d, "+
                                                                        "l.timestamp as t " +
                                                                        "FROM History AS l " +
                                                                        "WHERE " +
                                                                        "	l.userid = :userid " +
                                                                        "	AND " +
                                                                        "	l.sessid = :sessid " +
                                                                        "	AND " +
                                                                        "	l.exid>= :exIdInit " +
                                                                        "	AND " +
                                                                        "	l.exid<= :exIdFinish " +
                                                                        "	AND " +
                                                                        "	l.timestamp >= :start " +
                                                                        "	AND " +
                                                                        "	l.timestamp <= :end " +
                                                                        "GROUP BY d  " +
                                                                        "ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByUser", query = "SELECT l FROM History l WHERE l.userid= :userid ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByUserAndExercise", query = "SELECT l FROM History l WHERE l.userid= :userid AND l.exid= :exid ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByUserAndExerciseAndSessid", query = "SELECT l FROM History l WHERE l.userid= :userid AND l.exid= :exid AND l.sessid = :sessid AND l.passed is not NULL ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.findAllByExerciseAndSessid", query = "SELECT l FROM History l WHERE l.exid= :exid AND l.sessid = :sessid ORDER BY l.timestamp DESC"),
    @NamedQuery(name = "History.delete", query = "DELETE FROM History l WHERE l.id= :id"),
    @NamedQuery(name = "History.countExerciseInSession", query = "SELECT count(h) FROM History h WHERE h.sessid=:sessid AND h.exid=:exid"),
    @NamedQuery(name = "History.findLastInterruptedExercise", query = "SELECT h.exid FROM History h WHERE h.exid IN :interruptedExs AND h.sessid=:sessid ORDER BY h.timestamp DESC"),
        @NamedQuery(name = "History.findAllByUserAndExerciseAndSessidSolvedAgent", query = "SELECT l FROM History l WHERE l.userid= :userid AND l.exid= :exid AND l.sessid = :sessid AND l.passed is not null and l.levelStrategy= :rlagent ORDER BY l.timestamp DESC")
        ,
        @NamedQuery(name = "History.countExerciseInSession", query = "SELECT count(h) FROM History h WHERE h.sessid=:sessid AND h.exid=:exid"),
        @NamedQuery(name = "History.findAssignmentById", query = "SELECT h FROM History h WHERE h.id=:id"),


})

@XmlRootElement
public class History extends BaseEntity
{

    /**
     * Strategy that was used to select the level and threhsold for the history
     */
    public enum LevelStrategy
    {
        TRAINING, // For trainings (level always = -1)
        LEVEL, // Agente a livelli (RL).
        ADAPTIVE, // Agente adattivo.
        INCREMENTAL // Strategia incrementale. Due successi di fila per passare di livello.
    }

    @JSON(include = false)
    private static final long serialVersionUID = 1L;


    @Column(name = "userid", table = "history")
    @Basic
    private Integer userid;

    @Column(name = "exid", table = "history")
    @Basic
    private Integer exid;
            
    @Column(name = "sessid", table = "history")
    @Basic
    private Integer sessid;

    @Column(name = "absperformance", table = "history")
    @Basic
    private Double absperformance;
    
    @Column(name = "relperformance", table = "history")
    @Basic
    private Double relperformance;

    @Column(name = "passed", table = "history")
    @Basic
    private Boolean passed;    
    
    @Column(name = "timestamp", table = "history")
    @Basic
    private Long timestamp;
    
    @Column(name = "level", table = "history")
    @Basic
    private Integer level;

    @Column(name = "difficulty", table = "history")
    @Basic
    private String difficulty;
    
    @Column(name = "ptime", table = "history")
    @Basic
    private Double pTime;
    
    
    @Column(name = "pCorrect", table = "history")
    @Basic
    private Integer pCorrect;
    
    @Column(name = "pMissed", table = "history")
    @Basic
    private Integer pMissed;
    
    @Column(name = "pWrong", table = "history")
    @Basic
    private Integer pWrong;
    
    @Column(name = "maxtime", table = "history")
    @Basic
    private Double maxtime;


    @Column(name = "threshold", table = "history")
    @Basic
    private Double threshold;


    @Column(name = "strategy", table = "history")
    @Basic
    @Enumerated(EnumType.ORDINAL)
    private LevelStrategy levelStrategy;


    public boolean isSolved()
    {
        return passed != null;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getExid() {
        return exid;
    }

    public void setExid(Integer exid) {
        this.exid = exid;
    }

    public Integer getSessid() {
        return sessid;
    }

    public void setSessid(Integer sessid) {
        this.sessid = sessid;
    }

    public Double getAbsperformance()
    {
        if(Double.isNaN(absperformance)){
            return 0.0;
        }else{
            return absperformance;
        }
        
    }

    public void setAbsperformance(Double absperformance) {
        this.absperformance = absperformance;
    }

    public Double getRelperformance() {
        return relperformance;
    }

    public void setRelperformance(Double relperformance) {
        this.relperformance = relperformance;
    }
    
    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    
    public Double getpTime() {
        return pTime;
    }
    
    public Integer getpCorrect() {
      return pCorrect;
    }
    
    public Integer getpMissed() {
        return pMissed;
    }
    
    public Integer getpWrong() {
        return pWrong;
    }
    
    

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setpTime(Double pTime) {
        this.pTime = pTime;
    }
    
    public void setpCorrect(Integer pCorrect) {
        this.pCorrect = pCorrect;
    }
    
    public void setpMissed(Integer pMissed) {
        this.pMissed = pMissed;
    }
    
    public void setpWrong(Integer pWrong) {
        this.pWrong = pWrong;
    }
    
        /**
     * @return the maxtime
     */
    public Double getMaxtime() {
        return maxtime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * @param maxtime the maxtime to set
     */
    public void setMaxtime(Double maxtime) {
        this.maxtime = maxtime;
    }

    public Double getPassThreshold()
    {
        return threshold;
    }

    public void setPassThreshold(Double threshold)
    {
        this.threshold = threshold;
    }

    public LevelStrategy getLevelStrategy()
    {
        return levelStrategy;
    }

    public void setLevelStrategy(LevelStrategy rlagent)
    {
        this.levelStrategy = rlagent;
    }
}
