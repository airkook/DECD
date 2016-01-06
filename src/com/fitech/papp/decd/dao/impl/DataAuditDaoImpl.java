package com.fitech.papp.decd.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.DataAuditDao;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.CertificatesOfDepositVo;
import com.fitech.papp.decd.util.Constants;

/**
 * 数据审核dao实现
 * 
 * @author wupengzheng
 * 
 */

@Service(value = "dataAuditDao")
public class DataAuditDaoImpl extends DefaultBaseDao<CertificatesOfDeposit, Integer> implements DataAuditDao {

    @Override
    public List<CertificatesOfDeposit> findCertificatesOfDepositList() {
        String hql = "from certificates_of_deposit order by id";
        return this.findListByHsql(hql, null);
    }

    @Override
    public PageResults findCertificatesOfDepositInfo(Map<String, String> paramMap, int pageSize, int pageNo)
            throws BaseDaoException {
        String cpdm = paramMap.get("cpdm");
        String term = paramMap.get("term");
        String jxfs = paramMap.get("jxfs");
        String status = paramMap.get("status");
        String validateStatus = paramMap.get("validateStatus");

        // 检查这期数据是否合计校验通过
        Boolean termCheckFlag = this.checkTermValidate(term);

        StringBuilder sql = new StringBuilder();
        sql.append("select t.id,t.cpdm,t.jxfs,t.cpqx,t.fxdx,t.qdje,u.user_name,t.validate_status ");
        sql.append(" from certificates_of_deposit t left join user_info u on t.update_user_id = user_id");
        sql.append(" where 1=1 ");
        // 根据产品代码查询
        if (!StringUtil.isEmpty(cpdm)) {
            sql.append(" and t.cpdm  = '").append(cpdm).append("'");
        }
        // 根据期数查询（业务发生日即为期数）
        if (!StringUtil.isEmpty(term)) {
            sql.append(" and t.term  = '").append(term).append("'");
        }
        // 根据计息方式查询
        if (!StringUtil.isEmpty(jxfs)) {
            sql.append(" and t.jxfs  = '").append(jxfs).append("'");
        }
        // 根据校验状态查询
        if (!StringUtil.isEmpty(validateStatus)) {
            sql.append(" and t.validate_status  = '").append(validateStatus).append("'");
        }
        // 根据数据状态查询
        if (!StringUtil.isEmpty(status)) {
            sql.append(" and t.status  = '").append(status).append("'");
        }
        else {
            sql.append(" and t.status  = '").append(Constants.CDS_STATUS_REVIEW).append("'");
        }
        sql.append(" order by t.id ");
        PageResults<CertificatesOfDepositVo> pageResults = null;
        // 查询结果
        PageResults results = this.findPageBySql(sql.toString(), null, pageSize, pageNo);

        List<Object[]> resultList = results.getResults();

        List<CertificatesOfDepositVo> dataList = new ArrayList<CertificatesOfDepositVo>();

        for (Object[] obj : resultList) {
            CertificatesOfDepositVo codVo = new CertificatesOfDepositVo();
            codVo.setId(((BigDecimal) obj[0]).intValue());
            codVo.setCpdm((String) obj[1]);
            codVo.setJxfs((String) obj[2]);
            codVo.setCpqx((String) obj[3]);
            codVo.setFxdx((String) obj[4]);
            codVo.setQdje((String) obj[5]);
            codVo.setUpdateUserName(((String) obj[6]));
            codVo.setValidateStatus((String) obj[7]);
            if (Constants.VALIDATE_STATUS_PASS.equals(codVo.getValidateStatus())) {
                codVo.setValidateStatusDesc("校验通过");
            }
            else if (Constants.VALIDATE_STATUS_UNPASS.equals(codVo.getValidateStatus())) {
                codVo.setValidateStatusDesc("校验不通过");
            }
            else {
                codVo.setValidateStatusDesc("未校验");
            }
            // 如果整期合计校验不通过
            if (!termCheckFlag) {
                codVo.setSumValidateStatusDesc("(合计不通过)");
            }
            dataList.add(codVo);
        }
        results.setResults(dataList);
        return results;
    }

    /**
     * 检查这期数据是否合计校验通过
     * 
     * @param term
     * @return
     */
    @SuppressWarnings("unchecked")
    private Boolean checkTermValidate(String term) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("term", term);
        paramMap.put("validateType", Constants.VALIDATE_TYPE_TERM);
        paramMap.put("validateResultFlag", Constants.VALIDATE_RESULT_UNPASS);
        String hsql = " from ValidateResult where term=:term and validateType=:validateType and validateResultFlag=:validateResultFlag ";
        List<ValidateResult> list = this.findListByHsql(hsql, paramMap);
        if (list != null && list.size() > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void updateDataAuditPass(Map<String, String> paramMap) throws BaseDaoException {
        String ids = paramMap.get("ids");
        String sql = "update CERTIFICATES_OF_DEPOSIT t set t.status = " + Constants.CDS_STATUS_REVIEW_PASS
                + " where t.id in (" + ids + ") ";
        this.updateBysql(sql);
    }

    @Override
    public void updateDataAuditNoPass(Map<String, String> paramMap) throws BaseDaoException {
        String ids = paramMap.get("ids");
        String sql = "update CERTIFICATES_OF_DEPOSIT t set t.status = " + Constants.CDS_STATUS_REVIEW_UNPASS
                + " where t.id in (" + ids + ") ";
        this.updateBysql(sql);
    }

}
