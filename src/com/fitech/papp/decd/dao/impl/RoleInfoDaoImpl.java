package com.fitech.papp.decd.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.framework.core.util.ListUtil;
import com.fitech.papp.decd.dao.RoleInfoDao;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;

/**
 * 角色dao实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "roleInfoDao")
public class RoleInfoDaoImpl extends DefaultBaseDao<RoleInfo, Integer>
        implements RoleInfoDao
{

    public List<RoleInfo> findRoleInfoList()
    {
        String hsql = "from RoleInfo order by roleId asc";
        return this.findListByHsql(hsql, null);
    }

    public RoleInfo findRoleInfoByRoleId(Integer roleId)
    {
        String hsql = "from RoleInfo where roleId=" + roleId;
        return (RoleInfo) this.findObject(hsql);
    }
    
    public void saveRoleInfo(RoleInfo roleInfo) throws Exception
    {
        this.saveOrUpdate(roleInfo);
    }

    public void deleteRoleInfo(Integer roleId) throws Exception
    {
        this.delete(roleId);
    }

    public void deleteRoleMenuByRoleId(Integer roleId)
    {	
        String delSql = "from RoleMenuMap where id.roleId=" + roleId;
        this.deleteObjects(delSql);
    }

    public List findMenuByRoleId(Integer roleId)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct menu.* from menu menu");
        sb.append(" inner join role_menu_map rm on menu.menu_id=rm.menu_id ");
        sb.append(" inner join role_info r on r.role_id=rm.role_id ");
        sb.append(" where 1=1 and menu.par_id!=0 and r.role_id=" + roleId);
        return this.findListBySql(sb.toString(), null);
    }

    public List<Menu> findMenuAllList()
    {
        String hsql = "from Menu where parId!=0 order by menuId";
        return this.findListByHsql(hsql, null);
    }

    /**
     * 根据菜单ID查询对应的菜单ID以及父菜单ID
     */
    public List<Integer> findMenuAllByMenuIds(String menuIds)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("select menu_id,menu_name from menu ");
        sb.append(" where 1=1 and menu_id in (" + menuIds +") or menu_id in (select par_id from menu where menu_id in (" + menuIds +"))");
        List list =  this.findListBySql(sb.toString(), null);
        List<Integer> menuIdList = null;
        if (!ListUtil.isEmpty(list))
        {
            menuIdList = new ArrayList<Integer>();
            for (Iterator<Object[]> a = list.iterator(); a.hasNext();)
            {
                Object[] object = a.next();
                
                menuIdList.add(Integer.valueOf(object[0].toString()));
            }
        }
        return menuIdList;
    }

	public List<RoleInfo> findRoleInfoByRoleName(String roleName) {
		String hql="from RoleInfo where roleName='"+roleName+"'";
		return  this.findListByHsql(hql, null);
	}
	
	 public List<RoleInfo> findRoleInfoByRoleName(String roleName, String id){
	     String hql="from RoleInfo where roleName='"+roleName+"' and roleId <> '"+id+"' ";
	     return  this.findListByHsql(hql, null);
	    }

	public List findRoleMapBySql(String roleId) throws Exception {
		String sql="select t.user_id,t.role_id from USER_ROLE_MAP t where t.role_id="+roleId;
		return this.findListBySql(sql, null);
	}

	
    
    

}
