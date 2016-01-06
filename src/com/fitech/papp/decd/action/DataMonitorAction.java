package com.fitech.papp.decd.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.vo.DataMonitorVo;
import com.fitech.papp.decd.service.DataMonitorService;

/**
 * 数据监控Action类
 * 
 * @author wupengzheng
 * 
 */

public class DataMonitorAction extends DefaultBaseAction {

    /**
     * uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * 期数
     */
    private String term;

    /**
     * 数据监控列表
     */
    private DataMonitorVo dataMonitorVo;

    /** 日志 */
    private final Log log = LogFactory.getLog(AutoReportManageAction.class);

    /**
     * 数据监控Service
     */
    @Autowired
    private DataMonitorService dataMonitorService;

    public String init() {

        // 如果期数为空，初始化为昨天
        if (StringUtil.isEmpty(term)) {
            term = DateUtil.getLastDay(DateUtil.getTodayDateStr());
        }

        try {
            dataMonitorVo = dataMonitorService.queryDataMonitorVo(term);
        }
        catch (Exception e) {
            log.info("dataMonitorVo = " + dataMonitorVo.getAuditPassStatus());
        }

        return SUCCESS;
    }
    
    
    public String search(){
        try {
            dataMonitorVo = dataMonitorService.queryDataMonitorVo(term);
        }
        catch (Exception e) {
            log.info("dataMonitorVo = " + dataMonitorVo.getAuditPassStatus());
        }
        return SUCCESS;
    }

    public DataMonitorVo getDataMonitorVo() {
        return dataMonitorVo;
    }

    public void setDataMonitorVo(DataMonitorVo dataMonitorVo) {
        this.dataMonitorVo = dataMonitorVo;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

}
