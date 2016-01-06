package com.fitech.papp.decd.action;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.service.SubmitRecordsService;

public class SubmitRecordsAction extends DefaultBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String resultId;

    /** 开始期数 */
    private String startTerm;

    /** 结束期数 */
    private String endTerm;

    /** 上报机构名称 */
    private String orgName;

    /** 上报机构ID */
    private String reportOrgId;

    /** 上报机构ID */
    private String reportOrgIdItem;

    /** 期数 */
    private String term;

    /** 状态 */
    private Timestamp status;

    /** 上报时间 */
    private Integer reportTime;

    /** 上报机构list */
    private List orgList;

    /** 页面数据 */
    private PageResults pageResults;

    private String path;

    @Autowired
    private SubmitRecordsService submitRecordsService;

    /**
     * 报送记录查看初始化
     * */
    public String init() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("startTerm", startTerm);
        param.put("endTerm", endTerm);
        param.put("reportOrgId", reportOrgId);
        orgList = submitRecordsService.findAllOrgList();
        pageResults = submitRecordsService.findByPage(param, pageSize, pageNo);
        return "init";
    }

    /**
     * 报表下载
     * */
    public void downLoad() {
        String filePath = "";
        try {
            // 文件夹的日期和文件的日期为业务发生日的第二天
            String date = DateUtil.getNextDay(term).replace("-", "");
            reportOrgIdItem = reportOrgIdItem.trim();

            if (path.equals("a")) {
                // 报表下载路径
                // 拼接本地生成报表路径
                String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                        + Config.confMap.get("uploadFilePath");
                // 文件名
                String fileName = "";
                String path1 = path + File.separator + date;
                File file1 = new File(path1);
                if (!file1.exists() && !file1.isDirectory()) {
                    file1.mkdirs();
                }

                // 上海清算所
                if ("2".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("shanghaiUser") + "_" + date + ".csv";
                }
                // 中国外汇中心
                else if ("1".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                            + "_01" + ".csv";

                    String[] list = file1.list();
                    int j = 1;
                    int i = 0;
                    while (i != list.length) {
                        if (list[i].equals(fileName)) {
                            j++;
                            fileName = Config.confMap.get("chinaUser") + "_" + date + "_"
                                    + Config.confMap.get("undefinedCode") + "_0" + j + ".csv";
                            i = 0;
                            continue;
                        }
                        i++;
                    }
                    fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                            + "_0" + (j - 1) + ".csv";
                }
                filePath = path1 + File.separator + fileName;
            }
            else if (path.equals("b")) {
                // 反馈下载路径
                // 拼接本地生成报表路径
                String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                        + Config.confMap.get("downloadFilePath");
                String path1 = path + File.separator + date;
                // 文件名
                String fileName = "";
                // 上海清算所
                if ("2".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("shanghaiUser") + "_" + date + "_f.csv";
                    filePath = path1 + File.separator + fileName;
                }
                // 中国外汇中心
                if (("1").equals(reportOrgIdItem)) {
                    String fileName1 = "";
                    fileName = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_01" + ".csv";
                    fileName1 = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_02" + ".csv";
                    filePath = path1 + File.separator + fileName1;
                    File file = new File(path1 + File.separator + fileName1);
                    // 02不存在就下载01
                    if (!file.exists()) {
                        filePath = path1 + File.separator + fileName;
                    }
                }
            }
            if (!StringUtil.isEmpty(filePath)) {
                // 下载
                HttpServletResponse response = this.getResponse();
                submitRecordsService.downLoadFile(filePath, response);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测文件是否存在
     * */
    public void checkDownLoad() {
        String filePath = "";
        PrintWriter writer = null;
        try {
            writer = ServletActionContext.getResponse().getWriter();
            // 文件夹的日期和文件的日期为业务发生日的第二天
            String date = DateUtil.getNextDay(term).replace("-", "");
            reportOrgIdItem = reportOrgIdItem.trim();

            if (path.equals("a")) {
                // 报表下载路径
                // 拼接本地生成报表路径
                String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                        + Config.confMap.get("uploadFilePath");
                // 文件名
                String fileName = "";
                String path1 = path + File.separator + date;
                File file1 = new File(path1);
                if (!file1.exists() && !file1.isDirectory()) {
                    file1.mkdirs();
                }

                // 上海清算所
                if ("2".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("shanghaiUser") + "_" + date + ".csv";
                }
                // 中国外汇中心
                else if ("1".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                            + "_01" + ".csv";

                    String[] list = file1.list();
                    int j = 1;
                    int i = 0;
                    while (i != list.length) {
                        if (list[i].equals(fileName)) {
                            j++;
                            fileName = Config.confMap.get("chinaUser") + "_" + date + "_"
                                    + Config.confMap.get("undefinedCode") + "_0" + j + ".csv";
                            i = 0;
                            continue;
                        }
                        i++;
                    }
                    fileName = Config.confMap.get("chinaUser") + "_" + date + "_" + Config.confMap.get("undefinedCode")
                            + "_0" + (j - 1) + ".csv";
                }
                filePath = path1 + File.separator + fileName;
            }
            else if (path.equals("b")) {
                // 反馈下载路径
                // 拼接本地生成报表路径
                String path = this.getRequest().getSession().getServletContext().getRealPath("/")
                        + Config.confMap.get("downloadFilePath");
                String path1 = path + File.separator + date;
                // 文件名
                String fileName = "";
                // 上海清算所
                if ("2".equals(reportOrgIdItem)) {
                    fileName = Config.confMap.get("shanghaiUser") + "_" + date + "_f.csv";
                    filePath = path1 + File.separator + fileName;
                }
                // 中国外汇中心
                if ("1".equals(reportOrgIdItem)) {
                    String fileName1 = "";
                    fileName = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_01" + ".csv";
                    fileName1 = Config.confMap.get("chinaDownFeedBack") + "_" + date + "_02" + ".csv";
                    filePath = path1 + File.separator + fileName1;
                    File file = new File(path1 + File.separator + fileName1);
                    // 02不存在就下载01
                    if (!file.exists()) {
                        filePath = path1 + File.separator + fileName;
                    }
                }
            }
            File file = new File(filePath);
            // 文件存在则继续下载
            if (file.exists()) {
                writer.write("success");
            }
            else {
                writer.write("fail");
            }
        }
        catch (Exception e) {
            Log.error(e.getMessage());
            e.printStackTrace();
            writer.write("error");
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public String getStartTerm() {
        return startTerm;
    }

    public void setStartTerm(String startTerm) {
        this.startTerm = startTerm;
    }

    public String getEndTerm() {
        return endTerm;
    }

    public void setEndTerm(String endTerm) {
        this.endTerm = endTerm;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Timestamp getStatus() {
        return status;
    }

    public void setStatus(Timestamp status) {
        this.status = status;
    }

    public Integer getReportTime() {
        return reportTime;
    }

    public void setReportTime(Integer reportTime) {
        this.reportTime = reportTime;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getReportOrgId() {
        return reportOrgId;
    }

    public void setReportOrgId(String reportOrgId) {
        this.reportOrgId = reportOrgId;
    }

    public PageResults getPageResults() {
        return pageResults;
    }

    public void setPageResults(PageResults pageResults) {
        this.pageResults = pageResults;
    }

    public List getOrgList() {
        return orgList;
    }

    public void setOrgList(List orgList) {
        this.orgList = orgList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the reportOrgIdItem
     */
    public String getReportOrgIdItem() {
        return reportOrgIdItem;
    }

    /**
     * @param reportOrgIdItem the reportOrgIdItem to set
     */
    public void setReportOrgIdItem(String reportOrgIdItem) {
        this.reportOrgIdItem = reportOrgIdItem;
    }

}
