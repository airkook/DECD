/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.model.vo;

/**
 * 大额存单数据列表Vo
 * 
 * @author Administrator
 * 
 */
public class CdsVo {

    /** id */
    private Integer id;

    /** 产品代码 */
    private String cpdm;

    /** 计息方式 */
    private String jxfs;

    /** 产品期限 */
    private String cpqx;

    /** 发行对象 */
    private String fxdx;

    /** 起点金额（万元） */
    private String qdje;

    /** 数据状态 */
    private String status;

    /** 数据状态描述 */
    private String statusDesc;

    /** 校验状态 */
    private String validateStatus;

    /** 校验状态描述 */
    private String validateStatusDesc;

    /** 合计校验状态描述 */
    private String sumValidateStatusDesc;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the cpdm
     */
    public String getCpdm() {
        return cpdm;
    }

    /**
     * @param cpdm the cpdm to set
     */
    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    /**
     * @return the jxfs
     */
    public String getJxfs() {
        return jxfs;
    }

    /**
     * @param jxfs the jxfs to set
     */
    public void setJxfs(String jxfs) {
        this.jxfs = jxfs;
    }

    /**
     * @return the cpqx
     */
    public String getCpqx() {
        return cpqx;
    }

    /**
     * @param cpqx the cpqx to set
     */
    public void setCpqx(String cpqx) {
        this.cpqx = cpqx;
    }

    /**
     * @return the fxdx
     */
    public String getFxdx() {
        return fxdx;
    }

    /**
     * @param fxdx the fxdx to set
     */
    public void setFxdx(String fxdx) {
        this.fxdx = fxdx;
    }

    /**
     * @return the qdje
     */
    public String getQdje() {
        return qdje;
    }

    /**
     * @param qdje the qdje to set
     */
    public void setQdje(String qdje) {
        this.qdje = qdje;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the validateStatus
     */
    public String getValidateStatus() {
        return validateStatus;
    }

    /**
     * @param validateStatus the validateStatus to set
     */
    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

    /**
     * @return the validateStatusDesc
     */
    public String getValidateStatusDesc() {
        return validateStatusDesc;
    }

    /**
     * @param validateStatusDesc the validateStatusDesc to set
     */
    public void setValidateStatusDesc(String validateStatusDesc) {
        this.validateStatusDesc = validateStatusDesc;
    }

    /**
     * @return the statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     * @param statusDesc the statusDesc to set
     */
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    /**
     * @return the sumValidateStatusDesc
     */
    public String getSumValidateStatusDesc() {
        return sumValidateStatusDesc;
    }

    /**
     * @param sumValidateStatusDesc the sumValidateStatusDesc to set
     */
    public void setSumValidateStatusDesc(String sumValidateStatusDesc) {
        this.sumValidateStatusDesc = sumValidateStatusDesc;
    }

}
