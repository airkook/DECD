package com.fitech.papp.decd.model.pojo;


/**
 * RoleInfo entity. @author MyEclipse Persistence Tools
 */

public class RoleInfo implements java.io.Serializable
{

    // Fields

    private Integer roleId;

    private String roleName;

    // Constructors

    /** default constructor */
    public RoleInfo()
    {
    }

    /** full constructor */
    public RoleInfo(Integer roleId, String roleName)
    {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Property accessors

    public Integer getRoleId()
    {
        return this.roleId;
    }

    public void setRoleId(Integer roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleName()
    {
        return this.roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

}