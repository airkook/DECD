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
		<title>大额存单数据维护</title>
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
		$("#form1").attr("action","cdsManager_init.action");
		$("#form1").submit();
	});
	//校验
	$("#validateData").click(function(){
		$("#validateData").attr("disabled",true);
		$("#form1").attr("action","cdsManager_validateData.action");
		$("#form1").submit();
	});
	//增加
	$("#add").click(function(){
		var term = $("#term").val();
		window.open("cdsManager_toAdd.action?term="+term,"", "height=500, width=1024, top=50,left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
	});
	//提交审核
	$("#submitCheck").click(function(){
		var temp = false;
		$("input[name='checkBox']").each(function(){
			if($(this).attr("checked")){
				temp = true;
			}
		});
		if(!temp){
			alert("请选择至少一条数据！");
			return false;
		}
		$("#submitCheck").attr("disabled",true);
		$("#form1").attr("action","cdsManager_submitCheck.action");
		$("#form1").submit();
	});	
	//下载
	$("#downLoad").click(function(){
		$("#form1").attr("action","cdsManager_downLoad.action");
		$("#form1").submit();
	});
	//全选/反选
	$("input[name=checkAll]").click(function(){
	 var obj = $("input[name=checkAll]");
	 //全选
	 if(obj.attr("checked")){
	 	$("input[name='checkBox']").each(function () {
	 	    if(!$(this).attr("disabled")){
	 	    	$(this).attr("checked", true);
	 	    }
			
		});
	 }else{
	 	$("input[name='checkBox']").each(function () {
	 		if(!$(this).attr("disabled")){
	 	    	$(this).attr("checked", false);
	 	    }
		});
	 }
	});
	
	//单个选择如果全选了 
	$("input[name='checkBox']").click(function(){
		var temp = true;
		$("input[name='checkBox']").each(function(){
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
//修改
function editDetail(id){
	window.open("cdsManager_toEdit.action?id="+id,"", "height=500, width=1024, top=50,left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
}
//删除
function del(id){
	if(confirm("确定要删除吗？")){
		$("#form1").attr("action","cdsManager_del.action?id="+id);
		$("#form1").submit();
	}
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
	$("#form1").attr("action","cdsManager_init.action");
	$("#form1").submit() ;
}
//导入数据
function importData(){
	$('#uploadfile').dialog({
                modal:true,
                width:500,
                height:150,
                title: '导入数据',
                buttons: [{
                    text:'确定',
                    iconCls:'icon-ok',
                    handler:function(){
                    	$(this).attr("disabled",true);
                    	if($('#formFile').val()==''){
                    		alert("请选择一个文件！");
                    		return;
                    	}else{
                    		var reg = new RegExp(".xls$"); 
                        	var filename = $('#formFile').val().toLowerCase();
                        	if(!reg.test(filename)){
                        		alert("请上传 .xls格式的EXCEL文件！");
                        		return;
                        	}
                    	}
                    	$("#uploadForm").append($("#cpdm"));
                    	$("#uploadForm").append($("#jxfs"));
                    	$("#uploadForm").append($("#term"));
                    	$("#uploadForm").append($("#status"));
                    	$("#uploadForm").append($("#validateStatus"));
                		$("#uploadForm").submit();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-undo',
                    handler:function(){
                        $("#uploadfile").dialog('close');
                    }
                }]
    }).dialog('refresh').dialog('open');
}
</script>
</head>
<body>
<s:form action="cdsManager_init.action" method="post" id="form1" name="form1">
<p class="suoyinlan">当前位置：数据管理<span>&gt;&gt;</span>数据维护</p>
<s:hidden name="pageNo" id="pageNoHidden"></s:hidden>
<table width="100%" border="0" cellpadding="4" cellspacing="0" class="tab_content1">
  <tr>
  	<td width="15%" align="left">期数： <input type="text" name="term" size="12" value="${term}" onClick="return showCalendar('term', 'y-mm-dd');" readonly="readonly" style="text" id="term">
      <img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" onClick="return showCalendar('term', 'y-mm-dd');"></td>
	<td align="left" width="12%">产品代码： 
	<s:textfield name="cpdm" id="cpdm" size="12"></s:textfield>
	</td>
	<td align="left" width="20%">计息方式： 
	<s:select list="jxfsMap" name="jxfs" id="jxfs"/>
	</td>
  </tr>
  <tr>
  <td align="left">数据状态： 
	<s:select list="statusMap" name="status" id="status"/>
	</td>
   <td align="left">校验状态： 
	<s:select list="validateStatusMap" name="validateStatus" id="validateStatus"/>
	</td>
    <td colspan="2">
		<input type="button" id="search" class="buttonStyle1" value="查询" />
		&nbsp;
		<input type="button" id="validateData" class="buttonStyle1" value="校验" />
		&nbsp;
		<input type="button" id="submitCheck" class="buttonStyle1"
			value="提交审核" />
		&nbsp;
		<input type="button" id="add" class="buttonStyle1" value="增加" />
		&nbsp;
		<input type="button" id="downLoad" class="buttonStyle1" value="下载" />
		&nbsp;
		<input type="button" id="import" onclick="importData()" class="buttonStyle1" value="导入" />
	</td>	
  </tr>
</table>
<table border="0" cellspacing="1" class="tab_content">
  <tr>
  	<th><input type="checkBox" name="checkAll" /></th>
    <th>序号</th>
    <th>产品代码</th>
    <th>计息方式</th>
    <th>产品期限</th>
	<th>发行对象</th>
	<th>起点金额(万元)</th>
	<th>校验状态</th>
	<th>数据状态</th>
	<th>明细</th>
	<th>操作</th>
  </tr>
  <s:if test="pageResults==null || pageResults.results==null || pageResults.results.size==0">
	 <tr>
		 <td align="center" colspan="11" bgcolor="white" nowrap="nowrap">
		 没有符合条件的记录
		 </td>
	 </tr>
  </s:if>
<s:iterator value="pageResults.results" status='st' var="row">
  <tr height="35px">
  	<td nowrap="nowrap">
		<input type="checkBox" name="checkBox" value="${id}" />
  	</td>
    <td nowrap="nowrap">${st.count}</td>
    <td nowrap="nowrap">${cpdm}</td>
    <td nowrap="nowrap">${jxfs}</td>
	<td nowrap="nowrap">${cpqx}</td>
	<td nowrap="nowrap">${fxdx}</td>
	<td nowrap="nowrap">${qdje}</td>
	<td nowrap="nowrap" <s:if test="validateStatus==0">class="txt_red"</s:if>>${validateStatusDesc}<s:if test="sumValidateStatusDesc!=null"><font style="font-color:red;">${sumValidateStatusDesc}</font></s:if></td>
	<td nowrap="nowrap" <s:if test="status==4">class="txt_red"</s:if>>${statusDesc}</td>
	<td><a href="javascript:viewDetail(${id});" style="color: blue">查看详细</a></td>
	<td><input type="button" class="buttonStyle1" value="修改" onclick="editDetail(${id})" <s:if test="status==3||status==5">disabled</s:if>>&nbsp;
	<input type="button" class="buttonStyle1" value="删除" onclick="del(${id})" <s:if test="status==3">disabled</s:if>></td>
  </tr>
</s:iterator>
</table>
<s:if test="pageResults.pageCount>1">
<table cellspacing="0" cellpadding="0" width="100%" border="0">
  <tr>
    <td width="61%">共<span class="apartpage_span"><s:property value="pageResults.totalCount"/></span>条记录 第 <span class="apartpage_span"><s:property value="pageResults.currentPage"/></span>/ <span class="apartpage_span"><s:property value="pageResults.pageCount"/></span>页</span></td>
    <td width="39%">
    	<table width="80%" border="0" align="right">
	        <tr style="text-align:center">
	          <td width="12%"><a href="javascript:changePageNo('1')">首页</a>
	          <td width="7%" align="right"><img src="<%=basePath %>/images/pleft.gif" width="12" height="12" <s:if test="pageNo>1">onclick="changePageNo('<s:property value="pageNo-1"/>')"</s:if><s:else>onclick="changePageNo('1')"</s:else> style="cursor: hand"/></td>
	          <td width="13%"><s:property value="pageNo"/>/<s:property value="pageResults.pageCount"/>页</td>
	          <td width="7%" align="left"><img src="<%=basePath %>/images/pright.gif" width="12" height="12" <s:if test="pageResults.pageCount>pageNo">onclick="changePageNo('<s:property value="pageNo+1"/>')"</s:if><s:else>onclick="changePageNo('<s:property value="pageResults.pageCount"/>')"</s:else> style="cursor: hand"/></td>
	          <td width="16%"><a href="javascript:changePageNo('<s:property value="pageResults.pageCount"/>')">尾页</a></td>
	          <td width="17%">转到第</td>
	          <td width="14%">
	              <input name="textfield" type="text" id="textfield" size="2" />
	            </td>
	          <td width="7%">页</td>
	          <td width="7%"><img src="<%=basePath %>/images/go.gif" width="16" height="16" onclick="changePageNo(document.getElementById('textfield').value)" /></td>
	        </tr>
      	</table>
     </td>
  </tr>
</table>
</s:if>
</s:form>

<div id="uploadfile" class="easyui-dialog">
	<form method="post" id="uploadForm" enctype="multipart/form-data" action="cdsManager_uploadData.action">
            <table align="center" width="90%" class="datalist">
                <tr height="60">
                    <td width="30%" align="right">上传文件：</td>
                    <td width="60%" style="text-align:left">
                        <input type="file" name="excel" id="formFile"/>
                    </td>
                </tr>
            </table>
    </form>
</div> 

</body>
</html>