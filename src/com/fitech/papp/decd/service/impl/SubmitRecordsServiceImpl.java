package com.fitech.papp.decd.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.SubmitRecordsDao;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.pojo.ReportOrgInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.vo.ReportResultInfoVo;
import com.fitech.papp.decd.service.SubmitRecordsService;

/**
 * 报送记录查看 service
 * 
 * @author songfei
 * 
 */
@Service(value = "submitRecordsService")
public class SubmitRecordsServiceImpl extends DefaultBaseService<ReportResultInfo, Integer> implements
        SubmitRecordsService {

    @Autowired
    private SubmitRecordsDao submitRecordsDao;

    public PageResults findByPage(Map<String, String> param, int pageSize, int pageNo) {
        List<ReportResultInfoVo> list = new ArrayList<ReportResultInfoVo>();
        List<ReportResultInfo> li = new ArrayList<ReportResultInfo>();
        List<ReportOrgInfo> listOrg = new ArrayList<ReportOrgInfo>();

        PageResults pageResults = submitRecordsDao.findByPage(param, pageSize, pageNo);
        li = pageResults.getResults();
        listOrg = submitRecordsDao.findReportOrgInfo();
        for (ReportResultInfo re : li) {
            ReportResultInfoVo reVo = new ReportResultInfoVo();
            // 把报送结果放到VO对象中
            if (re.getResultId() != null)
                reVo.setResultId(re.getResultId());// ID
            if (re.getTerm() != null)
                reVo.setTerm(re.getTerm());// 报送期数
            if (re.getReportOrgId() != null)
                reVo.setReportOrgId(re.getReportOrgId());// 上报机构ID
            if (re.getReportTime() != null)
                reVo.setReportTime(re.getReportTime());// 上报时间
            if (re.getReportUserId() != null)
                reVo.setReportUserId(re.getReportUserId());// 报送人ID
            if (re.getReportStatus() != null)
                reVo.setReportStatus(re.getReportStatus());// 上报状态：1.反馈未抓取，2.成功，3.失败
            // 上报机构名称
            for (ReportOrgInfo org : listOrg) {
                if (org.getReportOrgId().equals(re.getReportOrgId())) {
                    reVo.setOrgName(org.getReportOrgName());
                }
            }
            list.add(reVo);
        }
        pageResults.setResults(list);
        return pageResults;
    }

    // 返回所有上报机构
    public List findAllOrgList() {
        List orgList = new ArrayList();
        orgList = submitRecordsDao.findReportOrgInfo();
        return orgList;
    }

    // 返回上报机构用户名
    public String findUserId(String id) {
        String str = "";
        List<ReportConfigInfo> list = submitRecordsDao.findUserId(id);
        if (list != null && list.size() > 0) {
            for (ReportConfigInfo info : list) {
                str += info.getFtpUserId();
            }
        }
        return str;
    }

    // 文件下载
    public void downLoadFile(String path, HttpServletResponse response) throws IOException {
        File file = new File(path);
        try {
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(file.getName().getBytes("utf-8")));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
