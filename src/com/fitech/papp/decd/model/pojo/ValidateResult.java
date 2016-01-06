package com.fitech.papp.decd.model.pojo;


/**
 * ValidateResult entity. @author MyEclipse Persistence Tools
 */

public class ValidateResult implements java.io.Serializable {

    // Fields

    private Integer validateResultId;

    private Integer cdsId;

    private Integer validateInfoId;

    private String validaetData;

    private String validateResultFlag;

    private String term;

    private String validateType;

    // Constructors

    /** default constructor */
    public ValidateResult() {
    }

    /** minimal constructor */
    public ValidateResult(Integer validateResultId, Integer validateInfoId, String validateResultFlag,
            String validateType) {
        this.validateResultId = validateResultId;
        this.validateInfoId = validateInfoId;
        this.validateResultFlag = validateResultFlag;
        this.validateType = validateType;
    }

    /** full constructor */
    public ValidateResult(Integer validateResultId, Integer cdsId, Integer validateInfoId, String validaetData,
            String validateResultFlag, String term, String validateType) {
        this.validateResultId = validateResultId;
        this.cdsId = cdsId;
        this.validateInfoId = validateInfoId;
        this.validaetData = validaetData;
        this.validateResultFlag = validateResultFlag;
        this.term = term;
        this.validateType = validateType;
    }

    // Property accessors

    public Integer getValidateResultId() {
        return this.validateResultId;
    }

    public void setValidateResultId(Integer validateResultId) {
        this.validateResultId = validateResultId;
    }

    public Integer getCdsId() {
        return this.cdsId;
    }

    public void setCdsId(Integer cdsId) {
        this.cdsId = cdsId;
    }

    public Integer getValidateInfoId() {
        return this.validateInfoId;
    }

    public void setValidateInfoId(Integer validateInfoId) {
        this.validateInfoId = validateInfoId;
    }

    public String getValidaetData() {
        return this.validaetData;
    }

    public void setValidaetData(String validaetData) {
        this.validaetData = validaetData;
    }

    public String getValidateResultFlag() {
        return this.validateResultFlag;
    }

    public void setValidateResultFlag(String validateResultFlag) {
        this.validateResultFlag = validateResultFlag;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getValidateType() {
        return this.validateType;
    }

    public void setValidateType(String validateType) {
        this.validateType = validateType;
    }

}
