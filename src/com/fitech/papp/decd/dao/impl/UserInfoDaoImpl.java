package com.fitech.papp.decd.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jfree.util.Log;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.BaseDaoException;
import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.util.ListUtil;
import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.UserInfoDao;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.UserInfoManagerVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;

/**
 * 用户dao实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoDao")
public class UserInfoDaoImpl extends DefaultBaseDao<UserInfo, Integer> implements UserInfoDao {

    public static Logger log = Logger.getLogger(UserInfoDaoImpl.class);

    /**
     * 根据用户名检索用户信息
     * 
     * @param userName 用户名
     * @return UserInfo 用户信息
     */
    public UserInfo findUserInfo(String userName) {
        String hsql = "from UserInfo where userName='" + userName + "'";
        return (UserInfo) this.findObject(hsql);
    }

    /**
     * 根据用户ID检索用户信息
     * 
     * @param userName 用户名
     * @return UserInfo 用户信息
     */
    @Override
    public UserInfo findUserInfoById(String userId) {
        String hsql = "from UserInfo where userId=" + userId;
        return (UserInfo) this.findObject(hsql);
    }

    /**
     * 查询所有的菜单栏
     * 
     * @return List<Menu> 菜单List
     */
    public List<Menu> findMenuList() {
        String hsql = "from Menu order by MENU_ID";
        return this.findListByHsql(hsql, null);
    }

    /**
     * 根据用户ID查询对应的菜单
     * 
     * @param userId 用户ID
     * @return List<Menu> 菜单List
     */
    public List<Menu> findMenuListByUserId(Integer userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct menu.* ");
        sql.append(" from menu menu inner join role_menu_map rm on menu.menu_id=rm.menu_id ");
        sql.append(" inner join role_info r on r.role_id=rm.role_id ");
        sql.append(" inner join user_role_map ur on ur.role_id=r.role_id ");
        sql.append(" inner join user_info u on u.user_id=ur.user_id ");
        sql.append(" where 1=1 and u.user_id=" + userId);
        sql.append(" order by menu.menu_id asc");
        List list = this.findListBySql(sql.toString(), null);
        List<Menu> menuList = null;
        if (!ListUtil.isEmpty(list)) {
            menuList = new ArrayList<Menu>();
            for (Iterator<Object[]> a = list.iterator(); a.hasNext();) {
                Menu vo = new Menu();
                Object[] object = a.next();
                vo.setMenuId(Integer.valueOf(object[0].toString()));
                vo.setMenuName(object[1].toString());
                if (object[3] != null) {
                    vo.setMenuUrl(object[3].toString());
                }
                vo.setParId(Integer.valueOf(object[4].toString()));
                menuList.add(vo);
            }
        }
        return menuList;
    }

    public boolean addUserInfo(UserInfo userInfo) {
        try {
            this.save(userInfo);
        }
        catch (BaseDaoException e) {
            Log.error("Fail to add user", e);
            return false;
        }
        return true;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        try {
            this.update(userInfo);
        }
        catch (BaseDaoException e) {
            Log.error("Fail to update user", e);
            return false;
        }
        return true;
    }

    public boolean delUserInfo(UserInfo userInfo) {
        try {
            this.delete(userInfo);
        }
        catch (BaseDaoException e) {
            Log.error("Fail to delete user", e);
            return false;
        }
        return true;
    }

    /**
     * 根据sql查询用户信息 分页查询
     * 
     * @param sql
     * @param map
     * @return
     */
    public PageResults findUserInfo(String sql, Map map, int pageSize, int pageNo) {
        PageResults<UserInfoManagerVo> pageResults = null;
        try {
            pageResults = this.findPageBySql(sql, map, pageSize, pageNo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pageResults;
    }

    /**
     * 修改用户信息 by sql
     */
    public void updateUserInfoBySql(String sql) {
        try {
            this.updateBysql(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有角色信息
     */
    public List<RoleInfo> findRoleByHQL() {
        String hql = "from RoleInfo";
        return this.findListByHsql(hql, null);
    }

    /**
     * 查询用户所具有的角色
     */
    public List findUserRoleMap(String sql) {
        return this.findListBySql(sql, null);
    }

    /**
     * 删除用户拥有的角色信息
     */
    public void delUserRoleMap(String userId) {
        try {
            String sql = "delete from USER_ROLE_MAP where USER_ID='" + userId + "'";
            this.updateBysql(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量添加用户拥有的角色信息
     */
    public void addBatch(List<String> list, String userId) {
        if (list.isEmpty()) {
            return;
        }
        final int batchSize = 1000;
        int count = 0;
        Session session = null;
        Connection con = null;
        SessionFactory sessionFactory = null;
        PreparedStatement ps = null;
        String sql = "insert into USER_ROLE_MAP values (?,?)";
        if (sessionFactory == null) {
            sessionFactory = getSessionFactory();
        }
        try {
            session = sessionFactory.getCurrentSession();
            con = session.connection();
            ps = con.prepareStatement(sql);
            for (int i = 0; i < list.size(); i++) {
                ps.setObject(1, userId);
                ps.setObject(2, list.get(i));
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

    public void delUserRoleByRoleId(String roleId) {
        try {
            String sql = "";
            if (roleId.equals("")) {
                return;
            }
            else {
                sql = "delete from USER_ROLE_MAP where role_id='" + roleId + "'";
            }
            updateBysql(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户ID查询用户对用的指标类别
     * 
     * @param userID 用户ID
     * @return List<TargetTypeInfo> 指标类别
     */
    @Override
    public String[] findTargetTypeInfoByUser(int userId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select t.target_type_id from target_type_info t ");
        if (1 != userId) {
            sb.append(" inner join role_target_map m on t.target_type_id=m.target_type_id and type_id='1'");
            sb.append(" inner join user_role_data_map dm on dm.role_id=m.role_id ");
        }
        sb.append(" where 1=1 ");
        // 不是管理员管理员
        if (1 != userId) {
            sb.append(" and dm.user_id=" + userId);
        }
        sb.append(" group by t.target_type_id order by t.target_type_id ");
        List<String> targetTypeList = this.findListBySql(sb.toString(), null);
        String[] reulst = null;
        if (!ListUtil.isEmpty(targetTypeList)) {
            reulst = new String[targetTypeList.size()];
            int i = 0;
            for (String obj : targetTypeList) {
                reulst[i] = obj;
                i++;
            }
        }
        return reulst;
    }

    /**
     * 取得此用户有查询权限的指标类型
     * 
     * @param userId
     * @return
     */
    @Override
    public String[] findTargetTypeInfoByTypeId(UserInfoVo loginUserInfo, String targetTypeId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select t.target_type_id from target_type_info t ");
        if (1 != loginUserInfo.getIsadmin().intValue()) {
            sb.append(" inner join role_target_map m on t.target_type_id=m.target_type_id and type_id='1'");
            sb.append(" inner join user_role_data_map dm on dm.role_id=m.role_id ");
        }
        sb.append(" where 1=1 ");
        // 不是管理员管理员
        if (1 != loginUserInfo.getIsadmin().intValue()) {
            sb.append(" and dm.user_id=" + loginUserInfo.getUserId());
        }
        sb.append(" and t.targer_type_par='" + targetTypeId + "' group by t.target_type_id order by t.target_type_id ");
        List<String> targetTypeList = this.findListBySql(sb.toString(), null);
        String[] reulst = null;
        if (!ListUtil.isEmpty(targetTypeList)) {
            reulst = new String[targetTypeList.size()];
            int i = 0;
            for (String obj : targetTypeList) {
                reulst[i] = obj;
                i++;
            }
        }
        return reulst;
    }
}
