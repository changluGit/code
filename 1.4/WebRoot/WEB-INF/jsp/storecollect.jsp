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
    
    <title>收藏餐厅</title>
    
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
			$("#dishesTag").css("background-color","#CCCCCC");
			$("#dishesTag").css("border-color","azure");
			$(".cancelLogo").bind("mousemove",function(){
				$(this).attr("src","/order/images/remove2.png");
			});
			$(".cancelLogo").bind("mouseleave",function(){
				$(this).attr("src","/order/images/remove1.png");
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
							
							<img class="logo" src="<s:property value="store.logoAddress"/>"/>
						</div>
				    
						<div class="logoR">
							
							<div class="logoRDetail">好评度:<s:property value="store.rating"/>分</div>
							<div class="logoRDetail">状态:<s:property value="store.state"/></div>
							
						</div>
						<div class="clear"></div>
						<div class="logoD">
							<div class="logoName">
								<a href="/order/store/show_gotoStorePage.action?storeId=<s:property value="store.id"/>"><s:property value="store.storeName"/></a>
								<a href="/order/core/storedelete.action?storeid=<s:property value="id"/>"><img class="cancelLogo" src="/order/images/remove1.png" /></a></div>
							<div class="logoDDetail">地址:&nbsp;&nbsp;&nbsp;<s:property value="store.businessAddress"/></div>
							<div class="logoDDetail">电话:&nbsp;&nbsp;&nbsp;<s:property value="store.telephone"/></div>
							<div class="logoDDetail">负责人:<s:property value="store.chargePerson"/></div>
							
						</div>
						</div>
		
						 </s:iterator>
					</div> 
					
				</div>
			</div>
  			<!-- end 内容 -->
  		</div>
  	</div>
  	<!-- end 主体 -->
  	<!-- 脚本 -->
 	<jsp:include page="../../footer.jsp"></jsp:include>
  </body>
</html>
