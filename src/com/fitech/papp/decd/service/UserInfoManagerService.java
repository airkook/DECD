package com.fitech.papp.decd.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;

/**
 * 用户管理服务层
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoManagerService")
public interface UserInfoManagerService {
    /**
     * 根据sql查询用户信息 分页查询
     * 
     * @param sql
     * @param map
     * @return
     */
    public PageResults findUserInfo(Map<String, String> map, int pageSize, int pageNo);

    /**
     * 修改用户信息 by sql
     * 
     * @param sql
     */
    public void updateUserInfo(String userId, String password, String thisLastName, String thisFirstName);

    /**
     * 查询所有角色信息
     * 
     * @return
     */
    public List<RoleInfo> findRoleByHQL();

    /**
     * 查询用户所具有的角色
     * 
     * @return
     */
    public List findUserRoleMap(String userId);

    /**
     * 删除用户角色信息
     * 
     * @param sql
     */
    public void delUserRoleMap(String userId);

    /**
     * 批量添加用户角色信息
     * 
     * @param listmap
     */
    public void addBatch(List<String> list, String userId);

    /**
     * 根据角色编号删除用户所具有的角色信息
     * 
     * @param roleId
     */
    public void delUserRoleByRoleId(String roleId);

    public UserInfo findUserInfoById(String userId);

}
