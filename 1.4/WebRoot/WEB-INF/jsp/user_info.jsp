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

<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
<link href="/order/css/public_frame.css" rel="stylesheet">
<script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
<script type="text/javascript" src="/order/qnui/js/sui.min.js"></script>
<style>
#showContainer {
	width: 600px;
	padding-left: 20px;
	padding-bottom: 10px;
	padding-top: 30px;
	height: auto;
}

.label {
	font-size: 18px;
	line-height: 40px;
	margin: 7px;
}

.info a{
	color:#96CDCD;
	cursor:pointer;
}
.info a:hover{
	color:#C0FF3E;
}
.jiben{
	width:80px;
	height:25px;
	padding-left:27px;
	background-color:#96CDCD;
	color:#FFF;
	padding-top:5px;
	font-size:15px;
}
.info{
	margin-buttom:20px;
	padding-left:20px;
	height:40px;
	font-size:14px;
}
.aTag{
	float:right;
	padding-right:300px;
}
.tanchu{
	margin:20px;
	margin-left:90px;
	font-size:15px;
}
.input{
	font-size:15px;
	margin-left:90px;
}
.tishi{
	margin:0 auto;
	padding:0 auto;
	width:100%;
	text-align:center;
	height:40px;
	background-color:#F0FFFF;
	padding-top:15px;
	font-size:15px;
	
}
</style>
<script>
	$(document).ready(function() {
		$("#nameM").click(function() {
			$("#nameM_form").submit();
		});
	});
	
	$(document).ready(function() {
		$("#emailM").click(function() {
			$("#emailM_form").submit();
		});
	});
	
	$(document).ready(function() {
		$("#telM").click(function() {
			$("#telM_form").submit();
		});
	});
	
	$(document).ready(function() {
		$("#telA").click(function() {
			$("#telA_form").submit();
		});
	});
	
	$(document).ready(function() {
		$("#pswM").click(function() {
			$("#pswM_form").submit();
		});
	});
	

</script>

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
				<div id="showContainer">
					<c:if test="${null!=tishi}">
						<div class="tishi">
							${tishi}
						</div>
					</c:if>
					<div class="jiben">基本信息</div>
					<hr size=5 width="99%"></hr>
						<div class="info">
							用户名：${result.name}<span class="aTag"><a data-toggle="modal" data-target="#nameMod">修改</a></span>
						</div>
						<div class="info">
							邮箱：${result.email}<span class="aTag"><a data-toggle="modal" data-target="#emailMod">修改</a></span>
						</div>
						<div class="info">
							手机号：<c:if test="${null==result.tel}"><span class="aTag"><a data-toggle="modal" data-target="#telAdd">添加</a></span></c:if>
							${result.tel}<c:if test="${null!=result.tel}"><span class="aTag"><a data-toggle="modal" data-target="#telMod">更改</a></span></c:if>
						</div>
						<div class="info">
							密码：******<span class="aTag"><a data-toggle="modal" data-target="#pswMod">修改</a></span>
						</div>
					
				</div>
				<!-- 弹出框Modal   修改姓名-->
				<div id="nameMod" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">修改用户名</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="/order/core/userInfo_nameM.action" id="nameM_form"
									method="post">
									<div class="control-group">
										<div class="tanchu" >当前用户名：&nbsp&nbsp ${result.name}</div>
									</div>
									<div class="control-group">
										<label class="input" >新用户名:</label>&nbsp&nbsp
										<div class="controls">
											<input type="text" name="userName" data-rules="required|minlength=3|maxlength=15" style="height:25px">
										</div>
									</div>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="nameM">确定</button>
								<button type="button" data-dismiss="modal"
									class="sui-btn btn-default btn-large">取消</button>
							</div>
			
						</div>
					</div>
				</div>
				
					<!-- 弹出框Modal     修改邮箱-->
				<div id="emailMod" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">修改邮箱</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="/order/core/userInfo_emailM.action" id="emailM_form"
									method="post">
									<div class="control-group">
										<div class="tanchu" >当前邮箱：&nbsp&nbsp${result.email}</div>
									</div>
									<div class="control-group">
										<label class="input" >新邮箱:</label>&nbsp&nbsp
										<div class="controls">
											<input type="text" data-rules="required|email" name="useremail" style="height:25px;width:150px;">
										</div>
									</div>
			
								</form>
			
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="emailM">确定</button>
								<button type="button" data-dismiss="modal"
									class="sui-btn btn-default btn-large">取消</button>
							</div>
			
						</div>
					</div>
				</div>
				
					<!-- 弹出框Modal     修改手机-->
				<div id="telMod" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">修改手机</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="/order/core/userInfo_telM.action" id="telM_form"
									method="post">
									<div class="control-group">
										<div class="tanchu" >当前手机号：&nbsp&nbsp${result.tel}</div>
									</div>
									<div class="control-group">
										<label class="input" >新手机号:</label>&nbsp&nbsp
										<div class="controls">
											<input type="text" data-rules="required|mobile" name="telMo" style="height:25px;width:150px;">
										</div>
									</div>
			
								</form>
			
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="telM">确定</button>
								<button type="button" data-dismiss="modal" class="sui-btn btn-default btn-large">取消</button>
							</div>
			
						</div>
					</div>
				</div>
				
					<!-- 弹出框Modal     添加手机-->
				<div id="telAdd" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">添加手机</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="/order/core/userInfo_telA.action" id="telA_form"
									method="post">
									<div class="control-group">
										<label class="input" >输入手机号:</label>&nbsp&nbsp
										<div class="controls">
											<input type="text" data-rules="required|mobile" name="telA" style="height:25px;width:150px;">
			
										</div>
									</div>
			
								</form>
			
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="telA">确定</button>
								<button type="button" data-dismiss="modal"
									class="sui-btn btn-default btn-large">取消</button>
							</div>
			
						</div>
					</div>
				</div>
				
					<!-- 弹出框Modal     修改密码-->
				<div id="pswMod" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">修改密码</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="/order/core/userInfo_pswM.action" id="pswM_form"
									method="post">
									<div class="control-group">
										<label class="input" >旧密码:</label>&nbsp&nbsp
										<div class="controls">
											<input type="password" data-rules="required|minlength=6|maxlength=18" name="pwdOld" style="height:25px;width:150px;">
										</div>
									</div>
									<div class="control-group">
										<label class="input" >新密码:</label>&nbsp&nbsp
										<div class="controls">
											<input type="password" data-rules="required|minlength=6|maxlength=18" name="pwdNew1" style="height:25px;width:150px;">
										</div>
									</div>
									<div class="control-group">
										<label class="input" >新密码:</label>&nbsp&nbsp
										<div class="controls">
											<input type="password" data-rules="required|minlength=6|maxlength=18" name="pwdNew2" style="height:25px;width:150px;">
										</div>
									</div>
			
								</form>
			
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="pswM">确定</button>
								<button type="button" data-dismiss="modal"
									class="sui-btn btn-default btn-large">取消</button>
							</div>
			
						</div>
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
