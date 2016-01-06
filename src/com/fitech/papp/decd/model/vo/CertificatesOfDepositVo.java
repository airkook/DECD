package com.fitech.papp.decd.model.vo;

public class CertificatesOfDepositVo {

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

    /** 修改人 */
    private String updateUserName;

    /** 校验状态 */
    private String validateStatus;

    /** 校验状态描述 */
    private String validateStatusDesc;

    /** 合计校验状态描述 */
    private String sumValidateStatusDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpdm() {
        return cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    public String getJxfs() {
        return jxfs;
    }

    public void setJxfs(String jxfs) {
        this.jxfs = jxfs;
    }

    public String getCpqx() {
        return cpqx;
    }

    public void setCpqx(String cpqx) {
        this.cpqx = cpqx;
    }

    public String getFxdx() {
        return fxdx;
    }

    public void setFxdx(String fxdx) {
        this.fxdx = fxdx;
    }

    public String getQdje() {
        return qdje;
    }

    public void setQdje(String qdje) {
        this.qdje = qdje;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

    public String getValidateStatusDesc() {
        return validateStatusDesc;
    }

    public void setValidateStatusDesc(String validateStatusDesc) {
        this.validateStatusDesc = validateStatusDesc;
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
