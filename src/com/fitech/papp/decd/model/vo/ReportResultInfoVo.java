package com.fitech.papp.decd.model.vo;

import java.math.BigDecimal;

/**
 * ReportResultInfo entity. @author MyEclipse Persistence Tools
 */

public class ReportResultInfoVo implements java.io.Serializable {

    //ID
    private Integer resultId;
    //报送期数
    private String term;
    //上报机构ID
    private Integer reportOrgId;
    //上报机构名称
    private String orgName;
    //上报时间
    private String reportTime;
    //报送人ID
    private Integer reportUserId;
    //上报状态：1.反馈未抓取，2.成功，3.失败
    private String reportStatus;

    // Constructors

    /** default constructor */
    public ReportResultInfoVo() {
    }

    
    public ReportResultInfoVo(Integer resultId, String term, Integer reportOrgId, String orgName, String reportTime,
            Integer reportUserId, String reportStatus) {
        super();
        this.resultId = resultId;
        this.term = term;
        this.reportOrgId = reportOrgId;
        this.orgName = orgName;
        this.reportTime = reportTime;
        this.reportUserId = reportUserId;
        this.reportStatus = reportStatus;
    }


    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getReportOrgId() {
        return reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(Integer reportUserId) {
        this.reportUserId = reportUserId;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    
}
