package com.fitech.papp.decd.model.vo;

import java.util.List;

import com.fitech.papp.decd.model.pojo.UserInfo;

/**
 * 用户信息
 * 
 * @author wupei
 * 
 */
public class UserInfoVo extends UserInfo {
    private List<MenuVo> menuVoList;

    /**
     * 获取menuVoList
     * 
     * @return menuVoList menuVoList
     */

    public List<MenuVo> getMenuVoList() {
        return menuVoList;
    }

    /**
     * 设置menuVoList
     * 
     * @param menuVoList menuVoList
     */

    public void setMenuVoList(List<MenuVo> menuVoList) {
        this.menuVoList = menuVoList;
    }
}
