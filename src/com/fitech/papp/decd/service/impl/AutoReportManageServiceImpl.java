package com.fitech.papp.decd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.papp.decd.dao.AutoReportManageDao;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;
import com.fitech.papp.decd.service.AutoReportManageService;

/**
 * 自动报送管理实现类
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "autoReportManageService")
public class AutoReportManageServiceImpl implements AutoReportManageService {
    /** 自动报送管理Dao */
    @Autowired
    private AutoReportManageDao autoReportManageDao;

    @Override
    public List<AutoReportInfoVo> queryReportOrgInfoList() throws Exception {
        return autoReportManageDao.queryReportOrgInfoList();
    }

    @Override
    public void updateAutoReportInfoRunTime(String timeValue, Integer reportOrgId, String flag) throws Exception {
        autoReportManageDao.updateAutoReportInfoRunTime(timeValue, reportOrgId, flag);
    }

    @Override
    public void updateAutoReportInfoIsUsed(Integer reportOrgId, String flag, String time, String isUsed)
            throws Exception {
        autoReportManageDao.updateAutoReportInfoIsUsed(reportOrgId, flag, time, isUsed);
    }

    /**
     * 根据报送日查询节假日报送日历
     * 
     * @param day
     * @return
     */
    @Override
    public List<ReportCalendarInfo> findCalendarInfoList(String day) {
        return autoReportManageDao.findCalendarInfoList(day);
    }

    /**
     * 根据业务发生日获取日历信息
     * 
     * @param ywfsrDate
     * @return
     */
    @Override
    public ReportCalendarInfo findCalInfoByYwfsr(String ywfsrDate) {
        return autoReportManageDao.findCalInfoByYwfsr(ywfsrDate);
    }

}
