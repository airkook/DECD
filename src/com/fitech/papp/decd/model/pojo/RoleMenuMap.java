package com.fitech.papp.decd.model.pojo;

/**
 * RoleMenuMap entity. @author MyEclipse Persistence Tools
 */

public class RoleMenuMap implements java.io.Serializable
{

    // Fields

    private RoleMenuMapId id;

    // Constructors

    /** default constructor */
    public RoleMenuMap()
    {
    }

    /** full constructor */
    public RoleMenuMap(RoleMenuMapId id)
    {
        this.id = id;
    }

    // Property accessors

    public RoleMenuMapId getId()
    {
        return this.id;
    }

    public void setId(RoleMenuMapId id)
    {
        this.id = id;
    }

}