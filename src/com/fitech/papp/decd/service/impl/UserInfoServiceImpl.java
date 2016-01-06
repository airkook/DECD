package com.fitech.papp.decd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitech.framework.core.util.ListUtil;
import com.fitech.papp.decd.dao.UserInfoDao;
import com.fitech.papp.decd.model.pojo.Menu;
import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.model.vo.MenuVo;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.UserInfoService;
import com.fitech.papp.decd.util.MD5;

/**
 * 用户service实现类
 * 
 * @author wupei
 * 
 */
@Service(value = "userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    /** 用户dao */
    @Autowired
    private UserInfoDao userInfoDao;

    public UserInfoVo findUserInfo(String userName, String password) throws Exception {
        // 根据密码取得 用户信息
        UserInfo userInfo = this.userInfoDao.findUserInfo(userName);
        UserInfoVo userInfoVo = null;
        if (userInfo != null) {
            userInfoVo = new UserInfoVo();
            // 判断密码是否正确
            if (null == password || MD5.GetMD5Code(password).equals(userInfo.getUserPwd())) {
                //
                BeanUtils.copyProperties(userInfoVo, userInfo);
                // 判断是否是系统管理员
                List<Menu> menuList = null;
                if (1 == userInfo.getIsadmin()) {
                    // 取所有的菜单栏
                    menuList = this.userInfoDao.findMenuList();

                }
                else {
                    // 根绝角色查询对应的菜单栏
                    menuList = this.userInfoDao.findMenuListByUserId(userInfo.getUserId());
                }
                userInfoVo.setMenuVoList(this.getMenuVoList(menuList));
            }
            else {
                return null;
            }
        }
        return userInfoVo;
    }

    /**
     * 根据菜单
     */
    private List<MenuVo> getMenuVoList(List<Menu> list) {
        List<MenuVo> menuVoList = null;
        if (!ListUtil.isEmpty(list)) {
            menuVoList = new ArrayList<MenuVo>();
            for (Menu menu : list) {
                if (0 == menu.getParId()) {
                    MenuVo vo = new MenuVo();
                    vo.setMenuId(menu.getMenuId());
                    vo.setMenuName(menu.getMenuName());
                    vo.setMenuUrl(menu.getMenuUrl());
                    for (Menu menu1 : list) {
                        if (menu1.getParId().equals(menu.getMenuId())) {
                            MenuVo vo1 = new MenuVo();
                            vo1.setMenuId(menu1.getMenuId());
                            vo1.setMenuName(menu1.getMenuName());
                            vo1.setMenuUrl(menu1.getMenuUrl());
                            for (Menu menu2 : list) {
                                MenuVo vo2 = new MenuVo();
                                vo2.setMenuId(menu2.getMenuId());
                                vo2.setMenuName(menu2.getMenuName());
                                vo2.setMenuUrl(menu2.getMenuUrl());
                                if (menu2.getParId().equals(menu1.getMenuId())) {
                                    vo1.getMenuList().add(vo2);
                                }
                            }
                            vo.getMenuList().add(vo1);
                        }
                    }
                    menuVoList.add(vo);
                }
            }
        }
        return menuVoList;
    }

    public boolean addUserInfo(UserInfo userInfo) {
        return userInfoDao.addUserInfo(userInfo);
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        return userInfoDao.updateUserInfo(userInfo);

    }

    public boolean delUserInfo(UserInfo userInfo) {
        return userInfoDao.delUserInfo(userInfo);
    }

    public UserInfo findUser(String userName) throws Exception {
        UserInfo userInfo = this.userInfoDao.findUserInfo(userName);
        return userInfo;
    }
}
