package com.fitech.papp.decd.model.vo;

public class DataMonitorVo {

    // 入库提示
    private String dataImportTips;

    // 审核提示
    private String auditTips;
    
    // 报送提示
    private String reportTips;

    // 反馈提示
    private String feedbackTips;

    // 抓取入库
    private Integer dataImportStatus;

    // 审核通过
    private Integer auditPassStatus;

    // 报送状态
    private Integer reportStatus;

    // 反馈状态
    private Integer feedbackStatus;

    public String getDataImportTips() {
        return dataImportTips;
    }

    public void setDataImportTips(String dataImportTips) {
        this.dataImportTips = dataImportTips;
    }

    public String getAuditTips() {
        return auditTips;
    }

    public void setAuditTips(String auditTips) {
        this.auditTips = auditTips;
    }

    public String getReportTips() {
        return reportTips;
    }

    public void setReportTips(String reportTips) {
        this.reportTips = reportTips;
    }

    public String getFeedbackTips() {
        return feedbackTips;
    }

    public void setFeedbackTips(String feedbackTips) {
        this.feedbackTips = feedbackTips;
    }

    public Integer getDataImportStatus() {
        return dataImportStatus;
    }

    public void setDataImportStatus(Integer dataImportStatus) {
        this.dataImportStatus = dataImportStatus;
    }

    public Integer getAuditPassStatus() {
        return auditPassStatus;
    }

    public void setAuditPassStatus(Integer auditPassStatus) {
        this.auditPassStatus = auditPassStatus;
    }

    public Integer getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Integer getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(Integer feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

}
