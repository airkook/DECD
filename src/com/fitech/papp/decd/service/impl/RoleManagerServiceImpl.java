package com.fitech.papp.decd.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.util.ListUtil;
import com.fitech.papp.decd.dao.RoleInfoDao;
import com.fitech.papp.decd.dao.RoleMenuMapDao;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.RoleMenuMap;
import com.fitech.papp.decd.model.pojo.RoleMenuMapId;
import com.fitech.papp.decd.service.RoleManagerService;

/**
 * 角色serivice实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "roleManagerService")
public class RoleManagerServiceImpl implements RoleManagerService
{
    /** 角色dao */
    @Autowired
    private RoleInfoDao roleInfoDao;

    /** 角色菜单dao */
    @Autowired
    private RoleMenuMapDao roleMenuMapDao;

    /**
     * 查询所有的角色
     * 
     * @return List<RoleInfo> 角色List
     */
    public List<RoleInfo> findRoleInfoList()
    {
        return roleInfoDao.findRoleInfoList();
    }

    /**
     * 根据角色ID查询角色信息
     * 
     * @param roleId 角色Id
     * @return RoleInfo 角色对象
     */
    public RoleInfo findRoleInfoByRoleId(Integer roleId)
    {
        return roleInfoDao.findRoleInfoByRoleId(roleId);
    }

    /**
     * 保存角色信息
     * 
     * @param roleInfo 角色对象
     */
    public void saveRoleInfo(RoleInfo roleInfo) throws Exception
    {
        this.roleInfoDao.saveRoleInfo(roleInfo);
    }

    /**
     * 根据角色ID删除角色菜单表以及角色表
     * 
     * @param roleId 角色ID
     */
    public void deleteRoleInfo(Integer roleId) throws Exception
    {
        // 删除角色菜单表
        this.roleInfoDao.deleteRoleMenuByRoleId(roleId);
        // 删除角色表
        this.roleInfoDao.deleteRoleInfo(roleId);
    }

    /**
     * 根据角色ID查询菜单信息
     * 
     * @param roleId 角色ID
     * @return List<Menu> 菜单list
     */
    public List<Menu> findMenuByRoleId(Integer roleId)
    {
        List list = this.roleInfoDao.findMenuByRoleId(roleId);
        List<Menu> menuList = null;
        if (!ListUtil.isEmpty(list))
        {
            menuList = new ArrayList<Menu>();
            for (Iterator<Object[]> a = list.iterator(); a.hasNext();)
            {
                Menu vo = new Menu();
                Object[] object = a.next();
                vo.setMenuId(Integer.valueOf(object[0].toString()));
                vo.setMenuDesc(object[2].toString());
                vo.setParId(Integer.valueOf(object[4].toString()));
                menuList.add(vo);
            }
        }
        return menuList;
    }

    /**
     * 查询所有的子菜单
     * 
     * @return List<Menu> 子菜单列表
     */
    public List<Menu> findMenuAllList()
    {
        return this.roleInfoDao.findMenuAllList();
    }

    /**
     * 保存当前角色对应的菜单
     * 
     * @param menus 菜单ID
     * @param roleId 角色ID
     * @throws Exception
     */
    public void saveRoleMenu(String[] menus, Integer roleId) throws Exception
    {
        // 删除角色菜单表
        this.roleInfoDao.deleteRoleMenuByRoleId(roleId);
        if (menus != null && menus.length > 0)
        {
            // 生成新的菜单表入库
            // 根据菜单ID查询对应的菜单以及上一级菜单
            StringBuffer menuIds = new StringBuffer();
            for (int i = 0; i < menus.length; i++)
            {
                menuIds.append(menus[i] + ",");
            }
            String menuId = menuIds.toString().substring(0,
                    menuIds.toString().length() - 1);
            List<Integer> menuIdList = this.roleInfoDao
                    .findMenuAllByMenuIds(menuId);
            if (!ListUtil.isEmpty(menuIdList))
            {
                for (Integer i : menuIdList)
                {
                    RoleMenuMap map = new RoleMenuMap();
                    RoleMenuMapId id = new RoleMenuMapId();
                    id.setMenuId(i);
                    map.setId(id);
                    id.setRoleId(roleId);
                    // 保存角色菜单表
                    roleMenuMapDao.saveRoleMenu(map);
                }
            }
        }
    }

    public RoleInfo findRoleInfoByRoleName(String roleName) {
        List<RoleInfo> roleInfoList= roleInfoDao.findRoleInfoByRoleName(roleName);
        return roleInfoList.isEmpty()?null:roleInfoList.get(0);
    }
    
	public RoleInfo findRoleInfoByRoleName(String roleName, String id) {
		List<RoleInfo> roleInfoList= roleInfoDao.findRoleInfoByRoleName(roleName,id);
		return roleInfoList.isEmpty()?null:roleInfoList.get(0);
	}

	/**
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List findRoleMapByRoldId(String roleId)throws Exception{
		List list=roleInfoDao.findRoleMapBySql(roleId);
		return list;
	}
	
	
    
}
