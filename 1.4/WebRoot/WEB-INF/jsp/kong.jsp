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
	<link href="/order/css/public_frame.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/order/css/usermain.css">
<link rel="stylesheet" type="text/css" href="/order/css/header.css">
<script type="text/javascript" src="/order/js/public_frame.js"></script>
 <style>
			.RBody{
				width:730px;
				height: 400px;
				border: 1px solid #CCCCCC;
				margin: 10px 10px;
				padding: 10px 10px;
				display: inline-block;
			}
			.RHead{
				padding: 5px 5px;
				
				height: 30px;
				border-bottom:2px solid #CCCCCC;
			}
			.RHeadL{
				font-family: "微软雅黑";
				font-size: 20px;
				color:darkslategrey;
				font-weight: bold;
				width: auto;
				float: left;
			}
			.RHeadR{
				font-family: "微软雅黑";
				font-size: 16px;
				color:darkslategrey;
				font-weight: bold;
				width: auto;
				margin: 2px 2px;
				padding: 2px 2px;
				float: right;
				border: 1px solid #CCCCCC;
				cursor: pointer;
			}
			.RCollect{
				width:200px;
				height: 200px;
				border: 1px solid #CCCCCC;
				margin: 10px 10px;
				padding: 10px 10px;
				float:left;
			}
			.logoBox{
				width:40%;
				height: 40%;
				border: 1px solid #CCCCCC;
				float: left;
			}
			.logo{
				width:100%;
				height: 100%;
			}
			.logoR{
				width:45%;
				height: 30%;
				padding: 1px 10px;
				float: left;
			}
			.logoD{
				width:auto;
				height: 60%;
				padding: 5px 0px;
			}
			.logoName{
				width:auto;
				padding: 2px 2px;
				font-family: "微软雅黑";
				font-size: 17px;
				color: grey;
				font-weight: bold;
			}
			.logoRDetail{
				width:auto;
				padding: 2px 2px;
				font-family: "微软雅黑";
				font-size: 12px;
				color: grey;
			}
			.logoDDetail{
				width:auto;
				padding: 2px 2px;
				font-family: "微软雅黑";
				font-size: 14px;
				color: grey;
			}
			.cancelLogo{
				width: 20px;
				height: 15px;
				float: right;
				margin: 0px 5px;
				cursor: pointer;
			}
			
			.clear{
				clear: both;
			}
			
			a{
				text-decoration: none;
				color:#2F4F4F;
			}
			
			.test{
				width:auto;
				height: 1px;
			}
			
		</style>
		<script>
		$(function(){
			$("#storeTag").css('background-color',"#CCCCCC");
			$("#storeTag").css("border-color","azure");
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
  	<jsp:include page="public/header.jsp"></jsp:include>
  	<!-- 主体  -->
  	<div class="mainBox">
		<div class="bodyBox">
			<!-- 侧边栏  -->
  			<jsp:include page="public/sidebar.jsp"></jsp:include>
  			<!-- 内容 -->
  			<div class="contentBox">
 
				<div class="RBody">
					<div class="RHead">
						<div class="RHeadL">我的收藏</div>
						<div class="RHeadR" id="dishesTag">&nbsp;<a href="/order/core/usermain2.action">菜品</a>&nbsp;</div>
						<div class="RHeadR" id="storeTag">&nbsp;<a href="/order/core/usermain1.action">店铺</a>&nbsp;</div>
					</div>
					
					
					<div>您还没有收藏的宝贝，赶快选购吧...</div>
				
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
