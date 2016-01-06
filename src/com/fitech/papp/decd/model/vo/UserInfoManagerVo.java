package com.fitech.papp.decd.model.vo;

/**
 * 用户管理Vo类
 * @author Administrator
 *
 */
public class UserInfoManagerVo {
	
	private Integer userId;//编号
	
	private String userName;//用户名
	
	private String orgId;//机构编号
	
	private String orgName;//机构名称

	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
}
