package com.fitech.papp.decd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fitech.framework.core.web.PageResults;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.UserInfoVo;

/**
 * 用户dao
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoDao")
public interface UserInfoDao {
    public UserInfo findUserInfo(String userName);

    public UserInfo findUserInfoById(String userId);

    public List<Menu> findMenuList();

    public List<Menu> findMenuListByUserId(Integer userId);

    public boolean addUserInfo(UserInfo userInfo);

    public boolean updateUserInfo(UserInfo userInfo);

    public boolean delUserInfo(UserInfo userInfo);

    /**
     * 根据sql查询用户信息 分页查询
     * 
     * @param sql
     * @param map
     * @return
     */
    public PageResults findUserInfo(String sql, Map map, int pageSize, int pageNo);

    /**
     * 修改用户信息 by sql
     * 
     * @param sql
     */
    public void updateUserInfoBySql(String sql);

    /**
     * 查询所有用户角色信息
     * 
     * @param hql
     * @return
     */
    public List<RoleInfo> findRoleByHQL();

    /**
     * 
     * @return
     */
    public List findUserRoleMap(String sql);

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

    /**
     * 取得此用户有查询权限的指标类型
     * 
     * @param userId
     * @return
     */
    public String[] findTargetTypeInfoByUser(int userId);
    
    /**
     * 取得此用户有查询权限的指标类型
     * 
     * @param userId
     * @return
     */
    public String[] findTargetTypeInfoByTypeId(UserInfoVo loginUserInfo,String targetTypeId);

}
