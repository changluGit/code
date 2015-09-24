<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
		margin-left:30px;
		padding-left:30px;
		margin-top:5px;
		margin-bottom:100px;
		float:left;
		color:#666;
		line-heigth:60px;
		width:410px;
	}
	.cont1{
		margin-left:10px;
		padding-left:10px;
		margin-top:5px;
		float:left;
		color:#666;
		width:320px;
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
	.mydialog1{
		margin-left:80px;
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
	.tishi1{
		margin:0 auto;
		padding:0 auto;
		width:100%;
		text-align:center;
		height:20px;
		background-color:#F0FFFF;
		font-size:15px;
		
	}
	.system{
		color:#FF6A6A;
	}
	.myinput{
		lenght:120px;
	}
</style>
</head>

<body>
		<div class="modify">店铺信息，<a href="javascript:void()" id="storeM" onclick="">修改请点我！</a>
			<span class="store"><a href="/order/business/business_showBusinessInfo.action">店主信息</a></span>
		</div>
		<c:if test="${null!=tishi}">
			 <div class="tishi">
				${tishi}
			</div> 
		</c:if>
		 <div class="cont">
			<div class="left">店铺名称：${store.storeName}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">电话：${store.telephone}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">注册号：${store.regid}&nbsp&nbsp<span class="system">(次编号为系统管理员下发，无法更改)</span>			
			<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">地址：${store.businessAddress}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">好评度：${store.rating}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">营业额：${store.ternover}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">月销量：${store.saleVolume}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">logo：<img src="${store.logoAddress}" style="width:50px;height:38px;"/>
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
		</div>
		<div class="cont">
			<div class="left">营业时间：${store.beginSaleHour}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">打样时间：${store.endSaleTime}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">店铺类别：${store.storeCategory}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">开店时间：${store.creatTime}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">口味：${store.taste}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">商家公告：${store.merchantsAnnouncement}
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			<div class="left">送餐时间：${store.foodDeliveryTime}分钟
				<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
			</div>
			 <%-- <div class="left"><a id="stateB">开关${store.state}</a>
			</div> --%> 
		</div>
		<div class="clear"></div> 
		<!-- 修改店铺信息 弹窗-->
		<div id="storeI" class="easyui-dialog mydia" title="修改店铺信息" style="width:740px;height:430px;"   
	     	 	 data-options="
				iconCls: 'icon-save',
				buttons: '#storeM-buttons'
			">
			<form action="/order/business/store_storeModify.action" id="storeM_form" method="post" enctype="multipart/form-data"> 
			  	<div style="margin:20px">
			  		<div class="cont1">
						<div class="left">店铺名称：<input value="${store.storeName}" type="text" name="name" class="easyui-validatebox myinput" data-options="required:true" ></input>
						</div> 
						<div class="left">订餐电话：<input value="${store.telephone}" type="text" name="tel" class="easyui-numberbox" data-options="required:true,validType:'length[11,11]'" invalidMessage='请填写正确手机号码(只能是数字)'></input>
						</div> 
						<div class="left">店铺logo：<s:file name="file"></s:file>
						</div> 
						<div class="left">店铺地址：<input value="${store.businessAddress}" type="text" name="addr" class="easyui-validatebox myinput" data-options="required:true" ></input>
						</div> 
						<div class="left">店铺类别：<input value="${store.storeCategory}" type="text" name="type" class="easyui-validatebox myinput" data-options="required:true" ></input>
						</div> 							
					</div>
					<div class="cont1"> 
						<div class="left">营业时间：
							<input value="${store.beginSaleHour}" editable="false " name="open" class="easyui-datetimebox" data-options="required:true" style="width:170px">
						</div> 
						<div class="left">打烊时间：
							<input value="${store.endSaleTime}" editable="false" name="stop" class="easyui-datetimebox" data-options="required:true" style="width:170px">
						</div> 
						<div class="left">商家公告：<input value="${store.merchantsAnnouncement}" type="text" name="gong" class="easyui-validatebox myinput" data-options="required:true" ></input>
						</div> 
						<div class="left">送餐速度：<input value="${store.foodDeliveryTime}" type="text" name="speed" class="easyui-numberbox" data-options="required:true" invalidMessage='(只能是数字)'>分钟</input>
						</div> 
						<div class="left">菜品口味：<input value="${store.taste}" type="text" name="taste" class="easyui-validatebox myinput" data-options="required:true" ></input>
						</div>  
					</div> 
					<div class="clear"></div>
				</div>
	 		</form>
		</div> 
		<div id="storeM-buttons">
			<a href="javascript:void(0)" id="storeB" class="easyui-linkbutton" onclick="">确定</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#storeI').dialog('close')">取消</a>
		</div> 
</body>
<script type="text/javascript">

$('#storeI').ready(function () {
	$('.mydia').dialog('close');
});
 $('#stateB').ready(function () {
	$("#stateB").click(function() {
		$.ajax({  
	    url: '/order/business/state_storeState.action',
	    data:{state:"open"}, 
	    dataType: "json",  
	    type: "POST", 
	   	success: function (responseJSON) {   
	   		 this.innerHTML=(this.innerHTML=='修改'?'关闭':'修改');
        	 alert('Ok');  
   		}  
		});
	});
	
}); 
 
$('#storeM').ready(function () {
	$("#storeM").click(function() {
		var top = $("#storeM").offset().top-20;
		var left = $("#storeM").offset().left+30;
		$('#storeI').window('open').window('resize',{top: top,left:left});
		$("#storeB").click(function() {
		$("#storeM_form").submit();
	});
	});
	
});
</script>
</html>
