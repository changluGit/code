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

<title>订单管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="../../../inc.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="/order/css/orderManage.css">
<script type="text/javascript" src="/order/js/datagrid-detailview.js"></script>
<script type="text/javascript" src="/order/js/orderManage.js"></script>
</head>

<body>
	<!-- =================================查询区域============================= -->
	 <div id="search_div">
    	<form id="search_form" method="post">
    	<table>
    		<tr>
    			<td>
    				<a class="searchType" href="javascript:void(0)">全部</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">已下单</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">取消订单中</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">已确认</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">派送中</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">送达</a>
    			</td>
    			<td>
    				<a class="searchType" href="javascript:void(0)">已取消</a>
    			</td>
    		</tr>
    		<tr>
    			
    		</tr>
    	</table>
    	</form>
    </div>
	
	<!-- ======================用户数据显示的datagrid================= -->
	
	 <table id="dg" style="width: 1024px;" title="订单管理" iconcls="icon-view"></table>

    
   
	
</body>
</html>

