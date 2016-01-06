package com.fitech.papp.decd.action;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.service.DataAuditService;
import com.fitech.papp.decd.util.Constants;

/**
 * 数据审核Action类
 * 
 * @author wupengzheng
 * 
 */
public class DataAuditAction extends DefaultBaseAction {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private DataAuditService dataAuditService;

    /** 日志 */
    private final Log log = LogFactory.getLog(DataAuditAction.class);

    /** 计息方式map */
    private Map<String, String> jxfsMap;

    /** 校验状态map */
    private Map<String, String> validateStatusMap;

    /** 数据状态map */
    private Map<String, String> statusMap;

    /** 批量审核id */
    private String ids;

    /** 产品代码 */
    private String cpdm;

    /** 期数 */
    private String term;

    /** 计息方式 */
    private String jxfs;

    /** 数据状态 */
    private String status;

    /** 校验状态 */
    private String validateStatus;

    /** 查询结果 */
    private PageResults pageResults;

    public String init() {

        try {
            // 如果期数为空，初始化为昨天
            if (StringUtil.isEmpty(term)) {
                term = DateUtil.getLastDay(DateUtil.getTodayDateStr());
            }
            if (StringUtil.isEmpty(status)) {
                status = Constants.CDS_STATUS_REVIEW;
            }
            // 查询参数
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            paramMap.put("cpdm", cpdm);
            paramMap.put("jxfs", jxfs);
            paramMap.put("status", status);
            paramMap.put("validateStatus", validateStatus);

            // 查询结果
            pageResults = dataAuditService.findCertificatesOfDepositInfo(paramMap, pageSize, pageNo);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return "init";
    }

    /**
     * 批量审核通过
     * 
     * @return
     */
    public String auditPass() {
        try {
            if (!StringUtil.isEmpty(ids)) {
                Map<String, String> param = new HashMap<String, String>();
                ids = ids.replaceAll(";", ",");
                param.put("ids", ids);
                dataAuditService.updateDataAuditPass(param);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return init();
    }

    /**
     * 批量审核不通过
     * 
     * @return
     */
    public String auditNoPass() {
        try {
            if (!StringUtil.isEmpty(ids)) {
                Map<String, String> param = new HashMap<String, String>();
                ids = ids.replaceAll(";", ",");
                param.put("ids", ids);
                dataAuditService.updateDataAuditNoPass(param);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return init();
    }

    /**
     * @return the jxfsMap
     */
    public Map<String, String> getJxfsMap() {
        jxfsMap = new TreeMap<String, String>();
        jxfsMap.put("", "全部");
        jxfsMap.put("固息", "固息");
        jxfsMap.put("浮息", "浮息");
        return jxfsMap;
    }

    public Map<String, String> getValidateStatusMap() {
        validateStatusMap = new TreeMap<String, String>();
        validateStatusMap.put("", "全部");
        validateStatusMap.put(Constants.VALIDATE_STATUS_PASS, "通过");
        validateStatusMap.put(Constants.VALIDATE_STATUS_UNPASS, "不通过");
        return validateStatusMap;
    }

    /**
     * @return the statusMap
     */
    public Map<String, String> getStatusMap() {
        statusMap = new TreeMap<String, String>();
        statusMap.put(Constants.CDS_STATUS_REVIEW, "提交审核");
        statusMap.put(Constants.CDS_STATUS_REVIEW_PASS, "审核通过");
        return statusMap;
    }

    /**
     * @param statusMap the statusMap to set
     */
    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
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
     * @param validateStatusMap the validateStatusMap to set
     */
    public void setValidateStatusMap(Map<String, String> validateStatusMap) {
        this.validateStatusMap = validateStatusMap;
    }

    public void setJxfsMap(Map<String, String> jxfsMap) {
        this.jxfsMap = jxfsMap;
    }

    public String getCpdm() {
        return cpdm;
    }

    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getJxfs() {
        return jxfs;
    }

    public void setJxfs(String jxfs) {
        this.jxfs = jxfs;
    }

    public String getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

    public PageResults getPageResults() {
        return pageResults;
    }

    public void setPageResults(PageResults pageResults) {
        this.pageResults = pageResults;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

}
