<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contentPath = request.getContextPath();
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
		<!-- å¼å¥jqueryåeasyuiçjsæä»¶ -->
		<script src="/order/easyui/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/order/easyui/jquery.easyui.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/order/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript" charset="utf-8"></script>
		
		<!-- jquery éªè¯æ¡æ¶js 
		
		<script src="/order/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
		<script src="/order/js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="/order/js/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>-->
		
		<link rel="stylesheet" href="/order/easyui/themes/metro/easyui.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<link rel="stylesheet" href="/order/easyui/themes/icon.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
		<!-- Replace favicon.ico & apple-touch-icon.png in the root of your domain and delete these references -->
		<link rel="shortcut icon" href="/favicon.ico">
		<link rel="apple-touch-icon" href="/apple-touch-icon.png">
