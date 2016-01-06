/**
 * fitech
 * xyf
 */
package com.fitech.papp.decd.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.service.IBaseService;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.CertificatesOfDeposit;
import com.fitech.papp.decd.model.pojo.ValidateResult;
import com.fitech.papp.decd.model.vo.CdsValidateResultVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;

/**
 * 大额存单数据维护service
 * 
 * @author xyf
 * 
 */
@Service(value = "cdsManagerService")
public interface CdsManagerService extends IBaseService<CertificatesOfDeposit, Integer> {

    /**
     * 分页查询
     * 
     * @param paramMap
     * @param pageSize
     * @param pageNo
     * @return
     * @throws Exception
     */
    public PageResults findCdsListByPage(Map<String, String> paramMap, int pageSize, int pageNo) throws Exception;

    /**
     * 根据id获取大额存单详细信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public CertificatesOfDeposit findCdsById(String id) throws Exception;

    /**
     * 获取数据字典
     * 
     * @return
     */
    public Map<String, Map<String, String>> findCodeLibMap();

    /**
     * 获取数据字典
     * 
     * @return
     */
    public Map<String, String> findCodeLibMap(String codeId);

    /**
     * 保存增加
     * 
     * @param certificatesOfDeposit
     * @param loginUserInfo
     * @return
     * @throws Exception
     */
    public String saveAdd(CertificatesOfDeposit certificatesOfDeposit, UserInfoVo loginUserInfo) throws Exception;

    /**
     * 保存修改
     * 
     * @param certificatesOfDeposit
     * @param loginUserInfo
     * @return
     * @throws Exception
     */
    public String saveEdit(CertificatesOfDeposit certificatesOfDeposit, UserInfoVo loginUserInfo) throws Exception;

    /**
     * 删除
     * 
     * @param id
     * @param term
     * @throws Exception
     */
    public void deleteById(String id, String term) throws Exception;

    /**
     * 提交审核
     * 
     * @param checkBox
     * @throws Exception
     */
    public void submitCheck(String[] checkBox) throws Exception;

    /**
     * 下载
     * 
     * @param response
     * @param paramMap
     * @throws Exception
     */
    public void downLoad(HttpServletResponse response, Map<String, String> paramMap) throws Exception;

    /**
     * 导入
     * 
     * @param excelFile
     * @param excelFileName
     * @param userInfo
     * @return
     * @throws Exception
     */
    public String uploadData(File excelFile, String excelFileName, UserInfoVo userInfo) throws Exception;

    /**
     * 校验数据
     * 
     * @param ids
     * @throws Exception
     */
    public void validateData(String ids) throws Exception;

    /**
     * 校验数据
     * 
     * @param ids
     * @throws Exception
     */
    public void validateData(List<CertificatesOfDeposit> cdsList) throws Exception;

    /**
     * 校验当期数据
     * 
     * @param paramMap
     * @throws Exception
     */
    public void validateData(Map<String, String> paramMap) throws Exception;

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
     * 更新数据为审核通过状态
     * 
     * @param term
     * @throws Exception
     */
    public void updateReviewPassStatus(String term) throws Exception;
}
