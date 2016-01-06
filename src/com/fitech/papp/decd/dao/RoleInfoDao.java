package com.fitech.papp.decd.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;

/**
 * 角色dao
 * @author wupei
 *
 */
@Service(value="roleInfoDao")
public interface RoleInfoDao
{
    public List<RoleInfo> findRoleInfoList();
    public RoleInfo findRoleInfoByRoleId(Integer roleId);
    public void saveRoleInfo(RoleInfo roleInfo) throws Exception;
    public void deleteRoleMenuByRoleId(Integer roleId);
    public void deleteRoleInfo(Integer roleId) throws Exception;
    public List findMenuByRoleId(Integer roleId);
    public List<Menu> findMenuAllList();
    public List<Integer> findMenuAllByMenuIds(String menuIds);
    public	List<RoleInfo> findRoleInfoByRoleName(String roleName);
    public List findRoleMapBySql(String roleId)throws Exception;
    public List<RoleInfo> findRoleInfoByRoleName(String roleName, String id);
}
