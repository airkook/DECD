package com.fitech.papp.decd.dao;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;

/**
 * 反馈文件查看dao
 * 
 * @author xujj
 * 
 */
@Service(value = "fkwjckDao")
public interface FkwjckDao extends IBaseDao<FeedbackInfo, Integer> {

    public PageResults select(Map<String, String> param, int pageNo, int pageSize, int reportOrgID) throws Exception;

    /**
     * @param term
     * @param cpdm
     * @return
     */
    public String searchId(String term, String cpdm) throws Exception;
}
