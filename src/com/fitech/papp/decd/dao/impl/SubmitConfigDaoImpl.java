package com.fitech.papp.decd.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.SubmitConfigDao;
import com.fitech.papp.decd.model.pojo.ReportCalendarInfo;
import com.fitech.papp.decd.model.pojo.ReportConfigInfo;

/**
 * 报送配置dao
 * 
 * @author songfei
 * */
@Service(value = "submitConfigDao")
public class SubmitConfigDaoImpl extends DefaultBaseDao<ReportConfigInfo, Integer> implements SubmitConfigDao {

    /** 报送配置信息 */
    public List findAllReportConfigInfo() {
        List<ReportConfigInfo> list = new ArrayList<ReportConfigInfo>();
        String hql = ("from ReportConfigInfo where 1=1 ");
        list = this.findListByHsql(hql, null);
        return list;
    }

    /*
     * 根据年份删除g
     */
    @Override
    public void deleteByYear(String term) throws Exception {

        String sql = " delete from REPORT_CALENDAR_INFO where REPORT_YEAR='" + term + "'";
        this.updateBysql(sql);

    }

    /*
     * 批量插入
     */
    @Override
    public void batchInsert(List<ReportCalendarInfo> list) throws Exception {
        if (list == null || list.size() == 0) {
            return;
        }

        final int batchSize = 1000;
        int count = 0;
        String sql = " insert into REPORT_CALENDAR_INFO " + "(CALENDAR_INFO_ID,REPORT_YEAR,REPORT_DATE,YWFSR_DATE) "
                + "values (SEQ_CALENDAR_INFO.nextval,?,?,?)";
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
                /*
                 * for (int j = 0; j < list.get(0); j++) { ps.setObject(j + 1,
                 * list.get(i)[j]); }
                 */
                ps.setObject(1, list.get(i).getReportYear());
                ps.setObject(2, list.get(i).getReportDate());
                ps.setObject(3, list.get(i).getYwfsrDate());

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

}
