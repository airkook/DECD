<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>报表输出</title>
<link href="<%=request.getContextPath()%>/css/table.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> 
<script type="text/javascript">
	$(function(){ 
		 $("#save").click(function(){
			 $("#save").attr("disabled",true);
		 	var newRoleName=$.trim($('#newRoleName').val());
		 	var id='${roleInfo.roleId}';
		 	if(newRoleName.length==0){
		 		alert("角色名称不能为空...");
		 		$("#save").attr("disabled",false);
		 		return;
		 	}
		 	if(newRoleName.length>10){
		 		alert("角色名称不能超过10个字符...");
		 		$("#save").attr("disabled",false);
		 		return;
		 	}
		    var uri="<%=request.getContextPath()%>/roleManager_saveRoleInfo.action";
			$.ajax({
				type:"POST",
				url:uri,
				cache:"false",
				dataType:"json",
				data:{"newRoleName":newRoleName,"id":id},
				success:function(msg){
					//alert(msg.flag);
					//var ms=eval("("+msg+")");
					if(msg.flag=="1"){
						window.location.href="<%=request.getContextPath()%>/roleManager_init.action";
					}else if(msg.flag="0"){
						alert("角色名称已经存在不能重复...");
					}else{
						alert("内部错误...");
					}
					$("#save").attr("disabled",false);
				}
				});
			});
		 });
</script>
</head>
<body style="text-align: center;">
<s:form action="roleManager_saveRoleInfo.action" method="post" id="form1" name="form1" >
<p class="suoyinlan" style="text-align: left;">当前位置<span>&gt;&gt;</span>权限管理<span>&gt;&gt;</span>角色信息修改</p>
<s:hidden name="roleInfo.roleId"></s:hidden>
     <div class="tab_div4">
      <table class="tab_content1">
		  <tr>
		    	<td colspan="2" align="center" class="tab_content" style="background-color: #c3eaa6"  ><strong>角色信息修改</strong></td>
		  </tr>
		  <tr>
			    <td width="43%" align="right">原角色名称:</td>
			    <td width="57%">
			    	<input type="text" value="${roleInfo.roleName}" disabled="disabled"/>
			    </td>
		  </tr>
		  <tr>
			    <td align="right">新角色名称:</td>
			    <td>
			     	<input type="text" name="roleName" id="newRoleName" />
			    </td>
		  </tr>
		  <tr>
		    <td colspan="2">
			     <div class="tab_line layout6">
				    <input type="button" class="buttonStyle1" value="保存" id="save"/>&nbsp;&nbsp;&nbsp;&nbsp;
				    <input type="button" class="buttonStyle1" value="返回" onClick="Javascript:history.go(-1)"/>
				 </div>
		    </td>
		  </tr>
      </table>
 </div>
</s:form>
</body>
</html>