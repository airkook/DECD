<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
			$("#selectForm").submit() ;
		}
	
//查询
  function doSearch(){
   var term=$("#term").val();
   var cpdm=$("#cpdm").val();
   var reportOrgID=$("#reportOrgID").val();
   window.location.href="fkwjck_search.action?term="+term+"&cpdm="+cpdm+"&reportOrgID="+reportOrgID;
}	

//修改
function editDetail(cpdm){
var term=$("#term").val();
    $.ajax({
             type: "POST",
			 url:"<%=basePath%>fkwjck_searchId.action",
			 dataType:"json",
			 data:{term:term,cpdm:cpdm},
			 success: function(data){ 
			  if(data.result=="error"){
                alert("此期不存在这个产品代码的数据记录");
	          }
			  else{
				var id=data.result;
			 	window.open("cdsManager_toEdit.action?id="+id,"", "height=500, width=1024, top=50,left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no"); 
			  }
		 }
	 } );
}

		
</script>

</head>

<body>

<p class="suoyinlan">当前位置：数据管理<span>&gt;&gt;</span>反馈文件查看<span>&gt;&gt;</span><s:if test="reportOrgID==1">中国外汇交易中心</s:if><s:else>上海清算所</s:else></p>
<table width="100%" border="0" cellpadding="4" cellspacing="0" class="tab_content1">
  <tr>
	<td width="20%" align="left">产品代码：<input type="text" name="date" size="12" id="cpdm"></td>
	<td width="20%" align="left">期数： <input type="text" name="term" size="12" value="${term}" onClick="return showCalendar('term', 'y-mm-dd');" readonly="readonly" style="text" id="term">
      <img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" onClick="return showCalendar('term', 'y-mm-dd');"></td>
    <td colspan="2"><input type="button" class="buttonStyle1" value="查询" onclick="doSearch()"/></td>
         <input type="hidden" value="${reportOrgID}" id="reportOrgID"/>
     </tr>
</table>
<div id="menuContent" class="menuContent" style="display:none; position:absolute;">
    <ul id="treeDemo" class="ztree" style="margin-top:0; width:238px;"></ul>
</div>

<table border="0" cellspacing="1" class="tab_content">
  <tr>
    <th>错误编号</th>
    <th>产品代码</th>
    <th>错误信息</th>
    <th>操作</th>
  </tr>
  
  <s:iterator value="pageResults.results" var="fk"><tr>
   <td>${flag}</td>
  <s:if test='flag=="S"'>
   <td colspan="2">无错误信息显示</td>
   <td> <input type="button" value="修&nbsp&nbsp改"  disabled="disabled" onclick="editDetail('<s:property value="#fk.cpdm"/>')"/></td>
   </s:if>
   <s:else>
    <td><s:property value="#fk.cpdm"/></td>
   <td>${errorInfo}</td>
   <td><input type="button" value="修改" class="buttonStyle1" onclick="editDetail('<s:property value="#fk.cpdm"/>')"/></td>
   </s:else>
  </tr>
</s:iterator>
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

</body>
</html>
