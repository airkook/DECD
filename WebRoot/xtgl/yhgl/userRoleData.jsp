<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/public.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jquery/jquery.js" ></script>
	<script type="text/javascript">
	/*
		var addReport=function(){
			var value=$.trim(document.getElementById('allRole').value);
			if(value.length==0){
				return;
			}
			var text= document.getElementById('allRole').options[document.getElementById('allRole').selectedIndex].text;
			var selectRole=document.getElementById('selectRole');
			for(var i=0;i<selectRole.options.length;i++){
				var thisval=selectRole.options[i].value;
				if(thisval==value){
					alert("已经拥有此角色，不能重复添加!!");
					return;
				}
			}
			var op=document.createElement("option");
			op.value=value;
			op.text=text;
			selectRole.options.add(op);
		};
		
		var delReport=function(){
			var selectRole=document.getElementById('selectRole');
			selectRole.remove(selectRole.selectedIndex);
		};
		
		*/
		
		
		function addReport(){
			 var allOption = $("#allRole option:selected");
			 var all = $("#selectRole option");
			 for(var i=0;i<allOption.length;i++){
			 	var bl = false;
			 	for(var j=0;j<all.length;j++){
			 		if(allOption.eq(i).val()==all.eq(j).val()){
			 			bl = true;
			 		}
			 	}
			 	if(bl){
			 		continue;
			 	}
			 	$("#selectRole").append("<option value='"+allOption.eq(i).val()+"'>"+allOption.eq(i).html()+"</option>");
			 }
		}
		function delReport(){
			var all = $("#selectRole option:selected");
			 for(var i=0;i<all.length;i++){
			 	all.eq(i).remove();
			 }
		}
		
		
		function saveRole(){
			var selectRole=document.getElementById('selectRole');
			var nowVal="";
			for(var i=0;i<selectRole.options.length;i++){
				var thisval=selectRole.options[i].value;
				nowVal+=thisval+",";
			}
			$('#nowRoleIds').val(nowVal);
			$('#toUpdateForm').attr("action","userManager_saveUserDataInfo.action");
			$('#toUpdateForm').submit();
		}
	</script>
  </head>
  
  <body style="text-align: center;">
  		<form action="" method="post" id="toUpdateForm">
  			<input name="userId" value="${userId}" type="hidden" >
  			<input name="nowRoleIds" value="" id="nowRoleIds" type="hidden">
  		</form>
  		
    	<p class="suoyinlan" style="text-align: left;">当前位置<span>&gt;&gt;</span>权限管理<span>&gt;&gt;</span>用户操作设置</p>
		<div class="tab_div4">
		<p style="text-align:center">用户名:   ${userInfo.userName}</p>
		<p class="txt2"><strong>&nbsp;&nbsp;操作设置</strong></p>
		<div class="tab_div4" style="margin-left: 110px;">
		<table cellspacing="0" cellpadding="0" border="0" class="tab_content1">
		  <tr>
		    <td width="42%" align="center">操作</td>
		    <td width="13%"></td>
		    <td width="45%" align="center">隶属于</td>
		  </tr>
		  <tr>
		    <td align="center" valign="top">
		     <select name="allRole" multiple="multiple" size="18" style="width:180px" id="allRole"> 
				 <s:iterator value="sysDataInfoList" status='st'>
					 <option value="<s:property value='roleId'/>"><s:property value='roleName'/></option>
				 </s:iterator>
			</select>
		</td>
		    <td align="center"><p><input type="button" name="add" value="→添加" onClick="addReport()" class="input-button"></p>
		    <p><input type="button" name="delete" value="←删除" onClick="delReport()" class="input-button"></p></td>
		    <td align="center" valign="top">
		     <select name="selectedRoleIds" multiple="multiple" size="18" style="width:180px" id="selectRole">
				 <s:iterator value="userDataInfoList" status='st'>
					 <option value="<s:property value='roleId'/>"><s:property value='roleName'/></option>
				 </s:iterator>
		 	</select>
		    </td>
		  </tr>
		</table>
		</div>
		
		<div class="tab_line" style="text-align:right">
		 <input type="submit" class="buttonStyle3" value="保存" onClick="saveRole()"/>&nbsp;&nbsp;&nbsp;&nbsp;
		 <input type="submit" class="buttonStyle3" value="返回" onClick="window.location.href='userManager_init.action'"/>
		</div>
		</div>
  </body>
</html>
