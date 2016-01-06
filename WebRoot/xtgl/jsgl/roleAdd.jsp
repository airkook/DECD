<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
		function addRoleInfo(){
			var roleName=$.trim($('#roleName').val());
			if(roleName.length>10){
				alert("角色名称不能超过10个字符...");
				return;
			}
			if(roleName==""){
				alert("角色名称不能为空!!");
				return;
			}
			$("#saveBut").attr("disabled",true);
			$('#add_roleName').val(roleName);
			var uri="<%=request.getContextPath()%>/roleManager_addRoleInfo.action";
			$.ajax({
				type:"POST",
				url:uri,
				cache:"false",
				data:$('#add_role_form').serialize(),
				success:function(msg){
					var ms=eval("("+msg+")");
					if(ms.flag=="1"){
						alert("添加角色信息成功!");
						window.location.href="<%=request.getContextPath()%>/roleManager_init.action";
					}else if(ms.flag="0"){
						alert("角色名称已经存在不能重复...");
					}else{
						alert("内部错误...");
					}
					$("#saveBut").attr("disabled",false);
				}
			});
		}
		
		function backPage(){
			window.location.href="<%=request.getContextPath()%>/roleManager_init.action";
		}
	
	</script>
  </head>
  
  <body>
  	<form action="" method="post" id="add_role_form">
  		<input type="hidden" name="roleInfo.roleName" id="add_roleName" >
  	</form>
    <p class="suoyinlan">当前位置<span>&gt;&gt;</span>权限管理<span>&gt;&gt;</span>角色信息添加</p>
     <div class="tab_div4">
      <table class="tab_content1" style="margin-left:100px;">
		  <tr>
		    <td colspan="2" align="center" class="tab_content" style="background-color: #c3eaa6"  ><strong>角色信息添加</strong></td>
		  </tr>
		  <tr>
		    <td width="43%" align="right">角色名称:</td>
		    <td width="57%"><input id="roleName" type="text" value="" /></td>
		  </tr>
		  <tr>
		    <td colspan="2">
		     <div class="tab_line layout6">
			  	<input type="button" id="saveBut" onclick="addRoleInfo()" class="buttonStyle1" value="保存"/>&nbsp;&nbsp;&nbsp;&nbsp;
			    <input type="button" class="buttonStyle1" value="返回" onClick="backPage()"/>
			</div>
		    </td>
		  </tr>
      </table>
    </div>
  </body>
</html>
