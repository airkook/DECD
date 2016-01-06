package com.fitech.papp.decd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.DataAuditDao;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.service.DataAuditService;

/**
 * 数据审核serivice实现类
 * 
 * @author wupengzheng
 * 
 */

@Service(value = "dataAuditService")
public class DataAuditServiceImpl implements DataAuditService {

    /** 数据审核dao */
    @Autowired
    private DataAuditDao dataAuditDao;

    @Override
    public List<CertificatesOfDeposit> findCertificatesOfDepositListByHQL() {
        return dataAuditDao.findCertificatesOfDepositList();
    }

    @Override
    public PageResults findCertificatesOfDepositInfo(Map<String, String> map, int pageSize, int pageNo)
            throws Exception {
        return dataAuditDao.findCertificatesOfDepositInfo(map, pageSize, pageNo);
    }

    @Override
    public void updateDataAuditPass(Map<String, String> map) throws Exception {
        dataAuditDao.updateDataAuditPass(map);

    }

    @Override
    public void updateDataAuditNoPass(Map<String, String> map) throws Exception {
        dataAuditDao.updateDataAuditNoPass(map);

    }

}
