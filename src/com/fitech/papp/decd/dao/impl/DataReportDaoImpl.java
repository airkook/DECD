package com.fitech.papp.decd.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.DataReportDao;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportOrgInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.DataReportVo;
import com.fitech.papp.decd.model.vo.ReportFtpParamVo;
import com.fitech.papp.decd.util.Constants;

/**
 * 数据上报dao实现类
 * 
 * @author xujj
 * 
 */
@Service(value = "dataReportDao")
public class DataReportDaoImpl extends DefaultBaseDao<ReportOrgInfo, Integer> implements DataReportDao {

    /*
     * 查询
     */
    @Override
    public List<DataReportVo> select(String term) throws Exception {
        String sql = " select roi.REPORT_ORG_NAME,rri.REPORT_STATUS,roi.REPORT_ORG_ID from report_org_info roi "
                + "left join report_result_info rri on roi.REPORT_ORG_ID=rri.REPORT_ORG_ID and rri.term='" + term + "'";
        List<Object[]> list = this.findListBySql(sql, null);
        int checkSucCount = 0;
        int allCount = 0;

        // 查询数据总条数
        String sql0 = " select count(1) from CERTIFICATES_OF_DEPOSIT where status<>'0' and term='" + term + "' ";
        allCount = this.findBySql(sql0);

        // 查询审核通过个数
        String sql1 = " select count(1) from CERTIFICATES_OF_DEPOSIT where status='5' and term='" + term + "' ";
        checkSucCount = this.findBySql(sql1);

        List<DataReportVo> list1 = new ArrayList<DataReportVo>();
        for (Object[] obj : list) {
            // 上报机构 上报状态
            DataReportVo drv = new DataReportVo();
            String reportOrgName = (String) obj[0];
            String reportStatus = (String) obj[1];
            int reportOrgId = (Integer.parseInt(obj[2].toString()));
            drv.setReportOrgName(reportOrgName);
            drv.setAllCount(allCount);
            drv.setCheckSucCount(checkSucCount);
            drv.setReportOrgId(reportOrgId);
            drv.setReportStatus(reportStatus);
            String reportStatusDesc = "";
            // 如果状态为空 状态变为未生成报表 (1为反馈未抓取，2为成功，3为失败)
            if (StringUtil.isEmpty(reportStatus)) {
                reportStatusDesc = "未生成报表";
            }
            else {
                if (Constants.REPORT_RESULT_RELEASE.equals(reportStatus)) {
                    reportStatusDesc = "已生成报表，未上报";
                }
                if (Constants.REPORT_RESULT_NOTFEEDBACK.equals(reportStatus)) {
                    reportStatusDesc = "已上报，反馈未抓取";
                }
                if (Constants.REPORT_RESULT_FEEDBACKSUCC.equals(reportStatus)) {
                    reportStatusDesc = "已上报，反馈结果：成功";
                }
                if (Constants.REPORT_RESULT_FEEBBACKFAIL.equals(reportStatus)) {
                    reportStatusDesc = "已上报，反馈结果：失败";
                }
                if (Constants.REPORT_RESULT_EDITDATA.equals(reportStatus)) {
                    reportStatusDesc = "已修改数据，未重新生成报表";
                }
            }
            drv.setReportStatusDesc(reportStatusDesc);
            list1.add(drv);
        }
        return list1;
    }

    /*
     * 数据上报 查寻
     */

    /*
     * 得到用户名
     */
    @Override
    public String getName(String reportOrgName) throws Exception {
        // 得到id
        String userName = "";
        String date = "";
        String sql = " select REPORT_ORG_ID from REPORT_ORG_INFO where REPORT_ORG_NAME='" + reportOrgName + "'";
        List list = this.findListBySql(sql, null);
        String sql1 = " select FTP_USER_ID from REPORT_CONFIG_INFO where REPORT_ORG_ID='" + list.get(0) + "'";
        List list2 = this.findListBySql(sql1, null);

        if (list2.size() > 0) {
            userName = (String) list2.get(0);
        }
        return userName;
    }

    @Override
    public String getDate(String term) throws Exception {
        String date = "";
        String sql = " select YWFSR from CERTIFICATES_OF_DEPOSIT where TERM='" + term + "'";
        List list = this.findListBySql(sql, null);
        if (list.size() > 0) {
            date = (String) list.get(0);
        }
        return date;

    }

    /*
     * ftp上传参数
     */
    @Override
    public List<ReportFtpParamVo> selectFtpParam(String reportOrgId) throws Exception {
        String sql = " select FTP_ADDRESS,FTP_PORT,FTP_USER_ID,FTP_PASSWORD,FEEDBACK_PATH from REPORT_CONFIG_INFO where REPORT_ORG_ID='"
                + reportOrgId + "'";
        String ftpAddress = "";
        int ftpPort;
        String ftpUserId = "";
        String ftpPassword = "";
        String feedBackPath = "";
        List<Object[]> list = this.findListBySql(sql, null);
        List<ReportFtpParamVo> rfpv = new ArrayList<ReportFtpParamVo>();
        for (Object[] obj : list) {
            ReportFtpParamVo rfp = new ReportFtpParamVo();
            ftpAddress = (String) obj[0];
            ftpPort = Integer.parseInt(((String) obj[1]));
            ftpUserId = (String) obj[2];
            ftpPassword = (String) obj[3];
            feedBackPath = (String) obj[4];
            rfp.setFtpAddress(ftpAddress);
            rfp.setFtpPort(ftpPort);
            rfp.setFtpPort(ftpPort);
            rfp.setFtpUserId(ftpUserId);
            rfp.setFtpPassword(ftpPassword);
            rfp.setFeedBackPath(feedBackPath);
            rfpv.add(rfp);
        }
        return rfpv;

    }

    /*
     * 查询结果表有无对应数据
     */
    @Override
    public List<ReportResultInfo> select(int reportOrgId2, String term) throws Exception {
        String hql = "from ReportResultInfo rri where rri.reportOrgId='" + reportOrgId2 + "' and rri.term='" + term
                + "'";
        List<ReportResultInfo> rri = this.findListByHsql(hql, null);
        return rri;
    }

    /*
     * 新增
     */
    @Override
    public void saveReportResult(ReportResultInfo rri) throws Exception {
        this.save(rri);
    }

    /**
     * 根据期数和上报机构ID删除反馈结果记录
     * 
     * @param term
     * @param reportOrgId
     * @throws BaseDaoException
     */
    @Override
    public void delFeedback(String term, String reportOrgId) throws BaseDaoException {
        String sql = "delete from FEEDBACK_INFO where TERM='" + term + "' and REPORT_ORG_ID='" + reportOrgId + "' ";
        this.updateBysql(sql);
    }

    /**
     * 保存反馈结果
     * 
     * @param feedbackInfoList
     * @throws Exception
     */
    @Override
    public void saveFeedbackInfoList(List<FeedbackInfo> feedbackInfoList) throws Exception {
        if (feedbackInfoList != null && feedbackInfoList.size() > 0) {
            for (FeedbackInfo feedbackInfo : feedbackInfoList) {
                this.getHibernateTemplate().save(feedbackInfo);
            }
        }
    }

    /*
     * 修改
     */
    @Override
    public void updateReportResult(ReportResultInfo rri1) throws Exception {
        this.update(rri1);
    }

    /**
     * 获取上报配置信息
     * 
     * @param reportOrgId
     * @return
     */
    public ReportConfigInfo findReportConfigInfo(String reportOrgId) {
        String hsql = "from ReportConfigInfo where reportOrgId='" + reportOrgId + "'";
        ReportConfigInfo reportConfigInfo = (ReportConfigInfo) this.findObject(hsql);
        this.getSession().clear();
        return reportConfigInfo;
    }
}
