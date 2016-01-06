package com.fitech.papp.decd.dao;

import org.springframework.stereotype.Service;

import com.fitech.papp.decd.model.pojo.RoleMenuMap;

/**
 * 角色菜单dao
 * @author wupei
 *
 */
@Service(value="roleMenuMapDao")
public interface RoleMenuMapDao
{
    public void saveRoleMenu(RoleMenuMap roleMenuMap) throws Exception;
}
