package com.fitech.papp.decd.model.pojo;


/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */

public class UserInfo implements java.io.Serializable
{

    // Fields

    private Integer userId;

    private String userName;

    private String userPwd;

    private String firstName;

    private String lastName;

    private String orgId;

    private String depId;

    private Integer sex;

    private Integer status;

    private Integer isadmin;

    // Constructors

    /** default constructor */
    public UserInfo()
    {
    }

    /** minimal constructor */
    public UserInfo(Integer userId, String userName, String userPwd,
            String orgId, Integer status)
    {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.orgId = orgId;
        this.status = status;
    }

    /** full constructor */
    public UserInfo(Integer userId, String userName, String userPwd,
            String firstName, String lastName, String orgId, String depId,
            Integer sex, Integer status, Integer isadmin)
    {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.firstName = firstName;
        this.lastName = lastName;
        this.orgId = orgId;
        this.depId = depId;
        this.sex = sex;
        this.status = status;
        this.isadmin = isadmin;
    }

    // Property accessors

    public Integer getUserId()
    {
        return this.userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserPwd()
    {
        return this.userPwd;
    }

    public void setUserPwd(String userPwd)
    {
        this.userPwd = userPwd;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getOrgId()
    {
        return this.orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String getDepId()
    {
        return this.depId;
    }

    public void setDepId(String depId)
    {
        this.depId = depId;
    }

    public Integer getSex()
    {
        return this.sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getIsadmin()
    {
        return this.isadmin;
    }

    public void setIsadmin(Integer isadmin)
    {
        this.isadmin = isadmin;
    }

}