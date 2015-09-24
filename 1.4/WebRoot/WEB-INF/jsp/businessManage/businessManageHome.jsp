<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<script type="text/javascript">
	$(document).ready(function() {

			var orderId = "";
			//点击左功能树，对应处理函数
			
			function addTab(title,url)
			{
				var tabs = $('#main_tabs');
				var opts = {
							title : title,
							closable : true,
							iconCls : 'icon-ok',
							content : '<iframe src="'+ url + '" allowTransparency="true" style="border:0;width:100%;height:95%;" frameBorder="0"></iframe>',
							border : false,
							fit : true
						};
				if (tabs.tabs('exists', opts.title)) {
							tabs.tabs('select', opts.title);
				} else {
					tabs.tabs('add', opts);
				}
			}
			/*--------------为module 自选项添加选中事件--------------*/
			$(".module_ul li").on("click", function(event) {
				
				var title = event.target.innerHTML;
				/*----------------会议发起--------------------*/
				if("回复评价" == title)
				{	
					addTab(title, "/order/business/orderEvaluate_gotoReplyPage.action");
				}

				else if("菜品管理" == title)
				{
					addTab(title, "/order/core/dishes_init.action");
				}

				else if("菜品类别" == title)

				{

					addTab(title, "/order/business/dishesCategory_gotoDishesCategoryPage.action");

				}
				
				else if("订单管理" == title)
				{
					addTab(title, "/order/business/orderM_gotoOrderManagePage.action");
				} 
				
				
				else if("商家信息" == title){
					
					addTab(title, "/order/business/business_showBusinessInfo.action");
				}
				else if("统计报表" == title)
				{	
					addTab(title, "/order/dish/gotoDish.action");
				}
				
			});
	
			
			jQuery.fn.extend({
				
				addTab: addTab
			});
			
			function changeStoreState(state){
			
				$.post("/order/business/state_storeState.action", {"state":state}, function(result){
				
					var type = result.type;
					var message = result.message;
					
					if(type == "Success"){
						if(state == "open"){
							$("#open").html("营业中");
						}
						else if(state == "close"){
						
							$("#open").html("营业");
							
							$.messager.confirm("消息", message,function(r){
							
								if(r)
								//跳转到主页面
									location.href = "/order/businessmanLogin.jsp";
							
							});
						}
							
					}
				},"json");
			}
			
			$("#open").on("click", function(){
				changeStoreState("open");
			});
			
			$("#close").on("click", function(){
				changeStoreState("close");
			});
			
			
			
			//设置 每10秒 查询一下未确定的订单
			setInterval(getNoconfirmOrder,10000);
				
			function getNoconfirmOrder(){
				$.post("/order/business/noConfirm_getNoComfirmOrder.action", function(result){
					
					var type = result.type;
					var message = result.message;
					
					if(type == 'Success'){
						orderId = message.id;
						var body = "订单编号："+ message.orderNum;
						body += "<br>下单用户：" + message.username;
						body += "<br>创建时间：" + message.createTime;
						body += "<div>"+
							"订单详细信息："+ message.dishDetail+";<br>订单总额：￥"+message.totalAmount+
							"元<br>配送地址：" + message.shouhuoren+"&nbsp;&nbsp"+message.tel +"&nbsp;&nbsp"+message.address+";<br>用户留言："+message.userMessage
						+"</div>";
						
						$.messager.defaults = { ok: "确认该订单", cancel: "稍候确认" };
  
        				$.messager.confirm("新订单", body, function (r) {
			            if (r) {
			                $.post("/order/business/orderM_handleOrderState.action", {"orderId": orderId,"newStateCode":4},function(result){
						
						var type = result.type;
						var message = result.message;
						if("Success" == type){
							$.messager.defaults = { ok: "确定", cancel: "稍候确认" };
							$.messager.alert("消息", "该订单已经确认，请尽快派送！", "info");
						}
					}, "json");
			            }
			            else {
			                $.post("/order/business/noConfirm_handleLaterOrder.action", {"orderId": orderId},function(result){
						
						var type = result.type;
						var message = result.message;
						if("Success" == type){
							$.messager.defaults = { ok: "确定", cancel: "稍候确认" };
							$.messager.alert("消息", message, "info");
						}
					}, "json");
			            }
        });
					}
				},"json");
			}
});
</script>

</head>

<body class="easyui-layout">
		<div data-options="region:'north',border:false" id="north" >
		<c:set var="loginedStore" value="${sessionScope.loginedStore }"></c:set>
			<div id="systemName">商家管理系统</div>
			<c:if test="${loginedStore.state == 'open' }">
				<div id="close">打烊</div>
				<div id="open">营业中</div>
			</c:if>
			<c:if test="${loginedStore.state == 'close' }">
				<div id="close">打样</div>
				<div id="open">营业</div>
			</c:if>
			<div id="headImg"><img style="height: 65px" src="/order/images/header2.png"/> <div id="busName">${sessionScope.loginedBussinessman.username }</div></div>
			
		</div>  
    	<div data-options="region:'south',border:false" id="south"></div> 
		<div data-options="region:'west',border:false"  style="width: 202px;margin-left: 0px;background-color: #4C5456" id="main_west">
		
		<div id="storeName">${sessionScope.loginedStore.storeName }</div>
				<div id="ulDiv">
					<ul class="module_ul">
						<li>商家信息</li>
						<li>菜品管理</li>
						<li>菜品类别</li>	
						<li>订单管理</li>
						<li>回复评价</li>
						<li>统计报表</li>
					</ul>
				</div>
				
		</div>
		<div data-options="region:'center',border:true" id="main_center">
			<div id="main_tabs" class="easyui-tabs">
				<div title="商家信息">
					<iframe src="/order/business/business_showBusinessInfo.action" allowTransparency="true" style="border:0;width:100%;height:95%;" frameBorder="0"></iframe>
				</div>
			</div>
		</div>
		
		<div id="dd" style="top:200px;"></div>
	
</body>
</html>
