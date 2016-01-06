/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.CdsManagerDao;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.CdsValidateResultVo;
import com.fitech.papp.decd.model.vo.CdsVo;
import com.fitech.papp.decd.util.Constants;

/**
 * 大额存单数据维护dao
 * 
 * @author xyf
 * 
 */
@Service(value = "cdsManagerDao")
public class CdsManagerDaoImpl extends DefaultBaseDao<CertificatesOfDeposit, Integer> implements CdsManagerDao {

    /** 同步锁 **/
    private static Object lock = new Object();

    /**
     * 分页查询
     * 
     * @param paramMap
     * @param pageSize
     * @param pageNo
     * @return
     * @throws BaseDaoException
     */
    @Override
    public PageResults findCdsListByPage(Map<String, String> paramMap, int pageSize, int pageNo)
            throws BaseDaoException {
        // 检查这期数据是否合计校验通过
        Boolean termCheckFlag = this.checkTermValidate(paramMap.get("term"));

        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("select cd.ID,cd.CPDM,cd.JXFS,cd.CPQX,cd.FXDX,cd.QDJE,cd.STATUS,cd.VALIDATE_STATUS");
        sqlsb.append(" from CERTIFICATES_OF_DEPOSIT cd ");
        String tmp = " where ";
        // 业务发生日即为期数
        if (!StringUtil.isEmpty(paramMap.get("term"))) {
            sqlsb.append(tmp).append(" cd.TERM = '").append(paramMap.get("term")).append("'");
            tmp = " and ";
        }
        // 产品代码
        if (!StringUtil.isEmpty(paramMap.get("cpdm"))) {
            sqlsb.append(tmp).append(" cd.CPDM like '%").append(paramMap.get("cpdm")).append("%'");
            tmp = " and ";
        }
        // 计息方式
        if (!StringUtil.isEmpty(paramMap.get("jxfs"))) {
            sqlsb.append(tmp).append(" cd.JXFS = '").append(paramMap.get("jxfs")).append("'");
            tmp = " and ";
        }
        // 校验状态
        if (!StringUtil.isEmpty(paramMap.get("validateStatus"))) {
            sqlsb.append(tmp).append(" cd.VALIDATE_STATUS = '").append(paramMap.get("validateStatus")).append("'");
            tmp = " and ";
        }
        // 数据状态
        if (!StringUtil.isEmpty(paramMap.get("status"))) {
            sqlsb.append(tmp).append(" cd.STATUS = '").append(paramMap.get("status")).append("'");
            tmp = " and ";
        }
        else {
            sqlsb.append(tmp).append(" cd.STATUS <> '").append(Constants.CDS_STATUS_DEL).append("'");
            tmp = " and ";
        }
        sqlsb.append(" order by cd.ID ");
        PageResults results = this.findPageBySql(sqlsb.toString(), null, pageSize, pageNo);
        List<Object[]> resultList = results.getResults();
        List<CdsVo> dataList = new ArrayList<CdsVo>();
        for (Object[] obj : resultList) {
            CdsVo cdsVo = new CdsVo();
            cdsVo.setId(((BigDecimal) obj[0]).intValue());
            cdsVo.setCpdm((String) obj[1]);
            cdsVo.setJxfs((String) obj[2]);
            cdsVo.setCpqx((String) obj[3]);
            cdsVo.setFxdx((String) obj[4]);
            cdsVo.setQdje((String) obj[5]);
            cdsVo.setStatus((String) obj[6]);
            if (Constants.CDS_STATUS_NORMAL.equals(cdsVo.getStatus())) {
                cdsVo.setStatusDesc("正常");
            }
            else if (Constants.CDS_STATUS_EDIT.equals(cdsVo.getStatus())) {
                cdsVo.setStatusDesc("补录");
            }
            else if (Constants.CDS_STATUS_REVIEW.equals(cdsVo.getStatus())) {
                cdsVo.setStatusDesc("提交审核");
            }
            else if (Constants.CDS_STATUS_REVIEW_UNPASS.equals(cdsVo.getStatus())) {
                cdsVo.setStatusDesc("审核不通过");
            }
            else if (Constants.CDS_STATUS_REVIEW_PASS.equals(cdsVo.getStatus())) {
                cdsVo.setStatusDesc("审核通过");
            }
            else {
                cdsVo.setStatusDesc("正常");
            }
            cdsVo.setValidateStatus((String) obj[7]);
            if (Constants.VALIDATE_STATUS_PASS.equals(cdsVo.getValidateStatus())) {
                cdsVo.setValidateStatusDesc("校验通过");
            }
            else if (Constants.VALIDATE_STATUS_UNPASS.equals(cdsVo.getValidateStatus())) {
                cdsVo.setValidateStatusDesc("校验不通过");
            }
            else {
                cdsVo.setValidateStatusDesc("未校验");
            }
            // 如果整期合计校验不通过
            if (!termCheckFlag) {
                cdsVo.setSumValidateStatusDesc("(合计不通过)");
            }
            dataList.add(cdsVo);
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

    /**
     * 获取所有数据字典
     * 
     * @return
     */
    @Override
    public List<Object[]> findCodeLibList() {
        String sql = "select CODE_ID,STANDARD_LIB_CODE,STANDARD_LIB_NAME from CODE_LIB_STANDARD order by CODE_ID,STANDARD_LIB_CODE ";
        return this.findListBySql(sql, null);
    }

    /**
     * 获取数据字典
     * 
     * @return
     */
    @Override
    public List<Object[]> findCodeLibList(String codeId) {
        String sql = "select CODE_ID,STANDARD_LIB_CODE,STANDARD_LIB_NAME from CODE_LIB_STANDARD where CODE_ID = '"
                + codeId + "' order by STANDARD_LIB_CODE ";
        return this.findListBySql(sql, null);
    }

    /**
     * 更新数据为提交审核状态
     * 
     * @param ids
     * @throws BaseDaoException
     */
    @Override
    public void updateReviewStatus(String ids) throws BaseDaoException {
        // 只有正常和修改以及审核不通过状态的数据可以提交审核
        String sql = "update CERTIFICATES_OF_DEPOSIT set STATUS = '" + Constants.CDS_STATUS_REVIEW + "' where ID in("
                + ids + ") and STATUS in ('" + Constants.CDS_STATUS_NORMAL + "','" + Constants.CDS_STATUS_EDIT + "','"
                + Constants.CDS_STATUS_REVIEW_UNPASS + "') ";
        this.updateBysql(sql);
    }

    /**
     * 更新数据为审核通过状态
     * 
     * @param term
     * @throws BaseDaoException
     */
    @Override
    public void updateReviewPassStatus(String term) throws BaseDaoException {
        String sql = "update CERTIFICATES_OF_DEPOSIT set STATUS = '" + Constants.CDS_STATUS_REVIEW_PASS
                + "' where TERM ='" + term + "' and STATUS <> '" + Constants.CDS_STATUS_DEL + "' ";
        this.updateBysql(sql);
    }

    /**
     * 检查产品代码是否已存在
     * 
     * @param certificatesOfDeposit
     * @return
     */
    @Override
    public Boolean checkCpdmExist(CertificatesOfDeposit certificatesOfDeposit) {
        StringBuilder hsqlsb = new StringBuilder();
        hsqlsb.append("from CertificatesOfDeposit where cpdm = '").append(certificatesOfDeposit.getCpdm()).append("' ");
        hsqlsb.append(" and status <> '").append(Constants.CDS_STATUS_DEL).append("' ");
        hsqlsb.append(" and term = '").append(certificatesOfDeposit.getYwfsr()).append("' ");
        if (certificatesOfDeposit.getId() != null) {
            hsqlsb.append(" and id <> ").append(certificatesOfDeposit.getId());
        }
        CertificatesOfDeposit cd = (CertificatesOfDeposit) this.findObject(hsqlsb.toString());
        if (cd != null) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 查询数据
     * 
     * @param paramMap
     * @return
     */
    @Override
    public List<CertificatesOfDeposit> findCdsList(Map<String, String> paramMap) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append(" from CertificatesOfDeposit cd ");
        String tmp = " where ";
        // 业务发生日即为期数
        if (!StringUtil.isEmpty(paramMap.get("term"))) {
            sqlsb.append(tmp).append(" cd.term = '").append(paramMap.get("term")).append("'");
            tmp = " and ";
        }
        // 产品代码
        if (!StringUtil.isEmpty(paramMap.get("cpdm"))) {
            sqlsb.append(tmp).append(" cd.cpdm like '%").append(paramMap.get("cpdm")).append("%'");
            tmp = " and ";
        }
        // 计息方式
        if (!StringUtil.isEmpty(paramMap.get("jxfs"))) {
            sqlsb.append(tmp).append(" cd.jxfs = '").append(paramMap.get("jxfs")).append("'");
            tmp = " and ";
        }
        // 校验状态
        if (!StringUtil.isEmpty(paramMap.get("validateStatus"))) {
            sqlsb.append(tmp).append(" cd.validateStatus = '").append(paramMap.get("validateStatus")).append("'");
            tmp = " and ";
        }
        // 数据状态
        if (!StringUtil.isEmpty(paramMap.get("status"))) {
            sqlsb.append(tmp).append(" cd.status = '").append(paramMap.get("status")).append("'");
            tmp = " and ";
        }
        else {
            sqlsb.append(tmp).append(" cd.status <> '").append(Constants.CDS_STATUS_DEL).append("'");
            tmp = " and ";
        }
        sqlsb.append(" order by cd.id ");
        List<CertificatesOfDeposit> resultList = this.findListByHsql(sqlsb.toString(), null);
        this.getSession().clear();
        return resultList;
    }

    /**
     * 更新数据为删除状态
     * 
     * @param ids
     * @throws BaseDaoException
     */
    @Override
    public void updateDeleteStatus(String ids) throws BaseDaoException {
        String sql = "update CERTIFICATES_OF_DEPOSIT set STATUS = '" + Constants.CDS_STATUS_DEL + "' where ID in("
                + ids + ") ";
        this.updateBysql(sql);
    }

    /**
     * 根据产品代码和业务发生日来获取数据
     * 
     * @param cpdm
     * @param ywfsr
     * @return
     */
    @Override
    public CertificatesOfDeposit findCdsByCpdmYwfsr(String cpdm, String ywfsr) {
        String hsql = " from CertificatesOfDeposit where cpdm = '" + cpdm + "' and ywfsr = '" + ywfsr + "' ";
        CertificatesOfDeposit cd = (CertificatesOfDeposit) this.findObject(hsql);
        this.getSession().clear();
        return cd;
    }

    /**
     * 根据期数查上报记录
     * 
     * @param term
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReportResultInfo> findReportResultInfoList(String term) {
        String hsql = " from ReportResultInfo where term = '" + term + "' ";
        List<ReportResultInfo> list = this.findListByHsql(hsql, null);
        this.getSession().clear();
        return list;
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
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("select vr.VALIDATE_RESULT_ID,vi.VALIDATE_DESC,vi.VALIDATE_TYPE,");
        sqlsb.append("vr.VALIDAET_DATA,vr.VALIDATE_RESULT_FLAG");
        sqlsb.append(" from VALIDATE_RESULT vr ");
        sqlsb.append(" left join VALIDATE_INFO vi on vr.VALIDATE_INFO_ID = vi.VALIDATE_INFO_ID ");
        sqlsb.append(" where vr.CDS_ID in (").append(cdsId).append(")");
        sqlsb.append(" or (vr.TERM = '").append(term).append("' and vr.VALIDATE_TYPE = '").append(
                Constants.VALIDATE_TYPE_TERM).append("') ");
        sqlsb.append(" order by vr.VALIDATE_RESULT_FLAG,vi.VALIDATE_TYPE desc ");
        List<Object[]> resultList = this.findListBySql(sqlsb.toString(), null);
        List<CdsValidateResultVo> vrList = new ArrayList<CdsValidateResultVo>();
        for (Object[] obj : resultList) {
            CdsValidateResultVo vo = new CdsValidateResultVo();
            vo.setValidateResultId(((BigDecimal) obj[0]).intValue());
            vo.setValidateDesc((String) obj[1]);
            vo.setValidateType((String) obj[2]);
            vo.setValidaetData((String) obj[3]);
            vo.setValidateResultFlag((String) obj[4]);
            if (Constants.VALIDATE_TYPE_BASE.equals(vo.getValidateType())) {
                vo.setValidateTypeDesc("基本");
            }
            else if (Constants.VALIDATE_TYPE_TABLE_INSIDE.equals(vo.getValidateType())) {
                vo.setValidateTypeDesc("表内");
            }
            else if (Constants.VALIDATE_TYPE_TABLE_EACH.equals(vo.getValidateType())) {
                vo.setValidateTypeDesc("表间");
            }
            else if (Constants.VALIDATE_TYPE_TERM.equals(vo.getValidateType())) {
                vo.setValidateTypeDesc("合计");
            }
            if (Constants.VALIDATE_RESULT_PASS.equals(vo.getValidateResultFlag())) {
                vo.setValidateResultFlagDesc("校验通过");
            }
            else if (Constants.VALIDATE_RESULT_UNPASS.equals(vo.getValidateResultFlag())) {
                vo.setValidateResultFlagDesc("校验不通过");
            }
            vrList.add(vo);
        }
        return vrList;
    }

    /**
     * 根据期数查询校验不通过的结果
     * 
     * @param term
     * @return
     */
    @Override
    public List<ValidateResult> findUnPassValiResList(String term) {
        String hsql = "from ValidateResult where term = '" + term + "' and validateResultFlag = '"
                + Constants.VALIDATE_RESULT_UNPASS + "' ";
        List<ValidateResult> list = this.findListByHsql(hsql, null);
        this.getSession().clear();
        return list;
    }

    /**
     * 生成并获取主键ID
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public String findCdsId() {
        synchronized (lock) {
            String sql = "select SEQ_CD.nextval from dual ";
            List<Object> list = this.findListBySql(sql, null);
            if (list != null && list.size() > 0) {
                return String.valueOf(list.get(0));
            }
            return null;
        }
    }

    /**
     * 批量插入数据
     * 
     * @param list
     * @throws Exception
     */
    @Override
    public void batchInsertCds(List<String[]> list) throws Exception {
        if (list == null || list.size() == 0) {
            return;
        }
        final int batchSize = 1000;
        int count = 0;
        String sql = "insert into CERTIFICATES_OF_DEPOSIT "
                + "(ID,FXRQC,FXRZH,CPDM,YWFSR,FXQSR,FXZZR,JHFXZL,DRFXJE,LJFXJE,CPQX,"
                + "FXDX,QDJE,JXFS,JZLLZL,LC,CSFXLL,FXPL,SFKTQZC,SFKSH,DRTQZCJE,LJTQZCJE,DRSHJE,LJSHJE,"
                + "DRDFJE,LJDFJE,MQCDYE,YEZJ,TERM,STATUS,VALIDATE_STATUS,UPDATE_USER_ID) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SessionFactory sessionFactory = null;
        Session session = null;
        Connection con = null;
        PreparedStatement ps = null;
        if (sessionFactory == null) {
            sessionFactory = getSessionFactory();
        }
        try {
            try {
                session = sessionFactory.getCurrentSession();
            }
            catch (Exception e) {
                session = sessionFactory.openSession();
            }
            con = session.connection();
            ps = con.prepareStatement(sql);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    ps.setObject(j + 1, list.get(i)[j]);
                }
                ps.addBatch();
                if (++count % batchSize == 0) {// 每隔1000条数据批量执行一次，以防止内存溢出
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            con.commit();
        }
        catch (Exception e) {
            try {
                con.rollback();
            }
            catch (Exception e2) {
            }
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * 批量更新数据
     * 
     * @param list
     * @throws Exception
     */
    @Override
    public void batchUpdateCds(List<String[]> list) throws Exception {
        if (list == null || list.size() == 0) {
            return;
        }
        final int batchSize = 1000;
        int count = 0;
        String sql = "update CERTIFICATES_OF_DEPOSIT set FXRQC=?, FXRZH=?, CPDM=?, YWFSR=?, FXQSR=?,"
                + " FXZZR=?, JHFXZL=?, DRFXJE=?, LJFXJE=?, CPQX=?, FXDX=?, QDJE=?, JXFS=?,"
                + " JZLLZL=?, LC=?, CSFXLL=?, FXPL=?, SFKTQZC=?, SFKSH=?, DRTQZCJE=?,"
                + " LJTQZCJE=?, DRSHJE=?, LJSHJE=?, DRDFJE=?, LJDFJE=?, MQCDYE=?, YEZJ=?,"
                + " TERM=?, STATUS=?, VALIDATE_STATUS=?, UPDATE_USER_ID=? where ID=?";
        SessionFactory sessionFactory = null;
        Session session = null;
        Connection con = null;
        PreparedStatement ps = null;
        if (sessionFactory == null) {
            sessionFactory = getSessionFactory();
        }
        try {
            try {
                session = sessionFactory.getCurrentSession();
            }
            catch (Exception e) {
                session = sessionFactory.openSession();
            }
            con = session.connection();
            ps = con.prepareStatement(sql);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    ps.setObject(j + 1, list.get(i)[j]);
                }
                ps.addBatch();
                if (++count % batchSize == 0) {// 每隔1000条数据批量执行一次，以防止内存溢出
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            con.commit();
        }
        catch (Exception e) {
            try {
                con.rollback();
            }
            catch (Exception e2) {
            }
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * 批量更新校验状态
     * 
     * @param list
     * @throws Exception
     */
    @Override
    public void batchUpdateValidateStatus(List<String[]> list) throws Exception {
        if (list == null || list.size() == 0) {
            return;
        }
        final int batchSize = 1000;
        int count = 0;
        String sql = "update CERTIFICATES_OF_DEPOSIT set VALIDATE_STATUS=? where ID=?";
        SessionFactory sessionFactory = null;
        Session session = null;
        Connection con = null;
        PreparedStatement ps = null;
        if (sessionFactory == null) {
            sessionFactory = getSessionFactory();
        }
        try {
            try {
                session = sessionFactory.getCurrentSession();
            }
            catch (Exception e) {
                session = sessionFactory.openSession();
            }
            con = session.connection();
            ps = con.prepareStatement(sql);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    ps.setObject(j + 1, list.get(i)[j]);
                }
                ps.addBatch();
                if (++count % batchSize == 0) {// 每隔1000条数据批量执行一次，以防止内存溢出
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            con.commit();
        }
        catch (Exception e) {
            try {
                con.rollback();
            }
            catch (Exception e2) {
            }
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * 根据ids获取数据
     * 
     * @param ids
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CertificatesOfDeposit> findCdsList(String ids) {
        String hsql = " from CertificatesOfDeposit where id in (" + ids + ") ";
        List<CertificatesOfDeposit> list = this.findListByHsql(hsql, null);
        this.getSession().clear();
        return list;
    }

    /**
     * 获取所有校验信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ValidateInfo> findValidateInfoList() {
        String hsql = " from ValidateInfo ";
        List<ValidateInfo> list = this.findListByHsql(hsql, null);
        this.getSession().clear();
        return list;
    }

    /**
     * 删除校验结果
     * 
     * @param cdsIds
     * @param term
     * @throws BaseDaoException
     */
    @Override
    public void delValidateResult(String cdsIds, String term) throws BaseDaoException {
        String sql = "";
        if (StringUtil.isEmpty(cdsIds)) {
            sql = "delete from VALIDATE_RESULT where TERM = '" + term + "' ";
        }
        else {
            sql = "delete from VALIDATE_RESULT where CDS_ID in (" + cdsIds + ") or (TERM = '" + term
                    + "' and VALIDATE_TYPE = '" + Constants.VALIDATE_TYPE_TERM + "')";
        }
        this.updateBysql(sql);
    }

    /**
     * 删除校验结果
     * 
     * @param cdsId
     * @throws BaseDaoException
     */
    @Override
    public void delValidateResult(String cdsId) throws BaseDaoException {
        String sql = "delete from VALIDATE_RESULT where CDS_ID = " + cdsId + "";
        this.updateBysql(sql);
    }

    /**
     * 批量插入校验结果
     * 
     * @param validateResultList
     * @throws Exception
     */
    @Override
    public void batchInsertValidateResult(List<ValidateResult> validateResultList) throws Exception {
        if (validateResultList == null || validateResultList.size() == 0) {
            return;
        }
        final int batchSize = 1000;
        int count = 0;
        String sql = "INSERT INTO VALIDATE_RESULT (VALIDATE_RESULT_ID, CDS_ID, VALIDATE_INFO_ID, VALIDAET_DATA, VALIDATE_RESULT_FLAG, TERM, VALIDATE_TYPE) VALUES (SEQ_VALIDATE_RESULT.nextval, ?, ?, ?, ?, ?, ?)";
        SessionFactory sessionFactory = null;
        Session session = null;
        Connection con = null;
        PreparedStatement ps = null;
        if (sessionFactory == null) {
            sessionFactory = getSessionFactory();
        }
        try {
            try {
                session = sessionFactory.getCurrentSession();
            }
            catch (Exception e) {
                session = sessionFactory.openSession();
            }
            con = session.connection();
            ps = con.prepareStatement(sql);
            for (int i = 0; i < validateResultList.size(); i++) {
                ps.setObject(1, validateResultList.get(i).getCdsId());
                ps.setObject(2, validateResultList.get(i).getValidateInfoId());
                ps.setObject(3, validateResultList.get(i).getValidaetData());
                ps.setObject(4, validateResultList.get(i).getValidateResultFlag());
                ps.setObject(5, validateResultList.get(i).getTerm());
                ps.setObject(6, validateResultList.get(i).getValidateType());
                ps.addBatch();
                if (++count % batchSize == 0) {// 每隔1000条数据批量执行一次，以防止内存溢出
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            con.commit();
        }
        catch (Exception e) {
            try {
                con.rollback();
            }
            catch (Exception e2) {
            }
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * 根据期数查上报记录
     * 
     * @param term
     * @return
     * @throws BaseDaoException
     */
    @Override
    public void saveCds(CertificatesOfDeposit certificatesOfDeposit) throws BaseDaoException {
        this.saveOrUpdate(certificatesOfDeposit);
        this.getSession().flush();
    }
}
