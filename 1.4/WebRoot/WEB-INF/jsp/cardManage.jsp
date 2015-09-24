<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
<link href="/order/css/public_frame.css" rel="stylesheet">
<script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
<script type="text/javascript" src="/order/qnui/js/sui.js"></script>
<script type="text/javascript" src="/order/js/public_frame.js"></script>
<style type="text/css">
#showContainer {
	border: 1px;
	border-style: solid;
	border-color: Gainsboro;
	margin-left: 10px;
	margin-top: 10px;
	width: 700px;
	padding-left: 30px;
	padding-bottom: 10px;
	padding-top: 10px;
	padding-right: 30px;
	height: auto;
}
</style>
</head>
<body>
	

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
		<div id="balance">
			<span style="font-size: 17px;">您当前的储值卡余额为：<span
				style="color: red;">￥${petCard.balance}</span>
			</span> <a href="javascript:void(0);" data-toggle="modal"
				data-target="#myModal" data-keyboard="false"
				class="sui-btn btn-large btn-primary">充值</a>
			<c:if test="${rechargeSum != null}">
				<span style="font-size: 17px;margin-left: 20px;color: #37a7ec">成功为储值卡充值<span
					style="color: red;">${rechargeSum}</span>元
				</span>
			</c:if>
		</div>
		<hr>
		<div id="balanceDetail">
			<span style="font-size: 15px;color: #37a7ec">储值卡信息 </span>
			<table class="sui-table table-bordered-simple">
				<thead>
					<tr>
						<th>卡号</th>
						<th>开卡时间</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${petCard.cardNumber }</td>
						<td>${petCard.createCardTime }</td>
						<c:if test="${petCard.state == 1 }">
							<td style="color: red;">被冻结</td>
						</c:if>
						<c:if test="${petCard.state == 0 }">
							<td style="color: Aqua;">正常使用</td>
						</c:if>
					</tr>
				</tbody>
			</table>

			<span style="font-size: 15px;color: #ff7300">明细表</span>
			<table class="sui-table table-zebra">
				<thead style="background-color: #37a7ec;color:white;">
					<tr>
						<th>时间</th>
						<th>变动金额</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${consumptionList }" var="consumption">
						<tr>
							<td>${consumption.occurrenceTime }</td>
							<c:if test="${consumption.consumptionAmount<0}">
								<td style="color: red;">${consumption.consumptionAmount }</td>
							</c:if>
							<c:if test="${consumption.consumptionAmount>0}">
								<td style="color: Aqua;">+${consumption.consumptionAmount }</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

			
			
				<!-- Modal-->
				<div id="myModal" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">充值</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="core/petcard_recharge.action" id="petCard_form"
									method="post">
									<div class="control-group">
										<label class="control-label" style="font-size: 18px;">充值金额: </label>
										<div class="controls">
											<input type="text" name="rechargeSum" style="height: 26px;" data-rules="required|digits" maxlength="9">
										</div>
									</div>
									<label style="font-size: 13px;color: red;margin-left: 20px;">充值金额限为1元的整数倍</label>
								</form>
							</div>
							<div class="modal-footer">
								<button type="button" class="sui-btn btn-primary btn-large"
									id="add_button">确定</button>
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
<script type="text/javascript">
	$(document).ready(function() {
		$("#add_button").click(function() {
			$("#petCard_form").submit();
		});
	});
</script>

</html>
