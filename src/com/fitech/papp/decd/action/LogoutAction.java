package com.fitech.papp.decd.action;

import javax.servlet.http.HttpSession;

import com.fitech.framework.core.web.action.DefaultBaseAction;

public class LogoutAction extends DefaultBaseAction {

	public String execute() {
		HttpSession session = this.getHttpSession();
		session.removeAttribute("operator");
		return "success";
	}
}
