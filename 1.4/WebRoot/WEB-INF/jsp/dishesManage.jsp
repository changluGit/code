<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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

<title>菜品管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="/inc.jsp"></jsp:include>
<script type="text/javascript">
	var queryData;
	
	var url;
	
	//添加菜品
	function saveDishes() {
		
		if(!$("#fm").form('validate'))
		{
			$.messager.alert('错误', "有<font color='red'>必填项未填或者数据格式不对</font><br>请返回修改！！！", 'warning');
			return;
		}
		
		$('#fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(json) {
				var json = eval('(' + json + ')');
				var type = json.type;
				var message = json.message;
				if (type == "Failure") {
					$.messager.alert('错误', message, 'warning');
				} else {
					$.messager.alert('成功', message, 'info', function() {

						$('#dlg').dialog('close'); // close the dialog
						$('#dg').datagrid('reload'); // reload the user data
					});

				}
			}
		});
	}
	
	$(document).ready(function(){
		
		//添加菜品
		function newDishes() {
			$('#dlg').dialog('open').dialog('setTitle', '菜品管理');
			$('#fm').form('clear');
			
			/*
				=============要修改的地方
			*/
			url = '/order/core/dishes_addDishes.action';
			$('#company').combobox({    
    		url:"/order/core/category_getAllDishesCategory.action",    
    		valueField:'name',    
    		textField:'name',
    		editable:false,
    		onShowPanel: function(res) {
    			$("#company").combobox("reload","/order/core/category_getAllDishesCategory.action");
    		}   
		});  
			
		}
		//修改菜品
		function editDishes() {
			var number = $('#dg').datagrid('getChecked').length;
	
			if(number>1)
			{
				$.messager.alert('注意', "请只选择一行数据", 'info');
				return;
			}
			var row = $('#dg').datagrid('getSelected');
			
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '修改菜品信息');
				
				/*
					=============要修改的地方
				*/
				var obj = {
						"name":row.name,
            			"price":row.price,
            			"taste":row.taste,
            			"detail":row.detail,
            			"dishesCategory":row.dishesCategoryName
				};
				$('#fm').form('load', obj);
				
				/*
					=============要修改的地方，网址
				*/
				url = '/order/core/dishes_updateDishes.action?id=' + row.id;
				
				$('#company').combobox({    
//     		url:"/order/core/category_getAllDishesCategory.action",    
//     		valueField:'name',    
//     		textField:'name',
    		editable:false,
    		onSelect: function(res) {
    			$("#company").combobox("reload","/order/core/category_getAllDishesCategory.action");
    		}
    		});
			} else {
				$.messager.alert('注意', "请选择一行数据", 'info');
			}
		}
		
		//删除
		function destroyDishes() {
			var rows = $('#dg').datagrid('getChecked');
			if(rows.length == 0)
			{
				$.messager.alert('注意', "请选择一行数据", 'info');
				return;
			}
			$.messager.confirm('确认', '你确定要删除选定的所有菜品吗？', function(r) {
				if(r)
				{
					var ids = "";
					for(var i = 0;i < rows.length; i++)
					{
						ids = ids + rows[i].id + ",";
					}
					var result = ids.substring(0, ids.length - 1);
					
					/*
						============要修改的地方
					*/
					$.post('/order/core/dishes_deleteDishes.action', {
						"ids" : result
					}, function(result) {
						if (result.type == "Success") {
							$.messager.alert('成功', result.message, 'info',
									function() {
									$('#dg').datagrid('reload');  // reload the user data
							     	 $('#dg').datagrid('clearSelections');//清除选择，解决删除后在编辑的bug
									});
							} else {
									$.messager.alert('错误', result.message, 'warning');
							}
						}, 'json');		
				}
				});
			
		}
		
		
		//修改菜品状态为在售
		function onSell() {
			var rows = $('#dg').datagrid('getChecked');
			if(rows.length == 0)
			{
				$.messager.alert('注意', "请选择一行数据", 'info');
				return;
			}
			$.messager.confirm('确认', '你确定要更改选定的所有菜品的状态吗？', function(r) {
				if(r)
				{
					var ids = "";
					for(var i = 0;i < rows.length; i++)
					{
						ids = ids + rows[i].id + ",";
					}
					var result = ids.substring(0, ids.length - 1);
					var status = "在售";
					/*
						============要修改的地方
					*/
					$.post('/order/core/dishes_upDateStatus.action', {
						"ids" : result,"status": status,
					}, function(result) {
						if (result.type == "Success") {
							$.messager.alert('成功', result.message, 'info',
									function() {
									$('#dg').datagrid('reload');  // reload the user data
							     	 $('#dg').datagrid('clearSelections');//清除选择，解决删除后在编辑的bug
									});
							} else {
									$.messager.alert('错误', result.message, 'warning');
							}
						}, 'json');		
				}
				});
			
		}
		
		
		//修改菜品状态为售罄
		function soldOut() {
			var rows = $('#dg').datagrid('getChecked');
			if(rows.length == 0)
			{
				$.messager.alert('注意', "请选择一行数据", 'info');
				return;
			}
			$.messager.confirm('确认', '你确定要更改选定的所有菜品的状态吗？', function(r) {
				if(r)
				{
					var ids = "";
					for(var i = 0;i < rows.length; i++)
					{
						ids = ids + rows[i].id + ",";
					}
					var result = ids.substring(0, ids.length - 1);
					var status = "售罄";
					/*
						============要修改的地方
					*/
					$.post('/order/core/dishes_upDateStatus.action', {
						"ids" : result,"status": status,
					}, function(result) {
						if (result.type == "Success") {
							$.messager.alert('成功', result.message, 'info',
									function() {
									$('#dg').datagrid('reload');  // reload the user data
							     	 $('#dg').datagrid('clearSelections');//清除选择，解决删除后在编辑的bug
									});
							} else {
									$.messager.alert('错误', result.message, 'warning');
							}
						}, 'json');		
				}
				});
			
		}

		//查询按钮事件的绑定
		$("#search_commit").on("click", function(){
			var name = $("#name").val();
			var status = $("#status").combobox('getValue');
			//alert(name + status);
			queryDate = {"name":name,"status":status};
			InitGrid(queryDate);
			/*
				==============要修改的地方
			*/
// 			$.post("/order/core/dishes_getDishesByNameOrStatus.action", $("#search_form").serialize(), function(result) {
				
// 					$('#dg').datagrid('loadData', result);
					
// 							没有得到查询数据，清空datagrid中原有数据
// 							$('#dg').datagrid('loadData', { total: 0, rows: [] });
// 							$.messager.alert('错误', result.message, 'warning');
					
// 				}, 'json');	
		});
		
		//查询重置按钮
		$("#search_reset").on("click", function(){
			
			/*
				=============要修改的地方
			*/
			$("input[name='name']").val(""); 
			$("#status").combobox("setValue", ""); 
		});
		
		//grid 的初始化
		//实现对DataGird控件的绑定操作
        function InitGrid(queryDate) {
            $('#dg').datagrid({   //定位到Table标签，Table标签的ID是grid
            	
            	/*
            		=======要修改的地方，提交网址
            	*/
                url: '/order/core/dishes_getDishesByNameOrStatus.action',   //指向后台的Action来获取当前菜单的信息的Json格式的数据
                title: '菜品信息',
                iconCls: 'icon-view',
                height: 450,
                width: function () { return document.body.clientWidth * 0.9},
                nowrap: true,
                checkOnSelect:true,
                selectOnCheck:true,
                autoRowHeight: true,
                striped: true,//是否显示斑马线
                collapsible: true,
                pagination: true,
                pageSize: 5,
                pageList: [5,15,25],
                rownumbers: true,
                //sortName: 'ID',    //根据某个字段给easyUI排序
                sortOrder: 'asc',
                remoteSort: false,
                idField: 'id',
                queryParams: queryDate,  //异步查询的参数
                columns: [[
                     	/*
                     		==============要修改的地方
                     	*/
                   	 { field: 'ck', checkbox: true },   //选择
                     { title: 'id', field: 'id', width: 50},
                     { title: '图片', field: 'pictureAddress', width: 100,height:100,
                     formatter:function(value, row, rec){
                       return "<img src='"+row.pictureAddress+"' width='100px' height='100px'/>";
                     }},
                     { title: '菜品名', field: 'name', width: 80 },
                     { title: '价格', field: 'price', width: 80 },
                     { title: '月售量', field: 'monthSales', width: 80 },	
                     { title: '好评数', field: 'goodNum', width: 80 },
                     { title: '状态', field: 'status', width: 80 },
                     { title: '详细介绍', field: 'detail', width: 250 },
                     { title: '口味', field: 'taste', width: 80 },
                     { title: '菜品类别', field: 'dishesCategoryName', width: 80,},

                ]],
                toolbar: [{
                    id: 'btnOnSell',
                    text: '在售',
                    iconCls: 'icon-ok',
                    handler: function () {
                    	onSell();//更改菜品为在售状态
                    }
                 }, '-',{
                    id: 'btnSoldOut',
                    text: '售罄',
                    iconCls: 'icon-no',
                    handler: function () {
                    	soldOut();//更改菜品为售罄状态
                    }
                }, '-',{
                    id: 'btnAdd',
                    text: '添加',
                    iconCls: 'icon-add',
                    handler: function () {
                    	newDishes();//实现添加记录的页面
                    }
                }, '-', {
                    id: 'btnEdit',
                    text: '修改',
                    iconCls: 'icon-edit',
                    handler: function () {
                    	editDishes();//实现修改记录的方法
                    }
                }, '-', {
                    id: 'btnDelete',
                    text: '删除',
                    iconCls: 'icon-remove',
                    handler: function () {
                    	destroyDishes();//实现直接删除数据的方法
                    }
                }, '-', {
                    id: 'btnReload',
                    text: '刷新',
                    iconCls: 'icon-reload',
                    handler: function () {
                        //实现刷新栏目中的数据
                        $("#dg").datagrid("reload",{type:'1'});
                    }
                }],
                onDblClickRow: function (rowIndex, rowData) {
                    $('#grid').datagrid('uncheckAll');
                    $('#grid').datagrid('checkRow', rowIndex);
                    editDishes();
                }
            });
        };
        
        InitGrid();
	});
	
</script>
</head>

<body>
	<!-- =================================查询区域============================= -->
	<div id="search_div">
		<form id="search_form" method="post">
			<table>
				<tr>
					<td><label for="name">菜品名:</label> <input type="text"
						name="name" id="name"></td>
					<td><label for="sex">状态:</label> <select id="status"
						class="easyui-combobox" name="status" style="width: 155px"
						data-options="panelHeight:100">
							<option value="" id="def">——</option>
							<option value="在售">在售</option>
							<option value="售罄">售罄</option>
					</select></td>
					<td><a id="search_commit" href="javascript:void(0)">查询</a></td>
					<td><a id="search_reset" href="javascript:void(0)">重置</a></td>
				</tr>
				<tr>				
				</tr>
			</table>
		</form>
	</div>

	<!-- ======================用户数据显示的datagrid================= -->

	<table id="dg" style="width: 1024px" title="菜品管理" iconcls="icon-view">
	</table>
	<!-- ==================添加，更新弹出框=========================== -->
	<div id="dlg" class="easyui-dialog"
		style="width:630px;height:330px;top:200px;padding:10px 20px"
		closed="true" buttons="#dlg-buttons">
		<div class="ftitle">菜品信息</div>
		<form id="fm" method="post" novalidate enctype="multipart/form-data">
			<div class="fitem">
				<label>菜品名：</label> <input name="name" class="easyui-textbox"
					required="true"> <label>价格：</label>
					 <input name="price" class="easyui-textbox"
					required="true"> 
			</div>
			<div class="fitem">
				<label>菜品类别：</label> <select id="company" class="easyui-combobox"
					name="dishesCategory" style="width:160px" 
					data-options="url:'/order/core/category_getAllDishesCategory.action',required:true,textField:'name',valueField:'name'"></select>
				<label>口味：</label> <input name="taste"
					class="easyui-textbox"> 
			</div>
			<div class="fitem">
				<label>详细介绍：</label> <input
					name="detail" class="easyui-textbox">

				<label>图片:</label> <s:file name="file"></s:file>
			</div>
		</form>
	</div>
	<!-- ==========================add和修改弹出框的保存和取消按钮============================= -->
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveDishes()" style="width:90px">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')"
			style="width:90px">取消</a>
	</div>

	<!-- ========================== css样式表 ============================= -->

	<style type="text/css">
#search_div {
	margin-top: 10px;
	margin-bottom: 20px;
}

#search_commit,#search_reset {
	margin-left: 15px;
	display: inline-block;
	height: 30px;
	width: 130px;
	font-size: 16px;
	line-height: 30px;
	text-align: center;
	outline: medium none;
	cursor: pointer;
	background-color: #32A5E7;
	color: #FFF;
	text-decoration: none;
}

#search_commit:HOVER {
	background: #2b8cc5
}

#search_reset:HOVER {
	background: #2b8cc5
}

#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}

.fitem input {
	width: 160px;
}
</style>
</body>
</html>

