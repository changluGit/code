<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<title>乐丰外卖</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<jsp:include page="inc.jsp"></jsp:include>
<script src="/order/js/Area.js" type="text/javascript"></script>
<script src="/order/js/AreaData_min.js" type="text/javascript"></script>


<link rel="stylesheet" type="text/css" href="/order/css/unRegisterIndex.css">

<script type="text/javascript">
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
		alert("您选择的地区码：" + areaID + "      地区名：" + areaName);
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
$(function(){
	$("#center_gotoHome").on("click", function(){
	
		console.log("zhang");
		$.post("/order/core/goto_noLoginAddressHandle.action", {"addressDefault":getAreaNamebyID(getAreaID())}, function(){
		
		
			location.href = "/order/core/goto_home.action";
		});
	});
});
	
	
	
</script>


</head>

<body>
	<div id='top'>
		<div id='top_center'>
			<div id='top_logo'>六六外卖</div>
			<div id="top_login"><a href="/order/login.jsp">登录</a> | <a href="/order/register.jsp">注册</a></div>
		</div>
	</div>

	<div id="center">
		<div id='center_font'><font>地址</font></div>
		<div id='center_selectAddress'><select id="seachprov"
			name="seachprov"
			onChange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>
		&nbsp;&nbsp; <select id="seachcity" name="homecity"
			onChange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
		<span id="seachdistrict_div"><select id="seachdistrict"
			name="seachdistrict"></select></span></div>
		<div id='center_gotoHome'>立即选餐</div>
		
	</div>
	
	<div id="footer">
	   <span>©2015 乐丰网  京ICP证070791号 京公网安备11010502025545号  </span>
	</div>
</body>
</html>
