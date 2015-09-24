<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Head -->
<c:set var="loginedUser" value="${sessionScope.loginedUser }"></c:set>
<div class="hU">
	<div class="hUL"><font id="font1">${sessionScope.defaultAddress }</font></div>
	
	<c:choose>
		<c:when test="${!empty loginedUser }">
			<div class="hUR">欢迎您，<a href="/order/core/userInfo_listUserInfo.action">${loginedUser.name }</a> &nbsp;&nbsp;
				<a href="/order/core/login_logout.action"> 退出 </a> &nbsp;&nbsp;
				<a href="/order/businessmanLogin.jsp">我是商家</a>
			</div>		
		</c:when>
		<c:otherwise>
			<a href="/order/login.jsp">登陆</a> | <a href="/order/register.jsp">注册</a>
		</c:otherwise>
	</c:choose>
</div>
<div class="clear"></div>
<div class="hD">
	<div class="hD1"><img src="/order/images/sixsixLogo.png" class="HLogo"/></div>
	<div class="hD2">
		<div class="hD2D"><a href="#">测试</a></div> 
		<div class="hD2D"><a href="/order/core/order_findLatest.action">我的菜篮子</a></div>
		<div class="hD2D"><a href="/order/core/goto_home.action">首页</a></div> 
	</div>
	<div class="hD3">
		<!-- <div class="searchBox">
			<input type="text" class="searchInput"></input>
			<div class="searchBtn">搜索</div>
		</div> -->
		
	</div>
	
</div>