<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>报表输出</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<link href="<%=request.getContextPath()%>/css/table.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ztree.core-3.5.js"></script>
<link href="<%=request.getContextPath()%>/css/jqueryUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/jqueryUI/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery.easyui.min.js" type="text/javascript"></script>
<script type="text/javascript">
	
	function deleteRoleInfo(roleId){
			var uri="<%=request.getContextPath()%>/roleManager_isToDel_execute.action?roleId="+roleId;
			$.ajax({
				type:"POST",
				url:uri,
				cache:"false",
				data:"",
				success:function(msg){
					var ms=eval("("+msg+")");
					if(ms.flag=="1"){
						$("#roleId").val(roleId);
						if(confirm("确认是否删除")){
							$("#form1").attr("action","roleManager_deleteRoleInfo.action");
							$("#form1").submit();
						}
					}else{
						alert("该角色信息被用户占用，暂时不能做删除操作...");
					}
				}
			});
		
	}
	function editRoleInfo(roleId){
		$("#roleId").val(roleId);
		$("#form1").attr("action","roleManager_edit.action");
		$("#form1").submit();
	}
	function showRoleMenu(roleId){
		$("#roleId").val(roleId);
		$("#form1").attr("action","roleManager_showRoleMenu.action");
		$("#form1").submit();
	}
	function toAddPage(){
		window.location.href="<%=request.getContextPath()%>/xtgl/jsgl/roleAdd.jsp";
	}
</script>
</head>
<body >
<s:form action="roleManager_init.action" method="post" id="form1" name="form1" >
<s:hidden name="roleId" id="roleId"></s:hidden>
<p class="suoyinlan">当前位置<span>&gt;&gt;</span>系统管理<span>&gt;&gt;</span>角色管理</p>
<table width="100%" border="0" cellpadding="4" cellspacing="0" style="padding:10px 30px;">
  <tr>
    <td width="9%" align="right"><input onclick="toAddPage()" type="button" class="buttonStyle1" value="增加角色" /></td>
  </tr>
</table>
<table border="0" width="100%" cellspacing="1" cellpadding="4"  class="tab_content">
<tr  class="tab_content"  >
    <th colspan="5"  >角色基本信息</th>
  </tr>
  <tr  class="tab_content">
    <th width="10%">序号</th>
    <th width="20%">角色名称</th>
    <th width="21%">修改</th>
    <th width="21%">删除</th>
    <th>菜单功能分配</th>
  </tr>
  <c:if test="${roleInfoList==null}">
	<tr>
		<td colspan="5" style="text-align: center;">没有符合条件的记录！</td>
	</tr>
  </c:if>
  <s:iterator value="roleInfoList" status='st'>
    <tr>
	    <td><s:property value='roleId'/></td>
	    <td><s:property value='roleName'/></td>
	    <td><a href="javascript:void(0);" onclick="editRoleInfo(<s:property value='roleId'/>);">修改</a></td>
	    <td><a href="javascript:void(0);" onclick="deleteRoleInfo(<s:property value='roleId'/>);">删除</a></td>
	    <td><a href="javascript:void(0);" onclick="showRoleMenu(<s:property value='roleId'/>);">菜单功能分配</a></td>
    </tr>
  </s:iterator>
 
</table>
<table cellspacing="0" cellpadding="0" width="100%" border="0" class="mar1">
  <tr>
    <td> 共<span class="apartpage_span">${count}</span>条记录
            &nbsp;第<span class="apartpage_span">1</span>/<span class="apartpage_span">1</span>页 </td>
          <td align="right"></td>
  </tr>
</table>
</s:form>
</body>
</html>