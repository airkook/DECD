package com.fitech.papp.decd.model.pojo;

/**
 * ValidateInfo entity. @author MyEclipse Persistence Tools
 */

public class ValidateInfo implements java.io.Serializable {

	// Fields

	private Integer validateInfoId;
	private String fieldEnName;
	private String fieldName;
	private String validateFormula;
	private String validateDesc;
	private String validateType;

	// Constructors

	/** default constructor */
	public ValidateInfo() {
	}

	/** minimal constructor */
	public ValidateInfo(Integer validateInfoId, String validateFormula,
			String validateType) {
		this.validateInfoId = validateInfoId;
		this.validateFormula = validateFormula;
		this.validateType = validateType;
	}

	/** full constructor */
	public ValidateInfo(Integer validateInfoId, String fieldEnName,
			String fieldName, String validateFormula, String validateDesc,
			String validateType) {
		this.validateInfoId = validateInfoId;
		this.fieldEnName = fieldEnName;
		this.fieldName = fieldName;
		this.validateFormula = validateFormula;
		this.validateDesc = validateDesc;
		this.validateType = validateType;
	}

	// Property accessors

	public Integer getValidateInfoId() {
		return this.validateInfoId;
	}

	public void setValidateInfoId(Integer validateInfoId) {
		this.validateInfoId = validateInfoId;
	}

	public String getFieldEnName() {
		return this.fieldEnName;
	}

	public void setFieldEnName(String fieldEnName) {
		this.fieldEnName = fieldEnName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValidateFormula() {
		return this.validateFormula;
	}

	public void setValidateFormula(String validateFormula) {
		this.validateFormula = validateFormula;
	}

	public String getValidateDesc() {
		return this.validateDesc;
	}

	public void setValidateDesc(String validateDesc) {
		this.validateDesc = validateDesc;
	}

	public String getValidateType() {
		return this.validateType;
	}

	public void setValidateType(String validateType) {
		this.validateType = validateType;
	}

}