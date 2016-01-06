package com.fitech.papp.decd.dao.impl;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.dao.impl.DefaultBaseDao;
import com.fitech.papp.decd.dao.RoleMenuMapDao;
import com.fitech.papp.decd.model.pojo.RoleMenuMap;

/**
 * 角色菜单dao实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "roleMenuMapDao")
public class RoleMenuMapDaoImpl extends DefaultBaseDao<RoleMenuMap, Integer> implements RoleMenuMapDao {

    public void saveRoleMenu(RoleMenuMap roleMenuMap) throws Exception {

        this.save(roleMenuMap);
    }

}
