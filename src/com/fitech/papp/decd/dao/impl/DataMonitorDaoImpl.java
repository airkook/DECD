package com.fitech.papp.decd.dao.impl;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.DataMonitorDao;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.DataMonitorVo;
import com.fitech.papp.decd.util.Constants;

/**
 * 数据监控DaoImpl
 * 
 * @author wupengzheng
 */
@Service(value = "dataMonitorDao")
public class DataMonitorDaoImpl extends DefaultBaseDao<DataMonitorVo, Integer> implements DataMonitorDao {

    @Override
    public DataMonitorVo queryDataMonitorVo(String term) {

        String sql1 = "select count(1) dataimportstatus from CERTIFICATES_OF_DEPOSIT t where t.status <> '0' and t.term = '"
                + term + "'";
        int dataImportStatus = findBySql(sql1);
        String sql2 = "select count(1) dataauditstatus from CERTIFICATES_OF_DEPOSIT t where t.status = '5' and t.term = '"
                + term + "'";
        int auditPassStatus = findBySql(sql2);

        String sql3 = " from ReportResultInfo t where t.reportOrgId = '1' and t.term = '" + term + "'";
        ReportResultInfo reportResultInfo1 = (ReportResultInfo) this.findObject(sql3);
        String sql4 = " from ReportResultInfo t where t.reportOrgId = '2' and t.term = '" + term + "'";
        ReportResultInfo reportResultInfo2 = (ReportResultInfo) this.findObject(sql4);

        String sql5 = " from FeedbackInfo t where t.reportOrgId = '1' and t.term ='" + term + "'";
        FeedbackInfo feedbackInfo1 = (FeedbackInfo) this.findObject(sql5);
        String sql6 = " from FeedbackInfo t where t.reportOrgId = '2' and t.term ='" + term + "'";
        FeedbackInfo feedbackInfo2 = (FeedbackInfo) this.findObject(sql6);

        DataMonitorVo dataMonitorVo = new DataMonitorVo();
        if (dataImportStatus > 0) {
            dataMonitorVo.setDataImportStatus(1);
            dataMonitorVo.setDataImportTips("已入库" + dataImportStatus + "条记录");
            if (auditPassStatus == dataImportStatus) {
                dataMonitorVo.setAuditPassStatus(1);
                dataMonitorVo.setAuditTips("审核通过" + auditPassStatus + "条记录");
            }
            else if (dataImportStatus > auditPassStatus) {
                dataMonitorVo.setAuditPassStatus(0);
                dataMonitorVo.setAuditTips("审核通过" + auditPassStatus + "条记录,未审核通过"
                        + (dataImportStatus - auditPassStatus) + "条记录");
            }
            // 上报状态检查
            String reportStatusTips = "";
            Boolean reportSuccessFlag1 = false;
            Boolean reportSuccessFlag2 = false;
            if (reportResultInfo1 == null) {
                reportStatusTips += "中国外汇交易中心：未生成报表，";
            }
            else {
                String reportStatus = reportResultInfo1.getReportStatus();
                if (Constants.REPORT_RESULT_RELEASE.equals(reportStatus)) {
                    reportStatusTips += "中国外汇交易中心：已生成报表未上报，";
                }
                else {
                    reportStatusTips += "中国外汇交易中心：已上报，";
                    reportSuccessFlag1 = true;
                }
            }
            if (reportResultInfo2 == null) {
                reportStatusTips += "上海清算所：未生成报表";
            }
            else {
                String reportStatus = reportResultInfo2.getReportStatus();
                if (Constants.REPORT_RESULT_RELEASE.equals(reportStatus)) {
                    reportStatusTips += "上海清算所：已生成报表未上报";
                }
                else {
                    reportStatusTips += "上海清算所：已上报";
                    reportSuccessFlag2 = true;
                }
            }
            // 都已上报则完成
            if (reportSuccessFlag1 && reportSuccessFlag2) {
                dataMonitorVo.setReportStatus(1);
            }
            else {
                dataMonitorVo.setReportStatus(0);
            }
            dataMonitorVo.setReportTips(reportStatusTips);

            // 反馈状态检查
            String feedbackStatusTips = "";
            Boolean feedbackSuccessFlag1 = false;
            Boolean feedbackSuccessFlag2 = false;
            if (feedbackInfo1 == null) {
                feedbackStatusTips += "中国外汇交易中心：未抓取反馈，";
            }
            else {
                String flag = feedbackInfo1.getFlag();
                if (Constants.FEEDBACK_FEEDBACKSUCC.equals(flag)) {
                    feedbackStatusTips += "中国外汇交易中心：反馈结果为成功，";
                    feedbackSuccessFlag1 = true;
                }
                else {
                    feedbackStatusTips += "中国外汇交易中心：反馈结果为失败，";
                }
            }
            if (feedbackInfo2 == null) {
                feedbackStatusTips += "上海清算所：未抓取反馈";
            }
            else {
                String flag = feedbackInfo2.getFlag();
                if (Constants.FEEDBACK_FEEDBACKSUCC.equals(flag)) {
                    feedbackStatusTips += "上海清算所：反馈结果为成功";
                    feedbackSuccessFlag2 = true;
                }
                else {
                    feedbackStatusTips += "上海清算所：反馈结果为失败";
                }
            }
            // 都已反馈成功则完成
            if (feedbackSuccessFlag1 && feedbackSuccessFlag2) {
                dataMonitorVo.setFeedbackStatus(1);
            }
            else {
                dataMonitorVo.setFeedbackStatus(0);
            }
            dataMonitorVo.setFeedbackTips(feedbackStatusTips);

        }
        else {
            dataMonitorVo.setDataImportStatus(0);
            dataMonitorVo.setDataImportTips("已入库" + dataImportStatus + "条记录");
            dataMonitorVo.setAuditPassStatus(0);
            dataMonitorVo.setAuditTips("审核通过" + auditPassStatus + "条记录");
            dataMonitorVo.setReportStatus(0);
            dataMonitorVo.setReportTips("中国外汇交易中心：未生成报表，上海清算所：未生成报表");
            dataMonitorVo.setFeedbackStatus(0);
            dataMonitorVo.setFeedbackTips("中国外汇交易中心：未抓取反馈，上海清算所：未抓取反馈");
        }

        return dataMonitorVo;
    }
}
