package com.fitech.papp.decd.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;

/**
 * 报送配置dao
 * 
 * @author songfei
 * */
@Service(value = "submitConfigDao")
public interface SubmitConfigDao extends IBaseDao<ReportConfigInfo, Integer> {

    /** 所有的报送机构 */
    public List findAllReportConfigInfo();

    /**
     * @param term
     */
    public void deleteByYear(String term) throws Exception;

    /**
     * @param insertList
     */
    public void batchInsert(List<ReportCalendarInfo> list) throws Exception;

}
