<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>系统首页</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css" type="text/css" media="screen" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/invalid.css" type="text/css" media="screen" />	
<!-- jQuery -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-1.3.2.min.js"></script>
<!-- jQuery Configuration -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/simpla.jquery.configuration.js"></script>
<!-- Facebox jQuery Plugin -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/facebox.js"></script>
<!-- jQuery WYSIWYG Plugin -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.wysiwyg.js"></script>

<%request.removeAttribute("userInfo"); %>

<script language="javascript"> 
window.parent.parent.location="login.jsp";
</script>
</head>

<body>

</body>
</html>
