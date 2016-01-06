package com.fitech.papp.decd.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.DataReportVo;
import com.fitech.papp.decd.model.vo.ReportFtpParamVo;

/**
 * 数据上报service
 * 
 * @author xujj
 * 
 */
@Service(value = "dataReportService")
public interface DataReportService {
    public List<DataReportVo> select(String term) throws Exception;

    /**
     * @param reportOrgName 得到用户名
     * @return
     */
    public String getName(String reportOrgName) throws Exception;

    /**
     * 获取上报配置信息
     * 
     * @param reportOrgId
     * @return
     */
    public ReportConfigInfo findReportConfigInfo(String reportOrgId);

    /**
     * @param 得到业务发生日期
     * @return
     */
    public String getDate(String term) throws Exception;

    public List<CertificatesOfDeposit> findCdsList(Map<String, String> paramMap) throws Exception;

    /**
     * @param reportOrgId
     * @return
     */
    public List<ReportFtpParamVo> selectFtpParam(String reportOrgId) throws Exception;

    public List<ReportResultInfo> select(int reportOrgId2, String term) throws Exception;

    /**
     * @param rri 新增
     */
    public void saveReportResult(ReportResultInfo rri) throws Exception;

    /**
     * 根据期数和上报机构ID删除反馈结果记录
     * 
     * @param term
     * @param reportOrgId
     * @throws Exception
     */
    public void delFeedback(String term, String reportOrgId) throws Exception;

    /**
     * 保存反馈结果
     * 
     * @param feedbackInfoList
     * @throws Exception
     */
    public void saveFeedbackInfoList(List<FeedbackInfo> feedbackInfoList) throws Exception;

    /**
     * 修改
     * 
     * @param rri1
     */
    public void updateReportResult(ReportResultInfo rri1) throws Exception;
}
