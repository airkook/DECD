package com.fitech.papp.decd.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.IBaseService;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.vo.ReportConfigInfoVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;

/**
 * 报送配置 service
 * 
 * @author songfei
 * 
 */
@Service(value = "submitConfigService")
public interface SubmitConfigService extends IBaseService<ReportConfigInfo, Integer> {

    public List findReportConfigList();

    public void saveOrUpdateReportConfig(ReportConfigInfo info) throws BaseDaoException;

    public ReportConfigInfoVo changeToVo(ReportConfigInfo info);

    public ReportConfigInfo changeToInfo(ReportConfigInfoVo infoVo);

    /**
     * @param excel
     * @param excelFileName
     * @param userInfo
     * @return
     */
    public String uploadData(File excelFile, String excelFileName, UserInfoVo userInfo) throws Exception;
}
