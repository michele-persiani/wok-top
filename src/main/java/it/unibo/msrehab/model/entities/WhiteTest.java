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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "whitetest")
@NamedQueries({
    @NamedQuery(name = "WhiteTest.findAll", query = "SELECT l FROM WhiteTest l ORDER BY l.id ASC"),
    @NamedQuery(name = "WhiteTest.delete", query = "DELETE FROM WhiteTest l WHERE l.id= :testid"),
    @NamedQuery(name = "WhiteTest.findAllByUser", query = "SELECT l FROM WhiteTest l WHERE l.patientid= :userid ORDER BY l.timestamp DESC")})

public class WhiteTest extends BaseEntity
{
    
    public enum Answers  {
        MAI, DIRADO, OGNITANTO, SPESSO, SEMPRE
    };

    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    @Column(name = "patientid", table = "whitetest")
    @Basic
    private Integer patientid;    
        
    @Column(name = "timestamp", table = "whitetest")
    @Basic
    private Long timestamp;
    
    @Column(name = "examinator", table = "whitetest")
    @Basic
    @NotNull
    @Size(min=2,
            message="Esaminatore non valido. Deve essere di almeno due caratteri")    
    private String examinator;    
    
    // Linguaggio
    @Column(name = "l01", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l01; 
    
    @Column(name = "l02", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l02; 
    
    @Column(name = "l03", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l03; 
    
    @Column(name = "l04", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l04; 
    
    @Column(name = "l05", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l05;
    
    @Column(name = "l06", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l06;
    
    @Column(name = "l07", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l07;
    
    @Column(name = "l08", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l08;    
    
    @Column(name = "l09", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l09;
    
    @Column(name = "l10", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers l10;
    
    // Memoria visuo-spaziale
    @Column(name = "mvs01", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs01;
    
    @Column(name = "mvs02", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs02;
    
    @Column(name = "mvs03", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs03;
    
    @Column(name = "mvs04", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs04;
    
    @Column(name = "mvs05", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs05;
    
    @Column(name = "mvs06", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs06;
    
    @Column(name = "mvs07", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs07;
    
    @Column(name = "mvs08", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mvs08;
    
    // Attenzione-concentrazione
    @Column(name = "ac01", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac01;
    
    @Column(name = "ac02", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac02;
    
    @Column(name = "ac03", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac03;
    
    @Column(name = "ac04", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac04;
    
    @Column(name = "ac05", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac05;
    
    @Column(name = "ac06", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac06;
    
    @Column(name = "ac07", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac07;
    
    @Column(name = "ac08", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers ac08;
    
    // Abilita' visuo-spaziali
    @Column(name = "avs01", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs01;
    
    @Column(name = "avs02", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs02;
    
    @Column(name = "avs03", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs03;
    
    @Column(name = "avs04", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs04;
    
    @Column(name = "avs05", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs05;
    
    @Column(name = "avs06", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers avs06;
    
    // Memoria verbale
    @Column(name = "mv01", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv01;
    
    @Column(name = "mv02", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv02;
    
    @Column(name = "mv03", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv03;
    
    @Column(name = "mv04", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv04;
    
    @Column(name = "mv05", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv05;
    
    @Column(name = "mv06", table = "whitetest")
    @Enumerated(EnumType.STRING)
    private Answers mv06;

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExaminator() {
        return examinator;
    }

    public void setExaminator(String examinator) {
        this.examinator = examinator;
    }

    public Answers getL01() {
        return l01;
    }

    public void setL01(Answers l01) {
        this.l01 = l01;
    }

    public Answers getL02() {
        return l02;
    }

    public void setL02(Answers l02) {
        this.l02 = l02;
    }

    public Answers getL03() {
        return l03;
    }

    public void setL03(Answers l03) {
        this.l03 = l03;
    }

    public Answers getL04() {
        return l04;
    }

    public void setL04(Answers l04) {
        this.l04 = l04;
    }

    public Answers getL05() {
        return l05;
    }

    public void setL05(Answers l05) {
        this.l05 = l05;
    }

    public Answers getL06() {
        return l06;
    }

    public void setL06(Answers l06) {
        this.l06 = l06;
    }

    public Answers getL07() {
        return l07;
    }

    public void setL07(Answers l07) {
        this.l07 = l07;
    }

    public Answers getL08() {
        return l08;
    }

    public void setL08(Answers l08) {
        this.l08 = l08;
    }

    public Answers getL09() {
        return l09;
    }

    public void setL09(Answers l09) {
        this.l09 = l09;
    }

    public Answers getL10() {
        return l10;
    }

    public void setL10(Answers l10) {
        this.l10 = l10;
    }

    public Answers getMvs01() {
        return mvs01;
    }

    public void setMvs01(Answers mvs01) {
        this.mvs01 = mvs01;
    }

    public Answers getMvs02() {
        return mvs02;
    }

    public void setMvs02(Answers mvs02) {
        this.mvs02 = mvs02;
    }

    public Answers getMvs03() {
        return mvs03;
    }

    public void setMvs03(Answers mvs03) {
        this.mvs03 = mvs03;
    }

    public Answers getMvs04() {
        return mvs04;
    }

    public void setMvs04(Answers mvs04) {
        this.mvs04 = mvs04;
    }

    public Answers getMvs05() {
        return mvs05;
    }

    public void setMvs05(Answers mvs05) {
        this.mvs05 = mvs05;
    }

    public Answers getMvs06() {
        return mvs06;
    }

    public void setMvs06(Answers mvs06) {
        this.mvs06 = mvs06;
    }

    public Answers getMvs07() {
        return mvs07;
    }

    public void setMvs07(Answers mvs07) {
        this.mvs07 = mvs07;
    }

    public Answers getMvs08() {
        return mvs08;
    }

    public void setMvs08(Answers mvs08) {
        this.mvs08 = mvs08;
    }

    public Answers getAc01() {
        return ac01;
    }

    public void setAc01(Answers ac01) {
        this.ac01 = ac01;
    }

    public Answers getAc02() {
        return ac02;
    }

    public void setAc02(Answers ac02) {
        this.ac02 = ac02;
    }

    public Answers getAc03() {
        return ac03;
    }

    public void setAc03(Answers ac03) {
        this.ac03 = ac03;
    }

    public Answers getAc04() {
        return ac04;
    }

    public void setAc04(Answers ac04) {
        this.ac04 = ac04;
    }

    public Answers getAc05() {
        return ac05;
    }

    public void setAc05(Answers ac05) {
        this.ac05 = ac05;
    }

    public Answers getAc06() {
        return ac06;
    }

    public void setAc06(Answers ac06) {
        this.ac06 = ac06;
    }

    public Answers getAc07() {
        return ac07;
    }

    public void setAc07(Answers ac07) {
        this.ac07 = ac07;
    }

    public Answers getAc08() {
        return ac08;
    }

    public void setAc08(Answers ac08) {
        this.ac08 = ac08;
    }

    public Answers getAvs01() {
        return avs01;
    }

    public void setAvs01(Answers avs01) {
        this.avs01 = avs01;
    }

    public Answers getAvs02() {
        return avs02;
    }

    public void setAvs02(Answers avs02) {
        this.avs02 = avs02;
    }

    public Answers getAvs03() {
        return avs03;
    }

    public void setAvs03(Answers avs03) {
        this.avs03 = avs03;
    }

    public Answers getAvs04() {
        return avs04;
    }

    public void setAvs04(Answers avs04) {
        this.avs04 = avs04;
    }

    public Answers getAvs05() {
        return avs05;
    }

    public void setAvs05(Answers avs05) {
        this.avs05 = avs05;
    }

    public Answers getAvs06() {
        return avs06;
    }

    public void setAvs06(Answers avs06) {
        this.avs06 = avs06;
    }

    public Answers getMv01() {
        return mv01;
    }

    public void setMv01(Answers mv01) {
        this.mv01 = mv01;
    }

    public Answers getMv02() {
        return mv02;
    }

    public void setMv02(Answers mv02) {
        this.mv02 = mv02;
    }

    public Answers getMv03() {
        return mv03;
    }

    public void setMv03(Answers mv03) {
        this.mv03 = mv03;
    }

    public Answers getMv04() {
        return mv04;
    }

    public void setMv04(Answers mv04) {
        this.mv04 = mv04;
    }

    public Answers getMv05() {
        return mv05;
    }

    public void setMv05(Answers mv05) {
        this.mv05 = mv05;
    }

    public Answers getMv06() {
        return mv06;
    }

    public void setMv06(Answers mv06) {
        this.mv06 = mv06;
    }
    
}