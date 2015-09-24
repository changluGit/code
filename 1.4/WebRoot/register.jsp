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

<title>My JSP 'register.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" type="text/css" href="css/rigester.css">
<link rel="stylesheet" type="text/css" href="css/login.css">	
<jsp:include page="inc.jsp"></jsp:include>
<script type="text/javascript" src="js/register.js"></script>
<script src="/order/js/Area.js" type="text/javascript"></script>
<script src="/order/js/AreaData_min.js" type="text/javascript"></script>
</head>

<body class="pg-unitive-signup theme--www signup-steps"
	id="yui_3_16_0_1_1438342708407_97">

	<header>
	<div class="wrapper cf">

		<a class="" href="#" gaevent="header/logo"></a> <span
			class="site-delimiter"></span> <span class="site-name f4">注册账号</span>

		<div class="login-block">
			<span class="f2 tip">已有账号？</span> <a class="btn btn-small login"
				href="/order/login.jsp" target="_blank">登录</a>
		</div>
	</div>
	</header>
	<div class="content" id="yui_3_16_0_1_1438342708407_208">

		<div class="J-unitive-signup-form" id="yui_3_16_0_1_1438342708407_11">
			<div class="tabview" data-uix="tabview"
				data-params="{&quot;trigger&quot;:&quot;.trigger&quot;,&quot;sheet&quot;:&quot;.sheet&quot;,&quot;activeClass&quot;:&quot;current&quot;}"
				id="yui_3_16_0_1_1438342708407_151">

				<div class="sheet" id="yui_3_16_0_1_1438342708407_13">
					<form action="" method="POST" id="yui_3_16_0_1_1438342708407_49">
						<span style="display:none"><input type="hidden" name="csrf"
							value="M_5hXfMnq79Qi9R941JBzZkLcfNb0kR74W-YwE7pj3Zj3k9MLeX7lFFrgLp8JIVY"></span>
						<div class="form-field form-field--email"
							id="yui_3_16_0_1_1438342708407_29">
							<div id="signup-email-auto" class="email-auto"
								style="display:none;">
								<p class="email-title">请选择您的邮箱类型...</p>
								<ul class="email-list"></ul>
							</div>
							<label>邮箱</label><input type="text" name="email"
								class="f-text J-email">
								<span id="span1" class="error"></span>
						</div>
						<div class="form-field form-field--uname"
							id="yui_3_16_0_1_1438342708407_32">
							<label>用户名</label><input type="text" name="username"
								class="f-text J-uname">
								<span id="span2" class="error"></span>
						</div>
						<div class="form-field form-field--pwd"
							id="yui_3_16_0_1_1438342708407_35">

							<label>创建密码</label><input type="password" name="password"
								class="f-text J-pwd">
								<span id="span3" class="error"></span>
						</div>
						<div class="form-field form-field--pwd2"
							id="yui_3_16_0_1_1438342708407_38">
							<label>确认密码</label><input type="password" name="password2"
								class="f-text J-pwd2">
								<span id="span4" class="error"></span>
						</div>
						<div class="form-field form-field--pwd2"
							id="yui_3_16_0_1_1438342708407_38">
							<label>真实姓名</label><input type="text" name="name"
								class="f-text J-uname">
								<span id="span5" class="error"></span>
						</div>
						<div class="form-field form-field--pwd2"
							id="yui_3_16_0_1_1438342708407_38">
							<label>电话号码</label><input type="text" name="tel"
								class="f-text J-uname">
								<span id="span6" class="error"></span>
						</div>
						
						<div class="form-field form-field--pwd2"
							id="yui_3_16_0_1_1438342708407_38">
							<label>选择地址</label>
							<select id="seachprov" name="seachprov"
			onChange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>
			&nbsp;&nbsp;
		<select id="seachcity" name="homecity"
			onChange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
		<span id="seachdistrict_div"><select id="seachdistrict"
			name="seachdistrict"></select></span> 
						</div>
						
						<div class="form-field form-field--pwd2"
							id="yui_3_16_0_1_1438342708407_38">
							<label>详细地址</label><input type="text" name="addressDetail"
								class="f-text J-uname">
								<span id="span7" class="error"></span>
						</div>
						
						<div class="form-field form-field--book">
							<label></label> <input type="checkbox" name="subscribe"
								id="subscribe" class="ui-checkbox f-check" checked="checked">

						</div>

						<div class="form-field">
							<a href="javascript:void(0)" id="registerButton">同意以下协议并注册</a>
						</div>
						<input type="hidden" name="fingerprint" class="J-fingerprint"
							value="0-1-1-">
					</form>
				</div>
			</div>
		</div>
		<div class="field-group">
			<a class="f1" href="" target="_blank">《用户协议》</a>
		</div>
	</div>


	<footer>
	<p class="copyright">
		©<a class="f1" href="">xx.com</a>&nbsp;<a class="f1"
			target="_blank" href="#">京ICP证070791号</a>&nbsp;<span class="f1">京公网安备11010502025545号</span>
	</p>
	</footer>




</body>
</html>
