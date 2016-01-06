package com.fitech.papp.decd.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportOrgInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.DataReportVo;
import com.fitech.papp.decd.model.vo.ReportFtpParamVo;

/**
 * 数据上报dao
 * 
 * @author xujj
 * 
 */
@Service(value = "dataReportDao")
public interface DataReportDao extends IBaseDao<ReportOrgInfo, Integer> {

    public List<DataReportVo> select(String term) throws Exception;

    /**
     * 得到用户名
     * 
     * @return
     */
    public String getName(String reportOrgName) throws Exception;

    /**
     * 得到日期
     * 
     * @return
     */
    public String getDate(String term) throws Exception;

    /**
     * ftp上传参数
     * 
     * @param reportOrgId
     */
    public List<ReportFtpParamVo> selectFtpParam(String reportOrgId) throws Exception;

    /**
     * @param reportOrgId2
     * @param term
     * @return
     */
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
     * @throws BaseDaoException
     */
    public void delFeedback(String term, String reportOrgId) throws BaseDaoException;

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

    /**
     * 获取上报配置信息
     * 
     * @param reportOrgId
     * @return
     */
    public ReportConfigInfo findReportConfigInfo(String reportOrgId);
}
