package com.fitech.papp.decd.model.pojo;

/**
 * ReportConfigInfo entity. @author MyEclipse Persistence Tools
 */

public class ReportConfigInfo implements java.io.Serializable {

    // Fields

    private Integer reportOrgId;

    private String ftpAddress;

    private String ftpUserId;

    private String ftpPassword;

    private String ftpPort;

    private String uploadPath;

    private String feedbackPath;

    // Constructors

    /** default constructor */
    public ReportConfigInfo() {
    }

    /** full constructor */
    public ReportConfigInfo(Integer reportOrgId, String ftpAddress, String ftpUserId, String ftpPassword,
            String ftpPort, String uploadPath, String feedbackPath) {
        this.reportOrgId = reportOrgId;
        this.ftpAddress = ftpAddress;
        this.ftpUserId = ftpUserId;
        this.ftpPassword = ftpPassword;
        this.ftpPort = ftpPort;
        this.uploadPath = uploadPath;
        this.feedbackPath = feedbackPath;
    }

    // Property accessors

    public Integer getReportOrgId() {
        return this.reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getFtpAddress() {
        return this.ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    public String getFtpUserId() {
        return this.ftpUserId;
    }

    public void setFtpUserId(String ftpUserId) {
        this.ftpUserId = ftpUserId;
    }

    public String getFtpPassword() {
        return this.ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpPort() {
        return this.ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getUploadPath() {
        return this.uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getFeedbackPath() {
        return this.feedbackPath;
    }

    public void setFeedbackPath(String feedbackPath) {
        this.feedbackPath = feedbackPath;
    }

}
