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
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script src="<%=request.getContextPath()%>/js/ShowCalendar.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
		<script src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"
			type="text/javascript"></script>
<style>
.input-button {

	font-size: 12px;
    filter: progid:dximagetransform.microsoft.gradient(gradienttype=0, startcolorstr=#ffffff, endcolorstr=#bfe1f2);
	cursor: hand;
	border: #d9b525 1px solid;
	color:#7b6613;
	padding:3px 10px;
	background:#fff;

	}
	.input-button1 {
	background: #efefef;
	color:3A4F6C;
	height: 22px;
	border: 1px solid #cccccc;

}
</style>
<script type="text/javascript">
$(document).ready(function(){
	//提示信息
	var msg = '${message}';
	if(msg!=''){
		alert(msg);
	}
	var flag = "${flag}";
	//如果是查看将下拉框和输入框只读
	if("view"==flag){
		$("select").each( function() {
			 $(this).attr("disabled","disabled");  	
		});
		$("input[type='text']").each( function() {
			 $(this).attr("readonly","readonly");  	
		});
	}
});
//增加
function saveAdd(){
	if(!check()){
		return;
	}
	$("#detailForm").attr("action","cdsManager_saveAdd.action");
	$("#detailForm").submit();
}
//修改
function saveEdit(){
	if(!check()){
		return;
	}
	$("#detailForm").attr("action","cdsManager_saveEdit.action");
	$("#detailForm").submit();
}
//校验输入项
function check() {  
	var isNull = false;
	$("font").each(function(){
		var mustId = $(this).attr("id").substring(0,$(this).attr("id").indexOf("_"));
		var columnName = $(this).attr("title");
		if($("#"+mustId).val()==""){
			isNull = true;
			alert(columnName+"不能为空");
			$("#"+mustId).focus();
			return false;
		}
		
	});
	if(isNull){
		return false;
	}
	
	var extleng = false;
	$("input").each(function(){
		//文本长度校验
		if($(this).attr("fieldlength")){
			var name = $(this).attr("fieldlength").substring(0,$(this).attr("fieldlength").indexOf("_"));
			var leng = $(this).attr("fieldlength").substring($(this).attr("fieldlength").indexOf("_")+1,$(this).attr("fieldlength").length);
			var val = $(this).val();
			if(getStrLength(val)>parseInt(leng)){
				extleng = true;
				alert(name+"不能大于"+leng+"个字符！");
				$(this).focus();
				return false;
			}
		}
		//数字校验
		if($(this).attr("numlength")){
			var name = $(this).attr("numlength").substring(0,$(this).attr("numlength").indexOf("_"));
			var leng = $(this).attr("numlength").substring($(this).attr("numlength").indexOf("_")+1,$(this).attr("numlength").length);
			var val = $(this).val();
			if(leng.indexOf("(")>-1 && leng.indexOf(")")>-1){
				var zhengshu = leng.substring(leng.indexOf("(")+1,leng.indexOf(","));
				var xiaoshu = leng.substring(leng.indexOf(",")+1,leng.indexOf(")"));
				if(!checkNum(val,zhengshu,xiaoshu)){
					extleng = true;
					alert(name+"必须为数字，且整数位不能大于"+zhengshu+"个字符，小数位不能大于"+xiaoshu+"个字符！");
					$(this).focus();
					return false;
				}
			}
		}
	});
	if(extleng){
		return false;
	}
	return true;
}
//计算字符长度
function getStrLength(str) {  
    var cArr = str.match(/[^\x00-\xff]/ig);  
    return str.length + (cArr == null ? 0 : cArr.length);  
}
//校验数字
function checkNum(val,zhengshu,xiaoshu) {
	if(val==''){
		return true;
	}
	var reg = /^(-?\d*)(\.\d+)?$/;
    if(reg.test(val)){
    	var valzs = "";
    	var valxs = "";
    	if(val.indexOf(".")>-1){
	    	valzs = val.substring(0,val.indexOf("."));
	    	valxs = val.substring(val.indexOf(".")+1,val.length);
    	}else{
    		valzs = val;
    	}
    	if(getStrLength(valzs)>parseInt(zhengshu)){
    		return false;
    	}
    	if(getStrLength(valxs)>parseInt(xiaoshu)){
    		return false;
    	}
    	return true;
    }else{
    	return false;
    }
}
</script>
  </head>
  <body>
	<table border="0" width="98%" align="center">
		<tr>
			<td nowrap  height="3"></td>
		</tr>
		<tr>
			<td nowrap >
				当前位置 &gt;&gt; 数据管理&gt;&gt; 数据维护   &gt;&gt;<s:if test="flag=='view'">查看详细</s:if><s:if test="flag=='add'">增加</s:if><s:if test="flag=='edit'">修改</s:if>
			</td>
		</tr>
	</table>
	
	<form name="detailForm" id="detailForm" method="post" action="" style="margin:0px;">
	<s:hidden name="certificatesOfDeposit.id" />
	<s:hidden name="certificatesOfDeposit.term" />
		<table border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor" width="98%">
			<tr class="titletab">
				<th align="center" colspan="6">
					大额存单<s:if test="flag=='view'">查看详细</s:if><s:if test="flag=='add'">增加</s:if><s:if test="flag=='edit'">修改</s:if>
				</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  width="14%">
					产品代码：
				</td>
				<td nowrap  width="20%">
					<input type="text" name="certificatesOfDeposit.cpdm" size="20" value="${certificatesOfDeposit.cpdm}" <s:if test="flag!='add'">readonly="readonly"</s:if> id="cpdm" fieldlength="产品代码_20">
					<font style="color: red;font-size:15px" title="产品代码" id="cpdm_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  width="12%">
					业务发生日：
				</td>
				<td nowrap  width="20%">
					<input type="text" name="certificatesOfDeposit.ywfsr" size="20" value="${certificatesOfDeposit.ywfsr }" readonly="readonly" id="ywfsr" <s:if test="flag=='add'">onclick="return showCalendar('ywfsr', 'y-mm-dd');"</s:if>>
					<img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" <s:if test="flag=='add'">onclick="return showCalendar('ywfsr', 'y-mm-dd');"</s:if>>
					<font style="color: red;font-size:15px" title="业务发生日" id="ywfsr_span">&nbsp;*</font>
				</td>
				<td nowrap >
				 	计息方式：
				 </td>
				<td nowrap  width="20%">
					<s:select list="codeMap.jxfs" name="certificatesOfDeposit.jxfs" id="jxfs" value="certificatesOfDeposit.jxfs" />
					<font style="color: red;font-size:15px" title="计息方式" id="jxfs_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
			    <td nowrap  >
					发行起始日：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.fxqsr" size="20" value="${certificatesOfDeposit.fxqsr }"  readonly="readonly" id="fxqsr" <s:if test="flag!='view'">onclick="return showCalendar('fxqsr', 'y-mm-dd');"</s:if>>
					<img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" <s:if test="flag!='view'">onclick="return showCalendar('fxqsr', 'y-mm-dd');"</s:if>>
					<font style="color: red;font-size:15px" title="发行起始日" id="fxqsr_span">&nbsp;*</font>
				</td>
				<td nowrap  >
					发行人全称：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.fxrqc"   size="20" value="${certificatesOfDeposit.fxrqc }"   id="fxrqc" fieldlength="发行人全称_200">
					<font style="color: red;font-size:15px" title="发行人全称" id="fxrqc_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					发行人账号：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.fxrzh"   size="20" value="${certificatesOfDeposit.fxrzh }"   id="fxrzh" fieldlength="发行人账号_10">
					<font style="color: red;font-size:15px" title="发行人账号" id="fxrzh_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					发行终止日：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.fxzzr" size="20" value="${certificatesOfDeposit.fxzzr }"  readonly="readonly" id="fxzzr" <s:if test="flag!='view'">onclick="return showCalendar('fxzzr', 'y-mm-dd');"</s:if>>
					<img src="<%=request.getContextPath()%>/images/calendar.gif" border="0" <s:if test="flag!='view'">onclick="return showCalendar('fxzzr', 'y-mm-dd');"</s:if>>
					<font style="color: red;font-size:15px" title="发行终止日" id="fxzzr_span">&nbsp;*</font>
				</td>
				<td nowrap  >
					计划发行总量(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.jhfxzl"   size="20" value="${certificatesOfDeposit.jhfxzl }"   id="jhfxzl" numlength="计划发行总量(亿元)_(9,10)">
					<font style="color: red;font-size:15px" title="计划发行总量(亿元)" id="jhfxzl_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					当日发行金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.drfxje"   size="20" value="${certificatesOfDeposit.drfxje }"   id="drfxje" numlength="当日发行金额(亿元)_(9,10)">
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					累计发行金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.ljfxje"   size="20" value="${certificatesOfDeposit.ljfxje }"    id="ljfxje" numlength="累计发行金额(亿元)_(9,10)">
					<font style="color: red;font-size:15px" title="累计发行金额(亿元)" id="ljfxje_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					产品期限：
				</td>
				<td nowrap  >
					<s:select list="codeMap.cpqx" name="certificatesOfDeposit.cpqx" id="cpqx" value="certificatesOfDeposit.cpqx" />
					<font style="color: red;font-size:15px" title="产品期限" id="cpqx_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					发行对象：
				</td>
				<td nowrap  >
					<s:select list="codeMap.fxdx" name="certificatesOfDeposit.fxdx" id="fxdx" value="certificatesOfDeposit.fxdx" />
					<font style="color: red;font-size:15px" title="发行对象" id="fxdx_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					基准利率种类：
				</td>
				<td nowrap  >
					<s:select list="codeMap.jzllzl" name="certificatesOfDeposit.jzllzl" id="jzllzl" value="certificatesOfDeposit.jzllzl" />
				</td>
				<td nowrap  >
					利差(BP/%)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.lc"   size="20" value="${certificatesOfDeposit.lc }" id="lc" fieldlength="利差(BP/%)_20">
				</td>
				<td nowrap  >
					付息频率：
				</td>
				<td nowrap  >
					<s:select list="codeMap.fxpl" name="certificatesOfDeposit.fxpl" id="fxpl" value="certificatesOfDeposit.fxpl" />
					<font style="color: red;font-size:15px" title="付息频率" id="fxpl_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					初始发行利率(%)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.csfxll"   size="20" value="${certificatesOfDeposit.csfxll }"   id="csfxll" numlength="初始发行利率(%)_(6,4)">
					<font style="color: red;font-size:15px" title="初始发行利率(%)" id="csfxll_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					起点金额(万元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.qdje"   size="20" value="${certificatesOfDeposit.qdje }"   id="qdje" numlength="起点金额(万元)_(10,0)">
					<font style="color: red;font-size:15px" title="起点金额(万元)" id="qdje_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					是否可提前支取：
				</td>
				<td nowrap  >
					<s:select list="codeMap.sfktqzc" name="certificatesOfDeposit.sfktqzc" id="sfktqzc" value="certificatesOfDeposit.sfktqzc" />
					<font style="color: red;font-size:15px" title="是否可提前支取" id="sfktqzc_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					是否可赎回：
				</td>
				<td nowrap  >
					<s:select list="codeMap.sfksh" name="certificatesOfDeposit.sfksh" id="sfksh" value="certificatesOfDeposit.sfksh" />
					<font style="color: red;font-size:15px" title="是否可赎回" id="sfksh_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					当日提前支取金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.drtqzcje"   size="20" value="${certificatesOfDeposit.drtqzcje }"    id="drtqzcje" numlength="当日提前支取金额(亿元)_(9,10)">
				</td>
				<td nowrap  >
					当日兑付金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.drdfje"   size="20" value="${certificatesOfDeposit.drdfje }"   id="drdfje" numlength="当日兑付金额(亿元)_(9,10)">
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					当日赎回金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.drshje"   size="20" value="${certificatesOfDeposit.drshje }"   id="drshje" numlength="当日赎回金额(亿元)_(9,10)">
				</td>
				<td nowrap  >
					累计提前支取金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.ljtqzcje"   size="20" value="${certificatesOfDeposit.ljtqzcje }"    id="ljtqzcje" numlength="累计提前支取金额(亿元)_(9,10)">
				</td>
				<td nowrap  >
					累计赎回金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.ljshje"   size="20" value="${certificatesOfDeposit.ljshje }"   id="ljshje" numlength="累计赎回金额(亿元)_(9,10)">
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap  >
					累计兑付金额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.ljdfje"   size="20" value="${certificatesOfDeposit.ljdfje }"    id="ljdfje" numlength="累计兑付金额(亿元)_(9,10)">
				</td>
				<td nowrap  >
					每期存单余额(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.mqcdye"   size="20" value="${certificatesOfDeposit.mqcdye }"    id="mqcdye" numlength="每期存单余额(亿元)_(9,10)">
					<font style="color: red;font-size:15px" title="每期存单余额(亿元)" id="mqcdye_span">&nbsp;&nbsp;*</font>
				</td>
				<td nowrap  >
					余额总计(亿元)：
				</td>
				<td nowrap  >
					<input type="text" name="certificatesOfDeposit.yezj"   size="20" value="${certificatesOfDeposit.yezj }"    id="yezj" numlength="余额总计(亿元)_(9,10)">
					<font style="color: red;font-size:15px" title="余额总计(亿元)" id="yezj_span">&nbsp;&nbsp;*</font>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td nowrap   align="center" colspan="6">
				<s:if test="flag=='add'">
					<input type="button" name="btnReLoad" value="添    加" class="input-button" onclick="saveAdd()">
				</s:if>
				<s:if test="flag=='edit'">
					<input type="button" name="btnReLoad" value="修    改" class="input-button" onclick="saveEdit()">
				</s:if>
				<s:if test="flag=='view'">
					<input type="button" name="btnReLoad" value="刷    新" class="input-button" onclick="window.location.reload();">
				</s:if>
					&nbsp;&nbsp;
					<input type="button" name="btnSubmit" value="关闭窗口" class="input-button" onclick="window.close();<s:if test="flag=='edit'">window.opener.document.forms[0].submit();</s:if>">
				</td>
			</tr>
		</table>
		<br />
		<s:if test="flag!='add'">
			<s:if test="validateResultList!=null && validateResultList.size!=0">
			<table border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor" width="98%">
			<tr class="titletab">
				<th align="center" colspan="5">
					校验信息查看
				</th>
			</tr>
			 <tr class="titletab">
			 	<th align="center" width="40px">
					序号
				</th>
				<th align="center" >
					公式
				</th>
				<th align="center" width="50px">
					类型
				</th>
				<th align="center" >
					校验数据
				</th>
				<th align="center" width="80px">
					校验状态
				</th>
			</tr>
			<s:iterator value="validateResultList" status='st' var="row">
			<tr bgcolor="#FFFFFF">
				<td  align="center" >
					${st.count }
				</td>
				<td  align="center" >
					${validateDesc }
				</td>
				<td align="center" >
					${validateTypeDesc }
				</td>
				<td  align="center" >
					${validaetData }
				</td>
				<td align="center" >
					<span <s:if test="validateResultFlag==0">style="color:red"</s:if>>${validateResultFlagDesc }</span>
				</td>
			 </tr>
			 </s:iterator>
		</table>
		</s:if>
	</s:if>
	</form>
  </body>
</html>
