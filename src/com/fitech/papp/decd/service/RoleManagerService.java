package com.fitech.papp.decd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;

/**
 * 角色管理 service
 * 
 * @author wupei
 * 
 */
@Service(value = "roleManagerService")
public interface RoleManagerService
{
    public List<RoleInfo> findRoleInfoList();

    public RoleInfo findRoleInfoByRoleId(Integer roleId);
    public RoleInfo findRoleInfoByRoleName(String roleName);
    public RoleInfo findRoleInfoByRoleName(String roleName, String id);
    public void saveRoleInfo(RoleInfo roleInfo) throws Exception;
    public void deleteRoleInfo(Integer roleId) throws Exception;
    public List<Menu> findMenuByRoleId(Integer roleId);
    public List<Menu> findMenuAllList();
    public void saveRoleMenu(String[] menus,Integer roleId) throws Exception;
    public List findRoleMapByRoldId(String roleId)throws Exception;
}
