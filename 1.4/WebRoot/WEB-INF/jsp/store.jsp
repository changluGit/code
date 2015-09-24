<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'store.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="/order/css/store.css">
	<jsp:include page="../../inc.jsp"></jsp:include>
	<script type="text/javascript" src="/order/js/store.js"></script>

  </head>
  
  <body>
    <jsp:include page="../../header.jsp"></jsp:include>
    <div id="main">
    
    	<div id="storeInfo">
    	<c:set var="visitedStore" value="${sessionScope.visitedStore }"></c:set>
    		<div id="storeLogo"><img style="width: 140px;height: 140px;" src="${visitedStore.logoAddress }"></img></div>
    		<div id="storeBrief">
    			<span id="storeName">${visitedStore.storeName  }<a href="javascript:void(0)" >${requestScope.change }</a></span><br>
    			<span id="rating">评分：${visitedStore.rating }</span><br>
    			<span id="address">地址：${visitedStore.businessAddress }</span>
    		</div>
    		<div id="storeRating">
    	
    			<div class="test"><font class="importance">${visitedStore.rating }</font>分<br>商家评分</div>
    		</div>
    		<div id="foodDeliveryTime">
    			<div class="test"><font class="importance">${visitedStore.foodDeliveryTime }</font>分钟<br>送餐速度</div>
    		</div>
    		<div id="saleVolume">
    			<div class="test"><font class="importance">${visitedStore.saleVolume }</font>份<br>商家月销量</div>
    		</div>
   		</div>
   		
   		<div id="bottomDiv">
   			<div id="dishesDiv">
   				<div id="dishesMenu">
   					<div id="dish"><a href="javascript:void(0)">菜品</a></div>
   					<div id="pinjia"><a href="javascript:void(0)">评价</a></div>
   				</div>
   				<!--菜品种类 -->
   				<div id="dishType">
   					
   				</div>
   				<!-- 菜品展示界面 -->
   				<div id="context">
   				
   				</div>
   				
   			</div>
   			<div id="announcement">
   				<div id="title">订餐必读&商家公告</div>
   				<div id="annoContext">
   					${visitedStore. merchantsAnnouncement}
   				</div>
   			</div>
   			
   		</div>
    </div>
    <div id="shoppingCar">
			
			<div id="carContain">
				
				<div id="carTable">
					
					<table id="shoppingTable">
						
						<tr id="th">
							
							<td>菜品[<a href="javascript:void(0)" id="clearShoppingCar">清空</a>]</td>
							<td>份数</td>
							<td>价格</td>
						</tr>
					</table>
				</div>
				<div id="total">
					共<font id="totalPart">0</font>份，总共<font id="totalPrice">0 元</font>
				</div>
				<div id="carFooter">
					<a href="javascript:void(0)" id="commitButton">提交订单</a>
				</div>
			</div>
		</div>
	<!-- easyui 弹出提示层 -->
	
	<div id="win" style="font-size: 26px; text-align: center;vertical-align: middle;">
	</div>
		
    <jsp:include page="../../footer.jsp"></jsp:include>
  </body>
</html>
