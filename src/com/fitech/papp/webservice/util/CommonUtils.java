package com.fitech.papp.webservice.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



public class CommonUtils {
	public static Logger log = Logger.getLogger(CommonUtils.class);

	// check user
//	public static boolean checkUser(OperatorForm user) {
//		user.setAddress(null == user.getAddress() ? "" : user.getAddress());
//		user.setAge(null == user.getAge() ? "" : user.getAge());
//		user.setFirstName(null == user.getFirstName() ? "" : user
//				.getFirstName());
//		user.setLastName(null == user.getLastName() ? "" : user.getLastName());
//		user.setMail(null == user.getMail() ? "" : user.getMail());
//		user.setTitle(null == user.getTitle() ? "" : user.getTitle());
//		//user.setSex(null==user.getSex()?"男":user.getSex());
//		// check password
//		if (StringUtils.isEmpty(user.getPassword())) {
//			log.error("User password can't be null, user : " + user);
//		}
//		if (StringUtils.isEmpty(user.getOrgId())) {
//			log.error("User OrgId can't be null, user : " + user);
//		}
//		if (StringUtils.isEmpty(user.getUserName())) {
//			log.error("User UserName can't be null, user : " + user);
//		}
//
//		return true;
//	}
}
