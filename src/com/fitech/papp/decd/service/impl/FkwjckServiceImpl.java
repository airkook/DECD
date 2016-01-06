package com.fitech.papp.decd.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.FkwjckDao;
import com.fitech.papp.decd.service.FkwjckService;

/**
 * 反馈文件查看
 * 
 * @author xujj
 * 
 */
@Service(value = "fkwjckService")
public class FkwjckServiceImpl implements FkwjckService {

    /** 反馈文件查看dao */
    @Autowired
    private FkwjckDao fkwjckDao;

    /*
     * 分页查询
     */
    @Override
    public PageResults select(Map<String, String> param, int pageNo, int pageSize, int reportOrgID) throws Exception {
        PageResults prList = fkwjckDao.select(param, pageNo, pageSize, reportOrgID);
        return prList;
    }

    /*
     * 查询id
     * 
     * @see
     */
    @Override
    public String searchId(String cpdm, String term) throws Exception {
        String id = fkwjckDao.searchId(term, cpdm);
        return id;
    }
}
