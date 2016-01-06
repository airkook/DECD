/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.util.DateUtil;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.vo.CdsValidateResultVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.CdsManagerService;
import com.fitech.papp.decd.util.Constants;

/**
 * 大额存单数据维护action
 * 
 * @author xyf
 * 
 */
public class CDSManageAction extends DefaultBaseAction {

    /** serialVersionUID */
    private static final long serialVersionUID = -6139605442136981484L;

    /** 日志 */
    private final Log log = LogFactory.getLog(CDSManageAction.class);

    /** 大额存单数据维护Service */
    @Autowired
    private CdsManagerService cdsManagerService;

    /** 计息方式map */
    private Map<String, String> jxfsMap;

    /** 数据状态map */
    private Map<String, String> statusMap;

    /** 校验状态map */
    private Map<String, String> validateStatusMap;

    /** 产品代码 */
    private String cpdm;

    /** 期数 */
    private String term;

    /** 计息方式 */
    private String jxfs;

    /** 校验状态 */
    private String status;

    /** 校验状态 */
    private String validateStatus;

    /** 查询结果 */
    private PageResults pageResults;

    /** checkBox选择的ID值 */
    private String[] checkBox;

    /** 上传文件 */
    private File excel;

    /** 上传文件名 */
    private String excelFileName;

    /** 提示信息 */
    private String message;

    /** 数据主键id */
    private String id;

    /** 操作标识 */
    private String flag;

    /** 大额存单 */
    private CertificatesOfDeposit certificatesOfDeposit;

    /** 数据字典map */
    private Map<String, Map<String, String>> codeMap;

    /** 校验结果 */
    private List<CdsValidateResultVo> validateResultList;

    /**
     * 页面初始化查询
     * 
     * @return
     */
    public String init() {
        try {
            // 如果期数为空，初始化为昨天
            if (StringUtil.isEmpty(term)) {
                term = DateUtil.getLastDay(DateUtil.getTodayDateStr());
            }
            // 查询参数
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            paramMap.put("cpdm", cpdm);
            paramMap.put("jxfs", jxfs);
            paramMap.put("status", status);
            paramMap.put("validateStatus", validateStatus);

            // 查询结果
            pageResults = cdsManagerService.findCdsListByPage(paramMap, pageSize, pageNo);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return SUCCESS;
    }

    /**
     * 查看详细
     * 
     * @return
     */
    public String toView() {
        try {
            flag = "view";
            certificatesOfDeposit = cdsManagerService.findCdsById(id);
            // 查询校验结果
            validateResultList = cdsManagerService.findValidateResultList(certificatesOfDeposit.getId().toString(),
                    certificatesOfDeposit.getTerm());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return "detail";
    }

    /**
     * 修改
     * 
     * @return
     */
    public String toEdit() {
        try {
            flag = "edit";
            certificatesOfDeposit = cdsManagerService.findCdsById(id);
            // 查询校验结果
            validateResultList = cdsManagerService.findValidateResultList(certificatesOfDeposit.getId().toString(),
                    certificatesOfDeposit.getTerm());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return "detail";
    }

    /**
     * 增加
     * 
     * @return
     */
    public String toAdd() {
        try {
            flag = "add";
            certificatesOfDeposit = new CertificatesOfDeposit();
            certificatesOfDeposit.setYwfsr(term);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return "detail";
    }

    /**
     * 保存增加
     * 
     * @return
     */
    public String saveAdd() {
        try {
            // 取得当前登录人
            UserInfoVo userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
            String result = cdsManagerService.saveAdd(certificatesOfDeposit, userInfo);
            if (!StringUtil.isEmpty(result)) {
                message = result;
                flag = "add";
            }
            else {
                message = "保存成功！";
                flag = "edit";
                // 查询校验结果
                validateResultList = cdsManagerService.findValidateResultList(certificatesOfDeposit.getId().toString(),
                        certificatesOfDeposit.getTerm());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            message = "保存失败！";
            flag = "add";
        }
        return "detail";
    }

    /**
     * 保存修改
     * 
     * @return
     */
    public String saveEdit() {
        try {
            // 取得当前登录人
            UserInfoVo userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
            String result = cdsManagerService.saveEdit(certificatesOfDeposit, userInfo);
            if (!StringUtil.isEmpty(result)) {
                message = result;
            }
            else {
                message = "保存成功！";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            message = "保存失败！";
        }
        // 查询校验结果
        validateResultList = cdsManagerService.findValidateResultList(certificatesOfDeposit.getId().toString(),
                certificatesOfDeposit.getTerm());
        flag = "edit";
        return "detail";
    }

    /**
     * 删除
     * 
     * @return
     */
    public String del() {
        try {
            cdsManagerService.deleteById(id, term);
            // 重新校验数据
            validateData();
            message = "";
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return init();
    }

    /**
     * 提交审核
     * 
     * @return
     */
    public String submitCheck() {
        try {
            cdsManagerService.submitCheck(checkBox);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return init();
    }

    /**
     * 下载
     * 
     * @return
     */
    public void downLoad() {
        try {
            // 如果期数为空，初始化为昨天
            if (StringUtil.isEmpty(term)) {
                term = DateUtil.getLastDay(DateUtil.getTodayDateStr());
            }
            // 查询参数
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            paramMap.put("cpdm", cpdm);
            paramMap.put("jxfs", jxfs);
            paramMap.put("status", status);
            paramMap.put("validateStatus", validateStatus);
            HttpServletResponse response = this.getResponse();
            cdsManagerService.downLoad(response, paramMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    /**
     * 导入
     * 
     * @return
     */
    public String uploadData() {
        try {
            String termStr = excelFileName.substring(0, excelFileName.indexOf(".")).trim();
            if (term != null && !term.equals(termStr)) {
                message = "文件导入的期数必须与当前页面的期数相同！";
                return init();
            }

            // 取得当前登录人
            UserInfoVo userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
            if (excel != null && !StringUtil.isEmpty(excelFileName)) {
                message = cdsManagerService.uploadData(excel, excelFileName, userInfo);
            }
            else {
                message = "请选择一个文件！";
            }
        }
        catch (Exception e) {
            message = "导入出现错误，请确认文件名和文件数据是否正确。";
            e.printStackTrace();
            log.error(e);
        }
        return init();
    }

    /**
     * 校验
     * 
     * @return
     */
    public String validateData() {
        try {
            // 如果期数为空，初始化为昨天
            if (StringUtil.isEmpty(term)) {
                term = DateUtil.getLastDay(DateUtil.getTodayDateStr());
            }
            // 查询参数
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("term", term);
            cdsManagerService.validateData(paramMap);
            message = "校验完成！";
        }
        catch (Exception e) {
            message = "校验出现错误！";
            e.printStackTrace();
            log.error(e);
        }
        return init();
    }

    /**
     * @return the codeMap
     */
    public Map<String, Map<String, String>> getCodeMap() {
        if (codeMap == null) {
            codeMap = cdsManagerService.findCodeLibMap();
        }
        return codeMap;
    }

    /**
     * @param codeMap the codeMap to set
     */
    public void setCodeMap(Map<String, Map<String, String>> codeMap) {
        this.codeMap = codeMap;
    }

    /**
     * @return the jxfsMap
     */
    public Map<String, String> getJxfsMap() {
        jxfsMap = cdsManagerService.findCodeLibMap("JXFS");
        return jxfsMap;
    }

    /**
     * @param jxfsMap the jxfsMap to set
     */
    public void setJxfsMap(Map<String, String> jxfsMap) {
        this.jxfsMap = jxfsMap;
    }

    /**
     * @return the statusMap
     */
    public Map<String, String> getStatusMap() {
        statusMap = new TreeMap<String, String>();
        statusMap.put("", "全部");
        statusMap.put(Constants.CDS_STATUS_NORMAL, "正常");
        statusMap.put(Constants.CDS_STATUS_EDIT, "补录");
        statusMap.put(Constants.CDS_STATUS_REVIEW, "提交审核");
        statusMap.put(Constants.CDS_STATUS_REVIEW_UNPASS, "审核不通过");
        statusMap.put(Constants.CDS_STATUS_REVIEW_PASS, "审核通过");
        return statusMap;
    }

    /**
     * @param statusMap the statusMap to set
     */
    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    /**
     * @return the validateStatusMap
     */
    public Map<String, String> getValidateStatusMap() {
        validateStatusMap = new TreeMap<String, String>();
        validateStatusMap.put("", "全部");
        validateStatusMap.put(Constants.VALIDATE_STATUS_PASS, "通过");
        validateStatusMap.put(Constants.VALIDATE_STATUS_UNPASS, "不通过");
        return validateStatusMap;
    }

    /**
     * @param validateStatusMap the validateStatusMap to set
     */
    public void setValidateStatusMap(Map<String, String> validateStatusMap) {
        this.validateStatusMap = validateStatusMap;
    }

    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @param term the term to set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return the pageResults
     */
    public PageResults getPageResults() {
        return pageResults;
    }

    /**
     * @param pageResults the pageResults to set
     */
    public void setPageResults(PageResults pageResults) {
        this.pageResults = pageResults;
    }

    /**
     * @return the checkBox
     */
    public String[] getCheckBox() {
        return checkBox;
    }

    /**
     * @param checkBox the checkBox to set
     */
    public void setCheckBox(String[] checkBox) {
        this.checkBox = checkBox;
    }

    /**
     * @return the excel
     */
    public File getExcel() {
        return excel;
    }

    /**
     * @param excel the excel to set
     */
    public void setExcel(File excel) {
        this.excel = excel;
    }

    /**
     * @return the excelFileName
     */
    public String getExcelFileName() {
        return excelFileName;
    }

    /**
     * @param excelFileName the excelFileName to set
     */
    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the cpdm
     */
    public String getCpdm() {
        return cpdm;
    }

    /**
     * @param cpdm the cpdm to set
     */
    public void setCpdm(String cpdm) {
        this.cpdm = cpdm;
    }

    /**
     * @return the jxfs
     */
    public String getJxfs() {
        return jxfs;
    }

    /**
     * @param jxfs the jxfs to set
     */
    public void setJxfs(String jxfs) {
        this.jxfs = jxfs;
    }

    /**
     * @return the validateStatus
     */
    public String getValidateStatus() {
        return validateStatus;
    }

    /**
     * @param validateStatus the validateStatus to set
     */
    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @return the certificatesOfDeposit
     */
    public CertificatesOfDeposit getCertificatesOfDeposit() {
        return certificatesOfDeposit;
    }

    /**
     * @param certificatesOfDeposit the certificatesOfDeposit to set
     */
    public void setCertificatesOfDeposit(CertificatesOfDeposit certificatesOfDeposit) {
        this.certificatesOfDeposit = certificatesOfDeposit;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the validateResultList
     */
    public List<CdsValidateResultVo> getValidateResultList() {
        return validateResultList;
    }

    /**
     * @param validateResultList the validateResultList to set
     */
    public void setValidateResultList(List<CdsValidateResultVo> validateResultList) {
        this.validateResultList = validateResultList;
    }

}
