package com.fitech.papp.decd.dao;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.vo.DataMonitorVo;

/**
 * 数据监控管理dao
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "dataMonitorDao")
public interface DataMonitorDao {

    
    /**
     * 查询监控列表
     * @return 监控列表
     */
    public DataMonitorVo queryDataMonitorVo(String term) throws Exception;
    
    
    
}
