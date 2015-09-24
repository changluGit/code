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
    
    <title>My JSP 'orderConfirm.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="/order/css/orderConfirm.css">
	<jsp:include page="../../inc.jsp"></jsp:include>
	<script type="text/javascript" src="/order/js/orderConfirm.js"></script>

  </head>
  
  <body>
    <jsp:include page="../../header.jsp"></jsp:include>
    <div id="main">
    <c:set var="visitedStore" value="${sessionScope.visitedStore }"></c:set>
    <c:set var="dishObjectMap" value="${sessionScope.temOrderDish }"></c:set>
    	<div id="addressNav">
    		||<a href="/order/store/show_gotoStorePage.action?storeId=${visitedStore.id }">${visitedStore.storeName }</a> > <font>确认购买</font>
    	</div>
    	<div id="orderDetailDiv">
    	<!-- ****************订单中菜品的确认******************** -->
    	
    		<div id="orderList">
    		<c:if test="${!empty param.onekey }">
    			<div id="changeOneKey">
    				<a href="javascript:void(0)" id="onekeyChange">菜品不满意，换！</a>
    			</div>
    		</c:if>
    		
    			<table id="listTable">
    				<tr>
    					<td class="dishName">
    						菜品
    					<td>
    					<td class="priceAndPart">
    						价格/份数
    					</td>
    				</tr>
    				
    				<c:forEach var="item" items="${dishObjectMap}"> 
    				
    					<tr>
    					<td class="dishName">
    						${item.key.name}
    					<td>
    					<td class="priceAndPart">
    						￥<c:out value="${item.key.price * item.value }"></c:out>/${item.value }
    					</td>
    					</tr>
					</c:forEach>
    				
    				<tr>
    					<td id="totalFont">
    						合计
    					<td>
    					<td id="totalPrice">
    						￥ ${sessionScope.totalPrice } 
    					</td>
    				</tr>
    			</table>
    		</div>
    		
    		<!-- **********订单信息确认区域*********** -->
    		<div id="deliveryDiv">
    			<div id="del_font">送餐情况</div>
    			<div id="del_address">
    				<div id="addNewAddress">添加新地址</div>
    			</div>
    			
    			<div id="del_message">
    				<font>给商家留言：</font><input type="text" name="message" placeholder="请输入" style="width: 380px;height: 25px; font-size: 16px;"/>
    			</div>
    			
    			<div id="payment_type">
    				<font>付款方式：</font><input type="radio" name = "paymentType" value="off">餐到付款<input type="radio" name="paymentType" value="on">在线支付
    			</div>
    			
    			<div id="payment_total">
    				<div id="pay_total_price">
    					<font id="font1">你需要支付</font><font id="font2">￥ ${sessionScope.totalPrice } 元</font>
    				</div>
    				<div id="pay_tatal_button">
    					<a href="javascript:void(0)">去付款</a>
    				</div>
    			</div>
    		</div>
    	</div>
    </div>
    
    <!-- 新建地址 弹出层-->
    <div id="dd2" style="top:220px;">
			<div class="dd2Input"><input type='text' name="name" placeholder="真实姓名"/> </div>
			<div class="dd2Input"><input type='text' name="tel" placeholder="电话号码"/> </div>
			<div class="dd2Input"><input type='text' name="addressDetail" placeholder="详细地址"/> </div>
			<a href="javascript:void(0)" id="addAddressButton2">确认</a>
		</div>
    
    <jsp:include page="../../footer.jsp"></jsp:include>
  </body>
</html>
