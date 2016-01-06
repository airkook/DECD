/**
 * fitech
 * xxj
 */
package com.fitech.papp.decd.model.vo;

/**
 * 上报FTP参数信息Vo
 * 
 * @author xujj
 * 
 */
public class ReportFtpParamVo {

    /** 上传FTP地址 */
    private String ftpAddress;

    /** ftp端口 */
    private int ftpPort;

    /** 用户名 */
    private String ftpUserId;

    /** 密码 */
    private String ftpPassword;

    /** 反馈路径 */
    private String feedBackPath;

    /**
     * @return the ftpAddress
     */
    public String getFtpAddress() {
        return ftpAddress;
    }

    /**
     * @param ftpAddress the ftpAddress to set
     */
    public void setFtpAddress(String ftpAddress) {
        this.ftpAddress = ftpAddress;
    }

    /**
     * @return the ftpPort
     */
    public int getFtpPort() {
        return ftpPort;
    }

    /**
     * @param ftpPort the ftpPort to set
     */
    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    /**
     * @return the ftpUserId
     */
    public String getFtpUserId() {
        return ftpUserId;
    }

    /**
     * @param ftpUserId the ftpUserId to set
     */
    public void setFtpUserId(String ftpUserId) {
        this.ftpUserId = ftpUserId;
    }

    /**
     * @return the ftpPassword
     */
    public String getFtpPassword() {
        return ftpPassword;
    }

    /**
     * @param ftpPassword the ftpPassword to set
     */
    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    /**
     * @return the feedBackPath
     */
    public String getFeedBackPath() {
        return feedBackPath;
    }

    /**
     * @param feedBackPath the feedBackPath to set
     */
    public void setFeedBackPath(String feedBackPath) {
        this.feedBackPath = feedBackPath;
    }

}
