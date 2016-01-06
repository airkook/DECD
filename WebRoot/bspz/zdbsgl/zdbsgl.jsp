<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>自动报送管理</title>
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

});
	function doUse(reportOrgId,flag,time,isUsed){
	if(isUsed == 1){
	   isUsed = 0;
	}else{
	   isUsed = 1;
	 }
     $("#reportOrgId").val(reportOrgId);
     $("#flag").val(flag);
     $("#time").val(time);
     $("#isUsed").val(isUsed);//
 	 //window.location.href="autoReportManage_doUse.action?reportOrgId="+reportOrgId+"&flag="+flag+"&time="+time+"&isUsed="+isUsed;
     $("#form1").submit();   
}
function changeTime(id1,reportOrgId,flag){
	var timeValue = document.getElementById("time"+id1).value;
	window.location.href="autoReportManage_changeTime.action?timeValue="+timeValue+"&reportOrgId="+reportOrgId+"&flag="+flag;
}
</script>
</head>
<body >
<s:form action="autoReportManage_doUse.action" method="post" id="form1" name="form1" >
<p class="suoyinlan">当前位置<span>&gt;&gt;</span>报送配置<span>&gt;&gt;</span>自动报送管理</p>

<table border="0" width="100%" cellspacing="1" cellpadding="4"  class="tab_content">

  <tr  class="tab_content">
  <!--   <th width="10%">序号</th> -->
    <th width="20%">上报机构</th>
    <th width="21%">任务类型</th>
    <th width="21%">时间</th>
    <th width="21%">启用状态</th>
    <th width="21%">操作</th>
    
  </tr>
  <s:iterator value="autoReportInfoVoList" status="st" var="arivl">
<tr>
 <td width="20%"><s:property value="#arivl.reportOrgName"/></td>
 <td width="20%">
	   <s:if test="#arivl.flag == 1">
	   报送
	     <s:property value="报送"/>
	   </s:if>
	    <s:else>
	    抓取反馈
	       <s:property value="抓取反馈"/>
	    </s:else>
 </td>
 
 <td width="21%">
<%--  <s:select list="timeMap" name="time" onchange="changeHidenValue(this)" id='time<s:property value="#st.count"/>'/> --%>
 		<select  id="time<s:property value="#st.count"/>" style="width: 100px;" onchange="changeTime('<s:property value="#st.count"/>','<s:property value="#arivl.reportOrgId"/>','<s:property value="#arivl.flag"/>')" >
 			<s:iterator value="timeMap" id="timeMap">
			    <option value="<s:property value="key"/>" 
			    
			    <s:if test="key==#arivl.time">selected</s:if>>
			       <s:property value="value"/>
			       
			    </option>   
			</s:iterator>
		</select>

 </td>
  <td width="20%">
   <s:if test="#arivl.isUsed == 0">
	   未启用
	   </s:if>
	    <s:else>
	  已启用 
	    </s:else>
  </td>
  
  
    <td width="21%">
        <s:if test="#arivl.isUsed== 0">
                 <input type="button"  class="buttonStyle1" onclick="doUse('<s:property value="#arivl.reportOrgId"/>','<s:property value="#arivl.flag"/>','<s:property value="#arivl.time"/>','<s:property value="#arivl.isUsed"/>')" value="启&nbsp&nbsp&nbsp用"/>
		</s:if>	
	     <s:if test="#arivl.isUsed== 1">
                 <input type="button"  class="buttonStyle1" onclick="doUse('<s:property value="#arivl.reportOrgId"/>','<s:property value="#arivl.flag"/>','<s:property value="#arivl.time"/>','<s:property value="#arivl.isUsed"/>')" value="停&nbsp&nbsp&nbsp用"/>
		</s:if>				
    </td>
</tr>
 </s:iterator>
  <input type="hidden" name="reportOrgId" id="reportOrgId"/>
  <input type="hidden" name="flag" id="flag"/>
  <input type="hidden" name="time" id="time"/>
  <input type="hidden" name="isUsed" id="isUsed"/>
  
</table>
</s:form>
</body>
</html>