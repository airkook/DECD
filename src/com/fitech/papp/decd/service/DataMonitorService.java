package com.fitech.papp.decd.service;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.vo.DataMonitorVo;

/**
 * 数据监控管理Service
 * 
 * @author wupengzheng
 * 
 */
@Service(value = "dataMonitorService")
public interface DataMonitorService {
    
    /**
     * 查询监控列表
     * @return 监控列表
     * @throws Exception
     */
   public DataMonitorVo queryDataMonitorVo(String term) throws Exception;
}
