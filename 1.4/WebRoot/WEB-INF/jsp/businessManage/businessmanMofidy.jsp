<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	.cont{
		margin-left:80px;
		margin-top:30px;
		margin-right:580px;
		margin-bottom:120px;
		color:#666;
	}
	.clear{
		clear:both;
	}
	.left{
		height:40px;
		margin-buttom:12px;
		font-size:14px;
	}
	.right{
		float:right;
		padding-right:80px;
	}
	.modify{
		margin-top:20px;
		padding-bottom:20px;
		color:#8FBC8F;
	}
	a{
		color:#B3EE3A;
		text-decoration:none;
		cursor:pointer;
	}
	a:hover{
		color:#FF7F50;
	}
	.mydialog{
		margin-top:20px;
		line-height:60px;
		margin-left:80px;
	}
	.mydialog1{
		margin-left:80px;
		line-height:60px;
	}
	.mydialog3{
		margin-left:80px;
		margin-top:60px;
		line-height:60px;
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
	.system{
		color:#FF6A6A;
	}
</style>

</head>

<body>
	<div class="cont">
		<c:if test="${null!=tishi}">
			<div class="tishi">
				${tishi}
			</div>
		</c:if>
		 <div class="left">商家编号：${businessman.businessmanNum}&nbsp&nbsp&nbsp<span class="system">(此编号为系统管理员下发，无法修改)</span></div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		<div class="left">储值卡号：${card.cardNumber}&nbsp&nbsp&nbsp<span class="system">(此编号为系统管理员下发，无法修改)	</span>
		</div><div class="clear"></div>
			<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" /> 
		<div class="left">邮箱：${businessman.email}
			<c:if test="${null!=businessman.email}"><span class="right"><a href="javascript:void()" id="emailM" onclick="">修改</a></span></c:if>
		</div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		<div class="clear"></div>
		<div class="left">身份证：${businessman.idCard}
			<c:if test="${null!=businessman.idCard}"><span class="right"><a href="javascript:void()" id="idcM" onclick="$('#idCardMod').dialog('open')">修改</a></span></c:if>
			<c:if test="${null==businessman.idCard}"><span class="right"><a href="javascript:void()" id="idcA" onclick="$('#idCardAdd').dialog('open')">添加</a></span></c:if>
		</div>
		<div class="clear"></div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		<div class="left">商家姓名：${businessman.username}
			<c:if test="${null!=businessman.username}"><span class="right"><a href="javascript:void()" id="nameM" onclick="$('#nameMod').dialog('open')">修改</a></span></c:if>
			<c:if test="${null==businessman.username}"><span class="right"><a href="javascript:void()" id="nameA" onclick="$('#nameAdd').dialog('open')">添加</a></span></c:if>
		</div><div class="clear">
		</div><hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		<div class="left">电话：${businessman.tell}
			<c:if test="${null!=businessman.tell}"><span class="right"><a id="telM" href="javascript:void()" onclick="$('#telMod').dialog('open')">修改</a></span></c:if>
			<c:if test="${null==businessman.tell}"><span class="right"><a id="telA" href="javascript:void()" onclick="$('#telAdd').dialog('open')">添加</a></span></c:if>
		</div><div class="clear"></div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		
		<%-- <div class="left">店铺名称：${store.storeName}
		<c:if test="${null!=store.storeName}"><span class="right"><a id="storeM" href="javascript:void()" onclick="$('#storeAdd').dialog('open')">修改</a></span></c:if>
		</div> 
			<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />--%>
		<div class="left">账户密码：*******<span class="right"><a id="pwdM" href="javascript:void()" onclick="$('#psw').dialog('open')" >修改</a></span>
		</div>
		<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
		<div class="left">
			修改完毕，点我<a type="button" href="/order/business/business_showBusinessInfo.action" style="color:#FF6347">返回</a>
		</div>
	</div>
	<!--修改用户名弹窗 -->
	<div id="nameMod" class="easyui-dialog mydia" title="用户名修改" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#name-buttons'
			">
			<form action="/order/business/businessM_businessNamemodify.action" id="nameM_form" method="post">
	  			 <div class="mydialog">原商家姓名：${businessman.username}</div> 
	  			 <div class="mydialog1">新商家姓名：<input type="text" name="userName" class="easyui-validatebox" data-options="required:true,validType:'length[3,16]'" ></input></div> 
				
  			</form>
	</div> 
	<div id="name-buttons">
		<a href="javascript:void(0)" id="nameB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#nameMod').dialog('close')">取消</a>
	</div> 
	<!--添加用户名弹窗 -->
	<div id="nameAdd" class="easyui-dialog mydia" title="用户名添加" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#nameAdd-buttons'
			">
			<form action="/order/business/businessM_businessNamemodify.action" id="nameAdd_form" method="post">
	  			 <div class="mydialog3">填写商家姓名：<input type="text" name="userName" class="easyui-validatebox" data-options="required:true,validType:'length[3,16]'" ></input></div> 
				
  			</form>
	</div>
	<div id="nameAdd-buttons">
		<a href="javascript:void(0)" id="nameAddB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#nameAdd').dialog('close')">取消</a>
	</div>  
	<!--修改email弹窗 -->
	<div id="emailMod" class="easyui-dialog mydia" title="email修改" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#email-buttons'
			">
			<form action="/order/business/businessM_businessEmailmodify.action" id="emailM_form" method="post">
	  			 <div class="mydialog">原商家email：${businessman.email}</div> 
	  			 <div class="mydialog1">新商家email：<input type="text" name="myemail" class="easyui-validatebox" data-options="required:true,validType:'email'" ></input></div> 
				
  			</form>
	</div>
	<div id="email-buttons">
		<a href="javascript:void(0)" id="emailB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#emailMod').dialog('close')">取消</a>
	</div>  
	<!-- 修改身份证号 弹窗-->
	<div id="idCardMod" class="easyui-dialog mydia" title="身份证号码修改" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#idCardMod-buttons'
			">
			<form action="/order/business/businessM_businessIdcardmodify.action" id="idCardM_form" method="post">
	  			 <div class="mydialog">原身份证号码：${businessman.idCard}</div> 
	  			 <div class="mydialog1">新身份证号码：<input type="text" name="idCard" class="easyui-validatebox" data-options="required:true,validType:'length[18,18]'" invalidMessage='长度必须是18位的身份证号码' ></input></div> 
				 
  			</form>
	</div>
	<div id="idCardMod-buttons">
		<a href="javascript:void(0)" id="idCardModB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#idCardMod').dialog('close')">取消</a>
	</div> 
	<!-- 添加身份证号 弹窗-->
	<div id="idCardAdd" class="easyui-dialog mydia" title="身份证号码添加" style="width:400px;height:260px;"   
     	 	 data-options="
			iconCls: 'icon-save',
			buttons: '#idCardAdd-buttons'
		">
		<form action="/order/business/businessM_businessIdcardmodify.action" id="idCardAdd_form" method="post"> 
  			 <div class="mydialog3">填写身份证号码：<input type="text" name="idCard" class="easyui-validatebox" data-options="required:true,validType:'length:18'" ></input></div> 
			
 			</form>
	</div> 
	<div id="idCardAdd-buttons">
		<a href="javascript:void(0)" id="idCardAddB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#idCardAdd').dialog('close')">取消</a>
	</div> 
	<!--修改电话弹窗 -->
		<div id="telMod" class="easyui-dialog mydia" title="电话号码修改" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#telMod-buttons'
			">
			<form action="/order/business/businessM_businessTelmodify.action" id="telM_form" method="post">
	  			 <div class="mydialog">原电话号码：${businessman.tell}</div> 
	  			 <div class="mydialog1">新电话号码：<input type="text" name="tel" class="easyui-numberbox" data-options="required:true,validType:'length[11,11]'" invalidMessage='请填写正确手机号码(只能是数字)'></input></div> 
				
  			</form>
	</div> 
	<div id="telMod-buttons">
		<a href="javascript:void(0)" id="telModB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#telMod').dialog('close')">取消</a>
	</div> 
	<!--添加电话弹窗 -->
	<div id="telAdd" class="easyui-dialog mydia" title="电话号码添加" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#telAdd-buttons'
			">
			<form action="/order/business/businessM_businessTelmodify.action" id="telAdd_form" method="post"> 
	  			 <div class="mydialog3">填写电话号码：<input type="text" name="tel" class="easyui-numberbox textbox" data-options="required:true,validType:'length[11,11]'" invalidMessage='请填写正确手机号码(只能是数字)'></input></div> 
				 
  			</form>
	</div> 
	<div id="telAdd-buttons">
		<a href="javascript:void(0)" id="telAddB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#telAdd').dialog('close')">取消</a>
	</div>
	<!--修改密码弹窗 -->
		<div id="psw" class="easyui-dialog mydia" title="密码修改" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#psw-buttons'
			">
			<form action="/order/business/businessM_businessPassmodify.action" id="psw_form" method="post"> 
	  			<div class="mydialog1">填写旧密码：<input type="password" name="pwdold" class="easyui-validatebox textbox" data-options="required:true,validType:'length[6,16]'" ></input></div> 
	  			<div class="mydialog1">填写新密码：<input type="password" name="pwd1" class="easyui-validatebox textbox" data-options="required:true,validType:'length[6,16]'" ></input></div> 
				<div class="mydialog1">重复新密码：<input type="password" name="pwd2" class="easyui-validatebox textbox" data-options="required:true,validType:'length[6,16]'" ></input></div> 
				
  			</form>
	</div> 
	<div id="psw-buttons">
		<a href="javascript:void(0)" id="pswB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#psw').dialog('close')">取消</a>
	</div> 
	<!--修改店铺名称弹窗 -->
	<div id="storeAdd" class="easyui-dialog mydia" title="店铺添加" style="width:400px;height:260px;"   
      	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#storeAdd-buttons'
			">
			<form action="/order/business/businessM_businessStoremodify.action" id="storeAdd_form" method="post"> 
				 <div class="mydialog">店铺原名：${store.storeName}</div>
	  			 <div class="mydialog1">填写店铺名称：<input type="text" name="storeName" class="easyui-validatebox textbox" data-options="required:true,validType:'lenght[6,16]'" ></input>
		  			
		  		</div> 
				
  			</form>
	</div> 
	<div id="storeAdd-buttons">
		<a href="javascript:void(0)" id="storeAddB" class="easyui-linkbutton" onclick="">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#storeAdd').dialog('close')">取消</a>
	</div> 
</body>
<script type="text/javascript">

$('#emailMod').ready(function () {
	$('.mydia').dialog('close');
});

	/*  easyui 弹框      :多个弹窗不会居中显示
 $('#nameMod').ready(function () {
	$("#nameB").click(function() {
		$("#nameM_form").submit();
	});
	
});
       下面可以*/
$('#nameM').ready(function () {
	$("#nameM").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#nameMod').window('open').window('resize',{top: top,left:left});
		$("#nameB").click(function() {
		$("#nameM_form").submit();
	});
	});
	
});

$('#emailM').ready(function () {
	$("#emailM").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#emailMod').window('open').window('resize',{top: top,left:left});
		$("#emailB").click(function() {
		$("#emailM_form").submit();
	});
	});
	
});

/* $('#nameAdd').ready(function () {
	$("#nameAddB").click(function() {
		$("#nameAdd_form").submit();
	});	
}); */
$('#nameA').ready(function () {
	$("#nameA").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#nameAdd').window('open').window('resize',{top: top,left:left});
		$("#nameAddB").click(function() {
		$("#nameAdd_form").submit();
	});
	});
	
});
/* $('#idCardMod').ready(function () {
	$("#idCardModB").click(function() {
		$("#idCardM_form").submit();
	});	
}); */
$('#idcM').ready(function () {
	$("#idcM").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#idCardMod').window('open').window('resize',{top: top,left:left});
		$("#idCardModB").click(function() {
		$("#idCardM_form").submit();
	});
	});
	
});
/* $('#idCardAdd').ready(function () {
	$("#idCardAddB").click(function() {
		$("#idCardAdd_form").submit();
	});	
}); */
$('#idcA').ready(function () {
	$("#idcA").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#idCardAdd').window('open').window('resize',{top: top,left:left});
		$("#idCardAddB").click(function() {
		$("#idCardAdd_form").submit();
	});
	});
	
});

/* $('#telMod').ready(function () {
	$("#telModB").click(function() {
		$("#telM_form").submit();
	});	
});
 */
$('#telM').ready(function () {
	$("#telM").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#telMod').window('open').window('resize',{top: top,left:left});
		$("#telModB").click(function() {
		$("#telM_form").submit();
	});
	});
	
});
/* $('#telAdd').ready(function () {
	$("#telAddB").click(function() {
		$("#telAdd_form").submit();
	});	
}); */
$('#telA').ready(function () {
	$("#telA").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#telAdd').window('open').window('resize',{top: top,left:left});
		$("#telAddB").click(function() {
		$("#telAdd_form").submit();
	});
	});
	
});
/* $('#storeAdd').ready(function () {
	$("#storeAddB").click(function() {
		$("#storeAdd_form").submit();
	});	
}); */
$('#storeA').ready(function () {
	$("#storeA").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#storeAdd').window('open').window('resize',{top: top,left:left});
		$("#storeAddB").click(function() {
		$("#storeAdd_form").submit();
	});
	});
	
});
/* $('#psw').ready(function () {
	$("#pswB").click(function() {
		$("#psw_form").submit();
	});	
}); */
$('#pwdM').ready(function () {
	$("#pwdM").click(function() {
		var top = $("#emailM").offset().top + 30;
		var left = $("#emailM").offset().left;
		$('#psw').window('open').window('resize',{top: top,left:left});
		$("#pswB").click(function() {
		$("#psw_form").submit();
	});
	});
	
});
</script>
</html>
