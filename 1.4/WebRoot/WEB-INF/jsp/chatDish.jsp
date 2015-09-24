<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'chat.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<jsp:include page="../../inc.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="/order/css/collect.css">
	<script src="/order/js/highcharts/highcharts.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
	$(function(){
		var today = new Date();       
	    var day = today.getDate();       
	    var monthstart = today.getMonth();
	    var monthend = today.getMonth()+1;   
	    var year = today.getFullYear();
	    var yearEnd = year;
	    if(monthend>=12) {
	    	monthend = 1;
	    	yearEnd += 1;
	    }    
	           
	    var datestart = year + "-" + monthstart + "-" + day;
	    var dateend = yearEnd + "-" + monthend + "-" + day;  
		$('#startTime').datebox("setValue",datestart);   
		$('#endTime').datebox("setValue",dateend);
		chatDishesNum();
		
	});
		
		
		
		function chatDishesNum(){
			
			var startTime=$('#startTime').datebox('getValue'); 
	        var endTime=$('#endTime').datebox('getValue');
	        var pieData;
	        if(endTime.length>9 && startTime.length>9){ 
	            var startTime = new Date(startTime); 
	            var endTime = new Date(endTime); 
	            if(Date.parse(startTime) - Date.parse(endTime)>=0){ 
	               alert("结束时间必须晚于开始时间，请重新选择!"); 
	               return false; 
	            } 
			}
		
			$.ajax({
				type : 'post',
				url : '/order/dish/chatDish.action',//请求数据的地址 
				async : true,
				dataType : "json",
				data:{
					beginDate:$("#startTime").datebox("getValue"),
					endDate:$("#endTime").datebox("getValue"),
				},
				success : function(data) {
					if(data.type=="Failure") {
						alert(data.message);
					} else {
					 new Highcharts.Chart(data.chatcolumn);
					 pieData = data.chatpie;
					  $('#containerpie').highcharts({
  		
 	        chart: {
 	            plotBackgroundColor: null,
 	            plotBorderWidth: null,
 	            plotShadow: false
 	        },
 	        title: {
 	            text: '不同菜品销量所占的比重'
 	        },
 	        tooltip: {
 	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
 	        },
 	        credits: {
                 enabled: false
                  } ,
 	     
 	        plotOptions: {
 	            pie: {
 	                allowPointSelect: true,
 	                cursor: 'pointer',
 	                dataLabels: {
 	                    enabled: true,
 	                    color: '#000000',
 	                    connectorColor: '#000000',
 	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
 	                }
 	            }
 	        },
 	        series: [{
 	            type: 'pie',
 	            name: '所占百分比',
 	            data: pieData
 	        }]
 	         
 	    });
					 
					}
				},
				
				
			});}
			
			
			function chatSale(){
			 var pieData;
			var startTime=$('#startTime').datebox('getValue'); 
	        var endTime=$('#endTime').datebox('getValue');
	        if(endTime.length>9 && startTime.length>9){ 
	            var startTime = new Date(startTime); 
	            var endTime = new Date(endTime); 
	            if(Date.parse(startTime) - Date.parse(endTime)>=0){ 
	               alert("结束时间必须晚于开始时间，请重新选择!"); 
	               return false; 
	            } 
			}
      
			$.ajax({
				type : 'post',
				url : '/order/dish/chatSales.action',//请求数据的地址 
				async : true,
				dataType : "json",
				data:{
					beginDate:$("#startTime").datebox("getValue"),
					endDate:$("#endTime").datebox("getValue"),
				},
				success : function(data) {
					if(data.type=="Failure") {
						alert(data.message);
					} else {
					new Highcharts.Chart(data.chatline);
					 pieData = data.chatpie;
					  $('#containerpie').highcharts({
  		
 	        chart: {
 	            plotBackgroundColor: null,
 	            plotBorderWidth: null,
 	            plotShadow: false
 	        },
 	        title: {
 	            text: '不同月份店铺销售额所占比重'
 	        },
 	        tooltip: {
 	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
 	        },
 	        credits: {
                 enabled: false
                  } ,
 	     
 	        plotOptions: {
 	            pie: {
 	                allowPointSelect: true,
 	                cursor: 'pointer',
 	                dataLabels: {
 	                    enabled: true,
 	                    color: '#000000',
 	                    connectorColor: '#000000',
 	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
 	                }
 	            }
 	        },
 	        series: [{
 	            type: 'pie',
 	            name: '所占百分比',
 	            data: pieData
 	        }]
 	         
 	    });
				
					}
					
					
				},
				
			});}
	
  		
	</script>
	<style type="text/css">
	a{
		 font-family: "微软雅黑";
	     font-size: 14px;
	     color: darkslategrey;
	     font-weight: bold;
		text-decoration:none;
	}
	</style>
  </head>
  
 <body> 
  <div style="margin:20px 0;"></div>
	<table>
		<tr>
			<td>Start Date:</td>
			<td>
				<input id="startTime" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
			</td>
			<td>End Date:</td>
			<td>
				<input id="endTime" class="easyui-datebox" data-options="sharedCalendar:'#cc'">
			</td>
			<td>
				<a href="javascript:void(0)" onclick="chatDishesNum();"><img width="20px" height="20px" src="/order/images/search.jpg"/>销量</a>
			</td>
			<td>
				<a href="javascript:void(0)" onclick="chatSale();"><img width="20px" height="20px" src="/order/images/search1.jpg"/>销售额</a>
			</td>
			
		</tr>
	</table>
	<div id="cc" class="easyui-calendar"></div>                 
 <div id="container" style="width:50%;height:400px;float: left"></div>
 <div id="containerpie" style="width:50%;height:400px;float: right"></div>
 
</body> 

</html>
