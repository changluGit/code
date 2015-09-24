<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<jsp:include page="../../../inc.jsp"></jsp:include>
	<link href="/order/css/public_frame.css" rel="stylesheet">
	<script type="text/javascript" src="/order/js/public_frame.js"></script>
	
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
  				<!-- 此处为页面内容 -->
  				<!-- 此处为页面内容 -->
  				<!-- 此处为页面内容 -->
  				<!-- 此处为页面内容 -->
  			</div>
  			<!-- end 内容 -->
  		</div>
  	</div>
  	<!-- end 主体 -->
  	<!-- 脚本 -->
  	<jsp:include page="/footer.jsp"></jsp:include>
  </body>
 </html>