<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'header.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<link rel="stylesheet" type="text/css" href="/order/css/header.css">
	<script type="text/javascript" src="inc.jsp"></script>
	<script type="text/javascript" src="/order/js/header.js"></script>
  </head>
  
  <body>
    <div id="header1">
			
			<div id="header1Main">
				
				<div id="address"><font id="font1">${sessionScope.defaultAddress }</font>
				<font>
					<c:if test="${!empty requestScope.canSwitchAddress }">
						<a href="javascript:void(0)" id='switchAddress'>&nbsp;&nbsp;[切换地址]</a>
					</c:if>
				</font></div>
			
				<div id="aDiv">
				
					<a href="/order/core/login_logout.action">退出</a>
					<a href="/order/businessmanLogin.jsp">我是商家</a>
				</div>
				<div id="loginUser">
				<c:set var="loginedUser" value="${sessionScope.loginedUser }"></c:set>
					<c:choose>
						<c:when test="${!empty loginedUser }">
							<font>欢迎你，<a href="/order/core/userInfo_listUserInfo.action"> ${loginedUser.name }</a> </font>
						</c:when>
						<c:otherwise>
							<a href="/order/login.jsp">登陆</a> | <a href="/order/register.jsp">注册</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div id="header2">
			
			<div id="header2Main">
				
				<div id="logo">
					<img src="/order/images/sixsixLogo.png" style="width:160px "/>
				</div>
			<div id="menu">
				
				<ul>
					<li><a href="/order/core/goto_home.action">首页</a></li>
					<c:if test="${!empty sessionScope.loginedUser }">
						<li><a href="/order/core/order_findLatest.action">我的菜篮子</a></li>
					</c:if>
					<c:if test="${empty sessionScope.loginedUser }">
						<li><a href="javascript:void(0)">我的菜篮子[未登录]</a></li>
					</c:if>
					
					<li><a href="javascript:void(0)">敬请期待</a></li>
				</ul>
			</div>
			<div id="search">
				
				<div id="inputControl">
					
					<input id="searchText" type="text" style="height: 28px;width: 260px;font-size: 15px;" placeholder="搜索商家，美食"/>
					
					<a href="javascript:void(0)" id="searchButton">搜索！</a>
					
				</div>
			</div>
			</div>
		</div>
  </body>
</html>
