<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>报表统计</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link href="<%=request.getContextPath()%>/css/table.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jquery/jquery.js" ></script>
  	<script type="text/javascript">
  		function toaddUser(){
  			window.location.href="toAddUserInfo.action";
  		}
  	</script>
  </head>
  <body>
      <p class="suoyinlan">当前位置<span>&gt;&gt;</span>系统管理<span>&gt;&gt;</span>用户管理</p>
	<form action="userManager_init.action" method="post" id="search_form">
	<table border="0" cellpadding="4" cellspacing="0" class="tab_content1">
	  <tr>
	    <td width="7%" style="color:red"></td>
	    <td width="16%" align="right">用户名(或姓名)：</td>
	    <td width="18%">
	   		 <s:textfield  name="userName"></s:textfield>
	    </td>
	    <td>
	    	<input type="submit" class="buttonStyle1" value="查询"  />&nbsp;&nbsp;&nbsp;
	    </td>
	    <td align="center"></td>
	  </tr>
	</table>
	</form>
	<table border="0" cellspacing="1" cellpadding="4" class="tab_content">
	  <tr  class="tab_content" id="tbcolor">
	    <th colspan="7" class="tab_content">用户基本信息</th>
	  </tr>
	 	 <tr class="tab_content">
	    <th width="10%">序号</th>
	    <th width="20%">用户名</th>
	    <th>姓名</th>
	    <%--<th>修改</th>--%>
	    <th >功能分配</th>
	  </tr>
	  <c:if test="${userInfoVo.totalCount == 0}">
		<tr>
			<td colspan="6" style="text-align: center;">没有符合条件的记录！</td>
		</tr>
	  </c:if>
	  <c:forEach items="${userInfoVo.results}" var="us" varStatus="i">
	  		<tr>
			    <td>${i.index+1}</td>
			    <td>${us[1]}</td>
			    <td>${us[3]}</td>
			    <%--<td><a href="userManager_edit.action?userId=${us[0]}">修改</a></td>--%>
			    <td><a href="userManager_toRoleInfo.action?userId=${us[0]}">角色分配</a>

			    </td>
		  	</tr>
		 
	  </c:forEach>
	</table>
	<!-- 
	<table cellspacing="0" cellpadding="0" width="100%" border="0" class="mar1">
	  <tr>
	    <td> 共<span class="apartpage_span">${userInfoVo.totalCount}</span>条记录
	      &nbsp;第<span class="apartpage_span">${userInfoVo.currentPage}</span>/<span class="apartpage_span">${userInfoVo.pageCount}</span>页 </td>
	    <td align="right"></td>
	  </tr>
	</table>
	 -->
	
  </body>
</html>
