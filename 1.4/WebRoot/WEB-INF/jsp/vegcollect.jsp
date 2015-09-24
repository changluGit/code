<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>收藏菜品</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<jsp:include page="../../inc.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="/order/css/collect.css">
    <link rel="stylesheet" type="text/css" href="/order/css/header.css">
    <link href="/order/css/public_frame.css" rel="stylesheet">
    <link href="/order/qnui/css/sui.min.css" rel="stylesheet">
	<script type="text/javascript" src="/order/js/public_frame.js"></script>
	<script>
		$(function(){
		    var a = "${beNull}";
			if(a=="true") {
			$("#colBox").html("您还没有收藏的宝贝，赶快选购吧。。。").css({"font-size":"18px"});
			}
			$("#storeTag").css('background-color',"#CCCCCC");
			$("#storeTag").css("border-color","azure");
			$(".removeLogo").bind("mousemove",function(){
				$(this).attr("src","/order/images/remove2.png");
			});
			$(".removeLogo").bind("mouseleave",function(){
				$(this).attr("src","/order/images/remove1.png");
			});
			$(".addLogo").bind("mousemove",function(){
				$(this).attr("src","/order/images/add2.jpg");
			});
			$(".addLogo").bind("mouseleave",function(){
				$(this).attr("src","/order/images/add.jpg");
			});
		});
  </script>
  </head>
 <body style="text-align: center;">
<!-- 头部  -->
  	<jsp:include page="/WEB-INF/jsp/public/header.jsp"></jsp:include>
  	<!-- 主体  -->
  	<div class="mainBox">
		<div class="bodyBox">
			<!-- 侧边栏  -->
  			<jsp:include page="/WEB-INF/jsp/public/sidebar.jsp"></jsp:include>
  			<!-- 内容 -->
  			<div class="contentBox">
 
				<div class="RBody">
					<div class="RHead">
						<div class="RHeadL">我的收藏</div>
						<div class="RHeadR" id="dishesTag">&nbsp;<a href="/order/core/vegcollect.action">菜品</a>&nbsp;</div>
						<div class="RHeadR" id="storeTag">&nbsp;<a href="/order/core/storecollect.action">店铺</a>&nbsp;</div>
					</div>
					<div class="clear"></div>
					<div id="colBox">
					 <s:iterator value="collects">
					 <div class="RCollect">
						<div class="logoBox">
							
							<img class="logo" src="<s:property value='dishes.pictureAddress'/>"/>
							
						</div>
				    
						<div class="logoR">
							 
							<div class="logoRDetail">月销量:<s:property value="dishes.monthSales"/>份/月</div>
							<div class="logoRDetail">价格:<s:property value="dishes.price"/>元/份</div>
							
						</div>
						<div class="clear"></div>
						<div class="logoD">
							<div class="logoName">
							<s:property value="dishes.name"/>
							
							<a href="/order/core/vegdelete.action?dishid=<s:property value="id"/>"><img class="cancelLogo removeLogo"  src="/order/images/remove1.png" /></a>
							<a href="/order/core/usermain3.action?dishid=<s:property value="dishes.id"/>"><img class="cancelLogo addLogo"  src="/order/images/add.jpg" /></a></div>
							<div class="logoDDetail">菜品状态:&nbsp;&nbsp;&nbsp;<s:property value="dishes.status"/></div>
							<div class="logoDDetail">口味:&nbsp;&nbsp;&nbsp;<s:property value="dishes.taste"/></div>
						</div></div>
						 </s:iterator>
						 </div>
				
				</div>
			</div>
  			<!-- end 内容 -->
  		</div>
  	</div>
  	<!-- end 主体 -->
  	<!-- 脚本 -->
  	<jsp:include page="/footer.jsp"></jsp:include>
 
  </body>
</html>
