/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.model.vo;

/**
 * 数据上报Vo
 * 
 * @author xujj
 * 
 */
public class DataReportVo {

    /** 机构ID */
    private int reportOrgId;

    /** 上报机构名称 */
    private String reportOrgName;

    /** 上报状态 */
    private String reportStatus;

    /** 上报状态描述 */
    private String reportStatusDesc;

    /** 总条数 */
    private int allCount;

    /** 审核通过个数 */
    private int checkSucCount;

    /**
     * @return the reportOrgName
     */
    public String getReportOrgName() {
        return reportOrgName;
    }

    /**
     * @param reportOrgName the reportOrgName to set
     */
    public void setReportOrgName(String reportOrgName) {
        this.reportOrgName = reportOrgName;
    }

    /**
     * @return the reportStatus
     */
    public String getReportStatus() {
        return reportStatus;
    }

    /**
     * @param reportStatus the reportStatus to set
     */
    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    /**
     * @return the allCount
     */
    public int getAllCount() {
        return allCount;
    }

    /**
     * @param allCount the allCount to set
     */
    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    /**
     * @return the checkSucCount
     */
    public int getCheckSucCount() {
        return checkSucCount;
    }

    /**
     * @param checkSucCount the checkSucCount to set
     */
    public void setCheckSucCount(int checkSucCount) {
        this.checkSucCount = checkSucCount;
    }

    /**
     * @return the reportOrgId
     */
    public int getReportOrgId() {
        return reportOrgId;
    }

    /**
     * @param reportOrgId the reportOrgId to set
     */
    public void setReportOrgId(int reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    /**
     * @return the reportStatusDesc
     */
    public String getReportStatusDesc() {
        return reportStatusDesc;
    }

    /**
     * @param reportStatusDesc the reportStatusDesc to set
     */
    public void setReportStatusDesc(String reportStatusDesc) {
        this.reportStatusDesc = reportStatusDesc;
    }

}
