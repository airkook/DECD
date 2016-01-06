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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/public.js"></script>
<script type="text/javascript">
$(function(){ 
 $("#save").click(function(){
 	var all = $("#selectReport option");
 	 $("#hidDiv").empty();
	 for(var i=0;i<all.length;i++){
	 	$("#hidDiv").append("<input type='hidden' name='selectReport' value='"+all.eq(i).val()+"' />");
	 }
	 $("#form1").submit();
	 return false;
 })
 $("#backInit").click(function(){
 	 $("#form1").attr("action","roleManager_init.action");
 	 $("#form1").submit();
 })
})
function addReport(){
 var allOption = $("#allMenu option:selected");
 var all = $("#selectReport option");
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
 	$("#selectReport").append("<option value='"+allOption.eq(i).val()+"'>"+allOption.eq(i).html()+"</option>");
 }
}
function delReport(){
	var all = $("#selectReport option:selected");
	 for(var i=0;i<all.length;i++){
	 	all.eq(i).remove();
	 }
}
</script>
</head>
<body style="text-align: center;">
<s:form action="roleManager_saveRoleMenu.action" method="post" id="form1" name="form1" >
<div id="hidDiv"></div>
<input type='hidden' name="roleId" value="${roleInfo.roleId}">
<div style=" text-align:left;">
<p class="suoyinlan">当前位置<span>&gt;&gt;</span>系统管理<span>&gt;&gt;</span>角色管理</p>
</div>
<div class="tab_div4">
<p style="text-align:center">角色名称:   ${roleInfo.roleName}</p>
<p class="txt2"><strong>角色权限设置</strong></p>
<div class="tab_div4" style="">
<table cellspacing="0" cellpadding="0" border="0" class="tab_content1" style="margin-left: 80px;">
  <tr>
    <td width="42%" align="center">全部功能</td>
    <td width="13%"></td>
    <td width="45%" align="center">具备的功能</td>
  </tr>
  <tr>
    <td align="center" valign="top">
     <select name="menuId" multiple="multiple" size="18" style="width:250px" id="allMenu">
     	 <s:iterator value="menuAllList" status='st'>
   		  <option value="<s:property value='menuId'/>"><s:property value='menuDesc'/></option>
   		  </s:iterator>
 	</select>
</td>
    <td align="center"><p><input type="button" name="add" value="→添加" onClick="addReport()" class="input-button"></p>
    <p><input type="button" name="delete" value="←删除" onClick="delReport()" class="input-button"></p></td>
    <td align="center" valign="top">
     <select name="selectRepList" multiple="multiple" size="18" style="width:300px" id="selectReport">
          <s:iterator value="menuList" status='st'>
   		 	 <option value="<s:property value='menuId'/>"><s:property value='menuDesc'/></option>
   		  </s:iterator>
    </select>
    </td>
  </tr>
</table>
</div>

<div class="tab_line" style="text-align:right">
 <input type="submit" class="buttonStyle3" value="保存" id="save"/>&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" class="buttonStyle3" value="返回" id="backInit"/>
</div>
</div>
</s:form>
</body>
</html>