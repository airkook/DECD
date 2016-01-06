package com.fitech.papp.webservice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fitech.framework.core.common.Config;
import com.fitech.papp.webservice.client.AuthWebServiceStub;
import com.fitech.papp.webservice.util.Blowfish;

/**
 * 权限过滤器
 * 
 * @author Chris
 * 
 */
public class AuthFilter implements Filter {

	private ServletContext application = null;
	public static Logger log = Logger.getLogger(AuthFilter.class);

	/**
     *
     */

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		String url = ((HttpServletRequest) req).getRequestURI();
		HttpServletResponse response = (HttpServletResponse) res;
		// response.setHeader("P3P","CP=CAO PSA OUR");//在IE浏览器frame框架下面session容易丢失，加上此方法可以解决
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		String path = request.getContextPath();
		if (application == null) {

			application = request.getSession().getServletContext();
		}
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path;
		log.info("url : " + url);
		System.out.println("url : " + url);
		if (Config.NEWPORTAL) {
			if ((url.indexOf("login.do") < 0)
					&& (url.indexOf("login.action") < 0)
					&& (url.indexOf("services") < 0)
					&& (url.indexOf("login.jsp") < 0)
					&& (url.indexOf("logout.jsp") < 0)) {
				String sessionId = session.getId();

				AuthWebServiceStub authStub = new AuthWebServiceStub(
						Config.NEWPORTALURL + "/services/AuthWebService?wsdl");
				AuthWebServiceStub.IsExist isExist = new AuthWebServiceStub.IsExist();
				isExist.setSessionId(sessionId);
				AuthWebServiceStub.IsExistResponse ssr = authStub
						.isExist(isExist);

				if ("0".equals(new Blowfish().decode(ssr.get_return()))) {
					response.sendRedirect(basePath + "/logout.jsp");
					return;
				}
				// 0不存在 如果存在，返回用户名
				// 解密用户名
				Blowfish bf1 = new Blowfish();
				String userName = bf1.decode(ssr.get_return());

				if (null == session
						.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)) {
					ServletContext application = session.getServletContext();
					HttpSession otherSession = (HttpSession) application
							.getAttribute(userName);
					if (otherSession != null) {
						if (session != otherSession) {

							try {

								otherSession.invalidate();
							} catch (Exception e) {
								System.out.println("session已注销");
							}
							otherSession = null;
						}
						application.removeAttribute(userName.trim());
					}

					session.setAttribute(
							Config.OPERATOR_SESSION_ATTRIBUTE_NAME,
							userName.trim());
					application.setAttribute(userName.trim(), session);
				}

			}
		}
		chain.doFilter(req, res);
	}

	public void destroy() {

	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}
}
