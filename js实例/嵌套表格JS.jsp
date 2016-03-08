<%@ page language="java"
	import="java.util.*,com.sf.fvp.waybill.common.model.*"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<title>logDataQuery</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- 引入 Bootstrap -->
<link href="showtool/css/bootstrap.min.css" rel="stylesheet">

<!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
<!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
<!--[if lt IE 9]>
         <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
         <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
      <![endif]-->
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="logFind.jsp">logFind</a></li>
	</ul>
	<div class="panel">
		<form role="form">
			<div class="form-group">
				<label for="logtype">系统模块</label> <select class="form-control"
					id="logtype" name="type">
						<option value="1">订单</option>
						<option value="2">运单</option>
					<!-- <optgroup label="订单">
						<option value="1">计算日志</option>
						<option value="2">订阅日志</option>
						<option value="3">把枪级联日志</option>
						<option value="4">rcv日志(成功)</option>
						<option value="5">rcv日志(失败)</option>
						<option value="6">rcv日志(webservice)</option>
						<option value="7">分发</option>
						<option value="8">puller日志</option>
						<option value="9">query日志</option>
					</optgroup> -->
					<!-- <optgroup label="运单">
						<option value="10">计算日志</option>
						<option value="11">订阅日志</option>
						<option value="12">把枪级联日志</option>
						<option value="13">rcv日志(成功)</option>
						<option value="14">rcv日志(失败)</option>
						<option value="15">rcv日志(webservice)</option>
						<option value="16">分发</option>
						<option value="17">puller日志</option>
						<option value="18">query日志</option>
					</optgroup> -->
					
				</select>
			</div>
			<div class="form-group" id = "key-input">
				<div class="col-sm-3">
					<input type="text" class="form-control" id="key" name="key"
						placeholder="key" value="">
				</div>
			</div>
			<button type="button" class="btn btn-default">Query</button>
		</form>
	</div>
	<div>
		<table id="contentTable" class="table table-bordered">
			<thead>
				<tr>
					<th width="50">序号</th>
					<th>内容</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
	<script src="showtool/js/jquery.min.js"></script>
	<!-- 包括所有已编译的插件 -->
	<script src="showtool/js/bootstrap.min.js"></script>

	<script type="text/javascript">
	
		function objectTable(obj){
			var array = [];
			if(obj.constructor == Object){
				array[0] = obj;
			}else if(obj.constructor == Array){
				array = obj;
			}else{
				return obj;
			}
			var table = "<table class='table table-bordered'>";
			var row = "<tr>";
			if(array.length == 0){
				return "";
			}
			if(array[0].constructor == String){
				return obj;
			}
			$.each(array[0], function(name, value) {
				var th = "<th>" + name + "</th>";
				row += th;
			});
			row += "</tr>";
			table += row;
			row = "";
			$.each(array,function(index,data){
				row += "<tr>";
				$.each(data, function(name, value) {
					if(value == null){
						value = "";
					}
					if(value.constructor == String && value.indexOf("{")>=0){
						value = $.parseJSON(value);
					}
					if(value.constructor == Object|| value.constructor == Array){
						value = objectTable(value);
					}
					var col = "<td>" + value + "</td>";
					row += col;
				});
				row += "</tr>";
			});
			
			table += row; 
			table += "</table>";
			return table;
		}
		function simpTable(){
			return 
		}
		function convertDate(s){
			var date = new Date(s);
			date.Format("yyyy-MM-dd hh:mm:ss");
		}
		function tableText(datas){
			var text = "";
			$.each(datas, function(index, data) {
				$.each(data, function(name, value) {
					var row = "<tr>";
					var col = "";
					row += "<td>" + name + "</td>";
					if(value != null){
						if(value.constructor == Object || value.constructor == Array){
							col = "<td>" + objectTable(value) + "</td>";
						}else{
							col = "<td>" + value + "</td>";
						}
					}else{
						col = "<td></td>";
					}
					row += col;
					row += "</tr>";
					text += row;
				});
			});
			return text;
		}
		$("button").click(function() {
			var param = {};
			var t = $('form').serializeArray();
			$.each(t, function() {
				param[this.name] = this.value;
			});
			if(!param.key){
				$('#key-input').addClass("has-error");
				return ;
			}else{
				$('#key-input').removeClass("has-error");
			}
			var type = param.type;
			var url = "queryLog/find";
			if(type == 1)
				url = "queryLog/findOrder";
			if(type == 2)
				url = "queryLog/findWaybill";
			$.getJSON(url, param, function(result) {
				if (result.success) {
					/* $(".table thead").empty(); */
					$(".table tbody").empty();
					if(result.datas.length > 0){
						var text = tableText(result.datas);
						$(".table tbody").append(text);
					}else{
						var text = "<tr><td colspan = 2>无数据</td><tr>"
						$(".table tbody").append(text);
					}
					
				}

			});
		});
	</script>
</body>
</html>
