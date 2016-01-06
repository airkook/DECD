package com.fitech.papp.decd.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.ValidateInfoDao;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.model.vo.ValidateInfoVo;
import com.fitech.papp.decd.util.Constants;

@Service(value = "validateInfoDao")
public class ValidateDaoImpl extends DefaultBaseDao<ValidateInfo, Integer> implements ValidateInfoDao{


    /**
     * 查询所有检验规则记录
     */
    @Override
    public List<ValidateInfo> findValidateInfoList()
    {
        String hsql = "from ValidateInfo order by validateInfoId asc";
        List<ValidateInfo> list=this.findListByHsql(hsql, null);
        return list;
    }

    /**
     * 查询指定类型的检验规则记录
     */
    @Override
    public List<ValidateInfo> findValidateInfoListByType(Integer validateType) {
        String hsql = "from ValidateInfo where validateType="+validateType+"order by validateInfoId asc";
        List<ValidateInfo> list=this.findListByHsql(hsql, null);
        return list;
    }

}
