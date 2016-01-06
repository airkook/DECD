package com.fitech.papp.decd.model.pojo;


/**
 * Menu entity. @author MyEclipse Persistence Tools
 */

public class Menu implements java.io.Serializable
{

    // Fields

    private Integer menuId;

    private String menuName;

    private String menuDesc;

    private String menuUrl;

    private Integer parId;

    // Constructors

    /** default constructor */
    public Menu()
    {
    }

    /** minimal constructor */
    public Menu(Integer menuId, String menuName, Integer parId)
    {
        this.menuId = menuId;
        this.menuName = menuName;
        this.parId = parId;
    }

    /** full constructor */
    public Menu(Integer menuId, String menuName, String menuDesc,
            String menuUrl, Integer parId)
    {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuDesc = menuDesc;
        this.menuUrl = menuUrl;
        this.parId = parId;
    }

    // Property accessors

    public Integer getMenuId()
    {
        return this.menuId;
    }

    public void setMenuId(Integer menuId)
    {
        this.menuId = menuId;
    }

    public String getMenuName()
    {
        return this.menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuDesc()
    {
        return this.menuDesc;
    }

    public void setMenuDesc(String menuDesc)
    {
        this.menuDesc = menuDesc;
    }

    public String getMenuUrl()
    {
        return this.menuUrl;
    }

    public void setMenuUrl(String menuUrl)
    {
        this.menuUrl = menuUrl;
    }

    public Integer getParId()
    {
        return this.parId;
    }

    public void setParId(Integer parId)
    {
        this.parId = parId;
    }

}