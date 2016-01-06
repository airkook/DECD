package com.fitech.papp.decd.model.vo;

import com.fitech.papp.decd.model.pojo.ValidateInfo;

public class ValidateInfoVo extends ValidateInfo{
    private String validateType;
    private String Desc;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getValidateType() {
        return validateType;
    }

    public void setValidateType(String validateType) {
        this.validateType = validateType;
    }
    
}
