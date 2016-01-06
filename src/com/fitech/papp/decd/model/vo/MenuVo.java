package com.fitech.papp.decd.model.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuVo {
    private Integer menuId;

    private String menuName;

    private String menuDesc;

    private String menuUrl;

    private Integer parId;

    private List<MenuVo> menuList = new ArrayList<MenuVo>();

    /**
     * 获取menuId
     * 
     * @return menuId menuId
     */

    public Integer getMenuId() {
        return menuId;
    }

    /**
     * 设置menuId
     * 
     * @param menuId menuId
     */

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * 获取menuName
     * 
     * @return menuName menuName
     */

    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置menuName
     * 
     * @param menuName menuName
     */

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * 获取menuDesc
     * 
     * @return menuDesc menuDesc
     */

    public String getMenuDesc() {
        return menuDesc;
    }

    /**
     * 设置menuDesc
     * 
     * @param menuDesc menuDesc
     */

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    /**
     * 获取menuUrl
     * 
     * @return menuUrl menuUrl
     */

    public String getMenuUrl() {
        return menuUrl;
    }

    /**
     * 设置menuUrl
     * 
     * @param menuUrl menuUrl
     */

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    /**
     * 获取parId
     * 
     * @return parId parId
     */

    public Integer getParId() {
        return parId;
    }

    /**
     * 设置parId
     * 
     * @param parId parId
     */

    public void setParId(Integer parId) {
        this.parId = parId;
    }

    /**
     * 获取menuList
     * 
     * @return menuList menuList
     */

    public List<MenuVo> getMenuList() {
        return menuList;
    }

    /**
     * 设置menuList
     * 
     * @param menuList menuList
     */

    public void setMenuList(List<MenuVo> menuList) {
        this.menuList = menuList;
    }

}
