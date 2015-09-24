<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>账户信息</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<jsp:include page="/inc.jsp"></jsp:include>

<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
<link href="/order/css/public_frame.css" rel="stylesheet">
<link href="/order/css/user_taste.css" rel="stylesheet">

<script type="text/javascript" src="/order/js/jquery.1.8.2.js"></script>
<script type="text/javascript" src="/order/js/user_taste.js"></script>
<!-- <script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
<script type="text/javascript" src="/order/qnui/js/sui.min.js"></script> -->

<script type="text/javascript" src="/order/js/public_frame.js"></script>
<script>
$(function(){
	
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
  				<div class="tasteBody">
				<div class="tasteFont">口味选择</div>
				<div class="tasteBox">
				<c:forEach var="taste" items="${tastes }">
					<div class="taste">
					<c:set var="flag" value="0"></c:set>
					<c:forEach var="userTaste" items="${userTastes }">
						<c:if test="${userTaste.taste.name==taste.name }">
						<c:set var="flag" value="1"></c:set>
						</c:if>	
					</c:forEach>
						<c:if test="${flag==0 }"><div class="tasteDetail" flag="0" myval="${taste.id }">${taste.name }</div></c:if>
						<c:if test="${flag==1 }"><div class="tasteDetail" style="color:white;background-color:#426AB3" flag="1" myval="${taste.id }">${taste.name }</div></c:if>
					</div>
				</c:forEach>
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
