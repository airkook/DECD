package com.fitech.papp.decd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.papp.decd.dao.CdsManagerDao;
import com.fitech.papp.decd.dao.DataReportDao;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.DataReportVo;
import com.fitech.papp.decd.model.vo.ReportFtpParamVo;
import com.fitech.papp.decd.service.DataReportService;

/**
 * 数据上报serviceImpl
 * 
 * @author xujj
 * 
 */
@Service(value = "dataReportService")
public class DataReportServiceImpl implements DataReportService {

    /** 数据上报dao */
    @Autowired
    private DataReportDao dataReportDao;

    /** 大额存单数据维护dao */
    @Autowired
    private CdsManagerDao cdsManagerDao;

    /*
     * 查询
     */

    @Override
    public List<DataReportVo> select(String term) throws Exception {
        List<DataReportVo> list = dataReportDao.select(term);
        return list;
    }

    /*
     * 得到用户名
     */
    @Override
    public String getName(String reportOrgName) throws Exception {
        String name = dataReportDao.getName(reportOrgName);
        return name;
    }

    /**
     * 获取上报配置信息
     * 
     * @param reportOrgId
     * @return
     */
    @Override
    public ReportConfigInfo findReportConfigInfo(String reportOrgId) {
        return dataReportDao.findReportConfigInfo(reportOrgId);
    }

    /*
     * 得到日期
     */
    @Override
    public String getDate(String term) throws Exception {
        String date = dataReportDao.getDate(term);
        return date;
    }

    /*
     * 查询CERTIFICATES_OF_DEPOSIT List
     */
    @Override
    public List<CertificatesOfDeposit> findCdsList(Map<String, String> paramMap) throws Exception {
        List<CertificatesOfDeposit> cdList = cdsManagerDao.findCdsList(paramMap);
        return cdList;
    }

    /*
     * 查FTP上传参数
     */
    @Override
    public List<ReportFtpParamVo> selectFtpParam(String reportOrgId) throws Exception {
        List<ReportFtpParamVo> rfpv = dataReportDao.selectFtpParam(reportOrgId);
        return rfpv;
    }

    /*
     * 查出报送结果表有无对应数据
     */
    @Override
    public List<ReportResultInfo> select(int reportOrgId2, String term) throws Exception {
        List<ReportResultInfo> rri = dataReportDao.select(reportOrgId2, term);
        return rri;
    }

    /*
     * 新增
     */
    @Override
    public void saveReportResult(ReportResultInfo rri) throws Exception {
        dataReportDao.saveReportResult(rri);
    }

    /**
     * 根据期数和上报机构ID删除反馈结果记录
     * 
     * @param term
     * @param reportOrgId
     * @throws Exception
     */
    public void delFeedback(String term, String reportOrgId) throws Exception {
        dataReportDao.delFeedback(term, reportOrgId);
    }

    /**
     * 保存反馈结果
     * 
     * @param feedbackInfoList
     * @throws Exception
     */
    public void saveFeedbackInfoList(List<FeedbackInfo> feedbackInfoList) throws Exception {
        dataReportDao.saveFeedbackInfoList(feedbackInfoList);
    }

    /*
     * 修改
     */
    @Override
    public void updateReportResult(ReportResultInfo rri1) throws Exception {
        dataReportDao.updateReportResult(rri1);
    }
}
