package com.fitech.papp.webservice.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.papp.decd.model.pojo.UserInfo;
import com.fitech.papp.decd.service.UserInfoService;
import com.fitech.papp.webservice.pojo.WebSysUser;
import com.fitech.papp.webservice.service.UserService;
import com.fitech.papp.webservice.util.Constant;

public class UserServiceImpl implements UserService {
	public static Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private UserInfoService userInfoService;



	public String insertUser(WebSysUser user) {
		log.info("Begin to insert user, user : " + user.getUsername());

		String result = "0";
		try {
			UserInfo userInfo = transToUser(user);
			// String seqId = userInfoService.querySeqUserInfo();
			// userInfo.setUserId(seqId);
			UserInfo existUserInfo = userInfoService.findUserInfo(
					userInfo.getUserName(), null);
			// 如果用户已经存在，返回1
			if (null != existUserInfo) {
				result = "1";
			} else {
				userInfoService.addUserInfo(userInfo);
			}
		} catch (Exception e) {
			log.error("Create user error userInfo : " + user, e);
			result = "3";
		}
		log.info("End to insert user, user : " + user.getUsername());
		return result;
	}

	private UserInfo transToUser(WebSysUser user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setDepId(user.getDepartment());
		userInfo.setFirstName(user.getRealName());
		userInfo.setIsadmin("true".equalsIgnoreCase(user.getIsSuper()) ? 1 : 0);
		// userInfo.setLastName(lastName)
		userInfo.setOrgId(user.getOrgId());
		// userInfo.setSex("");
		// 这个状态怎么设置？
		userInfo.setStatus(1);
		userInfo.setUserName(user.getUsername());
		// id怎么取值？每次获取最大值+1？还是使用sequence
		// userInfo.setUserId(2);
		userInfo.setUserPwd(user.getPassword());
		return userInfo;
	}

	public String updateUser(WebSysUser user) {
		log.info("Begin to update user, user : " + user.getUsername());
		String result = "0";
		try {
			// UserInfo userInfo = transToUser(user);
			UserInfo userInfo = userInfoService.findUser(user.getUsername());

			// 如果用户已经存在，返回1
			if (null == userInfo) {
				result = Constant.USER_UPDATE_FAILURE;
			} else {
				transToWebToUser(user, userInfo);
				userInfoService.updateUserInfo(userInfo);
			}
		} catch (Exception e) {
			log.error("Update user error userInfo : " + user, e);
			result = "3";
		}
		log.info("End to update user, user : " + user.getUsername());
		return result;
	}

	private void transToWebToUser(WebSysUser user, UserInfo existUserInfo) {
		existUserInfo.setDepId(user.getDepartment());
		existUserInfo.setFirstName(user.getRealName());
		existUserInfo.setIsadmin("true".equalsIgnoreCase(user.getIsSuper()) ? 1
				: 0);
		// userInfo.setLastName(lastName)
		existUserInfo.setOrgId(user.getOrgId());
		// userInfo.setSex("");
		// 这个状态怎么设置？
		existUserInfo.setStatus(1);
		existUserInfo.setUserName(user.getUsername());
		// 使用sequence,SEQ_USER_INFO
		// existUserInfo.setUserId(2);
		existUserInfo.setUserPwd(user.getPassword());

	}

	public String deleteUser(WebSysUser user) {
		log.info("Begin to delete user, user : " + user.getUsername());
		try {
			UserInfo userInfo = userInfoService.findUser(user.getUsername());
			if (null == userInfo) {
				return Constant.USER_DELETE_FAILURE;
			}
			// UserInfo userInfo = transToUserForDel(user);
			if (!userInfoService.delUserInfo(userInfo)) {
				return Constant.ORG_DELETE_FAILURE;
			}
		} catch (Exception e) {
			log.error("Update user error userInfo : " + user, e);
			return Constant.ORG_DELETE_FAILURE;
		}
		log.info("End to delete user, user : " + user.getUsername());
		return Constant.ORG_DELETE_SUCCESS;
	}

	// private UserInfo transToUserForDel(WebSysUser user) {
	// UserInfo userInfo = new UserInfo();
	// userInfo.setUserName(user.getUsername());
	// return userInfo;
	// }

	public String existsUser(String userLoginId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String existsUserByEmployeeId(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
