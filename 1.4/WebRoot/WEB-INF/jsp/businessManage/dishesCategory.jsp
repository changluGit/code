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
	
	<jsp:include page="../../../inc.jsp"></jsp:include>
	<script type="text/javascript" src="http://www.jeasyui.com/easyui/datagrid-detailview.js"></script>
	<style>
	
	.dishes{
		margin:5px 40px 5px 0px;
		font-family: "微软雅黑";
		font-size: 16px;
		color: brown;
		float:left;
	}
	.dishesBox{
		width:500px;
	}
	</style>
	<script>
	$(function(){
		$("#addDl").dialog("close");
		$("#editDl").dialog("close");
		$("#dg").datagrid({
			url:"/order/business/dishesCategory_showDishesCategories.action",
			rownumbers:true,
			pagination:true,
			singleSelect:true,
			pagePosition:'bottom',
			pageSize:10,
			pageList:[5,10,20,50],
			height:500,
			columns:[[
				{field:'id',title:'订单评价号',hidden:true}, 
				{field:'name',title:'名字',width:120}
    		]],
   			toolbar:[{
				iconCls: 'icon-add',
				text:"添加",
				handler: function(){
					$("#addDl").dialog({
				        title: "添加",
				        height: 100,
				        width: 200,
				        modal: true,
				        buttons: [{
				            id: "add_btn",
				            text: "确定",
				            handler: function () {
				            	if($("#addName").validatebox("isValid")) {
									$.ajax({
										type:"POST",
								 		url:"/order/business/dishesCategory_addCategory.action",
								 		data:{name:$("#addName").val()},
								 		dataType: "json",
								 		success:function(data){
								 			if(data.type=="Success") {
								 				$.messager.alert("",data.message);
								 				$("#dg").datagrid("reload");
								 				$("#addDl").dialog("close");
								 			}  else {
								 				$.messager.alert("",data.message);
								 			}
								 		},
								 		error:function() {
								 			$.messager.alert("","error");
								 		}
									});
								} else {
									$.messager.alert("","类别名不能为空");
								}
							},onClose: function () {
				            	$("#addDl").dialog("close");
				        	}
						}]
					})
				}		
			},'-',{
				iconCls: 'icon-edit',
				text:"编辑",
				handler: function(){
					var seleRow = $("#dg").datagrid('getSelected');
					$("#editName").val(seleRow.name);
					$("#editDl").dialog({
				        title: "编辑",
				        height: 100,
				        width: 200,
				        top:150,
				        modal: true,
				        buttons: [{
				            id: "edit_btn",
				            text: "确定",
				            handler: function () {
				            	if($("#editName").validatebox("isValid")) {
									$.ajax({
										type:"POST",
								 		url:"/order/business/dishesCategory_editCategory.action",
								 		data:{id:seleRow.id,name:$("#editName").val()},
								 		dataType: "json",
								 		success:function(data){
								 			if(data.type=="Success") {
								 				$.messager.alert("",data.message);
								 				$("#dg").datagrid("reload");
								 				$("#editDl").dialog("close");
								 			}  else {
								 				$.messager.alert("",data.message);
								 			}
								 		},
								 		error:function() {
								 			$.messager.alert("","error");
								 		}
									});
								} else {
									$.messager.alert("","类别名不能为空");
								}
							},onClose: function () {
				            	$("#editDl").dialog("close");
				        	}
						}]
					})
				}
			},'-',{
				iconCls: 'icon-remove',
				text:"删除",
				handler: function(){
					var seleRow = $("#dg").datagrid('getSelected');
					$.ajax({
						type:"POST",
				 		url:"/order/business/dishesCategory_removeCategory.action",
				 		data:{id:seleRow.id},
				 		dataType: "json",
				 		success:function(data){
				 			if(data.type=="Success") {
				 				$.messager.alert("",data.message);
				 				$("#dg").datagrid("reload");
				 			}  else {
				 				$.messager.alert("",data.message);
				 			}
				 		},
				 		error:function() {
				 			$.messager.alert("","error");
				 		}
					});
				}
			},'-',{
				iconCls: 'icon-reload',
				text:"刷新",
				handler: function(){
					$("#dg").datagrid("reload");
				}
			
			}]
			
    			
		});
	});
	</script>
	
  </head>
  
  <body>
  	<table id="dg"></table>
  	<div id="addDl" class="easyui-dialog" title="添加" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true">   
    	<input id="addName" style="width:100%" class="easyui-validatebox" data-options="required:true" type="text"></input>
	</div>
	<div id="editDl" class="easyui-dialog" title="编辑" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true">   
    	<input id="editName" style="width:100%" type="text" class="easyui-validatebox" data-options="required:true" ></input>
	</div> 
  </body>
</html>
