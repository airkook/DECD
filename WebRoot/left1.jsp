<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fp" uri="/fitechpage-tags"%>
<%@page import="com.fitech.papp.decd.model.vo.UserInfoVo" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.fitech.papp.decd.model.vo.MenuVo" %>
<%@page import="com.fitech.papp.decd.model.pojo.Menu" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/css/left.css" type="text/css" rel="stylesheet" />
<style>
.l4{ padding:0 0 0 15px;font-size:12px;color:#444;background:url(<%=request.getContextPath()%>/images/icon4.gif) no-repeat 10px 10px; width:200px; list-style:none}
</style>
<title>左栏</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery1.3.2.js"></script>
<script>
// 树状菜单
$(document).ready(function(){
   
   $(".l1").each(function(i,obj){
		$(this).attr('star','close');
	});

	$(".l1").click(function(){
		$(".slist").animate({height: 'toggle', opacity: 'hide'}, "slow");
		if($(this).attr('star') == 'open'){
			$(this).attr('star','close');
		}else{
			$(this).next(".slist").animate({height: 'toggle', opacity: 'show'}, "slow");
			$(".l1").each(function(i,obj){
				$(this).attr('star','close');
			});
			$(this).attr('star','open');
		}
	});

   
   $(".l2").toggle(function(){
     $("ul").animate({height: 'toggle', opacity: 'hide'}, "slow");
     $(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
   },function(){
		$("ul").animate({height: 'toggle', opacity: 'hide'}, "slow");
		$(this).next(".sslist").animate({height: 'toggle', opacity: 'toggle'}, "slow");
   });
   
    $(".l2").click(function(){
	$(".l3").removeClass("currentl3");
	$(".l2").removeClass("currentl2");
	$(this).addClass("currentl2");
	});  
   
   $(".l3").click(function(){
	$(".l3").removeClass("currentl3");		  
	$(this).addClass("currentl3");
   });  
   
    $(".close").toggle(function(){
	$(".slist").animate({height: 'toggle', opacity: 'hide'}, "fast");  
	$(".sslist").animate({height: 'toggle', opacity: 'hide'}, "fast");  
	 },function(){
	$(".slist").animate({height: 'toggle', opacity: 'show'}, "fast");  
	$(".sslist").animate({height: 'toggle', opacity: 'show'}, "fast");  
	});  
});

</script>
</head>
<body style="overflow-y:auto">
<div class="left_menu">
<% 
	UserInfoVo userInfo=(UserInfoVo)request.getSession().getAttribute("userInfo");
	if(null==userInfo)
	{
		userInfo=new UserInfoVo();
	}
	List<MenuVo> menus=userInfo.getMenuVoList();
	if(null==menus)
	{
		menus=new ArrayList<MenuVo>();
	}
		for(int i=0;i<menus.size();i++) {
			MenuVo menu=menus.get(i);
		
%>
 <h1 class="l1"><span><%=menu.getMenuName() %></span></h1>
  <div class="slist">
  		 <%
  			for(MenuVo m:menu.getMenuList()){
  		%>
  			<h2 class="<%if(m.getMenuList().size()>0){%>l2<%}else{%>l5<%}%>"><a href="<%=request.getContextPath()%>/<%=m.getMenuUrl() %>" target="my_frame2"><%=m.getMenuName() %></a></h2>
	  	
	  	<%if(m.getMenuList().size()>0){%>
  		<ul class="sslist">
  		<%}%>
	  	<%
  			for(MenuVo m2:m.getMenuList()){
  		%>
	      <li class="l3"><a href="<%=request.getContextPath()%>/<%=m2.getMenuUrl() %>" target="my_frame2"><%=m2.getMenuName() %></a></li>
	  	<%	
  			}
	  	%>
	  	<%
  			if(m.getMenuList().size()>0){
  		%>
  		</ul>
  		<%	
  			}
	  	%>
	  	<%	
  			}
	  	%>
  </div>
<%
	}
 %>
</div>
</body>
</html>