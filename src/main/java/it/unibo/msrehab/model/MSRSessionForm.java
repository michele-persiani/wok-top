package it.unibo.msrehab.model;

import flexjson.JSONSerializer;
import java.io.Serializable;
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
        
    private Integer ATT_SEL_STD;
    private Integer ATT_SEL_STD_FAC;
    private Integer ATT_SEL_STD_ORI;

    private Integer ATT_SEL_FLW;
    private Integer ATT_SEL_FLW_FAC;
    private Integer ATT_SEL_FLW_ORI;

    private Integer ATT_ALT;
    private Integer ATT_ALT_FAC;
    private Integer ATT_ALT_ORI;

    private Integer ATT_DIV;
    private Integer ATT_DIV_FAC;
    private Integer ATT_DIV_ORI;

    private Integer MEM_VIS_1;
    private Integer MEM_VIS_1_FAC;
    private Integer MEM_VIS_1_ORI;

    private Integer MEM_VIS_2;
    private Integer MEM_VIS_5;
    
    private Integer NBACK;
    private Integer NBACK_FAC;
    private Integer NBACK_ORI;
    
    private Integer MEM_LONG_1;

    private Integer RES_INH;

    private Integer PLAN_1;
    private Integer PLAN_2;
    private Integer PLAN_3;

    private Integer ATT_RFLXS;

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

    public Integer getATT_SEL_STD() {
        return ATT_SEL_STD;
    }

    public void setATT_SEL_STD(Integer attSelStd) {
        this.ATT_SEL_STD = attSelStd;
    }

    public Integer getATT_SEL_FLW() {
        return ATT_SEL_FLW;
    }

    public void setATT_SEL_FLW(Integer attSelFlw) {
        this.ATT_SEL_FLW = attSelFlw;
    }

    public Integer getATT_ALT() {
        return ATT_ALT;
    }

    public void setATT_ALT(Integer ATT_ALT) {
        this.ATT_ALT = ATT_ALT;
    }

    public Integer getATT_DIV() {
        return ATT_DIV;
    }

    public void setATT_DIV(Integer ATT_DIV) {
        this.ATT_DIV = ATT_DIV;
    }

    public Integer getMEM_VIS_1() {
        return MEM_VIS_1;
    }

    public void setMEM_VIS_1(Integer MEM_VIS_1) {
        this.MEM_VIS_1 = MEM_VIS_1;
    }

    public Integer getMEM_VIS_2() {
        return MEM_VIS_2;
    }

    public void setMEM_VIS_2(Integer MEM_VIS_2) {
        this.MEM_VIS_2 = MEM_VIS_2;
    }
     public Integer getMEM_VIS_5() {
        return MEM_VIS_5;
    }

    public void setMEM_VIS_5(Integer MEM_VIS_5) {
        this.MEM_VIS_5 = MEM_VIS_5;
    }

    public Integer getNBACK() {
        return NBACK;
    }

    public void setNBACK(Integer NBACK) {
        this.NBACK = NBACK;
    }

    public Integer getMEM_LONG_1() {
        return MEM_LONG_1;
    }

    public void setMEM_LONG_1(Integer MEM_LONG_1) {
        this.MEM_LONG_1 = MEM_LONG_1;
    }

    public Integer getRES_INH() {
        return RES_INH;
    }

    public void setRES_INH(Integer RES_INH) {
        this.RES_INH = RES_INH;
    }    

    public Integer getPLAN_1() {
        return PLAN_1;
    }

    public void setPLAN_1(Integer PLAN_1) {
        this.PLAN_1 = PLAN_1;
    }    
      public Integer getPLAN_2() {
        return PLAN_2;
    }

    public void setPLAN_2(Integer PLAN_2) {
        this.PLAN_2 = PLAN_2;
    }    

          public Integer getPLAN_3() {
        return PLAN_3;
    }

    public void setPLAN_3(Integer PLAN_3) {
        this.PLAN_3 = PLAN_3;
    } 
    
    
    public Integer getATT_SEL_STD_FAC() {
        return ATT_SEL_STD_FAC;
    }
     
    public void setATT_SEL_STD_FAC(Integer ATT_SEL_STD_FAC) {
        this.ATT_SEL_STD_FAC = ATT_SEL_STD_FAC;
    }

    public Integer getATT_SEL_FLW_FAC() {
        return ATT_SEL_FLW_FAC;
    }

    public void setATT_SEL_FLW_FAC(Integer ATT_SEL_FLW_FAC) {
        this.ATT_SEL_FLW_FAC = ATT_SEL_FLW_FAC;
    }

    public Integer getATT_ALT_FAC() {
        return ATT_ALT_FAC;
    }

    public void setATT_ALT_FAC(Integer ATT_ALT_FAC) {
        this.ATT_ALT_FAC = ATT_ALT_FAC;
    }

    public Integer getATT_DIV_FAC() {
        return ATT_DIV_FAC;
    }

    public void setATT_DIV_FAC(Integer ATT_DIV_FAC) {
        this.ATT_DIV_FAC = ATT_DIV_FAC;
    }

    public Integer getMEM_VIS_1_FAC() {
        return MEM_VIS_1_FAC;
    }

    public void setMEM_VIS_1_FAC(Integer MEM_VIS_1_FAC) {
        this.MEM_VIS_1_FAC = MEM_VIS_1_FAC;
    }

    public Integer getMEM_VIS_1_ORI() {
        return MEM_VIS_1_ORI;
    }

    public void setMEM_VIS_1_ORI(Integer MEM_VIS_1_ORI) {
        this.MEM_VIS_1_ORI = MEM_VIS_1_ORI;
    }

    public Integer getNBACK_FAC() {
        return NBACK_FAC;
    }

    public void setNBACK_FAC(Integer NBACK_FAC) {
        this.NBACK_FAC = NBACK_FAC;
    }

    public Integer getNBACK_ORI() {
        return NBACK_ORI;
    }

    public void setNBACK_ORI(Integer NBACK_ORI) {
        this.NBACK_ORI = NBACK_ORI;
    }

    public Integer getATT_SEL_STD_ORI() {
        return ATT_SEL_STD_ORI;
    }

    public void setATT_SEL_STD_ORI(Integer ATT_SEL_STD_ORI) {
        this.ATT_SEL_STD_ORI = ATT_SEL_STD_ORI;
    }

    public Integer getATT_SEL_FLW_ORI() {
        return ATT_SEL_FLW_ORI;
    }

    public void setATT_SEL_FLW_ORI(Integer ATT_SEL_FLW_ORI) {
        this.ATT_SEL_FLW_ORI = ATT_SEL_FLW_ORI;
    }

    public Integer getATT_ALT_ORI() {
        return ATT_ALT_ORI;
    }

    public void setATT_ALT_ORI(Integer ATT_ALT_ORI) {
        this.ATT_ALT_ORI = ATT_ALT_ORI;
    }

    public Integer getATT_DIV_ORI() {
        return ATT_DIV_ORI;
    }

    public void setATT_DIV_ORI(Integer ATT_DIV_ORI) {
        this.ATT_DIV_ORI = ATT_DIV_ORI;
    }

    public Integer getATT_RFLXS()
    {
        return this.ATT_RFLXS;
    }

    public void setATT_RFLXS(Integer ATT_RFLXS)
    {
        this.ATT_RFLXS = ATT_RFLXS;
    }
    
    public String toJSONString() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

}