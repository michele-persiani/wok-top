package it.unibo.msrehab.model.entities;

import flexjson.JSON;
import org.joda.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Class modeling a patient profile of the
 * multiple sclerosis rehabilitation system
 */


@Entity
@Table(name = "npstest")
@NamedQueries({
    @NamedQuery(name = "NPSTest.findAll", query = "SELECT l FROM NPSTest l ORDER BY l.id ASC"),
    @NamedQuery(name = "NPSTest.delete", query = "DELETE FROM NPSTest l WHERE l.id= :userid"),
    @NamedQuery(name = "NPSTest.findAllByUser", query = "SELECT l FROM NPSTest l WHERE l.userid= :userid ORDER BY l.timestamp DESC")})

public class NPSTest extends BaseEntity
{
    
    private final static int MIN_TMT = 0;

    //private final static int MIN_MMSE = 0;
    //private final static int MAX_MMSE = 30;

    private final static int MIN_MFISC = 0;
    private final static int MAX_MFISC = 40;

    private final static int MIN_MFISP = 0;
    private final static int MAX_MFISP = 36;

    private final static int MIN_MFISPS = 0;
    private final static int MAX_MFISPS = 8;
    
    private final static int MIN_MFISTOT = 0;
    private final static int MAX_MFISTOT = 84;
    
    //private final static int MIN_HADSA = 0;
    //private final static int MAX_HADSA = 21;

    //private final static int MIN_HADSD = 0;
    //private final static int MAX_HADSD = 21;

    //private final static int MIN_HADSTOT = 0;
    //private final static int MAX_HADSTOT = 42;
    
    private final static int MIN_STAI1 = 20;
    private final static int MAX_STAI1 = 80;
    
    private final static int MIN_STAI2 = 20;
    private final static int MAX_STAI2 = 80;
    
    private final static int MIN_BECK = 0;
    private final static int MAX_BECK = 63;
    
    private final static int MIN_MSQOL54 = 0;
    private final static int MAX_MSQOL54 = 100;
    
    private final static int MIN_DKEFSDESCR = 0;
    private final static int MAX_DKEFSDESCR = 64;
    
    private final static int MIN_DKEFSCAT = 0;
    private final static int MAX_DKEFSCAT = 16;
    
//    private final static double[][] mmseAdjTable = {
//        /* Education years / Age
//               20-29  30-39  40-49  50-59  60-69  70-79 */
//    /*0-3*/   { 0.72,  0.91,  1.10,  2.24,  2.99,  5.24},
//    /*4-5*/   {-0.17,  0.09,  0.31,  0.74,  1.27,  2.03},
//    /*6-8*/   {-0.81, -0.58, -0.38, -0.03,  0.53,  1.20},
//    /*9-13*/  {-1.41, -1.25, -1.11, -1.01, -0.51, -0.14},
//    /*<13*/   {-1.93, -1.90, -1.79, -1.69, -1.54, -1.15}
//    };
    
    /* Put -17 instead of -50 in the first row */
    private final static int[][] tmtaAdjTable = {
        /* Education years / Age
               20+ 25+ 30+ 35+  40+  45+  50+  55+  60+  65+  70+  75+ */        
    /*0-3*/   { 2, -1, -4, -8, -11, -14, -17, -21, -24, -27, -30, -34},
    /*4-5*/   { 5,  2, -2, -5,  -8, -11, -15, -18, -21, -24, -27, -31},
    /*6-8*/   { 9,  6,  3,  0,  -4,  -7, -10, -13, -17, -20, -23, -26},
    /*9-13*/  {17, 13, 10,  7,   4,   0,  -3,  -6,  -9, -13, -16, -19},
    /*13-17*/ {23, 19, 16, 13,  10,   6,   3,   0,  -3,  -7, -10, -13}
    };    

    private final static int[][] tmtbAdjTable = {
        /*Education years / Age
               20+  25+  30+  35+  40+  45+  50+  55+  60+  65+   70+   75+ */        
    /*0-3*/   {-8, -18, -28, -38, -48, -59, -69, -79, -89, -99, -109, -119},
    /*4-5*/   { 5,  -5, -16, -26, -36, -46, -56, -66, -76, -87,  -97, -107},
    /*6-8*/   {23,  13,   3,  -7, -17, -27, -37, -48, -58, -68,  -78,  -88},
    /*9-13*/  {55,  45,  34,  24,  14,   4,  -6, -16, -26, -37,  -47,  -57},
    /*15-17*/ {80,  70,  59,  49,  39,  29,  19,   9,  -1, -12,  -22,  -32}
    };

    private final static int[][] tmtbaAdjTable = {
        /* Education years / Age
                20+  25+  30+  35+  40+  45+  50+  55+  60+  65+  70+  75+ */        
    /*0-3*/   {-10, -17, -24, -31, -37, -44, -51, -58, -65, -72, -79, -85},
    /*4-5*/   {  0,  -7, -14, -21, -28, -35, -42, -48, -55, -62, -69, -76},
    /*6-8*/   { 14,   7,   0,  -7, -14, -20, -27, -34, -41, -48, -55, -61},
    /*9-13*/  { 38,  31,  24,  17,  10,   4,  -3, -10, -17, -24, -31, -38},
    /*15-17*/ { 57,  50,  43,  36,  30,  23,  16,   9,   2,  -5, -12, -18}
    };


    @JSON(include = false)
    private static final long serialVersionUID = 1L;

    
    // Patient id
    @Column(name = "userid", table = "npstest")
    @Basic
    private Integer userid;
    
    @Column(name = "timestamp", table = "npstest")
    @Basic
    private Long timestamp;
    
    @Column(name = "examinator", table = "npstest")
    @Basic
    @Size(min=2,
            message="Esaminatore non valido. Deve essere di almeno due caratteri")    
    private String examinator;    
            
    // Trail making test A raw
    @Column(name = "tmtaraw", table = "npstest")
    @Basic
    @NotNull(message = "TMTA non valido, non può essere vuoto")
    @Min(value=MIN_TMT,
            message="TMTA non valido, il minimo è " + MIN_TMT)
    private Integer tmtaraw;
    
    // Trail making test A adjusted
    @Column(name = "tmtaadj", table = "npstest")
    @Basic    
    private Integer tmtaadj;
    
    // Trail making test A equivalent
    @Column(name = "tmtaeq", table = "npstest")
    @Basic    
    private Integer tmtaeq;
    
    // Trail making test B raw
    @Column(name = "tmtbraw", table = "npstest")
    @Basic
    @NotNull(message = "TMTB non valido, non può essere vuoto")
    @Min(value=MIN_TMT,
            message="TMTB non valido, il minimo è " + MIN_TMT)
    private Integer tmtbraw;
    
    // Trail making test B adjusted
    @Column(name = "tmtbadj", table = "npstest")
    @Basic
    private Integer tmtbadj;
    
    // Trail making test B equivalent
    @Column(name = "tmtbeq", table = "npstest")
    @Basic    
    private Integer tmtbeq;
    
    // Trail making test B-A raw
    @Column(name = "tmtbaraw", table = "npstest")
    @Basic    
    private Integer tmtbaraw;
    
    // Trail making test B-A adjusted
    @Column(name = "tmtbaadj", table = "npstest")
    @Basic    
    private Integer tmtbaadj;
    
    // Trail making test B-A equivalent
    @Column(name = "tmtbaeq", table = "npstest")
    @Basic    
    private Integer tmtbaeq;
    
    // FOLSTEIN'S MINI-MENTAL STATE EXAMINATION raw
    //@Column(name = "mmseraw", table = "npstest")
    //@Basic
    //@NotNull(message = "MMSE non valido, non può essere vuoto")
    //@Min(value=MIN_MMSE,
    //        message="MMSE non valido, il minimo è " + MIN_MMSE)
    //@Max(value=MAX_MMSE,
    //        message="MMSE non valido, il massimo è " + MAX_MMSE)
    //private Integer mmseraw;
        
    // FOLSTEIN'S MINI-MENTAL STATE EXAMINATION adjusted
    //@Column(name = "mmseadj", table = "npstest")
    ///@Basic    
    //private Double mmseadj;
    
    // Fatigue impact (MFIS) Phisical
    @Column(name = "mfisp", table = "npstest")
    @Basic
    @NotNull(message = "MFISP non valido, non può essere vuoto")
    @Min(value = MIN_MFISP,
            message = "MFISP non valido, il minimo è " + MIN_MFISP)
    @Max(value = MAX_MFISP,
            message = "MFISP non valido, il massimo è " + MAX_MFISP)
    private Integer mfisp;
    
    // Fatigue impact (MFIS) Cognitive
    @Column(name = "mfisc", table = "npstest")
    @Basic
    @NotNull(message = "MFISC non valido, non può essere vuoto")
    @Min(value = MIN_MFISC,
            message = "MFISC non valido, il minimo è " + MIN_MFISC)
    @Max(value = MAX_MFISC,
            message = "MFISC non valido, il massimo è " + MAX_MFISC)
    private Integer mfisc;
    
    // Fatigue impact (MFIS) Psycho-social
    @Column(name = "mfisps", table = "npstest")
    @Basic
    @NotNull(message = "MFISPS non valido, non può essere vuoto")
    @Min(value = MIN_MFISPS,
            message = "MFISPS non valido, il minimo è " + MIN_MFISPS)
    @Max(value = MAX_MFISPS,
            message = "MFISPS non valido, il massimo è " + MAX_MFISPS)
    private Integer mfisps;

    // Fatigue impact (MFIS) Total
    @Column(name = "mfistot", table = "npstest")
    @Basic    
    private Integer mfistot;

//    // Hospital Anxiety and Depression Scale (HADS) Anxiety
//    @Column(name = "hadsa", table = "npstest")
//    @Basic
//    @NotNull(message = "HADSA non valido, non può essere vuoto")
//    @Min(value = MIN_HADSA,
//            message = "HADSA non valido, il minimo è " + MIN_HADSA)
//    @Max(value = MAX_HADSA,
//            message = "HADSA non valido, il massimo è " + MAX_HADSA)
//    private Integer hadsa;
//    
//    // Hospital Anxiety and Depression Scale (HADS) Depression
//    @Column(name = "hadsd", table = "npstest")
//    @Basic
//    @NotNull(message = "HADSD non valido, non può essere vuoto")
//    @Min(value = MIN_HADSD,
//            message = "HADSD non valido, il minimo è " + MIN_HADSD)
//    @Max(value = MAX_HADSD,
//            message = "HADSD non valido, il massimo è " + MAX_HADSD)
//    private Integer hadsd;
//
//    // Hospital Anxiety and Depression Scale (HADS) Total
//    @Column(name = "hadstot", table = "npstest")
//    @Basic
//    private Integer hadstot;
    
    // Stai-1
    @Column(name = "stai1", table = "npstest")
    @Basic
    @NotNull(message = "STAI-1 non valido, non può essere vuoto")
    @Min(value = MIN_STAI1,
            message = "STAI-1 non valido, il minimo è " + MIN_STAI1)
    @Max(value = MAX_STAI1,
            message = "STAI-1 non valido, il massimo è " + MAX_STAI1)
    private Integer stai1;
    
    // Stai-1
    @Column(name = "stai2", table = "npstest")
    @Basic
    @NotNull(message = "STAI-2 non valido, non può essere vuoto")
    @Min(value = MIN_STAI2,
            message = "STAI-2 non valido, il minimo è " + MIN_STAI2)
    @Max(value = MAX_STAI2,
            message = "STAI-2 non valido, il massimo è " + MAX_STAI2)
    private Integer stai2;
    
    // Beck
    @Column(name = "beck", table = "npstest")
    @Basic
    @NotNull(message = "BECK non valido, non può essere vuoto")
    @Min(value = MIN_BECK,
            message = "BECK non valido, il minimo è " + MIN_BECK)
    @Max(value = MAX_BECK,
            message = "BECK non valido, il massimo è " + MAX_BECK)
    private Integer beck;
    
    // msqol54
    @Column(name = "msqol54", table = "npstest")
    @Basic
    @NotNull(message = "MSQOL-54 non valido, non può essere vuoto")
    @Min(value = MIN_MSQOL54,
            message = "MSQOL-54 non valido, il minimo è " + MIN_MSQOL54)
    @Max(value = MAX_MSQOL54,
            message = "MSQOL-54 non valido, il massimo è " + MAX_MSQOL54)
    private Integer msqol54;

    // dkefsdescr
    @Column(name = "dkefsdescr", table = "npstest")
    @Basic
    @NotNull(message = "DKEFSDESCR non valido, non può essere vuoto")
    @Min(value = MIN_DKEFSDESCR,
            message = "DKEFSDESCR non valido, il minimo è " + MIN_DKEFSDESCR)
    @Max(value = MAX_DKEFSDESCR,
            message = "DKEFSDESCR non valido, il massimo è " + MAX_DKEFSDESCR)
    private Integer dkefsdescr;

    // dkefsdescr
    @Column(name = "dkefscat", table = "npstest")
    @Basic
    @NotNull(message = "DKEFSCAT non valido, non può essere vuoto")
    @Min(value = MIN_DKEFSCAT,
            message = "DKEFSCAT non valido, il minimo è " + MIN_DKEFSDESCR)
    @Max(value = MAX_DKEFSCAT,
            message = "DKEFSCAT non valido, il massimo è " + MAX_DKEFSDESCR)
    private Integer dkefscat;

    // Notes
    @Column(name = "notes", table = "npstest")
    @Basic
    private String notes;


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

    public Integer getTmtaraw() {
        return tmtaraw;
    }

    public void setTmtaraw(Integer tmtaraw) {
        this.tmtaraw = tmtaraw;
    }

    public Integer getTmtaadj() {
        return tmtaadj;
    }

    public void setTmtaadj(Integer tmtaadj) {
        this.tmtaadj = tmtaadj;
    }
    
    public void setTmtaadj(
            Integer tmtaraw,
            Integer birthYear,
            Integer educationYears) {
        
        int j = (LocalDateTime.now().getYear() - birthYear - 20) / 5;
        int i;
        if(educationYears<=3) {
            i=0;
        }
        else if(3<educationYears && educationYears<=5) {
            i=1;
        }
        else if(5<educationYears && educationYears<=8) {
            i=2;
        }
        else if(8<educationYears && educationYears<=13) {
            i=3;
        }
        else { // 13 < educationYears
            i=4;
        }
        
        this.tmtaadj = tmtaraw + tmtaAdjTable[i][j];
    }
    
    public void setTmtaadj(int birthyear, int educationyears) {
        try {
            this.setTmtaadj(tmtaraw, birthyear, educationyears);
        }
        catch (Exception e) {}
    }

    public Integer getTmtaeq() {
        return tmtaeq;
    }

    public void setTmtaeq(Integer tmtaeq) {
        this.tmtaeq = tmtaeq;
    }
    
    public void setTmtaeqFromTmtaadj(Integer tmtaadj) {
        if(tmtaadj>93) {
            this.tmtaeq = 0;
        }
        else if(69<=tmtaadj && tmtaadj<=93) {
            this.tmtaeq = 1;
        }
        else if(53<=tmtaadj && tmtaadj<=68) {
            this.tmtaeq = 2;
        }
        else if(45<=tmtaadj && tmtaadj<=52) {
            this.tmtaeq = 3;
        }
        else { // tmtaadj<45
            this.tmtaeq = 4;
        }
    }
    
    public void setTmtaeqFromTmtaadj() {
        try {
            this.setTmtaeqFromTmtaadj(this.tmtaadj);
        }
        catch (Exception e) {}
    }

    public Integer getTmtbraw() {
        return tmtbraw;
    }

    public void setTmtbraw(Integer tmtbraw) {
        this.tmtbraw = tmtbraw;
    }

    public Integer getTmtbadj() {
        return tmtbadj;
    }

    public void setTmtbadj(Integer tmtbadj) {
        this.tmtbadj = tmtbadj;
    }
    
    public void setTmtbadj(
            Integer tmtbraw,
            Integer birthYear,
            Integer educationYears) {
        
        int j = (LocalDateTime.now().getYear() - birthYear - 20) / 5;
        int i;
        if(educationYears<=3) {
            i=0;
        }
        else if(3<educationYears && educationYears<=5) {
            i=1;
        }
        else if(5<educationYears && educationYears<=8) {
            i=2;
        }
        else if(8<educationYears && educationYears<=13) {
            i=3;
        }
        else { // 13 < educationYears
            i=4;
        }
        
        this.tmtbadj = tmtbraw + tmtbAdjTable[i][j];
    }
    
    public void setTmtbadj(int birthyear, int educationyears) {
        try {
            this.setTmtbadj(tmtbraw, birthyear, educationyears);
        }
        catch (Exception e) {}
    }    
    
    public Integer getTmtbeq() {
        return tmtbeq;
    }

    public void setTmtbeq(Integer tmtbeq) {
        this.tmtbeq = tmtbeq;
    }

    public void setTmtbeqFromTmtbadj(Integer tmtbadj) {
        if(tmtbadj>282) {
            this.tmtbeq = 0;
        }
        else if(178<=tmtbadj && tmtbadj<=282) {
            this.tmtbeq = 1;
        }
        else if(136<=tmtbadj && tmtbadj<=177) {
            this.tmtbeq = 2;
        }
        else if(103<=tmtbadj && tmtbadj<=135) {
            this.tmtbeq = 3;
        }
        else { // tmtbadj<103
            this.tmtbeq = 4;
        }
    }
    
    public void setTmtbeqFromTmtbadj() {
        try {
            this.setTmtbeqFromTmtbadj(this.tmtbadj);
        }
        catch (Exception e) {}
    }
    
    public Integer getTmtbaraw() {
        return tmtbaraw;
    }

    public void setTmtbaraw(Integer tmtbaraw) {
        this.tmtbaraw = tmtbaraw;
    }
    
    public void setTmtbaraw() {
        try {
            this.tmtbaraw = this.tmtbraw - this.tmtaraw;
        }
        catch (Exception e) {}
    }

    public Integer getTmtbaadj() {
        return tmtbaadj;
    }

    public void setTmtbaadj(Integer tmtbaadj) {
        this.tmtbaadj = tmtbaadj;
    }
    
    public void setTmtbaadj(
            Integer tmtbaraw,
            Integer birthYear,
            Integer educationYears) {
        
        int j = (LocalDateTime.now().getYear() - birthYear - 20) / 5;
        int i;
        if(educationYears<=3) {
            i=0;
        }
        else if(3<educationYears && educationYears<=5) {
            i=1;
        }
        else if(5<educationYears && educationYears<=8) {
            i=2;
        }
        else if(8<educationYears && educationYears<=13) {
            i=3;
        }
        else { // 13 < educationYears
            i=4;
        }
        this.tmtbaadj = tmtbaraw + tmtbaAdjTable[i][j];
    }
    
    public void setTmtbaadj(int birthyear, int educationyears) {
        try {
            this.setTmtbaadj(tmtbaraw, birthyear, educationyears);
        }
        catch (Exception e) {}
    }

    public Integer getTmtbaeq() {
        return tmtbaeq;
    }

    public void setTmtbaeq(Integer tmtbaeq) {
        this.tmtbaeq = tmtbaeq;
    }

    public void setTmtbaeqFromTmtbaadj(Integer tmtbaadj) {
        if(tmtbaadj>186) {
            this.tmtbaeq = 0;
        }
        else if(112<=tmtbaadj && tmtbaadj<=186) {
            this.tmtbaeq = 1;
        }
        else if(88<=tmtbaadj && tmtbaadj<=111) {
            this.tmtbaeq = 2;
        }
        else if(58<=tmtbaadj && tmtbaadj<=87) {
            this.tmtbaeq = 3;
        }
        else { // tmtbaadj<58
            this.tmtbaeq = 4;
        }
    }
    
    public void setTmtbaeqFromTmtbaadj() {
        try {
            this.setTmtbaeqFromTmtbaadj(this.tmtbaadj);
        } catch (Exception e) {}
    }


    public Integer getMfisp() {
        return mfisp;
    }

    public void setMfisp(Integer mfisp) {
        this.mfisp = mfisp;
    }

    public Integer getMfisc() {
        return mfisc;
    }

    public void setMfisc(Integer mfisc) {
        this.mfisc = mfisc;
    }

    public Integer getMfisps() {
        return mfisps;
    }

    public void setMfisps(Integer mfisps) {
        this.mfisps = mfisps;
    }

    public Integer getMfistot() {
        return mfistot;
    }

    public void setMfistot(Integer mfistot) {
        this.mfistot = mfistot;
    }
    
    public void setMfistot(Integer mfisp, Integer mfisc, Integer mfisps) {
        this.mfistot = mfisp + mfisc + mfisps;
    }
    
    public void setMfistot() {
        try {
            setMfistot(mfisp + mfisc + mfisps);
        }
        catch (Exception e) {}
    }


    public Integer getStai1() {
        return stai1;
    }

    public void setStai1(Integer stai1) {
        this.stai1 = stai1;
    }

    public Integer getStai2() {
        return stai2;
    }

    public void setStai2(Integer stai2) {
        this.stai2 = stai2;
    }

    public Integer getMsqol54() {
        return msqol54;
    }

    public void setMsqol54(Integer msqol54) {
        this.msqol54 = msqol54;
    }
    
    public Integer getBeck() {
        return beck;
    }

    public void setBeck(Integer beck) {
        this.beck = beck;
    }

    public Integer getDkefsdescr() {
        return dkefsdescr;
    }

    public void setDkefsdescr(Integer dkefsdescr) {
        this.dkefsdescr = dkefsdescr;
    }
    
    public Integer getDkefscat() {
        return dkefscat;
    }

    public void setDkefscat(Integer dkefscat) {
        this.dkefscat = dkefscat;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}