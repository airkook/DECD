<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
$('#uploadfile').dialog('close');
	//提示信息
	var msg = '${message}';
	if(msg!=''){
		alert(msg);
	}
});

function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
//输入框空和长度的校验
function sub(){
	if(trim(document.getElementById("ftpAddress1").value)==""){
		alert("中国外汇交易中心的FTP地址不可为空");
		return false;
	}if(trim(document.getElementById("ftpAddress1").value).length>50){
		alert("中国外汇交易中心的FTP地址长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("ftpUserId1").value)==""){
		alert("中国外汇交易中心的用户名不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpUserId1").value).length>50){
		alert("中国外汇交易中心的用户名长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("ftpPassword1").value)==""){
		alert("中国外汇交易中心的密码不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpPassword1").value).length>32){
		alert("中国外汇交易中心的密码长度不能超过32");
		return false;
	}
	if(trim(document.getElementById("ftpPort1").value)==""){
		alert("中国外汇交易中心的端口号不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpPort1").value).length>10){
		alert("中国外汇交易中心的端口号长度不能超过10");
		return false;
	}
	if(isNaN(document.getElementById("ftpPort1").value)){
		alert("中国外汇交易中心的端口号必须为数字");
		return false;
	}
	if(trim(document.getElementById("uploadPath1").value).length>50){
		alert("中国外汇交易中心的上报路径长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("feedbackPath1").value).length>50){
		alert("中国外汇交易中心的反馈路径长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("ftpAddress2").value)==""){
		alert("上海清算所的FTP地址不可为空");
		return false;
	}if(trim(document.getElementById("ftpAddress2").value).length>50){
		alert("上海清算所的FTP地址长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("ftpUserId2").value)==""){
		alert("上海清算所的用户名不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpUserId2").value).length>50){
		alert("上海清算所的用户名长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("ftpPassword2").value)==""){
		alert("上海清算所的密码不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpPassword2").value).length>32){
		alert("上海清算所的密码长度不能超过32");
		return false;
	}
	if(trim(document.getElementById("ftpPort2").value)==""){
		alert("上海清算所的端口号不可为空");
		return false;
	}
	if(trim(document.getElementById("ftpPort2").value).length>10){
		alert("上海清算所的端口号长度不能超过10");
		return false;
	}
	if(isNaN(document.getElementById("ftpPort2").value)){
		alert("上海清算所的端口号必须为数字");
		return false;
	}
	if(trim(document.getElementById("uploadPath2").value)==""){
		alert("上海清算所的上报路径不可为空");
		return false;
	}
	if(trim(document.getElementById("uploadPath2").value).length>50){
		alert("上海清算所的上报路径长度不能超过50");
		return false;
	}
	if(trim(document.getElementById("feedbackPath2").value)==""){
		alert("上海清算所的反馈路径不可为空");
		return false;
	}
	if(trim(document.getElementById("feedbackPath2").value).length>50){
		alert("上海清算所的反馈路径长度不能超过50");
		return false;
	}
}

//导入
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
                    /* 	$("#uploadForm").append($("#cpdm")); */
                    /* 	$("#uploadForm").append($("#jxfs"));
                    	$("#uploadForm").append($("#term"));
                    	$("#uploadForm").append($("#status"));
                    	$("#uploadForm").append($("#validateStatus")); */
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
<p class="suoyinlan">当前位置<span>&gt;&gt;</span>报送配置<span>&gt;&gt;</span>报送地址配置</p>
<s:form action="submitConfig_save.action" id="selectForm" name="selectForm" onsubmit="return sub();">
<table border="0" width="400px" cellspacing="1" cellpadding="4"  class="tab_content">
  <s:hidden name="reConfigInfo_W_H.reportOrgId" value="1" ></s:hidden>
  <s:hidden name="reConfigInfo_Q_S_S.reportOrgId" value="2" ></s:hidden>
  <tr  class="tab_content"  >
    <th colspan="5" >中国外汇交易中心报送地址配置</th>
  </tr>
  <tr>
    <td>FTP地址</td>
    <td><input id="ftpAddress1" type="text" name="reConfigInfo_W_H.ftpAddress" value="${reConfigInfo_W_H.ftpAddress}"></td>
  </tr>
  <tr >
    <td width="50%">用户名</td>
    <td width="50%"><input id="ftpUserId1" type="text" name="reConfigInfo_W_H.ftpUserId" value="${reConfigInfo_W_H.ftpUserId}"></td>
  </tr>
  <tr>
    <td>密码</td>
    <td><input type="text" id="ftpPassword1" name="reConfigInfo_W_H.ftpPassword" value="${reConfigInfo_W_H.ftpPassword}"></td>
  </tr>
  <tr>
    <td>端口号</td>
    <td><input type="text" id="ftpPort1" name="reConfigInfo_W_H.ftpPort" value="${reConfigInfo_W_H.ftpPort}"></td>
  </tr>
  <tr>
    <td>上报路径</td>
    <td><input type="text" id="uploadPath1" name="reConfigInfo_W_H.uploadPath" value="${reConfigInfo_W_H.uploadPath}"></td>
  </tr>
  <tr>
    <td>反馈路径</td>
    <td><input type="text" id="feedbackPath1" name="reConfigInfo_W_H.feedbackPath" value="${reConfigInfo_W_H.feedbackPath}"></td>
  </tr>
    <tr>
    <td colspan="2"> &nbsp;</td>
    
  </tr>
  <th colspan="5"  >上海清算所报送地址配置</th>
   <tr>
    <td>FTP地址</td>
    <td><input type="text" id="ftpAddress2" name="reConfigInfo_Q_S_S.ftpAddress" value="${reConfigInfo_Q_S_S.ftpAddress}"></td>
  </tr>
  <tr class="tr_bg1">
    <td>用户名</td>
    <td><input type="text" id="ftpUserId2" name="reConfigInfo_Q_S_S.ftpUserId" value="${reConfigInfo_Q_S_S.ftpUserId}"></td>
  </tr>
  <tr>
    <td>密码</td>
    <td><input type="text" id="ftpPassword2" name="reConfigInfo_Q_S_S.ftpPassword" value="${reConfigInfo_Q_S_S.ftpPassword}"></td>
  </tr>
  <tr>
    <td>端口号</td>
    <td><input type="text" id="ftpPort2" name="reConfigInfo_Q_S_S.ftpPort" value="${reConfigInfo_Q_S_S.ftpPort}"></td>
  </tr>
  <tr>
    <td>上报路径</td>
    <td><input type="text" id="uploadPath2" name="reConfigInfo_Q_S_S.uploadPath" value="${reConfigInfo_Q_S_S.uploadPath}"></td>
  </tr>
  <tr>
    <td>反馈路径</td>
    <td><input type="text" id="feedbackPath2" name="reConfigInfo_Q_S_S.feedbackPath" value="${reConfigInfo_Q_S_S.feedbackPath}"></td>
  </tr>
  
   <tr>
    <td colspan="2">
    <input type="submit" class="buttonStyle1" value=" 保 存 ">
   		<input type="button" id="import" onclick="importData()" class="buttonStyle1" value="日历导入" />
   
  </td></tr>
</table>
</s:form>
<div id="uploadfile" class="easyui-dialog">
	<form method="post" id="uploadForm" enctype="multipart/form-data" action="submitConfig_uploadData.action">
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
<table cellspacing="0" cellpadding="0" width="100%" border="0" class="mar1">
</table>
</body>
</html>
