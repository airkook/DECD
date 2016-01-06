package com.fitech.papp.decd.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.papp.decd.dao.SubmitConfigDao;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;
import com.fitech.papp.decd.model.vo.ReportConfigInfoVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.SubmitConfigService;

/**
 * 报送配置 service
 * 
 * @author songfei
 * 
 */
@Service(value = "submitConfigService")
public class SubmitConfigServiceImpl extends DefaultBaseService<ReportConfigInfo, Integer> implements
        SubmitConfigService {

    @Autowired
    private SubmitConfigDao submitConfigDao;

    // 返回所有上报机构
    public List findReportConfigList() {
        List orgList = new ArrayList();
        orgList = submitConfigDao.findAllReportConfigInfo();
        return orgList;
    }

    public void saveOrUpdateReportConfig(ReportConfigInfo info) throws BaseDaoException {
        submitConfigDao.saveOrUpdate(info);
    }

    @Override
    public ReportConfigInfoVo changeToVo(ReportConfigInfo info) {
        ReportConfigInfoVo vo = new ReportConfigInfoVo();
        if (info != null) {
            vo.setFeedbackPath(info.getFeedbackPath());
            vo.setFtpAddress(info.getFtpAddress());
            vo.setFtpPassword(info.getFtpPassword());
            vo.setFtpPort(info.getFtpPort());
            vo.setFtpUserId(info.getFtpUserId());
            vo.setReportOrgId(info.getReportOrgId());
            vo.setUploadPath(info.getUploadPath());
        }
        return vo;
    }

    @Override
    public ReportConfigInfo changeToInfo(ReportConfigInfoVo infoVo) {
        ReportConfigInfo info = new ReportConfigInfo();
        if (infoVo != null) {
            info.setFeedbackPath(infoVo.getFeedbackPath());
            info.setFtpAddress(infoVo.getFtpAddress());
            info.setFtpPassword(infoVo.getFtpPassword());
            info.setFtpPort(infoVo.getFtpPort());
            info.setFtpUserId(infoVo.getFtpUserId());
            info.setReportOrgId(infoVo.getReportOrgId());
            info.setUploadPath(infoVo.getUploadPath());
        }
        return info;
    }

    /*
     * 导入
     */
    @Override
    public String uploadData(File excelFile, String excelFileName, UserInfoVo userInfo) throws Exception {

        // 根据文件名获取期数(年份)
        String term = excelFileName.substring(0, excelFileName.indexOf(".")).trim();
        // 取文件后缀名
        String fileType = excelFileName.substring(excelFileName.indexOf(".") + 1);
        if (!"xls".equals(fileType)) {
            return "请上传Excel文件！";
        }
        boolean result = term.matches("[0-9]+");
        if (term.length() != 4 || result != true) {
            return "文件名称的格式不正确，应为年份!";
        }
        InputStream is = new FileInputStream(excelFile);
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);

        // 读取数据

        List<ReportCalendarInfo> insertList = new ArrayList<ReportCalendarInfo>();
        Integer insertCount = 0;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            HSSFRow row = sheet.getRow(rowNum);
            if (StringUtil.isEmpty(getStringCellValue(row.getCell(0)))) {
                break;
            }
            // 报送日
            String reportDate = getStringCellValue(row.getCell(0));
            // 业务发生日
            String ywfsr = getStringCellValue(row.getCell(1));
            // 多个业务日 ,以逗号分隔
            String[] a = ywfsr.split(",");
            for (int i = 0; i < a.length; i++) {
                /* map.put("", value) */
                ReportCalendarInfo rci = new ReportCalendarInfo();
                rci.setReportDate(reportDate);
                rci.setYwfsrDate(a[i]);
                rci.setReportYear(term);
                insertList.add(rci);
                insertCount++;
            }
        }

        // 删除该年份下所有数据先
        submitConfigDao.deleteByYear(term);
        // 执行insert
        submitConfigDao.batchInsert(insertList);
        String msg = "导入成功！\\n";
        msg = msg + "共插入" + insertCount + "条记录；\\n";
        return msg;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                int i = (int) d;
                if (i == d) {
                    strCell = String.valueOf(i);
                }
                else {
                    strCell = String.valueOf(d);
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell.trim();
    }
}
