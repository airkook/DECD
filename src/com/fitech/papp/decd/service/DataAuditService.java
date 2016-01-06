package com.fitech.papp.decd.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;

/**
 * 数据审核  service
 * 
 * @author wupengzheng
 * 
 */

@Service(value = "dataAuditService")
public interface DataAuditService {
    /**
     * 查询数据审核信息列表
     */
    public List<CertificatesOfDeposit> findCertificatesOfDepositListByHQL();
    
    /**
     *根据sql查询数据审核分页查询
     */
    public PageResults findCertificatesOfDepositInfo(Map<String, String> map, int pageSize, int pageNo) throws Exception;
    
    /**
     * 审核通过
     * @param map
     * @throws Exception
     */
    public void updateDataAuditPass(Map<String, String> map) throws Exception;
    
    /**
     * 审核不通过
     * @param map
     * @throws Exception
     */
    public void updateDataAuditNoPass(Map<String, String> map) throws Exception;
    
}
