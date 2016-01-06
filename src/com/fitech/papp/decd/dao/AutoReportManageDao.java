package com.fitech.papp.decd.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;

/**
 * 自动报送管理dao
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "autoReportManageDao")
public interface AutoReportManageDao {

    /**
     * 查询自动报送机构列表
     * 
     * @return 自动报送机构列表
     * @throws Exception
     */
    public List<AutoReportInfoVo> queryReportOrgInfoList() throws Exception;

    /**
     * 更新自动上报机构的时间
     * 
     * @param timeValue 更新的时间值
     * @param reportOrgId 机构Id
     * @param flag 任务类型
     * @throws Exception
     */
    public void updateAutoReportInfoRunTime(String timeValue, Integer reportOrgId, String flag) throws Exception;

    /**
     * 更新自动上报机构的启动状态
     * 
     * @param reportOrgId 机构Id
     * @param isUsed 启用状态
     * @throws Exception
     */
    public void updateAutoReportInfoIsUsed(Integer reportOrgId, String flag, String time, String isUsed)
            throws Exception;

    /**
     * 根据报送日查询节假日报送日历
     * 
     * @param day
     * @return
     */
    public List<ReportCalendarInfo> findCalendarInfoList(String day);

    /**
     * 根据业务发生日获取日历信息
     * 
     * @param ywfsrDate
     * @return
     */
    public ReportCalendarInfo findCalInfoByYwfsr(String ywfsrDate);

}
