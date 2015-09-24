<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'businessmanLogin.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="/order/css/businessmanLogin.css">

	<jsp:include page="inc.jsp"></jsp:include>
	
	<script type="text/javascript" src="/order/js/businessmanLogin.js"></script>
  </head>
  
 <body>
		<div id="header">
			
		</div>
			
		<div id="main">
			
			<div id="left">
				
				<div id="imageDiv">
					
					<img src="/order/images/busLogo.png" width="500PX"/>
				</div>
			</div>
			<div id="right">
				
				<span id="loginTip">账号登陆</span>
				<div id="formDiv">
		
				<div id="error"></div>
			    <form method="post">
			    	
			    	<div class="inputControl">
			    		<input name="email" id="email" placeholder="请输入登陆邮箱"/><br>
			    	</div>
			    	<div class="inputControl">
			    		<input name="password" type="password" id="password" placeholder="请输入密码" /><br>
			    	</div>
			   		<div id="smailDiv">
			   			
			   			<div id="smailLeft"><input type="checkbox" /><font>7天内免登陆</font></div>
			   			<div id="smailRigth"><font>忘记密码？</font></div>
			   		</div>
			   		
			   		<div id="loginDiv">
			   			
			   			<a href="javascript:void(0)">登陆</a>
			   		</div>
			    </form>

				</div>
			</div>
		</div>
		<div id="footer"></div>
	</body>
</html>
