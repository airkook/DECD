/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.IBaseDao;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;
import com.fitech.papp.decd.model.pojo.ValidateInfo;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.CdsValidateResultVo;

/**
 * 大额存单数据维护dao
 * 
 * @author xyf
 * 
 */
@Service(value = "cdsManagerDao")
public interface CdsManagerDao extends IBaseDao<CertificatesOfDeposit, Integer> {

    /**
     * 分页查询
     * 
     * @param paramMap
     * @param pageSize
     * @param pageNo
     * @return
     * @throws BaseDaoException
     */
    public PageResults findCdsListByPage(Map<String, String> paramMap, int pageSize, int pageNo)
            throws BaseDaoException;

    /**
     * 获取所有数据字典
     * 
     * @return
     */
    public List<Object[]> findCodeLibList();

    /**
     * 获取数据字典
     * 
     * @return
     */
    public List<Object[]> findCodeLibList(String codeId);

    /**
     * 更新数据为提交审核状态
     * 
     * @param ids
     * @throws BaseDaoException
     */
    public void updateReviewStatus(String ids) throws BaseDaoException;

    /**
     * 更新数据为审核通过状态
     * 
     * @param term
     * @throws BaseDaoException
     */
    public void updateReviewPassStatus(String term) throws BaseDaoException;

    /**
     * 查询数据
     * 
     * @param paramMap
     * @return
     */
    public List<CertificatesOfDeposit> findCdsList(Map<String, String> paramMap);

    /**
     * 更新数据为删除状态
     * 
     * @param ids
     * @throws BaseDaoException
     */
    public void updateDeleteStatus(String ids) throws BaseDaoException;

    /**
     * 根据产品代码和业务发生日来获取数据
     * 
     * @param cpdm
     * @param ywfsr
     * @return
     */
    public CertificatesOfDeposit findCdsByCpdmYwfsr(String cpdm, String ywfsr);

    /**
     * 批量插入数据
     * 
     * @param list
     * @throws Exception
     */
    public void batchInsertCds(List<String[]> list) throws Exception;

    /**
     * 批量更新数据
     * 
     * @param list
     * @throws Exception
     */
    public void batchUpdateCds(List<String[]> list) throws Exception;

    /**
     * 批量更新校验状态
     * 
     * @param list
     * @throws Exception
     */
    public void batchUpdateValidateStatus(List<String[]> list) throws Exception;

    /**
     * 生成并获取主键ID
     * 
     * @return
     */
    public String findCdsId();

    /**
     * 根据ids获取数据
     * 
     * @param ids
     * @return
     */
    public List<CertificatesOfDeposit> findCdsList(String ids);

    /**
     * 获取所有校验信息
     * 
     * @return
     */
    public List<ValidateInfo> findValidateInfoList();

    /**
     * 删除校验结果
     * 
     * @param cdsIds
     * @param term
     * @throws BaseDaoException
     */
    public void delValidateResult(String cdsIds, String term) throws BaseDaoException;

    /**
     * 删除校验结果
     * 
     * @param cdsId
     * @throws BaseDaoException
     */
    public void delValidateResult(String cdsId) throws BaseDaoException;

    /**
     * 批量插入校验结果
     * 
     * @param validateResultList
     * @throws Exception
     */
    public void batchInsertValidateResult(List<ValidateResult> validateResultList) throws Exception;

    /**
     * 查询校验结果
     * 
     * @param cdsId
     * @param term
     * @return
     */
    public List<CdsValidateResultVo> findValidateResultList(String cdsId, String term);

    /**
     * 根据期数查询校验不通过的结果
     * 
     * @param term
     * @return
     */
    public List<ValidateResult> findUnPassValiResList(String term);

    /**
     * 检查产品代码是否已存在
     * 
     * @param certificatesOfDeposit
     * @return
     */
    public Boolean checkCpdmExist(CertificatesOfDeposit certificatesOfDeposit);

    /**
     * 根据期数查上报记录
     * 
     * @param term
     * @return
     */
    public List<ReportResultInfo> findReportResultInfoList(String term);

    /**
     * 根据期数查上报记录
     * 
     * @param term
     * @return
     * @throws BaseDaoException
     */
    public void saveCds(CertificatesOfDeposit certificatesOfDeposit) throws BaseDaoException;
}
