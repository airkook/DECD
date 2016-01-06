package com.fitech.papp.decd.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;
import com.fitech.papp.decd.service.AutoReportManageService;

/**
 * 自动报送管理Action类
 * 
 * @author wupengzheng
 * 
 */
public class AutoReportManageAction extends DefaultBaseAction {
    /**
     * uid
     */
    private static final long serialVersionUID = 1L;

    /** 自动报送管理service注解 */
    @Autowired
    private AutoReportManageService autoReportManageService;

    /** 日志 */
    private final Log log = LogFactory.getLog(AutoReportManageAction.class);

    /** 自动报送机构列表 */
    private List<AutoReportInfoVo> autoReportInfoVoList;

    /** 时间map */
    private Map<String, String> timeMap;

    /** 时间 */
    private String time;

    /** 是否启用 */
    private String isUsed;

    /** 改变时间值 */
    private String timeValue;

    /** 上报机构id */
    private Integer reportOrgId;

    /** 上报机构名称 */
    private String reportOrgName;

    /** 任务标识 */
    private String flag;

    /**
     * 初始化自动报送机构列表
     * 
     * @return 自动报送机构列表
     */
    public String init() {
        try {
            timeMap = new TreeMap<String, String>();
            for (int i = 0; i < 24; i++) {
                String hour = String.format("%02d", i);
                timeMap.put(hour + ":00", hour + ":00");
                timeMap.put(hour + ":30", hour + ":30");
            }
            autoReportInfoVoList = autoReportManageService.queryReportOrgInfoList();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("autoReportInfoVoList = " + autoReportInfoVoList.size());
        }

        return SUCCESS;
    }

    /**
     * 更新自动报送机构的时间
     * 
     * @return 更新后的时间
     */
    public String changeTime() {
        try {
            autoReportManageService.updateAutoReportInfoRunTime(timeValue, reportOrgId, flag);
        }
        catch (Exception e) {
            log.info("timeValue = " + timeValue + "reprtOrgId = " + reportOrgId + "falg = " + flag);
        }
        return init();
    }

    /**
     * 更新自动报送机构的启用状态
     * 
     * @return 更新后的状态
     */
    public String doUse() {
        try {
            autoReportManageService.updateAutoReportInfoIsUsed(reportOrgId, flag, time, isUsed);
        }
        catch (Exception e) {
            log.info("reportOrgId = " + reportOrgId + "flag =" + flag + "time =" + time + "isUsed =" + isUsed);
        }
        return init();
    }

    public List<AutoReportInfoVo> getAutoReportInfoVoList() {
        return autoReportInfoVoList;
    }

    public void setAutoReportInfoVoList(List<AutoReportInfoVo> autoReportInfoVoList) {
        this.autoReportInfoVoList = autoReportInfoVoList;
    }

    public Map<String, String> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(Map<String, String> timeMap) {
        this.timeMap = timeMap;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
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
}
