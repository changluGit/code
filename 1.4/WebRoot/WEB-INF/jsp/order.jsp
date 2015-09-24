<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<jsp:include page="../../inc.jsp"></jsp:include>
	<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
	<link href="/order/css/public_frame.css" rel="stylesheet">
	<link href="/order/css/user_order.css" rel="stylesheet">
	
	<script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
	<script type="text/javascript" src="/order/qnui/js/sui.js"></script>
	
	<script type="text/javascript" src="/order/js/user_order.js"></script>
	<script type="text/javascript" src="/order/js/public_frame.js"></script>
  </head>
  
  <body style="text-align: center;">
  <!-- 头部  -->
  	<jsp:include page="public/header.jsp"></jsp:include>
  	<!-- 主体  -->
  	<div class="mainBox">
		<div class="bodyBox">
			<!-- 侧边栏  -->
  			<jsp:include page="public/sidebar.jsp"></jsp:include>
  			<!-- 内容 -->
  			<div class="contentBox">
  
				<div class="tbody">
					<c:forEach var="order" items="${orders }">
						<div class="orderBody clear">
							<hr class="clear"/>
							<div class="orderImg"><img class="img" src="${order.store.logoAddress }"/></div>
							<div class="orderL">
								<span class="fontBr"><a class="fontBr" href="/order/store/show_gotoStorePage.action?storeId=${order.store.id }" id="${order.id}">${order.store.storeName }</a></span><br /><br />
								<span class="fontG">订单号:${order.orderNum }</span>
							</div> 
							<div class="orderM">
								<span class="fontG">电话:${order.store.telephone }</span><br /><br />
								<span class="fontG">下单时间:${order.createTime }</span>
							</div>
							<div class="orderR">
								<span class="fontG state">状态:
									<c:if test="${order.state==1 }">已取消</c:if>
									<c:if test="${order.state==2 }">送达</c:if>
									<c:if test="${order.state==3 }">派送中</c:if>
									<c:if test="${order.state==4 }">已确认</c:if>
									<c:if test="${order.state==5 }">取消订单中</c:if>
									<c:if test="${order.state==6 }">已下单</c:if>
								</span>
								<br /><br />
								<c:if test="${order.state==6 ||  order.state==4 }">
									<a name="${order.id }" class="btn1" href="javascript:void(0)"
									 onClick="cancelOrder(this,${order.id})">取消订单</a>
								</c:if>
							</div>
							
						</div>
						<!-- 点击订单后显示的详细信息 -->
					<div class="detailBody clear">
						<div class="detailL">
							<div class="detailL1">
							<c:set var="sumPrice" value="0"></c:set>
							<c:set var="sumNum" value="0"></c:set>
							<c:forEach var="ordreDishes" items="${order.orderDisheses }">
								<c:set var="sumPrice" value="${sumPrice+ordreDishes.dishes.price*ordreDishes.num}"></c:set>
								<c:set var="sumNum" value="${sumNum+ordreDishes.num}"></c:set>
							</c:forEach>
							<span class="fontB2">菜品共<span class="fontR">${sumNum }</span>份，总计<span class="fontR">${sumPrice }</span>元</span>
							</div>
							<div class="detailL2">
							<c:forEach var="ordreDishes" items="${order.orderDisheses }">
								<span class="fontG left">${ordreDishes.dishes.name }:</span>
								<span class="fontG right"> ￥${ordreDishes.dishes.price }  *  
								${ordreDishes.num }</span><br/><br/>
							</c:forEach>
							</div>
							<div class="detailL3">
							<div class="fontG2">地址：&nbsp;&nbsp;${order.store.businessAddress }</div>
							<div class="fontG2">姓名：&nbsp;&nbsp;${order.store.chargePerson }</div>
							<div class="fontG2">电话：&nbsp;&nbsp;${order.store.telephone }</div>
							<div class="fontG2">备注：&nbsp;&nbsp;${order.store.merchantsAnnouncement }</div>
							</div>
							<div class="detailL4">
							<br/></div>
						</div>
						<!-- 首次提交评价 -->
						<c:if test="${empty order.orderEvaluates && order.state==2 }">
							<div class="detailR">
								
								<form>
									<span class="fontB">评价</span><br /><br/>
									<input type="hidden" name="orderId" value="${order.id }" />
									<input type="radio" name="category" value="good" /> 好
									<input type="radio" name="category" value="medium" /> 中
									<input type="radio" name="category" value="bad" /> 差<br/><br/>
									<textarea rows="10" cols="50">${orderEvaluate.evaluateContent}</textarea>
									<br />
									<input type="submit" class="sui-btn btn-primary btn-large" style="margin-top: 5px;" 
											value="提交" name="bt"/>
								</form>
							</div>
						</c:if>
						<!-- 显示已回复评价 -->
						<c:if test="${order.state==2 }">
						<c:forEach var="orderEvaluate" items="${order.orderEvaluates }">
						<div class="detailR">
							<font style="font-size:15;color:grey">我的评价：<c:if test="${orderEvaluate.evaluateCategory=='good' }"><font style="color:green">好</font></c:if>
							<c:if test="${orderEvaluate.evaluateCategory=='medium' }"><font style="color:blue">中</font></c:if>
							<c:if test="${orderEvaluate.evaluateCategory=='bad' }"><font style="color:red">差</font></c:if></font>
							<br />
							<textarea readonly="readonly" rows="7" cols="50">${orderEvaluate.evaluateContent}</textarea>
							<br /><br/>
							<font style="font-size:15;color:grey">商家回复：</font><br/>
							<textarea readonly="readonly" rows="7" cols="50">${orderEvaluate.businessmanReply}</textarea>
						</div>
						</c:forEach>
						</c:if>
						<c:if test="${order.state!=2 }">
							<div class="detailR">
								<font style="font-size:15;color:orange">&nbsp;&nbsp;快餐未送达，暂时不能评价</font>
							</div>
						</c:if>
					</div>
					</c:forEach>
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
