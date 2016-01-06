package com.fitech.papp.decd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;

/**
 * 报送记录查看dao
 * 
 * @author songfei
 * */
@Service(value = "submitRecordsDao")
public interface SubmitRecordsDao extends IBaseDao<ReportResultInfo, Integer> {

    /** 页面数据 */
    public PageResults findByPage(Map<String, String> param, int pageSize, int pageNo);

    /** 所有的报送机构 */
    public List findReportOrgInfo();

    public List findUserId(String id);
}
