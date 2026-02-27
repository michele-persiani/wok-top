package it.unibo.msrehab.model.entities;

import flexjson.JSON;
import it.unibo.msrehab.model.entities.PersonalData.Gender;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Entity
@Table(name = "raotest")
@NamedQueries({
    @NamedQuery(name = "RaoTest.findAll", query = "SELECT l FROM RaoTest l ORDER BY l.id ASC"),
    @NamedQuery(name = "RaoTest.delete", query = "DELETE FROM RaoTest l WHERE l.id= :testid"),
    @NamedQuery(name = "RaoTest.findAllByUser", query = "SELECT l FROM RaoTest l WHERE l.userid= :userid ORDER BY l.timestamp DESC")})
    //@NamedQuery(name = "RaoTest.findTest", query = "SELECT l FROM RaoTest l WHERE l.email= :email")})


// TODO: Add comments to this class

public class RaoTest extends BaseEntity
{

    @JSON(include = false)
    private final static Map<String, Double> B_AGE
            = Collections.unmodifiableMap(
                    new HashMap<String, Double>() {
                        {
                            put("srtlts", 0.0);
                            put("srtcltr", 0.0);
                            put("spart1036", 0.0);
                            put("sdmt", 0.0);
                            put("pasat3", 0.0);
                            put("pasat2", 0.0);
                            put("srtd", 0.0);
                            put("spartd", 0.0);
                            put("wlg", 0.0);
                        }
                    });

    @JSON(include = false)
    private final static Map<String, Double> B_EDU
            = Collections.unmodifiableMap(
                    new HashMap<String, Double>() {
                        {
                            put("srtlts", 1.402);
                            put("srtcltr", 1.542);
                            put("spart1036", 0.368);
                            put("sdmt", 1.029);
                            put("pasat3", 1.698);
                            put("pasat2", 1.116);
                            put("srtd", 0.201);
                            put("spartd", 0.128);
                            put("wlg", 0.0);
                        }
                    });
    
    @JSON(include = false)
    private final static Map<String, Double> B_GEN
            = Collections.unmodifiableMap(
                    new HashMap<String, Double>() {
                        {
                            put("srtlts-male", 0.0);
                            put("srtlts-female", 0.0);
                            put("srtcltr-male", 0.0);
                            put("srtcltr-female", 0.0);
                            put("spart1036-male", 0.0);
                            put("spart1036-female", 0.0);
                            put("sdmt-male", 0.0);
                            put("sdmt-female", 0.0);
                            put("pasat3-male", 0.0);
                            put("pasat3-female", 0.0);
                            put("pasat2-male", 0.0);
                            put("pasat2-female", 0.0);
                            put("srtd-male", 0.0);
                            put("srtd-female", 0.0);
                            put("spartd-male", 0.0);
                            put("spartd-female", 0.0);
                            put("wlg-male", -2.123);
                            put("wlg-female", 2.123);
                        }
                    });
        
    @JSON(include = false)
    private final static Map<String, double[]> CONTROL_GROUP
            = Collections.unmodifiableMap(
                    new HashMap<String, double[]>() {
                        {
                            put("srtlts", new double[]{47.5, 13.1});
                            put("srtcltr", new double[]{40.3, 14.4});
                            put("spart1036", new double[]{20.9, 4.9});
                            put("sdmt", new double[]{50.9, 9.4});
                            put("pasat3", new double[]{45, 10.6});
                            put("pasat2", new double[]{36.5, 11.5});
                            put("srtd", new double[]{8.9, 2.2});
                            put("spartd", new double[]{7.2, 2.4});
                            put("wlg", new double[]{26.1, 5.8});
                        }
                    });
    
    
    @JSON(include = false)
    private static final long serialVersionUID = 1L;
    
    @Column(name = "userid", table = "raotest")
    @Basic
    private Integer userid;    
        
    @Column(name = "timestamp", table = "raotest")
    @Basic
    private Long timestamp;
    
    @Column(name = "examinator", table = "raotest")
    @Basic
    @Size(min=2,
            message="Esaminatore non valido. Deve essere di almeno due caratteri")    
    private String examinator;    
    
    // SRT-LTS
    @Column(name = "srtltsraw", table = "raotest")
    @Basic
    @NotNull(message = "SRT-LTS non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SRT-LTS non valido, il minimo Ã¨ " + 0)
    private Integer srtltsraw;
    
    @Column(name = "srtltsadj", table = "raotest")
    @Basic    
    private Double srtltsadj;
    
    @Column(name = "srtltseq", table = "raotest")
    @Basic    
    private Integer srtltseq;

    // SRT-CLTR
    @Column(name = "srtcltrraw", table = "raotest")
    @Basic
    @NotNull(message = "SRT-CLTR non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SRT-CLTR non valido, il minimo Ã¨ " + 0)    
    private Integer srtcltrraw;
    
    @Column(name = "srtcltradj", table = "raotest")
    @Basic    
    private Double srtcltradj;
    
    @Column(name = "srtcltreq", table = "raotest")
    @Basic    
    private Integer srtcltreq;
    
    // SPART10/36
    @Column(name = "spart1036raw", table = "raotest")
    @Basic
    @NotNull(message = "SPART10/36 non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SPART10/36 raw non valido, il minimo Ã¨ " + 0)    
    private Integer spart1036raw; 

    @Column(name = "spart1036adj", table = "raotest")
    @Basic    
    private Double spart1036adj; 

    @Column(name = "spart1036eq", table = "raotest")
    @Basic
    private Integer spart1036eq; 

    // SMDT
    @Column(name = "sdmtraw", table = "raotest")
    @Basic
    @NotNull(message = "SMDT non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SMDT non valido, il minimo Ã¨ " + 0)    
    private Integer sdmtraw; 

    @Column(name = "sdmtadj", table = "raotest")
    @Basic
    private Double sdmtadj; 

    @Column(name = "sdmteq", table = "raotest")
    @Basic
    private Integer sdmteq; 

    // PASAT 3
    @Column(name = "pasat3raw", table = "raotest")
    @Basic
    @NotNull(message = "PASAT 3 non valido, non puÃ² essere vuoto")
    @Min(value=0,
            message = "PASAT 3 non valido, il minimo Ã¨ " + 0)    
    private Integer pasat3raw; 

    @Column(name = "pasat3adj", table = "raotest")
    @Basic
    private Double pasat3adj; 

    @Column(name = "pasat3eq", table = "raotest")
    @Basic
    private Integer pasat3eq; 

    // PASAT 2
    @Column(name = "pasat2raw", table = "raotest")
    @Basic
    @NotNull(message = "PASAT 2 non valido, non puÃ² essere vuoto")
    @Min(value=0,
            message = "PASAT 2 non valido, il minimo Ã¨ " + 0)    
    private Integer pasat2raw; 

    @Column(name = "pasat2adj", table = "raotest")
    @Basic
    private Double pasat2adj; 

    @Column(name = "pasat2eq", table = "raotest")
    @Basic
    private Integer pasat2eq; 

    // SRTD
    @Column(name = "srtdraw", table = "raotest")
    @Basic
    @NotNull(message = "SRTD non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SRTD non valido, il minimo Ã¨ " + 0)    
    private Integer srtdraw;
    
    @Column(name = "srtdadj", table = "raotest")
    @Basic
    private Double srtdadj;
    
    @Column(name = "srtdeq", table = "raotest")
    @Basic
    private Integer srtdeq;
    
    // SPART-D
    @Column(name = "spartdraw", table = "raotest")
    @Basic
    @NotNull(message = "SPART-D non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "SPART-D non valido, il minimo Ã¨ " + 0)    
    private Integer spartdraw; 

    @Column(name = "spartdadj", table = "raotest")
    @Basic
    private Double spartdadj; 

    @Column(name = "spartdeq", table = "raotest")
    @Basic
    private Integer spartdeq; 

    // WLG
    @Column(name = "wlgraw", table = "raotest")
    @Basic
    @NotNull(message = "WLG non valido, non puÃ² essere vuoto")
    @Min(value = 0,
            message = "WLG non valido, il minimo Ã¨ " + 0)    
    private Integer wlgraw; 

    @Column(name = "wlgadj", table = "raotest")
    @Basic
    private Double wlgadj;

    @Column(name = "wlgeq", table = "raotest")
    @Basic
    private Integer wlgeq;

    public static boolean isPasat2Valid(Integer pasat2raw) {
        return true;
    }

    public static boolean isPasat3Valid(Integer pasat3raw) {
        return true;
    }

    public static boolean isSdmtValid(Integer sdmtraw) {
        return true;
    }

    public static boolean isSpart1036Valid(Integer spart1036raw) {
        return true;
    }

    public static boolean isSpartdValid(Integer spartdraw) {
        return true;
    }

    public static boolean isSrtcltrValid(Integer srtcltrraw) {
        return true;
    }

    public static boolean isSrtdValid(Integer srtdraw) {
        return true;
    }

    public static boolean isSrtltsValid(Integer srtltsraw) {
        return true;
    }

    public static boolean isWlgValid(Integer wlgraw) {
        return true;
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

    public String getExaminator() {
        return examinator;
    }

    public void setExaminator(String examinator) {
        this.examinator = examinator;
    }

    public Integer getSrtltsraw() {
        return srtltsraw;
    }

    public void setSrtltsraw(Integer srtltsraw) {
        this.srtltsraw = srtltsraw;
    }

    public Double getSrtltsadj() {
        return srtltsadj;
    }

    public void setSrtltsadj(Double srtltsadj) {
        this.srtltsadj = srtltsadj;
    }
    
    public void setSrtltsadj(PersonalData d) {
        this.srtltsadj =
                srtltsraw-B_EDU.get("srtlts")*(d.getEducationyears()-12.4);
    }

    public Integer getSrtltseq() {
        return srtltseq;
    }

    public void setSrtltseq(Integer srtltseq) {
        this.srtltseq = srtltseq;
    }

    public void setSrtltseq() {
        double z=(this.srtltsadj-CONTROL_GROUP.get("srtlts")[0])
                /CONTROL_GROUP.get("srtlts")[1];
        // check value of z and select value accordingly
        this.srtltseq = calcEq(z);
        
    }
    
    public Integer getSrtcltrraw() {
        return srtcltrraw;
    }

    public void setSrtcltrraw(Integer srtcltrraw) {
        this.srtcltrraw = srtcltrraw;
    }

    public Double getSrtcltradj() {
        return srtcltradj;
    }

    public void setSrtcltradj(Double srtcltradj) {
        this.srtcltradj = srtcltradj;
    }

    public void setSrtcltradj(PersonalData d) {
        this.srtcltradj =
                srtltsraw-B_EDU.get("srtcltr")*(d.getEducationyears()-12.4);
    }
    
    public Integer getSrtcltreq() {
        return srtcltreq;
    }

    public void setSrtcltreq(Integer srtcltreq) {
        this.srtcltreq = srtcltreq;
    }
    
    public void setSrtcltreq() {
        double z=(this.srtcltradj-CONTROL_GROUP.get("srtcltr")[0])
                /CONTROL_GROUP.get("srtcltr")[1];
        // check value of z and select value accordingly
        this.srtcltreq = calcEq(z);
        
    }

    public Integer getSpart1036raw() {
        return spart1036raw;
    }

    public void setSpart1036raw(Integer spart1036raw) {
        this.spart1036raw = spart1036raw;
    }

    public Double getSpart1036adj() {
        return spart1036adj;
    }

    public void setSpart1036adj(Double spart1036adj) {
        this.spart1036adj = spart1036adj;
    }
    
    public void setSpart1036adj(PersonalData d) {
        this.spart1036adj = this.spart1036raw
                -B_EDU.get("spart1036")*(d.getEducationyears()-12.4);
    }    

    public Integer getSpart1036eq() {
        return spart1036eq;
    }

    public void setSpart1036eq(Integer spart1036eq) {
        this.spart1036eq = spart1036eq;
    }
    
    public void setSpart1036eq() {
        double z=(this.spart1036adj-CONTROL_GROUP.get("spart1036")[0])
                /CONTROL_GROUP.get("spart1036")[1];
        // check value of z and select value accordingly
        this.spart1036eq = calcEq(z);
        
    }
    
    public Integer getSdmtraw() {
        return sdmtraw;
    }

    public void setSdmtraw(Integer sdmtraw) {
        this.sdmtraw = sdmtraw;
    }

    public Double getSdmtadj() {
        return sdmtadj;
    }

    public void setSdmtadj(PersonalData d) {
        this.sdmtadj = this.sdmtraw
                -B_EDU.get("sdmt")*(d.getEducationyears()-12.4);
    }
    
    public void setSdmtadj(Double sdmtadj) {
        this.sdmtadj = sdmtadj;
    }

    public Integer getSdmteq() {
        return sdmteq;
    }

    public void setSdmteq(Integer sdmteq) {
        this.sdmteq = sdmteq;
    }
    
    public void setSdmteq() {
        double z=(this.sdmtadj-CONTROL_GROUP.get("sdmt")[0])
                /CONTROL_GROUP.get("sdmt")[1];
        // check value of z and select value accordingly
        this.sdmteq = calcEq(z);
        
    }

    public Integer getPasat3raw() {
        return pasat3raw;
    }

    public void setPasat3raw(Integer pasat3raw) {
        this.pasat3raw = pasat3raw;
    }

    public Double getPasat3adj() {
        return pasat3adj;
    }

    public void setPasat3adj(Double pasat3adj) {
        this.pasat3adj = pasat3adj;
    }

    public void setPasat3adj(PersonalData d) {
        this.pasat3adj = this.pasat3raw
                -B_EDU.get("pasat3")*(d.getEducationyears()-12.4);
    }
    
    public Integer getPasat3eq() {
        return pasat3eq;
    }

    public void setPasat3eq(Integer pasat3eq) {
        this.pasat3eq = pasat3eq;
    }
    
    public void setPasat3eq() {
        double z=(this.pasat3adj-CONTROL_GROUP.get("pasat3")[0])
                /CONTROL_GROUP.get("pasat3")[1];
        // check value of z and select value accordingly
        this.pasat3eq = calcEq(z);
        
    }
    
    
    public Integer getPasat2raw() {
        return pasat2raw;
    }

    public void setPasat2raw(Integer pasat2raw) {
        this.pasat2raw = pasat2raw;
    }

    public Double getPasat2adj() {
        return pasat2adj;
    }

    public void setPasat2adj(Double pasat2adj) {
        this.pasat2adj = pasat2adj;
    }

    public void setPasat2adj(PersonalData d) {
        this.pasat2adj = this.pasat2raw
                -B_EDU.get("pasat2")*(d.getEducationyears()-12.4);
    }
    
    public Integer getPasat2eq() {
        return pasat2eq;
    }

    public void setPasat2eq(Integer pasat2eq) {
        this.pasat2eq = pasat2eq;
    }
    
    public void setPasat2eq() {
        double z=(this.pasat2adj-CONTROL_GROUP.get("pasat2")[0])
                /CONTROL_GROUP.get("pasat2")[1];
        // check value of z and select value accordingly
        this.pasat2eq = calcEq(z);
        
    }
    
    public Integer getSrtdraw() {
        return srtdraw;
    }

    public void setSrtdraw(Integer srtdraw) {
        this.srtdraw = srtdraw;
    }

    public Double getSrtdadj() {
        return srtdadj;
    }

    public void setSrtdadj(Double srtdadj) {
        this.srtdadj = srtdadj;
    }

    public void setSrtdadj(PersonalData d) {
        this.srtdadj =
                this.srtdraw-B_EDU.get("srtd")*(d.getEducationyears()-12.4);
    }    
    
    public Integer getSrtdeq() {
        return srtdeq;
    }

    public void setSrtdeq(Integer srtdeq) {
        this.srtdeq = srtdeq;
    }

    public void setSrtdeq() {
        double z=(this.srtdadj-CONTROL_GROUP.get("srtd")[0])
                /CONTROL_GROUP.get("srtd")[1];
        // check value of z and select value accordingly
        this.srtdeq = calcEq(z);
        
    }
    
    public Integer getSpartdraw() {
        return spartdraw;
    }

    public void setSpartdraw(Integer spartdraw) {
        this.spartdraw = spartdraw;
    }

    public Double getSpartdadj() {
        return spartdadj;
    }

    public void setSpartdadj(Double spartdadj) {
        this.spartdadj = spartdadj;
    }

    public void setSpartdadj(PersonalData d) {
        this.spartdadj =
                this.spartdraw-B_EDU.get("spartd")*(d.getEducationyears()-12.4);
    }
    
    public Integer getSpartdeq() {
        return spartdeq;
    }

    public void setSpartdeq(Integer spartdeq) {
        this.spartdeq = spartdeq;
    }

    public void setSpartdeq() {
        double z=(this.spartdadj-CONTROL_GROUP.get("spartd")[0])
                /CONTROL_GROUP.get("spartd")[1];
        // check value of z and select value accordingly
        this.spartdeq = calcEq(z);
        
    }
    
    public Integer getWlgraw() {
        return wlgraw;
    }

    public void setWlgraw(Integer wlgraw) {
        this.wlgraw = wlgraw;
    }

    public Double getWlgadj() {
        return wlgadj;
    }

    public void setWlgadj(Double wlgadj) {
        this.wlgadj = wlgadj;
    }

    public void setWlgadj(PersonalData d) {
        if(d.getGender()==Gender.F) {
            this.wlgadj = this.wlgraw-B_GEN.get("wlg-female");
        }
        else {
            this.wlgadj = this.wlgraw-B_GEN.get("wlg-male");
        }
    }
    
    public Integer getWlgeq() {
        return wlgeq;
    }

    public void setWlgeq(Integer wlgeq) {
        this.wlgeq = wlgeq;
    }
    
    public void setWlgeq() {
        double z=(this.wlgadj-CONTROL_GROUP.get("wlg")[0])
                /CONTROL_GROUP.get("wlg")[1];
        // check value of z and select value accordingly
        this.wlgeq = calcEq(z);
    }
    
    private int calcEq(double z) {
        if (z > -0.72) {
            return 4;
        } else if (z < -0.72 && z >= -0.98) {
            return 3;
        } else if (z < -0.98 && z >= -1.42) {
            return 2;
        } else if (z < -1.42 && z >= -1.63) {
            return 1;
        } else { // z<-1.63
            return 0;
        }

    }
}