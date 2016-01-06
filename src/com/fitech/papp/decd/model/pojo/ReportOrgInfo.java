package com.fitech.papp.decd.model.pojo;

/**
 * ReportOrgInfo entity. @author MyEclipse Persistence Tools
 */

public class ReportOrgInfo implements java.io.Serializable {

    // Fields

    private Integer reportOrgId;

    private String reportOrgName;

    // Constructors

    /** default constructor */
    public ReportOrgInfo() {
    }

    /** full constructor */
    public ReportOrgInfo(Integer reportOrgId, String reportOrgName) {
        this.reportOrgId = reportOrgId;
        this.reportOrgName = reportOrgName;
    }

    // Property accessors

    public Integer getReportOrgId() {
        return this.reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getReportOrgName() {
        return this.reportOrgName;
    }

    public void setReportOrgName(String reportOrgName) {
        this.reportOrgName = reportOrgName;
    }

}
