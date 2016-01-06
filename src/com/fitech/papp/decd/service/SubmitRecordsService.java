package com.fitech.papp.decd.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;

/**
 * 报送记录查看 service
 * 
 * @author songfei
 * 
 */
@Service(value = "submitRecordsService")
public interface SubmitRecordsService extends IBaseService<ReportResultInfo, Integer> {

    public PageResults findByPage(Map<String, String> param, int pageSize, int pageNo);

    public List findAllOrgList();

    public String findUserId(String id);

    public void downLoadFile(String path, HttpServletResponse response) throws IOException;
}
