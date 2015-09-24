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
    
    <title>My JSP 'home.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="/order/css/home.css"/>
	
	<jsp:include page="../../inc.jsp"></jsp:include>
	<script type="text/javascript" src="/order/js/home.js"></script>
	<script src="/order/js/Area.js" type="text/javascript"></script>
	<script src="/order/js/AreaData_min.js" type="text/javascript"></script>
  </head>
  
  <body>
  		
  		<jsp:include page="../../header.jsp"></jsp:include>
		<div id="main">
		<c:if test="${!empty sessionScope.loginedUser }">
			<!-- *******菜品推荐区域****** -->
			<div id='oneKey'>
				<div id="oneKeyDishDiv">
				
				</div>
			
				<div id="oneKeyButtonDiv">
					<div id="temOneKey">
					<a href="javascript:void(0)" id="placeButton">一键下单</a>
					<a href="javascript:void(0)" id="changeButton">更换一批</a></div>
				</div>
			</div>
		</c:if>
			
			<div id="foodType">
				
				<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/quanbu.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">全部</dd> 
    			</dl>
				<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/zhongcan.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">中餐</dd> 
    			</dl> 
    			<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/xican.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">西餐</dd> 
    			</dl> 
    			<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/xiaochi.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">小吃</dd> 
    			</dl> 
    			<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/shuiguo.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">水果</dd> 
    			</dl> 
    			
    			<dl class="footDl"> 
        			<dt class="footDt"><img src="/order/images/dianxin.png" style="margin-left: 15px;" width="70px"/></dt> 
        			<dd class="footDd">点心</dd> 
    			</dl> 
			</div>
			
			<div id="sort">
				
				<a href="javascript:void(0)">默认排序</a><a href="javascript:void(0)">销量</a><a href="javascript:void(0)">评价</a><a href="javascript:void(0)">送餐速度</a>
			</div>
			<div id="storeDiv">
				
				
				
			</div>
		</div>
		<!-- 弹出框 -->
		<div id="dd" style="top:200px;">
		</div>  
		<div id="dd2" style="top:220px;">
			<div class="dd2Input"><input type='text' name="name" placeholder="真实姓名"/> </div>
			<div class="dd2Input"><input type='text' name="tel" placeholder="电话号码"/> </div>
			
			<div id="selectDiv">
			<font style="font-size: 16px;">请选择地址:</font><br>
			<select id="seachprov" name="seachprov"
			onChange="changeComplexProvince(this.value, sub_array, 'seachcity', 'seachdistrict');"></select>
			&nbsp;&nbsp;
		<select id="seachcity" name="homecity"
			onChange="changeCity(this.value,'seachdistrict','seachdistrict');"></select>&nbsp;&nbsp;
		<span id="seachdistrict_div"><select id="seachdistrict"
			name="seachdistrict"></select></span></div>
			<div class="dd2Input"><input type='text' name="addressDetail" placeholder="详细地址"/> </div>
			<a href="javascript:void(0)" id="addAddressButton2">确认</a>
		</div>
		<div id="dd3" style="top:180px;">
		</div>  
		
		
		<jsp:include page="../../footer.jsp"></jsp:include>
  </body>
</html>
