package com.fitech.papp.decd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.ValidateInfo;

/**
 * 校验设置service
 * @author zhoufeng
 *
 */
@Service(value = "validateInfoService")
public interface ValidateInfoService {
    public List<ValidateInfo> findValidateInfoList();
    public List<ValidateInfo> findValidateInfoListByType(Integer validateType);
}
