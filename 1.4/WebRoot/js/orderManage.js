

$(document).ready(function(){
	
	var queryDate={};
	
	function transfStateStr(stateStr){
		
		if(stateStr == "全部")
			return -1;
		else if(stateStr == "已下单")
			return 6;
		else if(stateStr == "取消订单中")
			return 5;
		else if(stateStr == "已确认")
			return 4;
		else if(stateStr == "派送中")
			return 3;
		else if(stateStr == "送达")
			return 2;
		else if(stateStr == "已取消")
			return 1;
	}
	
	$(".searchType").on("click", function(){
		
		var stateStr =  $(this).text();
		var stateCode = transfStateStr(stateStr);
		if(stateCode > 0)
			queryDate = {"state":stateCode};
		
		InitGrid(queryDate);
		queryDate = {};
	});
	
	function handleOrderState(operation){
		
		var row = $("#dg").datagrid("getSelected");
		var index = $("#dg").datagrid("getRowIndex", row);
		console.log(operation);
		var newStateCode = "";
		var handleUrl = "/order/business/orderM_handleOrderState.action";
		if(operation == "接单"){
			newStateCode = 4;
			handleUrl = "/order/business/orderM_acceptOrder.action";
		}
		else if(operation == "确认取消"){
			newStateCode = 1;
			handleUrl = "/order/business/orderM_cancelOrder.action";
		}	
		else if(operation == "派单")
			newStateCode = 3;
		else if(operation == "送达"){
			
			newStateCode = 2;
			handleUrl = "/order/business/orderM_completeOrder.action";
		}
			
			
		
		
		
		$.post(handleUrl, {"orderId":row.id, "newStateCode":newStateCode},function(result){
			
			var type = result.type;
			var message = result.message;
			
			if("Success" == type){
				
				$.messager.alert("消息", "后台已经处理数据，点击确认后跟新前台数据！","info", function(){
					
					$('#dg').datagrid('updateRow',{
						index: index,
						row: message
					});
				});
			}
			else if("Failure" == type){
				$.messager.alert("error", message, "warning");
			}
		},"json");
	}
	
	//grid 的初始化
	//实现对DataGird控件的绑定操作
    function InitGrid(queryDate) {
    	$('#dg').datagrid({
            view: detailview,//注意1
            title: '我的取数',
            url:'/order/business/orderM_getDatagridJSON.action',
            fitColumns: true,
            singleSelect:true,
            height: 450,
            width: function () { return document.body.clientWidth * 0.9},
            queryParams:queryDate,
            pagination: true,
            pageNumber:1,
            pageSize:10,
            columns: [
                [
                 {title: 'id', field: 'id', width: 50},
                 { title: '订单编号', field: 'orderNum', width: 80 },
                 { title: '下单用户名', field: 'userName', width: 120 },
                 { title: '下单用户电话', field: 'tel', width: 80 },
                 { title: '下单地址', field: 'address', width: 100 },	
                 { title: '下单时间', field: 'createTime', width: 120 },
                 { title: '订单总额', field: 'totalAmount', width: 100 },
                 { title: '订单状态', field: 'state', width: 100 ,align:'center',
         			styler: function(value,row,index){
        				if (value == '已下单'){
        					return 'background-color:#FF6666;color:white;';
        				}
        				else if(value == "取消订单中"){
        					return 'background-color:#FFCC00;color:white;';
        				}
        				else if(value == "已确认"){
        					return 'background-color:#224B8F;color:white;';
        				}
        				else if(value == "派送中"){
        					return 'background-color:#66FF00;color:blank;';
        				}
        				else if(value == "送达"){
        					return 'background-color:#669999;color:blank;';
        				}
        				else if(value == "已取消"){
        					return 'background-color:#999D9C;color:white;';
        				}
        			}
                 },
                 { title: '用户留言', field: 'userMessage', width: 200 },
                ]
            ],
            toolbar: [{
                id: 'btnAdd',
                text: '接单',
                iconCls: 'icon-add',
                handler: function () {
                	handleOrderState("接单");
                }
            }, '-', {
                id: 'btnEdit',
                text: '确认取消',
                iconCls: 'icon-no',
                handler: function () {
                	handleOrderState("确认取消");
                }
            }, '-', {
                id: 'btnDelete',
                text: '派单',
                iconCls: 'icon-redo',
                handler: function () {
                	handleOrderState("派单");
                }
            }, '-', {
                id: 'btnView',
                text: '送达',
                iconCls: 'icon-ok',
                handler: function () {
                	handleOrderState("送达");
                }
            }, '-', {
                id: 'btnReload',
                text: '刷新',
                iconCls: 'icon-reload',
                handler: function () {
                    //实现刷新栏目中的数据
                    $("#dg").datagrid("reload");
                }
            }],
            detailFormatter:function(index,row){//注意2
                return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
            },
            onExpandRow:function(index,row){//注意3
            	$('#ddv-'+index).datagrid({  
                    url:'/order/business/orderM_getOrderDetail.action?orderId='+row.id,
                    fitColumns:true,  
                    singleSelect:true,  
                    height:'auto',  
                    columns: [
                              [
                               {title: '菜品名称', field: 'name', width: 50},
                               { title: '下单数量', field: 'num', width: 80 },
                               { title: '总价格', field: 'totalPrice', width: 120 },
                      
                              ]
                          ],
                          onResize:function(){  
                              $('#dg').datagrid('fixDetailRowHeight',index);  
                          },  
                          onLoadSuccess:function(){  
                              setTimeout(function(){  
                                  $('#dg').datagrid('fixDetailRowHeight',index);  
                              },0);  
                          }  
            	}); 
                $('#dg').datagrid('fixDetailRowHeight',index);
            }

        });
    }
    
    //初始化表格。
    InitGrid();
});