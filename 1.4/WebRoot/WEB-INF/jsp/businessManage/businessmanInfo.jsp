<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<title>My JSP 'main.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="/order/css/businessManageHome.css">

<jsp:include page="../../../inc.jsp"></jsp:include>

<style type="text/css">
	.cont1{
		margin-left:220px;
		padding-left:30px;
		padding-top:30px;
		margin-bottom:150px;
		float:left;
		color:#666;
		border-left:5px dashed #ddd;
	}
	.cont{
		margin-left:30px;
		padding-left:30px;
		padding-top:30px;
		margin-bottom:150px;
		float:left;
		color:#666;
		border-left:5px dashed #ddd;
	}
	.clear{
		clear:both;
	}
	.left{
		height:60px;
	}
	.modify{
		margin-top:20px;
		margin-bottom:20px;
		color:#CD5B45;
		height:50px;
		padding-left:60px;
		line-height:50px;
		background-color:#bbb;
	}
	table{
		width:600px;
	}
	a{
		color:#98FB98;
		text-decoration:none;
	}
	a:hover{
		color:#FF7F50;
	}
	.store{
		float:right;
		padding-right:60px;
	}
</style>
</head>

<body>
		<div class="modify">基本信息，<a href="/order/business/businessM_showBusinessInfo.action">修改请点我！</a>
			<span class="store"><a href="/order/business/store_gotoStore.action">店铺信息</a></span>
		</div>
		<div class="cont">
			<div class="left">商家编号：${businessman.businessmanNum}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">储值卡号：${card.cardNumber}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">邮箱：${businessman.email}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">身份证：${businessman.idCard}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
		</div>
		<div class="cont1">
			<div class="left">商家姓名：${businessman.username}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">电话：${businessman.tell}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">账户密码：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			
		</div>
		<%-- <div class="cont1">
			<div class="modify">拥有店铺信息，<a href="/order/business/businessM_showBusinessInfo.action">修改请点我！</a>
				<hr style="height:1px;border:1px;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">店铺名称：${store.storeName}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">电话：${store.cardNumber}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">注册号：${businessman.email}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">logo：${businessman.idCard}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">地址：${businessman.username}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">好评度：${businessman.tell}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">营业额：${store.storeName}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">月销量：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">创建时间：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">开店时间：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">打样时间：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">店铺类别：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">口味：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">商家公告：&nbsp&nbsp******
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
		</div>
		<div class="clear"></div> --%>
</body>
</html>
