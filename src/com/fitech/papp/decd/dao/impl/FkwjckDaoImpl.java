package com.fitech.papp.decd.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.util.StringUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.FkwjckDao;
import com.fitech.papp.decd.model.pojo.FeedbackInfo;

/**
 * 反馈文件查看dao实现类
 * 
 * @author xujj
 * 
 */
@Service(value = "fkwjckDao")
public class FkwjckDaoImpl extends DefaultBaseDao<FeedbackInfo, Integer> implements FkwjckDao {

    /*
     * 反馈文件查看 查寻
     */
    @Override
    public PageResults select(Map<String, String> param, int pageNo, int pageSize, int reportOrgID) throws Exception {
        String hql = "from FeedbackInfo where reportOrgId='" + reportOrgID + "'";
        String cpdm = param.get("cpdm");
        String term = param.get("term");
        if (!StringUtil.isEmpty(cpdm)) {
            hql = hql + " and cpdm like '%" + cpdm + "%'";
        }
        if (!StringUtil.isEmpty(term)) {
            hql = hql + " and term='" + term + "'";
        }
        PageResults pr = this.findPageByHsql(hql, param, pageSize, pageNo);
        return pr;
    }

    /*
     * 查询id
     */
    @Override
    public String searchId(String term, String cpdm) throws Exception {
        String id = "";
        String sql = " select ID from CERTIFICATES_OF_DEPOSIT where TERM='" + term + "' and CPDM='" + cpdm + "'";
        List list = this.findListBySql(sql, null);
        if (!list.isEmpty()) {
            id = list.get(0).toString();
        }
        return id;
    }
}
