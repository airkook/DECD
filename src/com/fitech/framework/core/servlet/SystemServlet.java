package com.fitech.framework.core.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.jfree.util.Log;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fitech.framework.core.common.Config;

/**
 * @author wupei
 */
public class SystemServlet extends HttpServlet {

    /**
     * uid
     */
    private static final long serialVersionUID = 1L;

    /**
     * ServletContext
     */
    private final ServletContext context = null;

    /**
     * 
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * 
     * @return void
     * @exception IOException ,ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Properties prop = new Properties();

        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        }
        catch (Exception e) {
            Log.error("Fail to load property", e);
        }
        ServletContext servletContext = this.getServletContext();

        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        try {
            // 系统根路径
            Config.WEBROOTPATH = servletContext.getRealPath("/");

            // 是否连接PORTAL系统
            Config.NEWPORTAL = new Boolean(prop.getProperty("new_portal"));
            System.out.println("是否连接PORTAL系统:" + Config.NEWPORTAL);

            // PORTAL系统URL
            Config.NEWPORTALURL = prop.getProperty("new_portal_url");
            System.out.println("PORTAL系统URL:" + Config.NEWPORTALURL);

            // 是否分发
            Config.ISADAPTER = new Boolean(prop.getProperty("isAdapter"));
            System.out.println("是否分发:" + Config.ISADAPTER);

            // 分发URL
            Config.ADAPTERURL = (prop.getProperty("dapterUr"));
            System.out.println("分发URL:" + Config.ADAPTERURL);
        }
        catch (Exception e) {
            e.printStackTrace();
            Config.NEWPORTAL = false;
            Config.NEWPORTALURL = "";
        }
        // end
        Enumeration en = prop.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = prop.getProperty(key);
            // System.out.println(key + "=" + Property);
            Config.confMap.put(key, Property);
        }

    }

    @Override
    public void destroy() {

    }
}
