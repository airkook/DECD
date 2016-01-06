package com.fitech.papp.decd.service.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.dao.UserInfoDao;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.service.UserInfoManagerService;
import com.fitech.papp.decd.util.MD5;

/**
 * 用户serivice实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoManagerService")
public class UserInfoManagerServiceImpl implements UserInfoManagerService {

    /** 用户dao */
    @Autowired
    private UserInfoDao userInfoDao;

    /**
     * 分页查询 用户管理信息
     */
    public PageResults findUserInfo(Map<String, String> map, int pageSize, int pageNo) {
        String userName = map.get("userName");
        String userId = map.get("userId");
        String selfUserId = map.get("selfUserId");
        String sql = " select us.user_id,us.user_name,us.first_name,us.last_name"
                + " from USER_INFO us where 1=1 ";
        if (!StringUtil.isBlank(userName)) {
            sql += " and user_name like '%" + userName + "%'";
        }
        if (!StringUtil.isBlank(selfUserId)) {
            sql += " and user_id <> " + selfUserId;
        }
        if (!StringUtil.isBlank(userId)) {
            sql += " and user_id =" + userId;
        }
        return userInfoDao.findUserInfo(sql, null, pageSize, pageNo);
    }

    /**
     * 修改用户信息
     */
    public void updateUserInfo(String userId, String thispassword, String thisLastName, String thisFirstName) {
        String sql = "";
        if (thispassword == null) {
            sql = "update USER_INFO set last_name='" + thisLastName + "',first_name='" + thisFirstName
                    + "'  where user_id=" + userId;
        }
        else if (thispassword.trim().equals("")) {
            sql = "update USER_INFO set last_name='" + thisLastName + "',first_name='" + thisFirstName
                    + "' where user_id=" + userId;
        }
        else {
            String newPwd = MD5.GetMD5Code(thispassword);
            sql = " update USER_INFO set USER_PWD='" + newPwd + "'," + " last_name='" + thisLastName + "',first_name='"
                    + thisFirstName + "' where user_id=" + userId;
        }
        userInfoDao.updateUserInfoBySql(sql);
    }

    /**
     * 查询所有角色信息
     * 
     * @return
     */
    public List<RoleInfo> findRoleByHQL() {
        return userInfoDao.findRoleByHQL();
    }

    public List findUserRoleMap(String userId) {
        String sql = " select rolemap.user_id ,rolemap.role_id,roleinfo.role_name from user_role_map rolemap"
                + " left join role_info  roleinfo on rolemap.role_id= roleinfo.role_id " + " where rolemap.user_id="
                + userId;
        return userInfoDao.findUserRoleMap(sql);
    }

    public void delUserRoleMap(String userId) {
        userInfoDao.delUserRoleMap(userId);
    }

    public void addBatch(List<String> list, String userId) {
        userInfoDao.addBatch(list, userId);
    }

    public void delUserRoleByRoleId(String roleId) {
        userInfoDao.delUserRoleByRoleId(roleId);
    }

    @Override
    public UserInfo findUserInfoById(String userId) {
        return userInfoDao.findUserInfoById(userId);
    }

}
