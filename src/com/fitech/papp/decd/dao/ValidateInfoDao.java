package com.fitech.papp.decd.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.ValidateInfo;
/**
 * 校验设置Dao
 * @author zhoufeng
 *
 */
@Service(value="validateInfoDao")
public interface ValidateInfoDao {
    public List<ValidateInfo> findValidateInfoList();
    public List<ValidateInfo> findValidateInfoListByType(Integer validateType);
}
