<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>数据监控</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
	<link href="<%=request.getContextPath()%>/css/table.css"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/zTreeStyle/zTreeStyle.css"
	type="text/css">
<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.ztree.core-3.5.js"></script>
<link
	href="<%=request.getContextPath()%>/css/jqueryUI/themes/default/easyui.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/jqueryUI/themes/icon.css"
	rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"
	type="text/javascript"></script>
	
		<script language="javascript">
		function changeShow(){
		  $("#dataImport").html("<span>入库记录数为<s:property value="dataMonitorVo.dataImportStatus" /></span>"); 
		}
		function mouseOut(flag){
		 if(flag > 0){
		 $("#dataImport").html("<img src='<%=request.getContextPath()%>/images/jd_normal.gif'/>"); 
		}else{
		$("#dataImport").html("<img src='<%=request.getContextPath()%>/images/jd_alarm.gif'/>"); 
		}
		}	
		function changeShow2(){
		  $("#auditPass").html("<span>审核通过数为<s:property value="dataMonitorVo.auditPassStatus" /></span>"); 
		}
		function mouseOut2(flag){
		 if(flag > 0){
		 $("#auditPass").html("<img src='<%=request.getContextPath()%>/images/jd_normal.gif'/>"); 
		}else{
		$("#auditPass").html("<img src='<%=request.getContextPath()%>/images/jd_alarm.gif'/>"); 
		}
		}	
		function changeShow3(){
		  $("#reportStatus").html("<span>上报数为<s:property value="dataMonitorVo.reportStatus" /></span>"); 
		}
		function mouseOut3(flag){
		 if(flag > 0){
		 $("#reportStatus").html("<img src='<%=request.getContextPath()%>/images/jd_normal.gif'/>"); 
		}else{
		$("#reportStatus").html("<img src='<%=request.getContextPath()%>/images/jd_alarm.gif'/>"); 
		}
		}
		function changeShow4(){
		  $("#feedbackStatus").html("<span>反馈数为<s:property value="dataMonitorVo.feedbackStatus" /></span>"); 
		}
		function mouseOut4(flag){
		if(flag > 0){
		 $("#feedbackStatus").html("<img src='<%=request.getContextPath()%>/images/jd_normal.gif'/>"); 
		}else{
		$("#feedbackStatus").html("<img src='<%=request.getContextPath()%>/images/jd_alarm.gif'/>"); 
		}
		 
		}
		//查询 
		 function doSearch(){
			   term=$("#term").val();
			window.location.href="dateMonitor_search.action?term="+term;
			}
				
		</script>
	</head>
	<body>
		<p class="suoyinlan">当前位置：数据监控<span>&gt;&gt;</span>数据监控</p>
		<div id="page1">
			<!--查询--->
				<form id="form"  action="" method="post">
					<table width="99%" cellspacing="0" cellpadding="0" border="0" class="tab_content1">
						<tbody>
							<tr height="30" align="left">
								<td width="20%" align="left">报送期数： <input type="text" name="term"
										size="12" value="${term}"
										onClick="return showCalendar('term', 'y-mm-dd');"
										readonly="readonly" style="text" id="term"> <img
										src="<%=request.getContextPath()%>/images/calendar.gif" border="0"
										onClick="return showCalendar('term', 'y-mm-dd');">
								</td>
								<td align="center">
									<input type="button" class="buttonStyle1" value="查 询 " onclick="doSearch()">
								</td>
							</tr>
						</tbody>
					</table>
					<table width="99%" cellspacing="0" cellpadding="0" border="0">
						<tbody>
							<tr height="30">
								<td width="10%" align="right">完成：</td>
				  				<td width="15%" align="left">&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/jd_normal.gif"/></td>
								<td width="7%" align="right">未完成：</td>
			      				<td width="15%" align="left">&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/jd_alarm.gif"/></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<table border="0" width="100%" cellspacing="1" cellpadding="4" class="tab_content">
					  <tr>
					  	 	<th width="20%" rowspan="2" class="tableHeader" >报表名称</th>
					  	 	<th colspan="4" class="tableHeader">进程</th>
			  	 	  </tr>
			  	 	  <tr>
							<th width="8%"  class="tableHeader">入库</th>
					  	 	<th width="8%"  class="tableHeader">审核</th>
					  	 	<th width="8%"  class="tableHeader">上报</th>
					  	 	<th width="8%"  class="tableHeader">反馈</th>
			  	 	  </tr>
			<s:if test="dataMonitorVo==null">
				<tr>
					<td align="center" colspan="10" bgcolor="white" nowrap="nowrap">
						没有符合条件的记录</td>
				</tr>
			</s:if>
			<s:else>
			<tr height="35px">
			        <td nowrap="nowrap">大额存单报表</td>
					<td nowrap="nowrap" id="dataImport" title="<s:property value="dataMonitorVo.dataImportTips"/>">
						<s:if test="dataMonitorVo.dataImportStatus gt 0">
						  <img  src="<%=request.getContextPath()%>/images/jd_normal.gif" />
						</s:if>	
						<s:else>
						 <img src="<%=request.getContextPath()%>/images/jd_alarm.gif"/>
						</s:else>
					</td>
					<td nowrap="nowrap" id="auditPass" title="<s:property value="dataMonitorVo.auditTips"/>">
					    <s:if test="dataMonitorVo.auditPassStatus gt 0">
						  <img src="<%=request.getContextPath()%>/images/jd_normal.gif"/>
						</s:if>
						<s:else>
						 <img src="<%=request.getContextPath()%>/images/jd_alarm.gif"/>
						</s:else>
					 </td>
					<td nowrap="nowrap" id="reportStatus" title="<s:property value="dataMonitorVo.reportTips"/>">
					   <s:if test="dataMonitorVo.reportStatus gt 0">
						  <img src="<%=request.getContextPath()%>/images/jd_normal.gif"/>
						</s:if>
						<s:else>
						 <img src="<%=request.getContextPath()%>/images/jd_alarm.gif"/>
						</s:else>
					</td>
					<td nowrap="nowrap" id="feedbackStatus" title="<s:property value="dataMonitorVo.feedbackTips"/>">
					    <s:if test="dataMonitorVo.feedbackStatus gt 0">
						  <img src="<%=request.getContextPath()%>/images/jd_normal.gif"/>
						</s:if>
						<s:else>
						 <img src="<%=request.getContextPath()%>/images/jd_alarm.gif"/>
						</s:else>
					</td>
				</tr>
			</s:else>
			
			
			</table>
<font color="red">提示：鼠标悬停显示详细信息</font> 
	</body>
</html>
