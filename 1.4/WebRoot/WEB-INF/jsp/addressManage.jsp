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

<title>收货地址</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
<link href="/order/css/public_frame.css" rel="stylesheet">
<script src="/order/js/Area.js" type="text/javascript"></script>
<script src="/order/js/AreaData_min.js" type="text/javascript"></script>

<script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
<script type="text/javascript" src="/order/qnui/js/sui.js"></script>
<script type="text/javascript" src="/order/js/public_frame.js"></script>
<style>
#showAddress {
	border: 1px;
	margin-top: 5px;
	padding-top: 20px;
	padding-left: 50px;
	width: 400px;
	height: 145px;
	border-style: solid;
	border-color: Gainsboro;
	line-height: 30px;
}

#showContainer {
	border: 1px;
	border-style: solid;
	border-color: Gainsboro;
	margin-left: 80px;
	margin-top: 10px;
	width: 500px;
	padding-left: 44px;
	padding-bottom: 10px;
	padding-top: 10px;
	height: auto;
}

.label {
	font-size: 18px;
	line-height: 30px;
	margin: 7px;
}
</style>
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
				<div id="showContainer">
					<button data-toggle="modal" data-target="#myModal"
						data-keyboard="false" class="sui-btn btn-primary btn-lg">添加新地址</button>
						
						<span style="color: red;font-size: 16px;">${msg}</span>
						
					<c:forEach items="${addressList}" var="address" varStatus="status">
						<c:if test="${address.defaultAddress==1}">
							<div id="showAddress" style="background-color: DarkGray">
								<form action="" id="form(${status.index})" method="post">
									<input type="hidden" name="addressId" value="${address.id }">
									<div class="control-group">
										<label class="label">收货人姓名: ${address.shouhuoren }</label>
									</div>
									<div class="control-group">
										<label class="label">收货人手机号:${address.shouhuoTell }</label>
									</div>
									<div class="control-group">
										<label class="label">收货地址：${address.address }</label>
									</div>
									<div class="control-group">
										<label class="label">详细地址：${address.detailAddress }</label>
									</div>
									<div style="font-size: 15px;margin-left: 250px;">
										<span>此为默认地址</span>&nbsp;&nbsp; <a href="javascript:void(0);"
											id="deleteAddress" onclick="del('form(${status.index})')"
											style="color:red;">删除</a>
									</div>

								</form>
							</div>
						</c:if>

						<c:if test="${address.defaultAddress==0}">
							<div id="showAddress">
								<form action="" id="form(${status.index})" method="post">
									<input type="hidden" name="addressId" value="${address.id }">
									<div class="control-group">
										<label class="label">收货人姓名: ${address.shouhuoren }</label>
									</div>
									<div class="control-group">
										<label class="label">收货人手机号:${address.shouhuoTell }</label>
									</div>
									<div class="control-group">
										<label class="label">收货地址：${address.address }</label>
									</div>
									<div class="control-group">
										<label class="label">详细地址：${address.detailAddress }</label>
									</div>
									<div style="font-size: 15px;margin-left: 250px;">
										<a href="javascript:void(0);" id="setDefault"
											onclick="setDef('form(${status.index})')">设为默认地址</a>&nbsp;&nbsp;
										<a href="javascript:void(0);" id="deleteAddress"
											onclick="del('form(${status.index})')" style="color:red;">删除</a>
									</div>
								</form>
							</div>
						</c:if>
					</c:forEach>
				</div>

				<!-- Modal-->
				<div id="myModal" tabindex="-1" role="dialog" data-hasfoot="false"
					class="sui-modal hide fade" style="width: 500px;">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" data-dismiss="modal" aria-hidden="true"
									class="sui-close">×</button>
								<h4 id="myModalLabel" class="modal-title">添加新地址</h4>
							</div>
							<div class="modal-body">
								<form class="sui-form form-horizontal sui-validate"
									action="core/address_addAddress.action" id="addAddress_form"
									method="post">
									<div class="control-group">
										<label class="control-label">收货人姓名: </label>
										<div class="controls">
											<input type="text" name="shouhuoren" data-rules="required">
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">收货人手机号:</label>
										<div class="controls">
											<input type="text" name="shouhuo_tell"
												data-rules="required|mobile">
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">收货地址：</label>
										<div class="controls">
											<input type="hidden" name="address" value="" id="address">
											<select id="seachprov" name="seachprov"
												onChange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>
											&nbsp;&nbsp; <select id="seachcity" name="homecity"
												onChange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
											<span id="seachdistrict_div"><select
												id="seachdistrict" name="seachdistrict"></select></span> 
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">详细地址：</label>
										<div class="controls">
											<input type="text" name="detailAddress" data-rules="required">
										</div>
									</div>
									
									
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
<script>
	$(document).ready(function() {
		$("#add_button").click(function() {
		
		showAreaID();
		$("#addAddress_form").submit();
		});
	});

	function setDef(id) {
		var form = document.getElementById(id);
		form.action = "core/address_setDefaultAddr.action";
		form.submit();
	};
	function del(id) {
		var form = document.getElementById(id);
		form.action = "core/address_deleteAddress.action";
		form.submit();
	};

	$(function() {
		initComplexArea('seachprov', 'seachcity', 'seachdistrict', area_array,
				sub_array, '44', '0', '0');
	});

	//得到地区码
	function getAreaID() {
		var area = 0;
		if ($("#seachdistrict").val() != "0") {
			area = $("#seachdistrict").val();
		} else if ($("#seachcity").val() != "0") {
			area = $("#seachcity").val();
		} else {
			area = $("#seachprov").val();
		}
		return area;
	}

	function showAreaID() {
		//地区码
		var areaID = getAreaID();
		//地区名
		var areaName = getAreaNamebyID(areaID);
		document.getElementById("address").value = areaName;
	}

	//根据地区码查询地区名
	function getAreaNamebyID(areaID) {
		var areaName = "";
		if (areaID.length == 2) {
			areaName = area_array[areaID];
		} else if (areaID.length == 4) {
			var index1 = areaID.substring(0, 2);
			areaName = area_array[index1] + " " + sub_array[index1][areaID];
		} else if (areaID.length == 6) {
			var index1 = areaID.substring(0, 2);
			var index2 = areaID.substring(0, 4);
			areaName = area_array[index1] + " " + sub_array[index1][index2]
					+ " " + sub_arr[index2][areaID];
		}		
		return areaName;
	}
</script>

</html>
