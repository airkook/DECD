package com.fitech.papp.decd.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.service.RoleManagerService;

/**
 * 角色管理
 * 
 * @author wupei
 * 
 */
public class RoleManagerAction extends DefaultBaseAction {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Autowired
    private RoleManagerService roleManagerService;

    private List<RoleInfo> roleInfoList;

    private RoleInfo roleInfo;

    private Integer roleId;

    private String roleName;

    private List<Menu> menuList;

    private List<Menu> menuAllList;

    private String[] selectReport;

    private int count;
    
    private String newRoleName;
    
    private String id;
    
    /**
     * 页面初始化
     */
    public String init() {
        roleInfoList = roleManagerService.findRoleInfoList();
        count = roleInfoList.size();
        return SUCCESS;
    }

    /**
     * 修改页面显示
     */
    public String edit() {
        // 根据角色ID查询角色信息
        if (roleId != null) {
            roleInfo = this.roleManagerService.findRoleInfoByRoleId(roleId);
        }
        return "edit";
    }

    /**
     * 修改页面保存
     */
    public void saveRoleInfo() {
        Integer idInt = Integer.valueOf(id);
        PrintWriter out = null;
        try {
            out = ServletActionContext.getResponse().getWriter();
            RoleInfo role = roleManagerService.findRoleInfoByRoleName(newRoleName,id);
            if (role == null) {
                RoleInfo roleInfo=roleManagerService.findRoleInfoByRoleId(idInt);
                roleInfo.setRoleName(newRoleName);
                roleManagerService.saveRoleInfo(roleInfo);
                out.print("{\"flag\":1}");
            } else {
                out.print("{\"flag\":0}");
            }
        }
        catch (Exception e) {
            out.print("{\"flag\":-1}");
            e.printStackTrace();
        }
        out.close();
    }

    /**
     * 添加角色信息
     * 
     * @return
     */

    public String addRoleInfo() {
        PrintWriter out = null;
        try {
            out = ServletActionContext.getResponse().getWriter();
            RoleInfo role = roleManagerService.findRoleInfoByRoleName(roleInfo.getRoleName());
            if (role == null) {
                roleManagerService.saveRoleInfo(roleInfo);
                out.print("{\"flag\":1}");
            }
            else {
                out.print("{\"flag\":0}");
            }
        }
        catch (Exception e) {
            out.print("{\"flag\":-1}");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除按钮按下
     * 
     * @throws Exception
     */
    public String deleteRoleInfo() throws Exception {

        this.roleManagerService.deleteRoleInfo(roleId);
        // 删除用户所具有角色信息
        // this.userInfoManagerService.delUserRoleByRoleId(roleId+"");
        return this.init();
    }

    /**
     * 菜单功能分配按下
     */
    public String showRoleMenu() {
        // 根据角色ID查询角色信息
        if (roleId != null) {
            roleInfo = this.roleManagerService.findRoleInfoByRoleId(roleId);
            // 根据角色ID查询菜单的信息
            menuList = this.roleManagerService.findMenuByRoleId(roleId);
            // 查询所有的菜单
            menuAllList = this.roleManagerService.findMenuAllList();
        }
        return "roleMenu";
    }

    /**
     * 保存菜单
     * 
     * @return
     * @throws Exception
     */
    public String saveRoleMenu() throws Exception {
        this.roleManagerService.saveRoleMenu(selectReport, roleId);
        return init();
    }

    /**
     * 删除前判断角色是否有被用户使用
     * 
     * @return
     */
    public String isToDel_execute() {
        PrintWriter out = null;
        boolean isok = false;
        try {
            out = ServletActionContext.getResponse().getWriter();
            List list = roleManagerService.findRoleMapByRoldId(roleId.toString());
            if (list.isEmpty()) {
                // 可以进行删除操作
                out.print("{\"flag\":1}");
            }
            else {
                // 不为空 不能做删除操作
                out.print("{\"flag\":0}");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            out.print("{\"flag\":0}");
        }
        return null;
    }

    /**
     * 设置roleInfoList
     * 
     * @param roleInfoList roleInfoList
     */

    public void setRoleInfoList(List<RoleInfo> roleInfoList) {
        this.roleInfoList = roleInfoList;
    }

    /**
     * 获取roleInfoList
     * 
     * @return roleInfoList roleInfoList
     */

    public List<RoleInfo> getRoleInfoList() {
        return roleInfoList;
    }

    /**
     * 设置roleInfo
     * 
     * @param roleInfo roleInfo
     */

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    /**
     * 获取roleInfo
     * 
     * @return roleInfo roleInfo
     */

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    /**
     * 设置roleName
     * 
     * @param roleName roleName
     */

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取roleName
     * 
     * @return roleName roleName
     */

    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置roleId
     * 
     * @param roleId roleId
     */

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取roleId
     * 
     * @return roleId roleId
     */

    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置menuList
     * 
     * @param menuList menuList
     */

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    /**
     * 获取menuList
     * 
     * @return menuList menuList
     */

    public List<Menu> getMenuList() {
        return menuList;
    }

    /**
     * 设置menuAllList
     * 
     * @param menuAllList menuAllList
     */

    public void setMenuAllList(List<Menu> menuAllList) {
        this.menuAllList = menuAllList;
    }

    /**
     * 获取menuAllList
     * 
     * @return menuAllList menuAllList
     */

    public List<Menu> getMenuAllList() {
        return menuAllList;
    }

    /**
     * 设置selectReport
     * 
     * @param selectReport selectReport
     */

    public void setSelectReport(String[] selectReport) {
        this.selectReport = selectReport;
    }

    /**
     * 获取selectReport
     * 
     * @return selectReport selectReport
     */

    public String[] getSelectReport() {
        return selectReport;
    }

    /**
     * 设置count
     * 
     * @param count count
     */

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取count
     * 
     * @return count count
     */

    public int getCount() {
        return count;
    }

    public String getNewRoleName() {
        return newRoleName;
    }

    public void setNewRoleName(String newRoleName) {
        this.newRoleName = newRoleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
