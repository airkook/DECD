package com.fitech.papp.decd.model.pojo;


/**
 * FeedbackInfo entity. @author MyEclipse Persistence Tools
 */

public class FeedbackInfo implements java.io.Serializable {

    // Fields

    private Integer feedbackId;

    private String term;

    private Integer reportOrgId;

    private String flag;

    private String cpdm;

    private String errorInfo;

    // Constructors

    /** default constructor */
    public FeedbackInfo() {
    }

    /** minimal constructor */
    public FeedbackInfo(Integer feedbackId, String term, Integer reportOrgId, String flag) {
        this.feedbackId = feedbackId;
        this.term = term;
        this.reportOrgId = reportOrgId;
        this.flag = flag;
    }

    /** full constructor */
    public FeedbackInfo(Integer feedbackId, String term, Integer reportOrgId, String flag, String cpdm, String errorInfo) {
        this.feedbackId = feedbackId;
        this.term = term;
        this.reportOrgId = reportOrgId;
        this.flag = flag;
        this.cpdm = cpdm;
        this.errorInfo = errorInfo;
    }

    // Property accessors

    public Integer getFeedbackId() {
        return this.feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

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

    public String getCpdm() {
        return this.cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    public String getErrorInfo() {
        return this.errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

}
