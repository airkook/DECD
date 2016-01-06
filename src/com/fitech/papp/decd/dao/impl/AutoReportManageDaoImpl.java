package com.fitech.papp.decd.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.AutoReportManageDao;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.vo.AutoReportInfoVo;

/**
 * 自动报送管理DaoImpl
 * 
 * @author wupengzheng
 */
@Service(value = "autoReportManageDao")
public class AutoReportManageDaoImpl extends DefaultBaseDao<AutoReportInfoVo, Integer> implements AutoReportManageDao {

    @Override
    public List<AutoReportInfoVo> queryReportOrgInfoList() {
        String sql = "select a.report_org_name,t.run_time,t.is_used,t.report_org_id,t.flag";
        sql += " from AUTO_REPORT_INFO t";
        sql += "   left join REPORT_ORG_INFO a ";
        sql += "  on t.report_org_id = a.report_org_id order by t.report_org_id,t.flag";

        List<Object[]> result = this.findListBySql(sql, null);
        List<AutoReportInfoVo> list = new ArrayList<AutoReportInfoVo>();

        for (Object[] obj : result) {

            AutoReportInfoVo autoReportInfoVo = new AutoReportInfoVo();
            autoReportInfoVo.setReportOrgName(obj[0].toString());
            if (obj[1] != null) {
                autoReportInfoVo.setTime(obj[1].toString());
            }
            else {
                autoReportInfoVo.setTime("01");
            }
            if (obj[2] != null) {
                autoReportInfoVo.setIsUsed(obj[2].toString());
            }
            else {
                autoReportInfoVo.setIsUsed("0");
            }
            autoReportInfoVo.setReportOrgId(((BigDecimal) obj[3]).intValue());

            autoReportInfoVo.setFlag(obj[4].toString());

            list.add(autoReportInfoVo);

        }
        return list;
    }

    @Override
    public void updateAutoReportInfoRunTime(String timeValue, Integer reportOrgId, String flag) throws Exception {

        String sql = "update auto_report_info t set t.run_time = '" + timeValue + "' where t.report_org_id ="
                + reportOrgId + " and t.flag = " + flag;
        this.updateBysql(sql);
    }

    @Override
    public void updateAutoReportInfoIsUsed(Integer reportOrgId, String flag, String time, String isUsed)
            throws Exception {
        String sql = "update auto_report_info t set t.is_used ='" + isUsed + "' where t.report_org_id = " + reportOrgId
                + " and t.flag = '" + flag + "' and t.run_time = '" + time + "'";

        this.updateBysql(sql);
    }

    /**
     * 根据报送日查询节假日报送日历
     * 
     * @param day
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ReportCalendarInfo> findCalendarInfoList(String day) {
        String hsql = "from ReportCalendarInfo where reportDate = '" + day + "' ";
        List<ReportCalendarInfo> list = this.findListByHsql(hsql, null);
        this.getSession().clear();
        return list;
    }

    /**
     * 根据业务发生日获取日历信息
     * 
     * @param ywfsrDate
     * @return
     */
    @Override
    public ReportCalendarInfo findCalInfoByYwfsr(String ywfsrDate) {
        String hsql = "from ReportCalendarInfo where ywfsrDate = '" + ywfsrDate + "' ";
        ReportCalendarInfo rri = (ReportCalendarInfo) this.findObject(hsql);
        this.getSession().clear();
        return rri;
    }

}
