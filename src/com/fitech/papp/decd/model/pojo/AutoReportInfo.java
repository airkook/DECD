package com.fitech.papp.decd.model.pojo;

/**
 * AutoReportInfo entity. @author MyEclipse Persistence Tools
 */

public class AutoReportInfo implements java.io.Serializable {

    // Fields

    private Integer reportOrgId;

    private String flag;

    private String runTime;

    private String isUsed;

    // Constructors

    /** default constructor */
    public AutoReportInfo() {
    }

    /** full constructor */
    public AutoReportInfo(Integer reportOrgId, String flag, String runTime, String isUsed) {
        this.reportOrgId = reportOrgId;
        this.flag = flag;
        this.runTime = runTime;
        this.isUsed = isUsed;
    }

    // Property accessors

    public Integer getReportOrgId() {
        return this.reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRunTime() {
        return this.runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

}
