package com.fitech.papp.decd.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.service.FkwjckService;

/**
 * 反馈文件查看action
 * 
 * @author xjj
 * 
 */
public class FkwjckAction extends DefaultBaseAction {
    private static final long serialVersionUID = 6410381425726210498L;

    /** 反馈文件查看 service注解 */
    @Autowired
    private FkwjckService fkwjckService;

    private PageResults pageResults;

    /** 产品代码 */
    private String cpdm;

    /** 期数 */
    private String term;

    private int reportOrgID;

    /**
     * 初始化
     * 
     * @return
     */
    public String init() throws Exception {
        // 默认为当前时间的前一天
        String now = DateUtil.getTodayDateStr();
        term = DateUtil.getLastDay(now);

        Map<String, String> param = new HashMap<String, String>();

        param.put("cpdm", cpdm);
        param.put("term", term);
        pageResults = fkwjckService.select(param, pageNo, pageSize, reportOrgID);

        return SUCCESS;

    }

    /**
     * 查询
     * 
     * @return
     */
    public String search() throws Exception {

        Map<String, String> param = new HashMap<String, String>();

        param.put("cpdm", cpdm);
        param.put("term", term);
        pageResults = fkwjckService.select(param, pageNo, pageSize, reportOrgID);

        return SUCCESS;

    }

    /**
     * ajax 查id
     * 
     * 
     */
    public void searchId() throws Exception {
        PrintWriter writer = null;
        writer = ServletActionContext.getResponse().getWriter();
        JSONObject jsonObject = new JSONObject();
        Map<String, String> map = new HashMap<String, String>();
        String jsonStr = "";
        // CERTIFICATES_OF_DEPOSIT查id
        String id = "";
        id = fkwjckService.searchId(cpdm, term);

        if (id.isEmpty()) {
            map.put("result", "error");
        }
        else {
            map.put("result", id);
        }

        jsonObject = JSONObject.fromObject(map);
        jsonStr = jsonObject.toString();
        writer.write(jsonStr);

        /*
         * return null;
         */
    }

    /**
     * @return the pageResults
     */
    public PageResults getPageResults() {
        return pageResults;
    }

    /**
     * @param pageResults the pageResults to set
     */
    public void setPageResults(PageResults pageResults) {
        this.pageResults = pageResults;
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
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the reportOrgID
     */
    public int getReportOrgID() {
        return reportOrgID;
    }

    /**
     * @param reportOrgID the reportOrgID to set
     */
    public void setReportOrgID(int reportOrgID) {
        this.reportOrgID = reportOrgID;
    }

}
