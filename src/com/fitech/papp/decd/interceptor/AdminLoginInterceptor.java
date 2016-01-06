package com.fitech.papp.decd.interceptor;

import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import java.util.Map;

public class AdminLoginInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 8102087153559658801L;

	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext ctx = invocation.getInvocationContext();
		Map session = ctx.getSession();

		Object user = session.get("userInfo");

		if (user != null) {
			return invocation.invoke();
		}

		return "logout";
	}

}
