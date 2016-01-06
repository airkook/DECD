<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>校验设置</title>
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
$(function(){
   $("#search").bind("click",function(){
        $("#search").attr("disabled",true);
		$("#form1").attr("action","validateInfoManager_findByType.action");
		$("#form1").submit();
   });
});
</script>
</head>
 <body>
 <s:form action="validateInfoManager_init.action" method="post" id="form1" name="form1" >
<p class="suoyinlan">当前位置<span>&gt;&gt;</span>校验设置<span>&gt;&gt;</span>校验规则查看</p>
<table border="0" cellpadding="4" cellspacing="0" class="tab_content1">
	  <tr>
	    <td width="50%" align="left">校验类型：
	    <s:select list="validateTypeMap" name="validateType" id="validateType" style="width:120px;"/>
	    </td>
	    <td>
	    	<input type="button" id="search" class="buttonStyle1" value="查询" />&nbsp;&nbsp;&nbsp;
	    </td>
	    <td align="center"></td>
	  </tr>
</table>
<table border="0" width="100%" cellspacing="1" cellpadding="4"  class="tab_content">
  <tr  class="tab_content">
    <th>序号</th>
    <th>字段名</th>
    <th>字段中文名</th>
    <th>校验公示</th>
    <th>校验描述</th>
    <th>校验类型</th>
  </tr>
  <s:if test="validateInfoList==null || validateInfoList.size==0">
	<tr>
		<td colspan="6" style="text-align: center;">没有符合条件的记录！</td>
	</tr>
  </s:if>
  <s:iterator value="validateInfoList" status='st'>
    <tr>
	    <td width="40px"><s:property value='validateInfoId'/></td>
	    <td width="80px"><s:property value='fieldEnName'/></td>
	    <td width="150px"><s:property value='fieldName'/></td>
	    <td style="word-break:break-all"><s:property value='validateFormula'/></td>
	    <td><s:property value='validateDesc'/></td>
	    <td width="80px">
		    <s:if test="validateType==1">基本校验</s:if>
		    <s:if test="validateType==2">表内校验</s:if>
		    <s:if test="validateType==3">表间校验</s:if>
		    <s:if test="validateType==4">合计校验</s:if>
	    </td>
    </tr>
  </s:iterator>
</table>
</s:form>
</body>
</html>