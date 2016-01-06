<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
		function backPage(){
			window.location="userManager_init.action";
		}
		
		function save(){
			var password1= $.trim($('#cur_password').val());
			var password2= $.trim($('#cur_password_rel').val());
			var lastName = $.trim($('#lastName').val());
			var firstName=$.trim($('#firstName').val());
			if(password1!=password2){
				alert("两次密码不一致!请检查...");
				return;
			}
			if(lastName==''){
				alert("名字不能为空!");
				return;
			}
			if(firstName==''){
				alert("姓不能为空!");
				return;
			}
			if(password1.length>12){
				alert("密码长度不能超过12位字符...");
				return;
			}
			if(firstName.length>2){
				alert("姓不能超过2个字符长度...");
				return;
			}
			if(lastName.length>3){
				alert("名字不能超过3个字符长度...");
				return;
			}
			$('#this_password').val(password1);
			$('#this_lastName').val(lastName);
			$('#this_firstName').val(firstName);
			$('#form_update').attr("action","userManager_update.action");
			$('#form_update').submit();
		}
		
	</script>
	
  </head>
  
  <body style="text-align: center;">
  		<form action="" method="post" id="form_update">
  			<input type="hidden" name="userId" id="userId" value="${userInfoVo.results[0][0]}">
  			<input type="hidden" name="thispassword" id="this_password">
  			<input type="hidden" name="thisLastName" id="this_lastName" >
  			<input type="hidden" name="thisFirstName" id="this_firstName">
  		</form>
  
    	<p class="suoyinlan" style="text-align: left;">当前位置<span>&gt;&gt;</span>权限管理<span>&gt;&gt;</span>用户详细修改</p>
		     <div class="tab_div4">
		      <table class="tab_content1">
		  <tr>
		    <td colspan="6" align="center" class="td_bt" style="background-color: #c3eaa6"><strong>用户详细修改</strong></td>
		  </tr>
		  <tr>
		    <td colspan="6"><p class="txt2"><strong>&nbsp;&nbsp;帐户信息</strong></p></td>
		  </tr>
		  <tr>
		    <td width="15%" align="right">用户名</td>
		    <td><input type="text" value="${userInfoVo.results[0][1]}" disabled="disabled">
		      <strong><font class="txt_red"> * </font></strong></td>
		    <%--<td>&nbsp;</td>
		     <td align="right">密码</td>
		      <td><input type="password" id="cur_password">
		      <strong><font class="txt_red"> * </font></strong></td>--%>
		   </tr>
		  <%--<tr>
		     
		      <td align="right">重复密码</td>
		      <td colspan="3"><strong><font class="txt_red"> 
		      <input type="password" name="password" maxlength="20" size="20" value="" class="input-text" id="cur_password_rel">
		      * 如不修改密码请保持密码为空！</font></strong></td>
		    </tr>
		  --%>
		  <tr>
		    <td colspan="6"><p class="txt2"><strong>&nbsp;&nbsp;用户信息</strong></p></td>
		  </tr>
		  <tr>
		    <td width="12%" align="right">姓</td>
		    <td width="20%"><input id="firstName" type="text" value="${userInfoVo.results[0][2]}">
		     	<strong><font class="txt_red"> * </font></strong>
		    </td>
		    <td width="10%" align="right">名</td>
		    <td width="20%"><input type="text" id="lastName" value="${userInfoVo.results[0][3]}" /><strong><font class="txt_red"> * </font></strong> </td>
		    <td align="right">性别</td>
		    <td>
		    	<select name="sex" id="sex">
		    		<c:if test="${userInfoVo.results[0][4]==1}">
		    			<option value="${userInfoVo.results[0][4]}">男</option>
		    		</c:if>
		    		<c:if test="${userInfoVo.results[0][4]!=1}">
		    			<option value="${userInfoVo.results[0][4]}">女</option>
		    		</c:if>
			    </select>
			</td>
		  </tr>
		  <tr>
		    <td colspan="5">&nbsp;</td>
		    <td>
		    <input type="button" class="buttonStyle3" value="保存" onClick="save()"/>&nbsp;&nbsp;&nbsp;&nbsp;
		    <input type="button" class="buttonStyle3" value="返回" onClick="backPage()"/></td>
		  </tr>
		  </table>
		</div>
  </body>
</html>
