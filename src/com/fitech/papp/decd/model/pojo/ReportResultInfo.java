package com.fitech.papp.decd.model.pojo;

/**
 * ReportResultInfo entity. @author MyEclipse Persistence Tools
 */

public class ReportResultInfo implements java.io.Serializable {

	// Fields

	private Integer resultId;
	private String term;
	private Integer reportOrgId;
	private String reportTime;
	private Integer reportUserId;
	private String reportStatus;

	// Constructors

	/** default constructor */
	public ReportResultInfo() {
	}

	/** minimal constructor */
	public ReportResultInfo(Integer resultId, String term,
			Integer reportOrgId, String reportTime, String reportStatus) {
		this.resultId = resultId;
		this.term = term;
		this.reportOrgId = reportOrgId;
		this.reportTime = reportTime;
		this.reportStatus = reportStatus;
	}

	/** full constructor */
	public ReportResultInfo(Integer resultId, String term,
			Integer reportOrgId, String reportTime, Integer reportUserId,
			String reportStatus) {
		this.resultId = resultId;
		this.term = term;
		this.reportOrgId = reportOrgId;
		this.reportTime = reportTime;
		this.reportUserId = reportUserId;
		this.reportStatus = reportStatus;
	}

	// Property accessors

	public Integer getResultId() {
		return this.resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
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

	public String getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getReportUserId() {
		return this.reportUserId;
	}

	public void setReportUserId(Integer reportUserId) {
		this.reportUserId = reportUserId;
	}

	public String getReportStatus() {
		return this.reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

}