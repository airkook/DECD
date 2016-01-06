package com.fitech.papp.decd.action;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.service.ValidateInfoService;
import com.fitech.papp.decd.util.Constants;

public class ValidateInfoManagerAction extends DefaultBaseAction {
    private static final long serialVersionUID = 1L;

    @Autowired
    private ValidateInfoService validateInfoService;

    private List<ValidateInfo> validateInfoList;

    private Integer validateType;

    private String test;

    /** 校验类型map **/
    private Map<String, String> validateTypeMap;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Integer getValidateType() {
        return validateType;
    }

    public void setValidateType(Integer validateType) {
        this.validateType = validateType;
    }

    /**
     * 校验规则查看初始化加载数据
     * 
     * @return
     */
    public String init() {
        validateType = Integer.parseInt(Constants.VALIDATE_TYPE_BASE);
        validateInfoList = validateInfoService.findValidateInfoListByType(validateType);
        return SUCCESS;
    }

    /**
     * 校验规则分类型查看
     * 
     * @return
     */
    public String findByType() {
        if (validateType == null) {
            validateInfoList = validateInfoService.findValidateInfoList();
        }
        else {
            validateInfoList = validateInfoService.findValidateInfoListByType(validateType);
        }
        return SUCCESS;
    }

    /**
     * @return the validateTypeMap
     */
    public Map<String, String> getValidateTypeMap() {
        validateTypeMap = new TreeMap<String, String>();
        // validateTypeMap.put("", "全部");
        validateTypeMap.put(Constants.VALIDATE_TYPE_BASE, "基本校验");
        validateTypeMap.put(Constants.VALIDATE_TYPE_TABLE_INSIDE, "表内校验");
        validateTypeMap.put(Constants.VALIDATE_TYPE_TABLE_EACH, "表间校验");
        validateTypeMap.put(Constants.VALIDATE_TYPE_TERM, "合计校验");
        return validateTypeMap;
    }

    /**
     * @param validateTypeMap the validateTypeMap to set
     */
    public void setValidateTypeMap(Map<String, String> validateTypeMap) {
        this.validateTypeMap = validateTypeMap;
    }

    public List<ValidateInfo> getValidateInfoList() {
        return validateInfoList;
    }

    public void setValidateInfoList(List<ValidateInfo> validateInfoList) {
        this.validateInfoList = validateInfoList;
    }

}
