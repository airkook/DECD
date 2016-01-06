<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合报送平台-用户登录</title>
<link href="<%=request.getContextPath()%>/css/style.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> 
<script type="text/javascript">
$(function(){ 
	$("#login").click(function(){
		var username = $("#username").val();
		var pass = $("#password").val();
		if(username==''){
			$("#userImg").css("visibility","visible");
			return false;
		}
		if(pass==''){
			$("#passwordImg").css("visibility","visible");
			return false;
		}
		$("#form1").submit();
	})

	$("#username").change(function(){
		var username = $("#username").val();
		if(username==''){
			$("#userImg").css("visibility","visible");
		}else{
			$("#userImg").css("visibility","hidden");
		}
	})
	
	$("#password").change(function(){
		var password = $("#password").val();
		if(password==''){
			$("#passwordImg").css("visibility","visible");
		}else{
			$("#passwordImg").css("visibility","hidden");
		}
	})
})
</script>
</head>
<body>
<s:form action="/login.action" method="post" id="form1" name="form1" >
<div class="login">
  <div class="userlogin">
    <div class="userlogin1"></div>
    <div class="userlogin2"><span class="logo"></span>
      <div class="loginbar">
        <ul>
          <li>
            <label>用户名：</label>
            <span class="user">
            <input type="text"  name="username" class="logininput" id="username" value="<s:property value='username'/>"/>
            </span>
             <img src="<%=request.getContextPath()%>/images/warning.gif"
												width="16" height="16" style="visibility: hidden;" title="请输入用户名" id="userImg"/>
          </li>
          <li>
            <label>密&nbsp;&nbsp;&nbsp;码：</label>
            <span class="password">
           	 <input name="password" type="password" class="logininput" id="password" />
            </span>
            <s:if test="msg!=null">
              <img src="<%=request.getContextPath()%>/images/warning.gif"
												width="16" height="16"  title="请输入正确的密码" id="passwordImg"/>           
            </s:if>
            <s:if test="msg!=null">
              <img src="<%=request.getContextPath()%>/images/warning.gif"
												width="16" height="16" style="visibility: hidden;" title="请输入正确的密码" id="passwordImg"/>               
            </s:if>
            </li>
          <li><span>
            <input type="button" value="" class="loginbtn" id="login"/>
            </span></li>
        </ul>
      </div>
    </div>
  </div>
</div>
</s:form>
</body>
</html>