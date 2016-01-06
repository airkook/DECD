<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表输出</title>
<link href="<%=request.getContextPath()%>/css/table.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/jqueryUI/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>


<script type="text/javascript">

$(function(){ 
var msg = '${message}';
	if(msg!=''){
		alert(msg);
	}
});

//查询 
function doSearch(){
	var term=$("#term").val();
	window.location.href="dataReport_search.action?term="+term;
}

//本地生成报文
function writeReport(reportOrgName,reportOrgId,allCount,checkSucCount) {
    if(allCount=="0"){
		alert("此期数据记录数为0，不能生成报表");
		return;
	}
	if(allCount!=checkSucCount){
		alert("此期数据没有全部审核通过，不能生成报表");
		return;
	}
	$("#write").attr("disabled",true);
	$("#reportOrgName").val(reportOrgName);
	$("#reportOrgId").val(reportOrgId);
	$("#form1").attr("action","dataReport_writeReport.action");
    $("#form1").submit();
}

//上传到ftp
function uploadReport(reportOrgId,reportStatus){
	if(reportStatus==""){
	 	alert("请先生成报表");
      	return;
	}
	if(reportStatus=="4"){
	 	alert("已修改数据，请重新生成报表");
      	return;
	}
	if(reportStatus=="2"){
	 	alert("已上报并且反馈结果为成功，无须重新上报");
      	return;
	}
	$("#upload").attr("disabled",true);
	$("#reportOrgId").val(reportOrgId);
	$("#form1").attr("action","dataReport_uploadReport.action");
    $("#form1").submit();
}

//下载反馈文件
 function touckFeedBack(reportOrgId,reportStatus){
	 if(reportStatus==""){
		 	alert("请先生成报表");
	      	return;
		}
		if(reportStatus=="4"){
		 	alert("已修改数据，请重新生成报表");
	      	return;
		}
		if(reportStatus=="0"){
		 	alert("请先进行上报");
	      	return;
		}
		if(reportStatus=="2"){
		 	alert("已上报并且反馈结果为成功，无须重新抓取反馈");
	      	return;
		}
		$("#feedback").attr("disabled",true);
     $.ajax({
         type: "POST",
		 url:"<%=basePath%>dataReport_touckFeedBack.action",
		 dataType:"json",
		 data:{term:$('#term').val(),reportOrgId:reportOrgId},
		 success: function(data){
				  if(data.result=="error"){
            		    alert(data.message);
            		  }
            		else{
                		alert("抓取反馈完成");
             			//window.location.href="dataReport_doDownLoad.action?term="+$('#term').val()+"&reportOrgId="+reportOrgId; 
             			doSearch();
            		}
				  $("#feedback").attr("disabled",false);
		   }
 } );
		
}



</script>
</head>

<body>
	<p class="suoyinlan">
		当前位置：数据报送<span>&gt;&gt;</span>数据上报
	</p>
	<s:form method="post" id="form1" name="form1">
		<table width="100%" border="0" cellpadding="4" cellspacing="0"
			class="tab_content1">
			<tr>
				<td width="20%" align="left">期数： <input type="text" name="term"
					size="12" value="${term}"
					onClick="return showCalendar('term', 'y-mm-dd');"
					readonly="readonly" style="text" id="term"> 
					<img src="<%=request.getContextPath()%>/images/calendar.gif" border="0"
					onClick="return showCalendar('term', 'y-mm-dd');">
				</td>
				<td><input type="button" class="buttonStyle1" value="查&nbsp;询"
					onclick="doSearch()" /> &nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
		<table border="0" cellspacing="1" class="tab_content">
			<tr>
				<th>报表名称</th>
				<th>上报机构</th>
				<th>状态</th>
				<th>数据记录数</th>
				<th>审核通过数</th>
				<th>操作</th>
			</tr>
			<s:iterator value="dataReportList" var="drt">
				<tr>
					<td>大额存单</td>
					<td><s:property value="#drt.reportOrgName" />
					</td>
					<td><s:property value="#drt.reportStatusDesc" />
					</td>
					<td><s:property value="#drt.allCount" />
					</td>
					<td><s:property value="#drt.checkSucCount" />
					</td>
					<td><input type="button" class="buttonStyle1" id="write" value=" 生成报表"
						onclick="writeReport('<s:property value="#drt.reportOrgName"/>','<s:property value="#drt.reportOrgId"/>','<s:property value="#drt.allCount"/>','<s:property value="#drt.checkSucCount" />')">&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="buttonStyle1" id="upload" value=" 上报 " 
						onclick="uploadReport('<s:property value="#drt.reportOrgId"/>','<s:property value="#drt.reportStatus"/>')">&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="buttonStyle1" id="feedback" value=" 抓取反馈 "  
						onclick="touckFeedBack('<s:property value="#drt.reportOrgId"/>','<s:property value="#drt.reportStatus"/>')">
					</td>

				</tr>
			</s:iterator>
			<input type="hidden" value="" id="reportOrgName" name="reportOrgName" />
			<input type="hidden" value="" id="reportOrgId" name="reportOrgId" />
		</table>
	</s:form>
</body>
</html>