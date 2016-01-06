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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>报表输出</title>
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
<script type="text/javascript">
$(function(){ 
	//导入窗口
	$('#uploadfile').dialog('close');
	//提示信息
	var msg = '${message}';
	if(msg!=''){
		alert(msg);
	}
	//查询
	$("#search").click(function(){
		$("#search").attr("disabled",true);
		$("#form1").attr("action","dataAudit_init.action");
		$("#form1").submit();
	});
	//审核通过
	$("#auditPass").click(function(){
	
	   var ids = "";
       var flag = 0;
       $("input[name='dataAudit']:checkbox").each(function(){
          if (true == $(this).attr("checked")) {
           ids += $(this).val()+";";
            flag += 1;
          }
		});
		if(0 < flag) {
		ids = ids.substring(0, ids.length - 1);
		$("#ids").attr("value",ids); 
		}else {
		alert('请至少选择一项！');
		return false;
        };
       
		$("#auditPass").attr("disabled",true);
		$("#form1").attr("action","dataAudit_auditPass.action");
		$("#form1").submit();
	});	
	//审核不通过
	$("#auditNoPass").click(function(){
	
	    var ids = "";
       var flag = 0;
       $("input[name='dataAudit']:checkbox").each(function(){
          if (true == $(this).attr("checked")) {
           ids += $(this).val()+";";
            flag += 1;
          }
		});
		if(0 < flag) {
		ids = ids.substring(0, ids.length - 1);
		$("#ids").attr("value",ids); 
		}else {
		alert('请至少选择一项！');
		return false;
        };
	
		$("#auditNoPass").attr("disabled",true);
		$("#form1").attr("action","dataAudit_auditNoPass.action");
		$("#form1").submit();
	});
	//下载
	$("#downLoad").click(function(){
		$("#form1").attr("action","dataAudit_downLoad.action");
		$("#form1").submit();
	});
	//全选/反选
	$("input[name=checkAll]").click(function(){
	 var obj = $("input[name=checkAll]");
	 //全选
	 if(obj.attr("checked")){
	 	$("input[name='dataAudit']").each(function () {
	 	    if(!$(this).attr("disabled")){
	 	    	$(this).attr("checked", true);
	 	    }
			
		});
	 }else{
	 	$("input[name='dataAudit']").each(function () {
	 		if(!$(this).attr("disabled")){
	 	    	$(this).attr("checked", false);
	 	    }
		});
	 }
	});
	
	//单个选择如果全选了 
	$("input[name='dataAudit']").click(function(){
		var temp = true;
		$("input[name='dataAudit']").each(function(){
			if(!$(this).attr("checked") && !$(this).attr("disabled")){
				temp = false;
			}
		});
		if(temp){
			$("input[name=checkAll]").attr("checked",true);
		}else{
			$("input[name=checkAll]").attr("checked",false);	
		}
	});
});

//查看详细
function viewDetail(id){
	window.open("cdsManager_toView.action?id="+id,"", "height=500, width=1024, top=50,left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
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
	$("#form1").attr("action","dataAudit_init.action");
	$("#form1").submit() ;
}


</script>
</head>
<body>
	<s:form action="dataAudit_init.action" method="post" id="form1"
		name="form1">
		<p class="suoyinlan">
			当前位置：数据管理<span>&gt;&gt;</span>数据审核
		</p>
		<s:hidden name="pageNo" id="pageNoHidden"></s:hidden>
		<s:hidden name="ids" id="ids" value=""></s:hidden>
		<table width="100%" border="0" cellpadding="4" cellspacing="0"
			class="tab_content1">
			<tr>
				<td width="15%" align="left">期数： <input type="text" name="term"
					size="12" value="${term}"
					onClick="return showCalendar('term', 'y-mm-dd');"
					readonly="readonly" style="text" id="term"> <img
					src="<%=request.getContextPath()%>/images/calendar.gif" border="0"
					onClick="return showCalendar('term', 'y-mm-dd');">
				</td>
				<td align="left" width="12%">产品代码： <s:textfield name="cpdm"
						id="cpdm" size="12"></s:textfield></td>
				<td align="left" width="20%">计息方式： <s:select list="jxfsMap" name="jxfs"
						id="jxfs" /></td>
			</tr>
			<tr>
				<td align="left">校验状态： <s:select list="validateStatusMap"
						name="validateStatus" id="validateStatus" /></td>
				<td align="left">数据状态： 
					<s:select list="statusMap" name="status" id="status"/>
					</td>
				<td colspan="2">
					<input type="submit" id="search"class="buttonStyle1" value="查询" />&nbsp;
					<input type="submit" id="auditPass" <s:if test="status==5">disabled</s:if> class="buttonStyle1" value="审核通过" />&nbsp;
					<input type="submit" id="auditNoPass" class="buttonStyle1" value="审核不通过" />&nbsp;
				</td>
			</tr>
		</table>
		<table border="0" cellspacing="1" class="tab_content">
			<tr>
				<th><input type="checkBox" name="checkAll" />
				</th>
				<th>序号</th>
				<th>产品代码</th>
				<th>计息方式</th>
				<th>产品期限</th>
				<th>发行对象</th>
				<th>起点金额(万元)</th>
				<th>明细</th>
				<th>修改人</th>
				<th>校验状态</th>
			</tr>
			<s:if
				test="pageResults==null || pageResults.results==null || pageResults.results.size==0">
				<tr>
					<td align="center" colspan="10" bgcolor="white" nowrap="nowrap">
						没有符合条件的记录</td>
				</tr>
			</s:if>
			<s:iterator value="pageResults.results" status='st' var="row">
				<tr height="35px">
					<td nowrap="nowrap"><input type="checkBox" name="dataAudit"
						value="${id}" /></td>
					<td nowrap="nowrap">${st.count}</td>
					<td nowrap="nowrap">${cpdm}</td>
					<td nowrap="nowrap">${jxfs}</td>
					<td nowrap="nowrap">${cpqx}</td>
					<td nowrap="nowrap">${fxdx}</td>
					<td nowrap="nowrap">${qdje}</td>
					<td><a href="javascript:viewDetail(${id});"
						style="color: blue">查看详细</a>
					</td>
					<td nowrap="nowrap">${updateUserName}</td>
					<td nowrap="nowrap" >
					  <span <s:if test="validateStatus==0">class="txt_red"</s:if>>${validateStatusDesc}<s:if test="sumValidateStatusDesc!=null"><font style="font-color:red;">${sumValidateStatusDesc}</font></s:if></span>
				   </td>
				</tr>
			</s:iterator>
			
		</table>
		<s:if test="pageResults.pageCount>1">
		<table cellspacing="0" cellpadding="0" width="100%" border="0">
			<tr>
				<td width="61%">共<span class="apartpage_span"><s:property
							value="pageResults.totalCount" />
				</span>条记录 第 <span class="apartpage_span"><s:property
							value="pageResults.currentPage" />
				</span>/ <span class="apartpage_span"><s:property
							value="pageResults.pageCount" />
				</span>页</span>
				</td>
				<td width="39%">
					<table width="80%" border="0" align="right">
						<tr style="text-align:center">
							<td width="12%"><a href="javascript:changePageNo('1')">首页</a>
							<td width="7%" align="right"><img
								src="<%=basePath%>/images/pleft.gif" width="12" height="12"
								<s:if test="pageNo>1">onclick="changePageNo('<s:property value="pageNo-1"/>')"</s:if>
								<s:else>onclick="changePageNo('1')"</s:else>
								style="cursor: hand" />
							</td>
							<td width="13%"><s:property value="pageNo" />/<s:property
									value="pageResults.pageCount" />页</td>
							<td width="7%" align="left"><img
								src="<%=basePath%>/images/pright.gif" width="12" height="12"
								<s:if test="pageResults.pageCount>pageNo">onclick="changePageNo('<s:property value="pageNo+1"/>')"</s:if>
								<s:else>onclick="changePageNo('<s:property value="pageResults.pageCount"/>')"</s:else>
								style="cursor: hand" />
							</td>
							<td width="16%"><a
								href="javascript:changePageNo('<s:property value="pageResults.pageCount"/>')">尾页</a>
							</td>
							<td width="17%">转到第</td>
							<td width="14%"><input name="textfield" type="text"
								id="textfield" size="2" /></td>
							<td width="7%">页</td>
							<td width="7%"><img src="<%=basePath%>/images/go.gif"
								width="16" height="16"
								onclick="changePageNo(document.getElementById('textfield').value)" />
							</td>
						</tr>
					</table></td>
			</tr>
		</table>
		</s:if>
	</s:form>

</body>
</html>