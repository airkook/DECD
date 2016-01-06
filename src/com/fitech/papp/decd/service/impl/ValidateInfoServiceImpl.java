package com.fitech.papp.decd.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.papp.decd.dao.ValidateInfoDao;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.service.ValidateInfoService;
/**
 * 校验规则service实现类
 * @author zhoufeng
 *
 */
@Service(value = "validateInfoService")
public class ValidateInfoServiceImpl implements ValidateInfoService {
    @Autowired
    private ValidateInfoDao validateInfoDao;
    @Override
    public List<ValidateInfo> findValidateInfoList() {
        return validateInfoDao.findValidateInfoList();
    }
    @Override
    public List<ValidateInfo> findValidateInfoListByType(Integer validateType) {
        return validateInfoDao.findValidateInfoListByType(validateType);
    }
    
}
