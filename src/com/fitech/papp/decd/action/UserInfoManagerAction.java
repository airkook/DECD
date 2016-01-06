package com.fitech.papp.decd.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.web.PageResults;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.dao.impl.UserInfoDaoImpl;
import com.fitech.papp.decd.model.pojo.RoleInfo;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.UserInfoManagerVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.UserInfoManagerService;
import com.fitech.papp.decd.util.MD5;

/**
 * 用户管理Action类
 * 
 * @author wupei
 * 
 */
public class UserInfoManagerAction extends DefaultBaseAction {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private UserInfoManagerService userInfoManagerService;

    /**
     * 页面用户对象
     */
    private PageResults<UserInfoManagerVo> userInfoVo;

    /** 查询条件用户名 */
    private String userName;

    /** 用户ID */
    private String userId;

    /** 新密码 */
    private String thispassword;

    /** 新名字 */
    private String thisLastName;

    /** 姓 */
    private String thisFirstName;

    /** 用户信息 */
    private UserInfo userInfo;

    /** 所有角色信息 */
    private List<RoleInfo> roleList;

    /** 用户权限 */
    private List haveRole;

    /** 用户角色集合 */
    private String nowRoleIds;

    /**
     * 用户管理初始化加载数据
     * 
     * @return
     */
    public String init() {
        userInfo = (UserInfoVo) this.getRequest().getSession().getAttribute("userInfo");
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        map.put("selfUserId", String.valueOf(userInfo.getUserId()));
        userInfoVo = userInfoManagerService.findUserInfo(map, pageSize, pageNo);
        return "init";
    }

    /**
     * 修改按钮按下页面
     * 
     * @return
     */
    public String edit() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        userInfoVo = userInfoManagerService.findUserInfo(map, pageSize, pageNo);

        return "edit";
    }

    /**
     * 修改页面保存
     * 
     * @return
     */
    public String update() {
        userInfoManagerService.updateUserInfo(userId, thispassword, thisLastName, thisFirstName);
        return "update";
    }

    /**
     * 去分配角色信息界面
     * 
     * @return
     */
    public String toRoleInfo() {
        userInfo = userInfoManagerService.findUserInfoById(userId);
        // 查询所以角色
        roleList = userInfoManagerService.findRoleByHQL();
        // 查询当前用户角色
        haveRole = userInfoManagerService.findUserRoleMap(userId);
        return "toRole";
    }

    /**
     * 修改用户角色保存按下
     * 
     * @return
     */
    public String updateRoleInfo() {
        try {
            List<String> list = new ArrayList<String>();
            if (nowRoleIds == null) {
                nowRoleIds = "";
            }
            else if (nowRoleIds.trim().equals("")) {
                nowRoleIds = "";
            }
            if (!nowRoleIds.trim().equals("")) {
                String[] roleids = nowRoleIds.replaceAll(" ", "").split(",");
                for (int i = 0; i < roleids.length; i++) {
                    list.add(roleids[i]);
                }
            }
            userInfoManagerService.delUserRoleMap(userId);
            if (!list.isEmpty()) {
                userInfoManagerService.addBatch(list, userId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "updateRoleInfo";
    }

    /**
     * @param strs 格式为 1,2,3,4 用逗号隔开的字符串
     * @return List 字符串集合
     */
    public List<String> fromStringToList(String strs) {
        List<String> list = new ArrayList<String>();
        if (strs == null) {
            strs = "";
        }
        else if (strs.trim().equals("")) {
            strs = "";
        }
        if (!strs.trim().equals("")) {
            String[] roleids = strs.replaceAll(" ", "").split(",");
            for (int i = 0; i < roleids.length; i++) {
                list.add(roleids[i]);
            }
        }
        return list;
    }

    @Autowired
    private UserInfoDaoImpl userInfoDao;

    public String doAddUserInfo() {
        PrintWriter out = null;
        try {
            out = ServletActionContext.getResponse().getWriter();
            UserInfo us = userInfoDao.findUserInfo(userInfo.getUserName());
            if (us != null) {
                out.print("{\"flag\":0}");
            }
            else {
                userInfo.setIsadmin(0);
                userInfo.setStatus(1);
                String pwd = MD5.GetMD5Code(userInfo.getUserPwd());
                userInfo.setUserPwd(pwd);
                userInfoDao.addUserInfo(userInfo);
                out.print("{\"flag\":1}");
            }

        }
        catch (Exception e) {
            out.print("{\"flag\":-1}");
            e.printStackTrace();
        }
        return null;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getNowRoleIds() {
        return nowRoleIds;
    }

    public void setNowRoleIds(String nowRoleIds) {
        this.nowRoleIds = nowRoleIds;
    }

    public List getHaveRole() {
        return haveRole;
    }

    public void setHaveRole(List haveRole) {
        this.haveRole = haveRole;
    }

    public List<RoleInfo> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleInfo> roleList) {
        this.roleList = roleList;
    }

    public String getThispassword() {
        return thispassword;
    }

    public void setThispassword(String thispassword) {
        this.thispassword = thispassword;
    }

    public String getThisLastName() {
        return thisLastName;
    }

    public void setThisLastName(String thisLastName) {
        this.thisLastName = thisLastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserInfoManagerService getUserInfoManagerService() {
        return userInfoManagerService;
    }

    public void setUserInfoManagerService(UserInfoManagerService userInfoManagerService) {
        this.userInfoManagerService = userInfoManagerService;
    }

    public PageResults<UserInfoManagerVo> getUserInfoVo() {
        return userInfoVo;
    }

    public void setUserInfoVo(PageResults<UserInfoManagerVo> userInfoVo) {
        this.userInfoVo = userInfoVo;
    }

    public String getThisFirstName() {
        return thisFirstName;
    }

    public void setThisFirstName(String thisFirstName) {
        this.thisFirstName = thisFirstName;
    }

}
