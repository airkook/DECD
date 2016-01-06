<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%@ page import="com.fitech.framework.core.common.Config" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>综合报送平台</title>
<script>

</script>
</head>
	<%
		if(Config.NEWPORTAL){
	%>
	<frameset rows="*" frameborder="0" border="0">
	<%
		}else{
	%>
	<frameset rows="90,*" frameborder="0" border="0">
	<frame name="header" src="header.html"> 
	<%
		}
	%>
	<frame name="main_all" id="frame_totle" src="index.jsp">
</frameset><noframes></noframes>

</html>
