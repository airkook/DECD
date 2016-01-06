package com.fitech.papp.decd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;


/**
 * 数据审核dao
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "dataAuditDao")
public interface DataAuditDao {

    /**
     * 查询数据审核列表
     */
    public List<CertificatesOfDeposit> findCertificatesOfDepositList();
    
    /**
     * 根据条件分页查询
     * @param sql
     * @param map
     * @param pageSize
     * @param pageNo
     * @return
     */
    public PageResults findCertificatesOfDepositInfo(Map<String, String> paramMap, int pageSize, int pageNo) throws BaseDaoException;
    
    /**
     * 审核通过
     * @param map
     * @throws Exception
     */
    public void updateDataAuditPass(Map<String, String> paramMap)throws BaseDaoException;
    
    /**
     * 审核不通过
     * @param map
     * @throws Exception
     */
    public void updateDataAuditNoPass(Map<String, String> paramMap) throws BaseDaoException;
}
