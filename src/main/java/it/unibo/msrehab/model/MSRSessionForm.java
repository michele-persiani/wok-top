package it.unibo.msrehab.model;

import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Class modeling the session form of the
 * multiple sclerosis rehabilitation system
 */

public class MSRSessionForm implements Serializable
{
    
    @NotNull
    private Integer usrgrpid;
    
    @NotNull
    private String difficulty;
        
    private List<Integer> ATT_SEL_STD = new ArrayList<>();
    private List<Integer> ATT_SEL_STD_FAC = new ArrayList<>();
    private List<Integer> ATT_SEL_STD_ORI = new ArrayList<>();

    private List<Integer> ATT_SEL_FLW = new ArrayList<>();
    private List<Integer> ATT_SEL_FLW_FAC = new ArrayList<>();
    private List<Integer> ATT_SEL_FLW_ORI = new ArrayList<>();

    private List<Integer> ATT_ALT = new ArrayList<>();
    private List<Integer> ATT_ALT_FAC = new ArrayList<>();
    private List<Integer> ATT_ALT_ORI = new ArrayList<>();

    private List<Integer> ATT_DIV = new ArrayList<>();
    private List<Integer> ATT_DIV_FAC = new ArrayList<>();
    private List<Integer> ATT_DIV_ORI = new ArrayList<>();

    private List<Integer> MEM_VIS_1 = new ArrayList<>();
    private List<Integer> MEM_VIS_1_FAC = new ArrayList<>();
    private List<Integer> MEM_VIS_1_ORI = new ArrayList<>();

    private List<Integer> MEM_VIS_2 = new ArrayList<>();
    private List<Integer> MEM_VIS_5 = new ArrayList<>();
    
    private List<Integer> NBACK = new ArrayList<>();
    private List<Integer> NBACK_FAC = new ArrayList<>();
    private List<Integer> NBACK_ORI = new ArrayList<>();
    
    private List<Integer> MEM_LONG_1 = new ArrayList<>();

    private List<Integer> RES_INH = new ArrayList<>();

    private List<Integer> PLAN_1 = new ArrayList<>();
    private List<Integer> PLAN_2 = new ArrayList<>();
    private List<Integer> PLAN_3 = new ArrayList<>();

    private List<Integer> ATT_RFLXS = new ArrayList<>();

    public MSRSessionForm() {
    }

    public Integer getUsrgrpid() {
        return usrgrpid;
    }

    public void setUsrgrpid(Integer usrgrpid) {
        this.usrgrpid = usrgrpid;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Integer> getATT_SEL_STD() {
        return ATT_SEL_STD;
    }

    public void setATT_SEL_STD(List<Integer> attSelStd) {
        this.ATT_SEL_STD = attSelStd;
    }

    public List<Integer> getATT_SEL_FLW() {
        return ATT_SEL_FLW;
    }

    public void setATT_SEL_FLW(List<Integer> attSelFlw) {
        this.ATT_SEL_FLW = attSelFlw;
    }

    public List<Integer> getATT_ALT() {
        return ATT_ALT;
    }

    public void setATT_ALT(List<Integer> ATT_ALT) {
        this.ATT_ALT = ATT_ALT;
    }

    public List<Integer> getATT_DIV() {
        return ATT_DIV;
    }

    public void setATT_DIV(List<Integer> ATT_DIV) {
        this.ATT_DIV = ATT_DIV;
    }

    public List<Integer> getMEM_VIS_1() {
        return MEM_VIS_1;
    }

    public void setMEM_VIS_1(List<Integer> MEM_VIS_1) {
        this.MEM_VIS_1 = MEM_VIS_1;
    }

    public List<Integer> getMEM_VIS_2() {
        return MEM_VIS_2;
    }

    public void setMEM_VIS_2(List<Integer> MEM_VIS_2) {
        this.MEM_VIS_2 = MEM_VIS_2;
    }
     public List<Integer> getMEM_VIS_5() {
        return MEM_VIS_5;
    }

    public void setMEM_VIS_5(List<Integer> MEM_VIS_5) {
        this.MEM_VIS_5 = MEM_VIS_5;
    }

    public List<Integer> getNBACK() {
        return NBACK;
    }

    public void setNBACK(List<Integer> NBACK) {
        this.NBACK = NBACK;
    }

    public List<Integer> getMEM_LONG_1() {
        return MEM_LONG_1;
    }

    public void setMEM_LONG_1(List<Integer> MEM_LONG_1) {
        this.MEM_LONG_1 = MEM_LONG_1;
    }

    public List<Integer> getRES_INH() {
        return RES_INH;
    }

    public void setRES_INH(List<Integer> RES_INH) {
        this.RES_INH = RES_INH;
    }    

    public List<Integer> getPLAN_1() {
        return PLAN_1;
    }

    public void setPLAN_1(List<Integer> PLAN_1) {
        this.PLAN_1 = PLAN_1;
    }    
      public List<Integer> getPLAN_2() {
        return PLAN_2;
    }

    public void setPLAN_2(List<Integer> PLAN_2) {
        this.PLAN_2 = PLAN_2;
    }    

          public List<Integer> getPLAN_3() {
        return PLAN_3;
    }

    public void setPLAN_3(List<Integer> PLAN_3) {
        this.PLAN_3 = PLAN_3;
    } 
    
    
    public List<Integer> getATT_SEL_STD_FAC() {
        return ATT_SEL_STD_FAC;
    }
     
    public void setATT_SEL_STD_FAC(List<Integer> ATT_SEL_STD_FAC) {
        this.ATT_SEL_STD_FAC = ATT_SEL_STD_FAC;
    }

    public List<Integer> getATT_SEL_FLW_FAC() {
        return ATT_SEL_FLW_FAC;
    }

    public void setATT_SEL_FLW_FAC(List<Integer> ATT_SEL_FLW_FAC) {
        this.ATT_SEL_FLW_FAC = ATT_SEL_FLW_FAC;
    }

    public List<Integer> getATT_ALT_FAC() {
        return ATT_ALT_FAC;
    }

    public void setATT_ALT_FAC(List<Integer> ATT_ALT_FAC) {
        this.ATT_ALT_FAC = ATT_ALT_FAC;
    }

    public List<Integer> getATT_DIV_FAC() {
        return ATT_DIV_FAC;
    }

    public void setATT_DIV_FAC(List<Integer> ATT_DIV_FAC) {
        this.ATT_DIV_FAC = ATT_DIV_FAC;
    }

    public List<Integer> getMEM_VIS_1_FAC() {
        return MEM_VIS_1_FAC;
    }

    public void setMEM_VIS_1_FAC(List<Integer> MEM_VIS_1_FAC) {
        this.MEM_VIS_1_FAC = MEM_VIS_1_FAC;
    }

    public List<Integer> getMEM_VIS_1_ORI() {
        return MEM_VIS_1_ORI;
    }

    public void setMEM_VIS_1_ORI(List<Integer> MEM_VIS_1_ORI) {
        this.MEM_VIS_1_ORI = MEM_VIS_1_ORI;
    }

    public List<Integer> getNBACK_FAC() {
        return NBACK_FAC;
    }

    public void setNBACK_FAC(List<Integer> NBACK_FAC) {
        this.NBACK_FAC = NBACK_FAC;
    }

    public List<Integer> getNBACK_ORI() {
        return NBACK_ORI;
    }

    public void setNBACK_ORI(List<Integer> NBACK_ORI) {
        this.NBACK_ORI = NBACK_ORI;
    }

    public List<Integer> getATT_SEL_STD_ORI() {
        return ATT_SEL_STD_ORI;
    }

    public void setATT_SEL_STD_ORI(List<Integer> ATT_SEL_STD_ORI) {
        this.ATT_SEL_STD_ORI = ATT_SEL_STD_ORI;
    }

    public List<Integer> getATT_SEL_FLW_ORI() {
        return ATT_SEL_FLW_ORI;
    }

    public void setATT_SEL_FLW_ORI(List<Integer> ATT_SEL_FLW_ORI) {
        this.ATT_SEL_FLW_ORI = ATT_SEL_FLW_ORI;
    }

    public List<Integer> getATT_ALT_ORI() {
        return ATT_ALT_ORI;
    }

    public void setATT_ALT_ORI(List<Integer> ATT_ALT_ORI) {
        this.ATT_ALT_ORI = ATT_ALT_ORI;
    }

    public List<Integer> getATT_DIV_ORI() {
        return ATT_DIV_ORI;
    }

    public void setATT_DIV_ORI(List<Integer> ATT_DIV_ORI) {
        this.ATT_DIV_ORI = ATT_DIV_ORI;
    }

    public List<Integer> getATT_RFLXS()
    {
        return this.ATT_RFLXS;
    }

    public void setATT_RFLXS(List<Integer> ATT_RFLXS)
    {
        this.ATT_RFLXS = ATT_RFLXS;
    }
    
    public String toJSONString() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

}