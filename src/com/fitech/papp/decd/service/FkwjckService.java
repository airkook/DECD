package com.fitech.papp.decd.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;

/**
 * 反馈文件查看service
 * 
 * @author xujj
 * 
 */
@Service(value = "fkwjckService")
public interface FkwjckService {
    public PageResults select(Map<String, String> param, int pageNo, int pageSize, int reportOrgID) throws Exception;

    /**
     * 查询id
     * 
     * @param cpdm
     * @param term
     * @return
     */
    public String searchId(String cpdm, String term) throws Exception;
}
