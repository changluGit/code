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
		$("#replyDl").dialog("close");
		$("#dg").datagrid({
			view: detailview,   
			url:"/order/business/orderEvaluate_showEvaluates.action",
			rownumbers:true,
			pagination:true,
			singleSelect:true,
			height: 500,
			pagePosition:'bottom',
			pageSize:10,
			pageList:[5,10,20,50],
			
			columns:[[
				{field:'id',title:'订单评价号',hidden:true}, 
				
       			{field:'orderNum',title:'订单号',width:120},
       			{field:'updateTime',title:'更新时间',width:130,sortable:true},      
       			{field:'userName',title:'用户名',width:70},    
        		{field:'evaluateCategory',title:'评价级别',width:60,sortable:true,
        			formatter: function(value,row,index){
        				if(value=="medium") return "<div style='width:100%;height:100%;text-align:center;color:white;background-color:blue'>中</div>";
        				else if(value=="bad") return "<div style='width:100%;height:100%;text-align:center;color:white;background-color:red'>差</div>";
        				else return "<div style='width:100%;height:100%;text-align:center;color:white;background-color:green'>好</div>";
        			}
        		},
        		{field:'state',title:'状态',width:60,sortable:true,
        			formatter: function(value,row,index){
        				if(value=="未回复") {
        					return "<font style='color:red'>"+value+"</font>";
        				} else {
        					return "<font style='color:green'>"+value+"</font>";
        				}
        			}
        		},
        		{field:'evaluateContent',title:'评价内容',width:300},
        		{field:'reply',title:'回复内容',width:300} 
    		]],
   			toolbar:[{
				iconCls: 'icon-edit',
				text: "回复",
				handler: function(){
					var seleRow = $("#dg").datagrid('getSelected');
					if(seleRow.state=="未回复") {
						$("#replyContent").removeAttr("readonly");
						$("#replyContent").val("");
						$("#replyDl").dialog({
					        title: "回复评价",
					        height: 200,
					        width: 300,
					        modal: true,
					        top:100,
					        buttons: [{
					            id: "reply_btn",
					            text: "保存",
					            handler: function () {
					               $.ajax({
						               	type:"post",
						               	url:"/order/business/orderEvaluate_reply.action",
						               	dataType:"json",
						               	data:{id:seleRow.id,reply:$("#replyContent").val()},
						               	success:function(data) {
						               		if(data.type == "Success") {
						               			$.messager.alert("",data.message);
						               			$("#dg").datagrid("reload");
						               			$("#replyDl").dialog("close");
						               			
						               		} else {
						               			$.messager.alert("",data.message);
						               		}
						               	}, error:function(data) {
						               		$.messager.alert("","error");
						               	}
					               });
					            }
					        },{
					            id: "cancel_btn",
					            text: "取消",
					            handler: function () {
					            	$("#replyDl").dialog("close");
					        	}
					        }],
					        onLoad:function() {
					                
					        },
					        onClose: function () {
					            $("#replyDl").dialog("close");
					        }
						});
					} else {
						$.messager.alert('','您已回复，无法进行此操作');
					}
					
				}
			},'-', {
				iconCls: 'icon-search',
				text: "查看",
				handler: function(){
					var seleRow = $("#dg").datagrid('getSelected');
					$("#replyContent").val(seleRow.reply);
					$("#replyContent").attr("readonly","readonly");
					$("#replyDl").dialog({
				        title: "查看回复",
				        height: 200,
				        width: 300,
				        top:100,
				        modal: true,
				        buttons: [{
				            id: "reply_btn",
				            text: "确定",
				            handler: function () {
				               $("#replyDl").dialog("close");
				            }
				        }],
				        onLoad:function() {
				                
				        },
				        onClose: function () {
				            $("#replyDl").dialog("close");
				        }
					});
				}
			},'-',{
				iconCls: 'icon-reload',
				text: "刷新",
				handler: function(){
					$("#dg").datagrid("reload");
				}
			
			}],
			
    		detailFormatter:function(index,row){   
        		return '<div id="ddv-' + index + '" style="padding:5px 0"></div>';   
    		},   
			onExpandRow: function(index,row){   
		        $('#ddv-'+index).panel({   
		            border:false,   
		            cache:false,   
		            href:'/order/business/orderEvaluate_showDetail.action?id='+row.id,   
		            onLoad:function(){   
		                $('#dg').datagrid('fixDetailRowHeight',index);   
		            }   
		        });   
		        $('#dg').datagrid('fixDetailRowHeight',index);   
    		},   
			onDblClickRow: function(index, row) {
				if(row.state=="未回复") {
					$("#replyContent").removeAttr("readonly");
					$("#replyContent").val("");
					$("#replyDl").dialog({
				        title: "回复评价",
				        height: 200,
				        width: 300,
				        modal: true,
				        top:100,
				        buttons: [{
				            id: "reply_btn",
				            text: "保存",
				            handler: function () {
				               $.ajax({
					               	type:"post",
					               	url:"/order/business/orderEvaluate_reply.action",
					               	dataType:"json",
					               	data:{id:row.id,reply:$("#replyContent").val()},
					               	success:function(data) {
					               		if(data.type == "Success") {
					               			$.messager.alert("",data.message);
					               			$("#dg").datagrid("reload");
					               			$("#replyDl").dialog("close");
					               		} else {
					               			$.messager.alert("",data.message);
					               		}
					               	}, error:function(data) {
					               		$.messager.alert("","error");
					               	}
				               });
				            }
				        },{
				            id: "cancel_btn",
				            text: "取消",
				            handler: function () {
				            	$("#replyDl").dialog("close");
				        	}
				        }],
				        onLoad:function() {
				                
				        },
				        onClose: function () {
				            $("#replyDl").dialog("close");
				        }
					});
				} else {
					$("#replyContent").val(row.reply);
					$("#replyContent").attr("readonly","readonly");
					$("#replyDl").dialog({
				        title: "查看回复",
				        height: 200,
				        width: 300,
				        top:100,
				        modal: true,
				        buttons: [{
				            id: "reply_btn",
				            text: "确定",
				            handler: function () {
				               $("#replyDl").dialog("close");
				            }
				        }],
				        onLoad:function() {
				                
				        },
				        onClose: function () {
				            $("#replyDl").dialog("close");
				        }
					});
				}
				
			}
    			
		});
	});
	function saveReply(id, content) {
		$.ajax({
			type:"POST",
	 		url:"/order/business/orderEvaluate_reply.action",
	 		data:{id:id,content:content},
	 		dataType: "json",
	 		success:function(data){
	 			$.messager.alert("",data.message);
	 		},
	 		error:function() {
	 			$.messager.alert("", "error");
	 		}
		});
	}
	</script>
	
  </head>
  
  <body>
  	<table id="dg"></table>
  	<div id="replyDl" class="easyui-dialog">   
    	<textarea id="replyContent" style="width:280px;height:120px"></textarea>
	</div> 
  </body>
</html>
