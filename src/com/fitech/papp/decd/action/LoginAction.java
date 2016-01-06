package com.fitech.papp.decd.action;

import java.rmi.RemoteException;

import javax.servlet.http.HttpSession;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.fitech.framework.core.common.Config;
import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.papp.decd.model.pojo.Operator;
import com.fitech.papp.decd.model.vo.UserInfoVo;
import com.fitech.papp.decd.service.UserInfoService;
import com.fitech.papp.webservice.client.AuthWebServiceStub;
import com.fitech.papp.webservice.client.AuthWebServiceStub.SetSession;
import com.fitech.papp.webservice.client.AuthWebServiceStub.SetSessionResponse;
import com.fitech.papp.webservice.util.Blowfish;

/**
 * 登陆Action
 * 
 * @author wupei
 * 
 */
public class LoginAction extends DefaultBaseAction {
    private Log log = LogFactory.getLog(LoginAction.class);

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Autowired
    private UserInfoService userInfoService;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** error信息 */
    private String msg;

    private UserInfoVo userInfo;

    public String execute() {

        HttpSession session = this.getRequest().getSession();

        Operator operator = new Operator();

        if (null != session) {
            operator.setSessionId(session.getId());
        }

        operator.setIpAdd(this.getRequest().getRemoteAddr());
        String userName = this.getRequest().getParameter("username");
        log.info(Config.NEWPORTAL);
        if (!Config.NEWPORTAL && null == password && null == (this.getRequest().getSession().getAttribute("userInfo"))) {
            log.error("user login error,pass is null");
            log.info(password);
            return "error";
        }
        if (Config.NEWPORTAL && null != password) {
            log.error("pass is null");
            this.msg = "登录失败！";
            return "error";
        }
        if (Config.NEWPORTAL) {
            if (userName != null && !userName.equals("")) {
                Blowfish bf = new Blowfish();
                username = bf.decode(userName);
            }

            // debug
            System.out.println("userName:" + userName);
            try {
                userInfo = userInfoService.findUserInfo(username, password);
                if (userInfo == null) {
                    log.error("user login error ,query error");
                    return "error";
                }
                this.getRequest().getSession().setAttribute("userInfo", userInfo);
                operator.setUserName(username);
                // return SUCCESS;
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("user login error ,query error, exception:", e);
                this.msg = "登录失败！";
                return "error";
            }

        }

        // 调用portal端的鉴权
        if (Config.NEWPORTAL && null != userName && !userName.equals("")) {
            AuthWebServiceStub authStub = null;
            try {
                authStub = new AuthWebServiceStub(Config.NEWPORTALURL + "/services/AuthWebService?wsdl");
            }
            catch (AxisFault e) {
                log.error("Fail to AxisFault", e);
                return "error";

            }
            SetSession setSession = new SetSession();
            setSession.setSessionId(session.getId());
            setSession.setUsername(new Blowfish().encode(operator.getUserName()));
            SetSessionResponse ssr = null;
            try {
                ssr = authStub.setSession(setSession);
            }
            catch (RemoteException e) {
                log.error("Fail to RemoteException", e);
            }
            String rt = ssr.get_return();
            if (rt != null && rt.equals("1")) {
                // 跳转到portal的登录界面
                log.error("Fail to authStub");
                return "error";
            }
        }

        try {
            if (!StringUtil.isBlank(username) && !StringUtil.isBlank(password)) {
                // 根据用户名查询用户 判断密码是否正确
                userInfo = userInfoService.findUserInfo(username, password);
                if (userInfo == null) {
                    this.msg = "msg000001";
                    return INPUT;
                }
                this.getRequest().getSession().setAttribute("userInfo", userInfo);
                return SUCCESS;
            }
            else if (!StringUtil.isBlank(username) && Config.NEWPORTAL) {
                // 根据用户名查询用户 判断密码是否正确
                userInfo = userInfoService.findUserInfo(username, null);
                if (userInfo == null) {
                    log.error("user login error ,query error");
                    return "error";
                }
                this.getRequest().getSession().setAttribute("userInfo", userInfo);
                return SUCCESS;
            }
            else if (null != (this.getRequest().getSession().getAttribute("userInfo"))) {
                return SUCCESS;
            }

            this.msg = "msg000001";
            return INPUT;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.msg = "登录失败！";
            return INPUT;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取userInfo
     * 
     * @return userInfo userInfo
     */

    public UserInfoVo getUserInfo() {
        return userInfo;
    }

    /**
     * 设置userInfo
     * 
     * @param userInfo userInfo
     */

    public void setUserInfo(UserInfoVo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 设置msg
     * 
     * @param msg msg
     */

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
