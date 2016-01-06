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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="<%=request.getContextPath()%>/css/table.css"
			rel="stylesheet" type="text/css" />
		<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
		<link
			href="<%=request.getContextPath()%>/css/jqueryUI/themes/default/easyui.css"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=request.getContextPath()%>/css/jqueryUI/themes/icon.css"
			rel="stylesheet" type="text/css" />
		<script src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"
			type="text/javascript"></script>
<script type="text/javascript">
		
		//初始化
		$(function(){
			var errormsg = '${errorMsg}';
			if(errormsg!=''){
				alert(errormsg);
			}
		});

		//查询
		function search(){
			if(!sub()){
				return;
			};
			$("#selectForm").attr("action","submitRecords_init.action");
			$("#selectForm").submit() ;
		}
		//换页
		function changePageNo(pageNo){
			var patten = /^[0-9]+$/;
			if(pageNo==''){
				alert("请输入数值！");
				return;
			}
			if (!patten.test(pageNo)) {
				alert("请输入正确数值！");
				return;
			}
		    if(parseInt(pageNo)<1){
				alert("无此页，请重新输入！");
				return;
			}
			if(parseInt(pageNo)>parseInt("${pageResults.pageCount}")){
				alert("无此页，请重新输入！");
				return;
			}
			$("#pageNoHidden").val(pageNo);
			$("#selectForm").attr("action","submitRecords_init.action");
			$("#selectForm").submit() ;
		}
		//报表下载
		function reportDownload(resultId,term,reportOrgId,reportStatus){
			if(reportStatus=='4'){
				alert("已修改数据，请重新生成报表");
				return;
			}
			$("#path").val("a");
			$("#term").val(term);
			$("#resultId").val(resultId);
			$("#reportOrgIdItem").val(reportOrgId);
			var checkUrl = "<%=basePath%>submitRecords_checkDownLoad.action";
			//var data = $("#selectForm").serialize();
			$.ajax({
	             type: "POST",
				 url: checkUrl,
				 dataType: "text",
				 data:{
					   path:'a',
					   term:term,
					   resultId:resultId,
					   reportOrgIdItem:reportOrgId
					   },
				 success: function(data){
						   if(data=='success'){
								$("#selectForm").attr("action","submitRecords_downLoad.action");
								$("#selectForm").submit();
							}else{
								alert("文件不存在！") ;
							}
			 	}
			 } );
		}
		//反馈下载
		function backDownload(resultId,term,reportOrgId,reportStatus){
			if(reportStatus=='0'||reportStatus=='1'){
				alert("反馈尚未抓取");
				return;
			}
			if(reportStatus=='4'){
				alert("已修改数据，请重新生成报表");
				return;
			}
			$("#path").val("b");
			$("#term").val(term);
			$("#resultId").val(resultId);
			$("#reportOrgIdItem").val(reportOrgId);
			var checkUrl = "submitRecords_checkDownLoad.action";
			//var data = $("#selectForm").serialize();
			$.post(checkUrl,
					{
				   "path":"b",
				   "term":term,
				   "resultId":resultId,
				   "reportOrgIdItem":reportOrgId
				   },
				   function(data){
						if(data=='success'){
							$("#selectForm").attr("action","submitRecords_downLoad.action");
							$("#selectForm").submit();
						}else{
							alert("文件不存在！");
						}
				   }
			);
		}
		//开始日期必须小于结束日期
		function sub(){
			if(document.getElementById("endTerm").value==''){
				return true;
			}else if(document.getElementById("startTerm").value>document.getElementById("endTerm").value){
				alert("开始日期必须小于等于结束日期");
				return false;
			}else{
				return true;
			}
		}
</script>
</head>

<body>
<p class="suoyinlan">当前位置：数据报送<span>&gt;&gt;</span>报送记录查看</p>
<s:form action="submitRecords_init.action" id="selectForm" name="selectForm" >
	<s:hidden name="pageNo" id="pageNoHidden"></s:hidden>
	<s:hidden name="path" id="path"></s:hidden>
	<s:hidden name="resultId" id="resultId"></s:hidden>
	<s:hidden name="term" id="term"></s:hidden>
	<s:hidden name="reportOrgIdItem" id="reportOrgIdItem"></s:hidden>
  <table width="100%" border="0" cellpadding="4" cellspacing="0" class="tab_content1">
	  <tr>
	   <td align="right">开始期数：</td>
	    <td><input type="text" name="startTerm" size="12" value="${startTerm}" onClick="return showCalendar('startTerm', 'y-mm-dd');" readonly="true" style="text" id="startTerm">
	    <img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" onClick="showCalendar('startTerm', 'y-mm-dd');"></td>
		<td align="right">结束期数：</td>
	    <td><input type="text" name="endTerm" size="12" value="${endTerm}" onClick="return showCalendar('endTerm', 'y-mm-dd');" readonly="true" style="text" id="endTerm">
	    <img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" onClick="showCalendar('endTerm', 'y-mm-dd');"></td>
		<td align="right">上报机构：</td>
	    <td>
		<s:select list="orgList "  listValue="reportOrgName " listKey="reportOrgId "  name="reportOrgId" id="reportOrgId"
	             headerKey="" headerValue="全部" ></s:select></td>
	    <td><input type="button" class="buttonStyle1" onclick="search()" value="查&nbsp;询" />
	      &nbsp;&nbsp;&nbsp;&nbsp;
	   </td>
  </tr>
</table>

<table border="0" cellspacing="1" class="tab_content">
  <thead>
  <tr>
	<th>报表名称</th>
	<th>上报机构</th>
	<th>期数</th>
	<th>状态</th>
    <th>上报时间</th>
	<th>操作</th>
  </tr>
  </thead>
  <tbody>
	<s:if test="pageResults==null || pageResults.results==null || pageResults.results.size==0">
		<tr>
			<td align="center" colspan="9" bgcolor="white">没有满足条件的报送记录！</td>
		</tr>
	</s:if>
	<s:else>
		<s:iterator value="pageResults.results" var="v">
			<tr onMouseOver="this.style.backgroundColor='#CDE9F8'" onMouseOut="this.style.backgroundColor='#FFFFFF'">
				<td>大额存单报表</td>
				<td><s:property value="#v.orgName" /></td>
				<td><s:property value="#v.term" /></td>
				<td><s:if test="#v.reportStatus == 0">
						已生成报表，未上报
					</s:if>
					<s:elseif test="#v.reportStatus == 1">
						已上报，反馈未抓取
					</s:elseif>
					<s:elseif test="#v.reportStatus == 2">
						已上报，反馈结果：成功
					</s:elseif>
					<s:elseif test="#v.reportStatus == 3">
						已上报，反馈结果：失败
					</s:elseif>
					<s:elseif test="#v.reportStatus == 4">
						已修改数据，未重新生成报表
					</s:elseif>
					<s:else>
					</s:else>
					</td>
				<td><s:property value="#v.reportTime" /></td>
				<td> <input type="button" onclick="reportDownload('${resultId}','${term}','${reportOrgId}','${reportStatus}')" class="buttonStyle1" value="报表下载" />&nbsp; 
					 <input type="button" onclick="backDownload('${resultId}','${term}','${reportOrgId}','${reportStatus}')" class="buttonStyle1" value="反馈下载" />
				</td>
			</tr>
		</s:iterator>
	</s:else>
  </tbody>
</table>
<!-- 分页 -->
			<table cellspacing="0" cellpadding="0" width="100%" border="0">
			<tr>
				<td width="61%">共<span class="apartpage_span"><s:property value="pageResults.totalCount" />
				</span>条记录 第 <span class="apartpage_span"><s:property value="pageResults.currentPage" />
				</span>/ <span class="apartpage_span"><s:property value="pageResults.pageCount" />
				</span>页</span>
				</td>
				<td width="39%">
					<table width="80%" border="0" align="right">
						<tr style="text-align:center">
							<td width="12%"><a href="javascript:changePageNo('1')">首页</a>
							<td width="7%" align="right"><img src="<%=request.getContextPath()%>/images/pleft.gif" width="12" height="12" <s:if test="pageNo>1">onclick="changePageNo('<s:property value="pageNo-1"/>')"</s:if> <s:else>onclick="changePageNo('1')"</s:else> style="cursor: hand" />
							</td>
							<td width="13%"><s:property value="pageNo" />/<s:property value="pageResults.pageCount" />页</td>
							<td width="7%" align="left"><img src="<%=request.getContextPath()%>/images/pright.gif" width="12" height="12" <s:if test="pageResults.pageCount>pageNo">onclick="changePageNo('<s:property value="pageNo+1"/>')"</s:if> <s:else>onclick="changePageNo('<s:property value="pageResults.pageCount"/>')"</s:else> style="cursor: hand" />
							</td>
							<td width="16%"><a href="javascript:changePageNo('<s:property value="pageResults.pageCount"/>')">尾页</a>
							</td>
							<td width="17%">转到第</td>
							<td width="14%"><input name="textfield1" type="text" id="textfield1" size="2" /></td>
							<td width="7%">页</td>
							<td width="7%"><img src="<%=request.getContextPath()%>/images/go.gif" width="16" height="16" onclick="changePageNo(document.getElementById('textfield1').value)" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
</s:form>
</body>
</html>
