package com.fitech.papp.decd.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.SubmitRecordsDao;
import com.fitech.papp.decd.model.pojo.ReportOrgInfo;
import com.fitech.papp.decd.model.pojo.ReportResultInfo;

/**
 * 报送记录查看dao
 * 
 * @author songfei
 * */
@Service(value = "submitRecordsDao")
public class SubmitRecordsDaoImpl extends DefaultBaseDao<ReportResultInfo, Integer> implements SubmitRecordsDao {

    @Override
    public PageResults findByPage(Map<String, String> param, int pageSize, int pageNo) {
        PageResults pageResults = null;
        String hql = ("from ReportResultInfo where 1=1 ");
        try {
            if (param != null) {
                if (!StringUtil.isEmpty(param.get("reportOrgId"))) {
                    hql = hql + (" and reportOrgId like '%" + param.get("reportOrgId") + "' ");
                }
                // 开始期数
                if (!StringUtil.isEmpty(param.get("startTerm"))) {
                    hql = hql + (" and term >= '" + param.get("startTerm").trim() + "'");
                }
                // 结束期数
                if (!StringUtil.isEmpty(param.get("endTerm"))) {
                    hql = hql + (" and term <= '" + param.get("endTerm").trim() + "'");
                }
                hql = hql + "order by term desc,reportOrgId";
                pageResults = this.findPageByHsql(hql, null, pageSize, pageNo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pageResults;
    }

    /** 所有的报送机构 */
    public List findReportOrgInfo() {
        List<ReportOrgInfo> list = new ArrayList<ReportOrgInfo>();
        String hql = ("from ReportOrgInfo where 1=1 ");
        list = this.findListByHsql(hql, null);
        return list;
    }

    // 返回上报机构
    public List findUserId(String id) {
        List list = new ArrayList();
        String hql = "from ReportConfigInfo t where t.reportOrgId = " + id;
        list = this.findListByHsql(hql, null);
        return list;
    }
}
