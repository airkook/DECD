package com.fitech.papp.decd.model.vo;

public class AutoReportInfoVo {

    // 报送机构id
    private Integer reportOrgId;

    // 报送机构名称
    private String reportOrgName;

    // 运行时间
    private String time;

    // 是否启用
    private String isUsed;

    // 任务标识
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getReportOrgId() {
        return reportOrgId;
    }

    public void setReportOrgId(Integer reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public String getReportOrgName() {
        return reportOrgName;
    }

    public void setReportOrgName(String reportOrgName) {
        this.reportOrgName = reportOrgName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

}
