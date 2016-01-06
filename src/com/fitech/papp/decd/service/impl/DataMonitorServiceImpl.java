package com.fitech.papp.decd.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.papp.decd.dao.DataMonitorDao;
import com.fitech.papp.decd.model.vo.DataMonitorVo;
import com.fitech.papp.decd.service.DataMonitorService;

/**
 * 数据监控实现类
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "dataMonitorService")
public class DataMonitorServiceImpl extends DefaultBaseService<DataMonitorVo, Integer> implements DataMonitorService {

    /**
     * 数据监控Dao
     */
    @Autowired
    private DataMonitorDao dataMonitorDao;

    @Override
    public DataMonitorVo queryDataMonitorVo(String term) throws Exception {
        return dataMonitorDao.queryDataMonitorVo(term);
    }

}
