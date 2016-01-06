package com.fitech.papp.decd.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.vo.ReportConfigInfoVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.SubmitConfigService;

public class SubmitConfigAction extends DefaultBaseAction {

    /**
     * 报送配置action songfei
     */
    private static final long serialVersionUID = 1L;

    private ReportConfigInfoVo reConfigInfo_W_H;// 中国外汇交易中心

    private ReportConfigInfoVo reConfigInfo_Q_S_S;// 上海清算所

    @Autowired
    private SubmitConfigService submitConfigService;

    private String message;

    /** 上传文件名 */
    private String excelFileName;

    /** 上传文件 */
    private File excel;

    /** 日志 */
    private final Log log = LogFactory.getLog(CDSManageAction.class);

    /**
     * 报送记录查看初始化
     * */
    public String init() {
        List<ReportConfigInfo> list = new ArrayList<ReportConfigInfo>();
        list = submitConfigService.findReportConfigList();
        if (list != null && list.size() > 0) {
            for (ReportConfigInfo rci : list) {
                if (rci.getReportOrgId() == 1) {
                    reConfigInfo_W_H = submitConfigService.changeToVo(rci);// 中国外汇交易中心
                }
                else if (rci.getReportOrgId() == 2) {
                    reConfigInfo_Q_S_S = submitConfigService.changeToVo(rci);// 上海清算所
                }
            }
        }
        return "init";
    }

    /**
     * 保存修改的报送记录
     * */
    public String save() {
        ReportConfigInfo reportConfigInfo_W_H = new ReportConfigInfo();
        ReportConfigInfo reportConfigInfo_Q_S_S = new ReportConfigInfo();
        try {
            if (reConfigInfo_W_H != null) {
                // 类型转换，Vo转换成实体类
                reportConfigInfo_W_H = submitConfigService.changeToInfo(reConfigInfo_W_H);
                // 保存修改的中国外汇交易中心报送地址配置
                submitConfigService.saveOrUpdateReportConfig(reportConfigInfo_W_H);
            }
            if (reConfigInfo_Q_S_S != null) {
                // 类型转换，Vo转换成实体类
                reportConfigInfo_Q_S_S = submitConfigService.changeToInfo(reConfigInfo_Q_S_S);
                // 保存修改的上海清算所报送地址配置
                submitConfigService.saveOrUpdateReportConfig(reportConfigInfo_Q_S_S);
            }
            message = "保存成功";
        }
        catch (Exception e) {
            e.printStackTrace();
            message = "保存失败";
        }
        return "init";
    }

    /**
     * 日历导入
     * */
    public String uploadData() {

        try {
            String termStr = excelFileName.substring(0, excelFileName.indexOf(".")).trim();

            // 取得当前登录人
            UserInfoVo userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
            if (excel != null && !StringUtil.isEmpty(excelFileName)) {

                message = submitConfigService.uploadData(excel, excelFileName, userInfo);

            }
            else {
                message = "请选择一个文件！";
            }
        }
        catch (Exception e) {
            message = "导入出现错误，请确认文件名和文件数据是否正确。";

            e.printStackTrace();
            log.error(e);
            return init();
        }

        return init();
    }

    public ReportConfigInfoVo getReConfigInfo_W_H() {
        return reConfigInfo_W_H;
    }

    public void setReConfigInfo_W_H(ReportConfigInfoVo reConfigInfoWH) {
        reConfigInfo_W_H = reConfigInfoWH;
    }

    public ReportConfigInfoVo getReConfigInfo_Q_S_S() {
        return reConfigInfo_Q_S_S;
    }

    public void setReConfigInfo_Q_S_S(ReportConfigInfoVo reConfigInfoQSS) {
        reConfigInfo_Q_S_S = reConfigInfoQSS;
    }

    public SubmitConfigService getSubmitConfigService() {
        return submitConfigService;
    }

    public void setSubmitConfigService(SubmitConfigService submitConfigService) {
        this.submitConfigService = submitConfigService;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the excelFileName
     */
    public String getExcelFileName() {
        return excelFileName;
    }

    /**
     * @param excelFileName the excelFileName to set
     */
    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    /**
     * @return the excel
     */
    public File getExcel() {
        return excel;
    }

    /**
     * @param excel the excel to set
     */
    public void setExcel(File excel) {
        this.excel = excel;
    }

}
