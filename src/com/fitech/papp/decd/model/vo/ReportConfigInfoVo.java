package com.fitech.papp.decd.model.vo;

public class ReportConfigInfoVo {
    
    //上报机构ID
    private Integer reportOrgId;
    //FTP地址
    private String ftpAddress;
    //用户名
    private String ftpUserId;
    //密码
    private String ftpPassword;
    //端口号
    private String ftpPort;
    //上报路径
    private String uploadPath;
    //反馈路径
    private String feedbackPath;
    
    public ReportConfigInfoVo(Integer reportOrgId, String ftpAddress, String ftpUserId, String ftpPassword,
            String ftpPort, String uploadPath, String feedbackPath) {
        super();
        this.reportOrgId = reportOrgId;
        this.ftpAddress = ftpAddress;
        this.ftpUserId = ftpUserId;
        this.ftpPassword = ftpPassword;
        this.ftpPort = ftpPort;
        this.uploadPath = uploadPath;
        this.feedbackPath = feedbackPath;
    }

    public ReportConfigInfoVo() {
        super();
    }

    public Integer getReportOrgId() {
        return reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getFtpAddress() {
        return ftpAddress;
    }

    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    public String getFtpUserId() {
        return ftpUserId;
    }

    public void setFtpUserId(String ftpUserId) {
        this.ftpUserId = ftpUserId;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(String ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getFeedbackPath() {
        return feedbackPath;
    }

    public void setFeedbackPath(String feedbackPath) {
        this.feedbackPath = feedbackPath;
    }
    
    
}
