/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.service.impl.DefaultBaseService;
import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.ExcelExportInfo;
import com.fitech.framework.core.util.ExcelUtil;
import com.fitech.framework.core.util.ReflectionUtils;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.CdsManagerDao;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.CdsValidateResultVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.CdsManagerService;
import com.fitech.papp.decd.util.Constants;
import com.fitech.papp.decd.util.FormulaCalculator;
import com.fitech.papp.decd.util.ValidateUtil;

/**
 * 大额存单数据维护service
 * 
 * @author xyf
 * 
 */
@Service(value = "cdsManagerService")
public class CdsManagerServiceImpl extends DefaultBaseService<CertificatesOfDeposit, Integer> implements
        CdsManagerService {

    /** 大额存单数据维护dao */
    @Autowired
    private CdsManagerDao cdsManagerDao;

    /**
     * 分页查询
     * 
     * @param paramMap
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    @Override
    public PageResults findCdsListByPage(Map<String, String> paramMap, int pageSize, int pageNo) throws Exception {
        return cdsManagerDao.findCdsListByPage(paramMap, pageSize, pageNo);
    }

    /**
     * 根据id获取大额存单详细信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public CertificatesOfDeposit findCdsById(String id) throws Exception {
        return cdsManagerDao.read(Integer.parseInt(id));
    }

    /**
     * 获取数据字典
     * 
     * @return
     */
    @Override
    public Map<String, Map<String, String>> findCodeLibMap() {
        List<Object[]> list = cdsManagerDao.findCodeLibList();
        Map<String, Map<String, String>> codeMap = new HashMap<String, Map<String, String>>();
        for (Object[] arr : list) {
            String codeId = arr[0].toString().toLowerCase();
            if (codeMap.containsKey(codeId)) {
                Map<String, String> map = codeMap.get(codeId);
                map.put(String.valueOf(arr[1]), String.valueOf(arr[2]));
                codeMap.put(codeId, map);
            }
            else {
                Map<String, String> map = new TreeMap<String, String>();
                map.put("", "--请选择--");
                map.put(String.valueOf(arr[1]), String.valueOf(arr[2]));
                codeMap.put(codeId, map);
            }
        }
        return codeMap;
    }

    /**
     * 获取数据字典
     * 
     * @return
     */
    @Override
    public Map<String, String> findCodeLibMap(String codeId) {
        List<Object[]> list = cdsManagerDao.findCodeLibList(codeId);
        Map<String, String> map = new TreeMap<String, String>();
        map.put("", "全部");
        for (Object[] arr : list) {
            map.put(String.valueOf(arr[1]), String.valueOf(arr[2]));
        }
        return map;
    }

    /**
     * 保存增加
     * 
     * @param certificatesOfDeposit
     * @param loginUserInfo
     * @return
     * @throws Exception
     */
    @Override
    public String saveAdd(CertificatesOfDeposit certificatesOfDeposit, UserInfoVo loginUserInfo) throws Exception {
        // 业务发生日为它的期数
        certificatesOfDeposit.setTerm(certificatesOfDeposit.getYwfsr());
        // 新增的数据状态为补录
        certificatesOfDeposit.setStatus(Constants.CDS_STATUS_EDIT);
        // 未校验
        certificatesOfDeposit.setValidateStatus(Constants.VALIDATE_STATUS_UNVALI);
        // 修改人
        certificatesOfDeposit.setUpdateUserId(loginUserInfo.getUserId());
        // 检查产品代码是否已存在
        Boolean existFlag = cdsManagerDao.checkCpdmExist(certificatesOfDeposit);
        if (existFlag) {
            return "输入的产品代码在本期已存在！";
        }
        // 保存
        cdsManagerDao.saveCds(certificatesOfDeposit);

        // 校验
        // this.validateData(certificatesOfDeposit.getId().toString());
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("term", certificatesOfDeposit.getTerm());
        this.validateData(paramMap);

        // 判断上报记录中是否有记录，有则修改状态为4：已修改数据，未重新生成报表
        List<ReportResultInfo> reportList = cdsManagerDao.findReportResultInfoList(certificatesOfDeposit.getTerm());
        if (reportList != null && reportList.size() > 0) {
            for (ReportResultInfo rri : reportList) {
                rri.setReportStatus(Constants.REPORT_RESULT_EDITDATA);
                cdsManagerDao.update(rri);
            }
        }

        return "";
    }

    /**
     * 保存修改
     * 
     * @param certificatesOfDeposit
     * @param loginUserInfo
     * @return
     * @throws Exception
     */
    @Override
    public String saveEdit(CertificatesOfDeposit certificatesOfDeposit, UserInfoVo loginUserInfo) throws Exception {
        // 修改的数据状态为补录
        certificatesOfDeposit.setStatus(Constants.CDS_STATUS_EDIT);
        // 未校验
        certificatesOfDeposit.setValidateStatus(Constants.VALIDATE_STATUS_UNVALI);
        // 修改人
        certificatesOfDeposit.setUpdateUserId(loginUserInfo.getUserId());
        // 检查产品代码是否已存在
        Boolean existFlag = cdsManagerDao.checkCpdmExist(certificatesOfDeposit);
        if (existFlag) {
            return "输入的产品代码在本期已存在！";
        }
        // 保存
        cdsManagerDao.update(certificatesOfDeposit);
        // 校验
        // this.validateData(certificatesOfDeposit.getId().toString());
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("term", certificatesOfDeposit.getTerm());
        this.validateData(paramMap);

        // 判断上报记录中是否有记录，有则修改状态为4：已修改数据，未重新生成报表
        List<ReportResultInfo> reportList = cdsManagerDao.findReportResultInfoList(certificatesOfDeposit.getTerm());
        if (reportList != null && reportList.size() > 0) {
            for (ReportResultInfo rri : reportList) {
                rri.setReportStatus(Constants.REPORT_RESULT_EDITDATA);
                cdsManagerDao.update(rri);
            }
        }
        return "";
    }

    /**
     * 删除
     * 
     * @param id
     * @param term
     * @throws Exception
     */
    @Override
    public void deleteById(String id, String term) throws Exception {
        // cdsManagerDao.delete(CertificatesOfDeposit.class,
        // Integer.parseInt(id));
        // 逻辑删除
        cdsManagerDao.updateDeleteStatus(id);
        // 删除校验结果
        cdsManagerDao.delValidateResult(id, term);
    }

    /**
     * 提交审核
     * 
     * @param checkBox
     * @throws Exception
     */
    @Override
    public void submitCheck(String[] checkBox) throws Exception {
        if (checkBox == null || checkBox.length == 0) {
            return;
        }
        String checkIds = "";
        for (String id : checkBox) {
            checkIds += id + ",";
        }
        checkIds = checkIds.substring(0, checkIds.length() - 1);
        cdsManagerDao.updateReviewStatus(checkIds);
    }

    /**
     * 下载
     * 
     * @param response
     * @param paramMap
     * @throws Exception
     */
    @Override
    public void downLoad(HttpServletResponse response, Map<String, String> paramMap) throws Exception {
        List<CertificatesOfDeposit> cdList = cdsManagerDao.findCdsList(paramMap);

        // 下载设置
        response.setContentType("application/vnd.ms-excel; CHARSET=utf8");
        response.setHeader("Content-Disposition", "attachment; filename=" + paramMap.get("term") + ".xls");
        OutputStream outputFile = response.getOutputStream();

        ExcelExportInfo setInfo = new ExcelExportInfo();
        // 设置导出信息
        LinkedHashMap<String, List> map = new LinkedHashMap<String, List>();
        map.put("Sheet1", cdList);
        List<String[]> headNames = new ArrayList<String[]>();
        headNames.add(Constants.CDS_HEADNAME);
        List<String[]> fieldNames = new ArrayList<String[]>();
        fieldNames.add(Constants.CDS_FIELDNAME);
        setInfo.setObjsMap(map);
        setInfo.setFieldNames(fieldNames);
        setInfo.setHeadNames(headNames);
        setInfo.setOut(outputFile);
        // 将需要导出的数据输出到output
        ExcelUtil.export2Excel(setInfo);

        outputFile.close();
        outputFile.flush();
    }

    /**
     * 导入
     * 
     * @param excelFile
     * @param excelFileName
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public String uploadData(File excelFile, String excelFileName, UserInfoVo userInfo) throws Exception {
        // 根据文件名获取期数
        String term = excelFileName.substring(0, excelFileName.indexOf(".")).trim();
        String fileType = excelFileName.substring(excelFileName.indexOf(".") + 1);
        if (!"xls".equals(fileType)) {
            return "请上传Excel文件！";
        }
        if (term.length() != 10 || DateUtil.getDateByString(term) == null) {
            return "文件名称的格式不正确，应为期数！";
        }
        InputStream is = new FileInputStream(excelFile);
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        HSSFSheet sheet = workbook.getSheetAt(0);
        if (sheet.getLastRowNum() > 1001) {
            return "导入数据最大只支持1000条！";
        }
        // 读取数据
        List<String[]> insertList = new ArrayList<String[]>();
        List<String[]> updateList = new ArrayList<String[]>();
        Integer allCount = 0;
        Integer successCount = 0;
        Integer insertCount = 0;
        Integer updateCount = 0;
        Integer reviewCount = 0;
        Integer reviewPassCount = 0;
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            allCount++;
            HSSFRow row = sheet.getRow(rowNum);
            if (StringUtil.isEmpty(getStringCellValue(row.getCell(0)))) {
                break;
            }
            // 产品代码
            String cpdm = getStringCellValue(row.getCell(2));
            // 业务发生日
            String ywfsr = getStringCellValue(row.getCell(3));

            // 根据产品代码和业务发生日来判断是否存在此条数据
            CertificatesOfDeposit cd = cdsManagerDao.findCdsByCpdmYwfsr(cpdm, ywfsr);
            if (cd == null) {
                String[] params = new String[32];
                params[0] = cdsManagerDao.findCdsId();// 主键ID
                for (int cellNum = 0; cellNum <= 26; cellNum++) {
                    String cellValue = getStringCellValue(row.getCell(cellNum));
                    params[cellNum + 1] = cellValue;
                }
                params[28] = term;// 期数
                params[29] = Constants.CDS_STATUS_EDIT;// 数据状态：补录
                params[30] = Constants.VALIDATE_STATUS_UNVALI;// 校验状态：未校验
                params[31] = userInfo.getUserId().toString();
                insertList.add(params);
                insertCount++;
            }
            else {
                // 如果是提交审核状态则不可补录
                if (Constants.CDS_STATUS_REVIEW.equals(cd.getStatus())) {
                    reviewCount++;
                    continue;
                }
                // 如果是审核通过状态则不可补录
                if (Constants.CDS_STATUS_REVIEW_PASS.equals(cd.getStatus())) {
                    reviewPassCount++;
                    continue;
                }
                String[] params = new String[32];
                for (int cellNum = 0; cellNum <= 26; cellNum++) {
                    String cellValue = getStringCellValue(row.getCell(cellNum));
                    params[cellNum] = cellValue;
                }
                params[27] = term;// 期数
                params[28] = Constants.CDS_STATUS_EDIT;// 数据状态：补录
                params[29] = Constants.VALIDATE_STATUS_UNVALI;// 校验状态：未校验
                params[30] = userInfo.getUserId().toString();
                params[31] = cd.getId().toString();
                updateList.add(params);
                updateCount++;
            }
            successCount++;
        }

        // 批量插入和更新数据
        cdsManagerDao.batchInsertCds(insertList);
        cdsManagerDao.batchUpdateCds(updateList);

        // 校验
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("term", term);
        this.validateData(paramMap);

        String msg = "导入成功！\\n";
        msg = msg + "共导入" + allCount + "条记录；\\n";
        msg = msg + "成功补录" + successCount + "条记录；\\n其中";
        msg = msg + "插入" + insertCount + "条记录；\\n";
        msg = msg + "更新" + updateCount + "条记录；\\n";
        if (reviewCount > 0) {
            msg = msg + reviewCount + "条记录因正在提交审核不能补录；\\n";
        }
        if (reviewPassCount > 0) {
            msg = msg + reviewPassCount + "条记录因审核通过不能补录；\\n";
        }
        return msg;
    }

    /**
     * 查询校验结果
     * 
     * @param cdsId
     * @param term
     * @return
     */
    @Override
    public List<CdsValidateResultVo> findValidateResultList(String cdsId, String term) {
        return cdsManagerDao.findValidateResultList(cdsId, term);
    }

    /**
     * 根据期数查询校验不通过的结果
     * 
     * @param term
     * @return
     */
    @Override
    public List<ValidateResult> findUnPassValiResList(String term) {
        return cdsManagerDao.findUnPassValiResList(term);
    }

    /**
     * 更新数据为审核通过状态
     * 
     * @param term
     * @throws Exception
     */
    @Override
    public void updateReviewPassStatus(String term) throws Exception {
        cdsManagerDao.updateReviewPassStatus(term);
    }

    /**
     * 校验当期数据
     * 
     * @param paramMap
     * @throws Exception
     */
    @Override
    public void validateData(Map<String, String> paramMap) throws Exception {
        List<CertificatesOfDeposit> cdsList = cdsManagerDao.findCdsList(paramMap);
        this.validateData(cdsList);
    }

    /**
     * 校验数据
     * 
     * @param ids
     * @throws Exception
     */
    @Override
    public void validateData(String ids) throws Exception {
        if (StringUtil.isEmpty(ids)) {
            return;
        }
        // 获取需要校验的所有数据
        List<CertificatesOfDeposit> cdsList = cdsManagerDao.findCdsList(ids);
        this.validateData(cdsList);
    }

    /**
     * 校验数据
     * 
     * @param ids
     * @throws Exception
     */
    @Override
    public void validateData(List<CertificatesOfDeposit> cdsList) throws Exception {
        if (cdsList == null || cdsList.size() == 0) {
            return;
        }
        // 获取所有校验信息
        List<ValidateInfo> validateInfoList = cdsManagerDao.findValidateInfoList();
        List<String[]> validateResStatusList = new ArrayList<String[]>();
        List<ValidateResult> validateResultList = new ArrayList<ValidateResult>();

        // 获取整期的这一期和上一期所有数据进行整期合计类型的校验
        Map<String, String> paramMap = new HashMap<String, String>();
        String term = cdsList.get(0).getYwfsr();
        paramMap.put("term", term);
        List<CertificatesOfDeposit> cdsTermList = cdsManagerDao.findCdsList(paramMap);
        paramMap.put("term", DateUtil.getLastDay(term));
        List<CertificatesOfDeposit> cdsLastTermList = cdsManagerDao.findCdsList(paramMap);
        // 整期的合计校验
        this.validateTerm(cdsTermList, cdsLastTermList, validateInfoList, validateResultList);

        // 循环数据进行单条数据的校验
        for (CertificatesOfDeposit cd : cdsList) {
            String[] res = new String[2];
            res[1] = cd.getId().toString();
            // 校验单条数据
            Boolean validateRes = this.validate(cd, validateInfoList, validateResultList, cdsTermList, cdsLastTermList);
            if (validateRes) {
                res[0] = Constants.VALIDATE_STATUS_PASS;
            }
            else {
                res[0] = Constants.VALIDATE_STATUS_UNPASS;
            }
            validateResStatusList.add(res);
        }

        // 批量更新校验状态
        cdsManagerDao.batchUpdateValidateStatus(validateResStatusList);

        // 删除整期所有校验结果
        cdsManagerDao.delValidateResult(null, term);

        // 插入所有校验结果
        cdsManagerDao.batchInsertValidateResult(validateResultList);
    }

    /**
     * 整期的合计校验
     * 
     * @param cdsTermList
     * @param cdsLastTermList
     * @param validateInfoList
     * @param validateResultList
     * @throws Exception
     */
    private void validateTerm(List<CertificatesOfDeposit> cdsTermList, List<CertificatesOfDeposit> cdsLastTermList,
            List<ValidateInfo> validateInfoList, List<ValidateResult> validateResultList) throws Exception {
        String term = cdsTermList.get(0).getYwfsr();

        // 循环公式进行校验
        for (ValidateInfo validateInfo : validateInfoList) {
            // 只校验整期合计类型的公式
            if (Constants.VALIDATE_TYPE_TERM.equals(validateInfo.getValidateType())) {
                String formula = validateInfo.getValidateFormula();
                formula = formula.toLowerCase();
                String calFormula = formula;
                Map<String, Object> mapVars = new HashMap<String, Object>();
                int paramNum = 1;
                List<String> todayColList = this.findColsInFormula(formula, "todaysum\\([a-z]+\\)");
                List<String> lastdayColList = this.findColsInFormula(formula, "lastdaysum\\([a-z]+\\)");
                // 将当日的合计数替换进公式
                for (String todayCol : todayColList) {
                    String colName = todayCol.substring(todayCol.indexOf("(") + 1, todayCol.lastIndexOf(")"));
                    String sumValue = this.findSumValueByCds(cdsTermList, colName);
                    todayCol = todayCol.replaceAll("\\(", "\\\\(");
                    todayCol = todayCol.replaceAll("\\)", "\\\\)");
                    mapVars.put("x" + paramNum, new BigDecimal(sumValue));
                    calFormula = calFormula.replaceAll(todayCol, "x" + paramNum);
                    paramNum++;
                    formula = formula.replaceAll(todayCol, sumValue);
                }
                // 将前一日的合计数替换进公式
                for (String lastdayCol : lastdayColList) {
                    String colName = lastdayCol.substring(lastdayCol.indexOf("(") + 1, lastdayCol.lastIndexOf(")"));
                    String sumValue = this.findSumValueByCds(cdsLastTermList, colName);
                    lastdayCol = lastdayCol.replaceAll("\\(", "\\\\(");
                    lastdayCol = lastdayCol.replaceAll("\\)", "\\\\)");
                    mapVars.put("x" + paramNum, new BigDecimal(sumValue));
                    calFormula = calFormula.replaceAll(lastdayCol, "x" + paramNum);
                    paramNum++;
                    formula = formula.replaceAll(lastdayCol, sumValue);
                }
                calFormula = calFormula.replaceAll("=", "==");
                // 合计比较校验
                Boolean validateFlag = null;
                try {
                    validateFlag = FormulaCalculator.validate(calFormula, mapVars);
                }
                catch (Exception e) {
                    validateFlag = false;
                }
                // 不通过则入结果表
                if (!validateFlag) {
                    ValidateResult validateResult = new ValidateResult();
                    validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                    validateResult.setValidaetData(formula);
                    validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_UNPASS);
                    validateResult.setTerm(term);
                    validateResult.setValidateType(validateInfo.getValidateType());
                    validateResultList.add(validateResult);
                }
                else {
                    ValidateResult validateResult = new ValidateResult();
                    validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                    validateResult.setValidaetData(formula);
                    validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_PASS);
                    validateResult.setTerm(term);
                    validateResult.setValidateType(validateInfo.getValidateType());
                    validateResultList.add(validateResult);
                }
            }
        }
    }

    /**
     * 获取匹配的字符串
     * 
     * @param formula
     * @param regStr
     * @return
     */
    private List<String> findColsInFormula(String formula, String regStr) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            String str = matcher.group(0);
            list.add(str);
        }
        return list;
    }

    /**
     * 获取数值
     * 
     * @param cdsTermList
     * @param cpdm
     * @param colName
     * @return
     */
    private String findValueByCds(List<CertificatesOfDeposit> cdsTermList, String cpdm, String colName) {
        String fieldValue = "0";
        BigDecimal value = new BigDecimal(0);
        for (CertificatesOfDeposit cd : cdsTermList) {
            if (cpdm.equals(cd.getCpdm())) {
                Object obj = ReflectionUtils.getFieldValue(cd, colName);
                fieldValue = String.valueOf(obj == null ? "" : obj);
                if (StringUtil.isEmpty(fieldValue)) {
                    fieldValue = "0";
                }
                BigDecimal colValue = null;
                try {
                    colValue = new BigDecimal(fieldValue);
                }
                catch (Exception e) {
                    colValue = new BigDecimal(0);
                }
                value = colValue;
            }
        }
        return value.toString();
    }

    /**
     * 获取合计数
     * 
     * @param cdsTermList
     * @param colName
     * @return
     */
    private String findSumValueByCds(List<CertificatesOfDeposit> cdsTermList, String colName) {
        BigDecimal sumValue = new BigDecimal(0);
        for (CertificatesOfDeposit cd : cdsTermList) {
            Object obj = ReflectionUtils.getFieldValue(cd, colName);
            String fieldValue = String.valueOf(obj == null ? "" : obj);
            if (StringUtil.isEmpty(fieldValue)) {
                fieldValue = "0";
            }
            BigDecimal colValue = null;
            try {
                colValue = new BigDecimal(fieldValue);
            }
            catch (Exception e) {
                colValue = new BigDecimal(0);
            }
            sumValue = sumValue.add(colValue);
        }
        return sumValue.toString();
    }

    /**
     * 校验单条数据
     * 
     * @param cd
     * @param validateInfoList
     * @param validateResultList
     * @param cdsTermList
     * @param cdsLastTermList
     * @return
     * @throws Exception
     */
    private Boolean validate(CertificatesOfDeposit cd, List<ValidateInfo> validateInfoList,
            List<ValidateResult> validateResultList, List<CertificatesOfDeposit> cdsTermList,
            List<CertificatesOfDeposit> cdsLastTermList) throws Exception {
        Boolean flag = true;
        ValidateUtil validateUtil = new ValidateUtil();

        // 循环公式进行校验
        for (ValidateInfo validateInfo : validateInfoList) {
            // 基本校验
            if (Constants.VALIDATE_TYPE_BASE.equals(validateInfo.getValidateType())) {
                String formula = validateInfo.getValidateFormula();
                String methodName = formula.substring(0, formula.indexOf("("));
                String paramStr = formula.substring(formula.indexOf("(") + 1, formula.lastIndexOf(")"));
                String[] params = paramStr.split(",");
                // 获取需要校验的那个字段的内容放入参数
                Object obj = ReflectionUtils.getFieldValue(cd, params[0].toLowerCase());
                String fieldValue = String.valueOf(obj == null ? "" : obj);
                params[0] = fieldValue;
                // 如果是basevalue基于某个字段的校验，则第2个参数需要取出字段内容放入参数
                if ("basevalue".equals(methodName)) {
                    Object obj1 = ReflectionUtils.getFieldValue(cd, params[1].toLowerCase());
                    String fieldValue1 = String.valueOf(obj1 == null ? "" : obj1);
                    params[1] = fieldValue1;
                }

                Class[] classParams = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    classParams[i] = String.class;
                }
                // 反射调用公式方法进行校验
                Boolean validateFlag = (Boolean) ReflectionUtils.invokeMethod(validateUtil, methodName, classParams,
                        params);
                // 不通过则入结果表
                if (!validateFlag) {
                    flag = false;
                    ValidateResult validateResult = new ValidateResult();
                    validateResult.setCdsId(cd.getId());
                    validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                    validateResult.setValidaetData(fieldValue);
                    validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_UNPASS);
                    validateResult.setTerm(cd.getTerm());
                    validateResult.setValidateType(validateInfo.getValidateType());
                    validateResultList.add(validateResult);
                }
                else {
                    ValidateResult validateResult = new ValidateResult();
                    validateResult.setCdsId(cd.getId());
                    validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                    validateResult.setValidaetData(fieldValue);
                    validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_PASS);
                    validateResult.setTerm(cd.getTerm());
                    validateResult.setValidateType(validateInfo.getValidateType());
                    validateResultList.add(validateResult);
                }
            }
            // 表内校验
            else if (Constants.VALIDATE_TYPE_TABLE_INSIDE.equals(validateInfo.getValidateType())) {
                String formula = validateInfo.getValidateFormula();
                // 如果是条件型校验
                if (formula.indexOf("ifthen") > -1) {
                    formula = formula.toLowerCase();
                    formula = formula.replaceAll("=", "==");
                    String paramStr = formula.substring(formula.indexOf("(") + 1, formula.lastIndexOf(")"));
                    String[] params = paramStr.split(",");
                    Map<String, Object> mapVars = new HashMap<String, Object>();
                    for (String field : Constants.CDS_FIELDNAME) {
                        Object obj = ReflectionUtils.getFieldValue(cd, field);
                        String fieldValue = String.valueOf(obj == null ? "" : obj);
                        mapVars.put(field, fieldValue);
                        if (StringUtil.isEmpty(fieldValue)) {
                            fieldValue = "\'\'";
                        }
                        formula = formula.replaceAll(field, fieldValue);
                    }
                    // 条件型校验
                    Boolean validateFlag = null;
                    try {
                        validateFlag = ValidateUtil.ifthen(params[0], params[1], mapVars);
                    }
                    catch (Exception e) {
                        validateFlag = false;
                    }
                    formula = formula.replaceAll("==", "=");
                    // 不通过则入结果表
                    if (!validateFlag) {
                        flag = false;
                        ValidateResult validateResult = new ValidateResult();
                        validateResult.setCdsId(cd.getId());
                        validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                        validateResult.setValidaetData(formula);
                        validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_UNPASS);
                        validateResult.setTerm(cd.getTerm());
                        validateResult.setValidateType(validateInfo.getValidateType());
                        validateResultList.add(validateResult);
                    }
                    else {
                        ValidateResult validateResult = new ValidateResult();
                        validateResult.setCdsId(cd.getId());
                        validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                        validateResult.setValidaetData(formula);
                        validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_PASS);
                        validateResult.setTerm(cd.getTerm());
                        validateResult.setValidateType(validateInfo.getValidateType());
                        validateResultList.add(validateResult);
                    }
                }
                // 其它为计算比较型校验
                else {
                    formula = formula.toLowerCase();
                    String calFormula = formula;
                    Map<String, Object> mapVars = new HashMap<String, Object>();
                    int paramNum = 1;
                    List<String> todayColList = this.findColsInFormula(formula, "today\\([a-z]+\\)");
                    List<String> lastdayColList = this.findColsInFormula(formula, "lastday\\([a-z]+\\)");
                    List<String> todaySumColList = this.findColsInFormula(formula, "todaysum\\([a-z]+\\)");
                    List<String> lastdaySumColList = this.findColsInFormula(formula, "lastdaysum\\([a-z]+\\)");
                    // 将当日数替换进公式
                    for (String todayCol : todayColList) {
                        String colName = todayCol.substring(todayCol.indexOf("(") + 1, todayCol.lastIndexOf(")"));
                        String value = this.findValueByCds(cdsTermList, cd.getCpdm(), colName);
                        todayCol = todayCol.replaceAll("\\(", "\\\\(");
                        todayCol = todayCol.replaceAll("\\)", "\\\\)");
                        mapVars.put("x" + paramNum, new BigDecimal(value));
                        calFormula = calFormula.replaceAll(todayCol, "x" + paramNum);
                        paramNum++;
                        formula = formula.replaceAll(todayCol, value);
                    }
                    // 将前一日数替换进公式
                    for (String lastdayCol : lastdayColList) {
                        String colName = lastdayCol.substring(lastdayCol.indexOf("(") + 1, lastdayCol.lastIndexOf(")"));
                        String value = this.findValueByCds(cdsLastTermList, cd.getCpdm(), colName);
                        lastdayCol = lastdayCol.replaceAll("\\(", "\\\\(");
                        lastdayCol = lastdayCol.replaceAll("\\)", "\\\\)");
                        mapVars.put("x" + paramNum, new BigDecimal(value));
                        calFormula = calFormula.replaceAll(lastdayCol, "x" + paramNum);
                        paramNum++;
                        formula = formula.replaceAll(lastdayCol, value);
                    }
                    // 将当日的合计数替换进公式
                    for (String todaySumCol : todaySumColList) {
                        String colName = todaySumCol.substring(todaySumCol.indexOf("(") + 1, todaySumCol
                                .lastIndexOf(")"));
                        String sumValue = this.findSumValueByCds(cdsTermList, colName);
                        todaySumCol = todaySumCol.replaceAll("\\(", "\\\\(");
                        todaySumCol = todaySumCol.replaceAll("\\)", "\\\\)");
                        mapVars.put("x" + paramNum, new BigDecimal(sumValue));
                        calFormula = calFormula.replaceAll(todaySumCol, "x" + paramNum);
                        paramNum++;
                        formula = formula.replaceAll(todaySumCol, sumValue);
                    }
                    // 将前一日的合计数替换进公式
                    for (String lastdaySumCol : lastdaySumColList) {
                        String colName = lastdaySumCol.substring(lastdaySumCol.indexOf("(") + 1, lastdaySumCol
                                .lastIndexOf(")"));
                        String sumValue = this.findSumValueByCds(cdsLastTermList, colName);
                        lastdaySumCol = lastdaySumCol.replaceAll("\\(", "\\\\(");
                        lastdaySumCol = lastdaySumCol.replaceAll("\\)", "\\\\)");
                        mapVars.put("x" + paramNum, new BigDecimal(sumValue));
                        calFormula = calFormula.replaceAll(lastdaySumCol, "x" + paramNum);
                        paramNum++;
                        formula = formula.replaceAll(lastdaySumCol, sumValue);
                    }

                    calFormula = calFormula.replaceAll("=", "==");
                    // 比较校验
                    Boolean validateFlag = null;
                    try {
                        validateFlag = FormulaCalculator.validate(calFormula, mapVars);
                    }
                    catch (Exception e) {
                        validateFlag = false;
                    }
                    // 不通过则入结果表
                    if (!validateFlag) {
                        flag = false;
                        ValidateResult validateResult = new ValidateResult();
                        validateResult.setCdsId(cd.getId());
                        validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                        validateResult.setValidaetData(formula);
                        validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_UNPASS);
                        validateResult.setTerm(cd.getTerm());
                        validateResult.setValidateType(validateInfo.getValidateType());
                        validateResultList.add(validateResult);
                    }
                    else {
                        ValidateResult validateResult = new ValidateResult();
                        validateResult.setCdsId(cd.getId());
                        validateResult.setValidateInfoId(validateInfo.getValidateInfoId());
                        validateResult.setValidaetData(formula);
                        validateResult.setValidateResultFlag(Constants.VALIDATE_RESULT_PASS);
                        validateResult.setTerm(cd.getTerm());
                        validateResult.setValidateType(validateInfo.getValidateType());
                        validateResultList.add(validateResult);
                    }
                }
            }
        }

        return flag;
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
