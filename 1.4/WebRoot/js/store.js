/**
 * 
 * 828477 
 * 用于处理 商店页面的 菜品展示
 */

$(document).ready(function(){
	
	var category = "";
	var sort = "monthSales";
	
	//处理弹出层的效果
	function dialogHandle(){
		
		
		$('#win').dialog({  
			title:"消息提示",
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closable:true,
			closed:true,
		    width:400,   
		    height:200,   
		    modal:true  
		}); 
	}
	
	//处理店铺收藏
	var collectStoreButton = $("#storeName a");
	$(collectStoreButton).on("click", function(){
		
		$.post("/order/store/show_toggleCollectStore.action", function(result){
			
			var type = result.type;
			var message = result.message;
			
			if(type == "Success"){
				
				collectStoreButton.text("[" + message+"]");
			}
			else if(type = "Failure"){
				
				alert(message);
			}
			
		}, "json");
	});
	
	
	//处理搜索菜品
	$("#searchButton").on("click", function(){
		
		//拿到输入内容
		var inputContext =  $(this).prev("input").val();
		
		if(inputContext == "" || inputContext == undefined)
			return;
		
		$.post("/order/store/dish_search.action", {"inputContext": inputContext},function(result){
			
			var type = result.type;
			var message = result.message;
			
			if(type == "Success"){
				
				$("#context").html(message);
				
				
			}
			else if(type = "Failure"){
				
				$("#context").html(message);
			}
			
			dishDivHandle();
			
		}, "json");
	});
	
	//商品类别展示 处理
	$.ajax({
		   type: "POST",
		   url: "/order/store/dish_getDishCategory.action",
		   dataType: "json",
		   async: false,
		   success: function(result){
				
				var type = result.type;
				var message = result.message;
				
				 if("Success" == type)
		            {
					 	$("#dishType").html(message);
		            }
		            else if("Failure" == type){
		                
		            	$("#dishType").html(message);
		            }
				 
			}
		});
	
	
	//加载菜单的处理
	function showDishes(category, sort){
		var result="";
		if("" == category ||  undefined == category)
			category = "";
		result += "category="+ category+"&";
		if("" == sort || undefined == sort)
			sort = "";
		result += "sort=" +sort; 
		
		$.ajax({
			   type: "POST",
			   url: "/order/store/dish_showDishByCategory.action",
			   dataType: "json",
			   data: result,
			   async: false,
			   success: function(result){
					
					var type = result.type;
					var message = result.message;
					
					 if("Success" == type)
			            {
						 	$("#context").html(message);
			            }
			            else if("Failure" == type){
			                
			            	$("#context").html(message);
			            }
					 //绑定点击事件
					 dishDivHandle();
					 
				}
			});
		
	}
	
	
	//菜单显示区域中，点击事件的绑定
	function dishDivHandle(){
		
		
		//绑定 排序按钮事件
		 $("#sortCondition a").on("click", function(){
			 
			
			 sort = $(this).text();
			
			 
			 if(sort == "默认排序"){
				 sort = "monthSales";
			 }
			 else if(sort == "价格")
				 sort = "price";
			 else if(sort == "销量")
				 sort = "monthSales";
			 else if(sort == "好评数")
				 sort = "goodNum";
			 
			 showDishes(category, sort);
		 });
		 
		 //绑定 购买和收藏按钮事件
		 $(".buyButton").on("click", function(){
			 
			 //拿到菜品的id
			 var dishId = $(this).prev("input").val();
			 
			 var dishFont = $(this).parent().prev(".dishFont");
			 //拿到菜品名称
			 var dishName = dishFont.children(".dishName").text();
			 var disPrice = dishFont.children(".rmb").children(".price").text();
			 
			 addShopCar(dishName, disPrice, dishId);
			 
		 });
		 
		//处理菜品收藏
			$(".collectButton").on("click", function(){
				
				var dishButton = $(this);
				//首先拿到菜品id
				var dishId = $(this).prevAll("input").val();
				$.post("/order/store/show_toggleCollectDish.action",{"dishId":dishId}, function(result){
					
					var type = result.type;
					var message = result.message;
					
					
					
					if(type == "Success"){
						
						dishButton.text(message);
					}
					else if(type = "Failure"){
						
						alert(message);
					}
					
				}, "json");
			});
	}
	
	//处理添加到购物车的显示
	function addShopCar(dishName, dishPrice,dishId){
		
		//讲购买的菜品写到后台
		$.post("/order/store/car_subAndAddDishHandle.action", {"dishId": dishId});
		
		var flag = true;
		var newPart;
		if(0 != $(".orderList").size()){
			$(".orderList").each(function(index, element){
				
				
				if($(this).children('.nameCar').text() == dishName){
					
					//份数加一
					var part = $(this).children(".partCar").children(".partValue").text();
					newPart = parseInt(part) + 1;
					$(this).children(".partCar").children(".partValue").text(newPart);
					//价格翻倍
					$(this).children(".priceCar").text("￥" + (newPart * parseInt(dishPrice)));
					flag = false;
				}
				
			});
		}
		
		var lastElement;
		if(flag){
			newPart = 1;
			if(0 != $(".orderList").size()){
				lastElement = $(".orderList").last();
			}
			else
				lastElement = $("#th");
				$("<tr class='orderList'>"+
					
					"<td class='nameCar'>"+ dishName+"</td>"+
					"<td class='partCar'><a class='sub'>-</a>&nbsp;<font class='partValue'>1</font>&nbsp;<a class='add'>+</a></td>"+
					"<td class='priceCar'>￥"+ dishPrice+"</td>"+
				"<input type='hidden' name='dishId' value='"+ dishId+"'></tr>").insertAfter(lastElement);
			
			//处理 加减按钮的绑定
			$(".add").unbind("click");
			$(".sub").unbind("click");
			bindSubAndAddButton();
		}
		
		//处理份数和价格的变化
		var total = $("#totalPart").text();
		$("#totalPart").text(parseInt(total) + 1);
		var totalPrice = $("#totalPrice");
		totalPrice.text((parseInt(totalPrice.text()) + parseInt(dishPrice))+"元");
		
	}
	
	//清空购物车
	$("#clearShoppingCar").on("click", function(){
		
		$.post("/order/store/car_clearShoppingCar.action");
		
		$(".orderList").remove();
		$("#totalPart").text("0");
		$("#totalPrice").text("0元");
		
	});
	
	//处理菜品展示问题
	$(".type").on("click", function(){
		
		category = $(this).children("a").text();
		
		if(category == "全部")
			category = "";
		showDishes(category, "monthSales");
	});
	
	//处理提交订单
	$("#commitButton").on("click", function(){
		
		if(0 == $(".orderList").size())
			return;
		location.href = "/order/store/car_gotoOrderConfirm.action";
		
	});
	
	//拉取购物车总菜单
	$.post("/order/store/car_getShoppingCarDishes.action", function(message){
		
		$("#th").after(message);
		
		bindSubAndAddButton();
		
	});
	
	//绑定 购物车加减按钮的 函数
	function bindSubAndAddButton(){
		
		//绑定增加 按钮
		$(".add").on("click", function(){
			
			var dishId = $(this).parent().siblings("input[name='dishId']").val();
		
			var addButton = $(this);
			//首先提交给后台处理 增加
			$.post("/order/store/car_subAndAddDishHandle.action", {"dishId": dishId, "type":"add"},function(result){
				
				var type = result.type;
				var message = result.message;
				if("Success" == type){
					
					addButton.prev(".partValue").text(message.part);
					addButton.parent().siblings(".priceCar").text("￥" + message.price);
					$("#totalPart").text(message.totalPart);
					$("#totalPrice").text(message.totalPrice + "元");
					
				}
				else{
					
					$.messager.alert("错误", message, "warning");
				}
				
			},"json");
		});
		
		//绑定 减少 按钮
		$(".sub").on("click",function(){
			
			var dishId = $(this).parent().siblings("input[name='dishId']").val();
			var value = $(this).next(".partValue").text();
			if(isOutOfIndex(value))
				return; //份数等于0 不能再减了 bug 
			
			var subButton = $(this);
			//首先提交给后台处理 增加
			$.post("/order/store/car_subAndAddDishHandle.action", {"dishId": dishId, "type":"sub"},function(result){
				
				var type = result.type;
				var message = result.message;
				if(type == "Success"){
					subButton.next(".partValue").text(message.part);
					subButton.parent().siblings(".priceCar").text(message.price);
					$("#totalPart").text(message.totalPart);
					$("#totalPrice").text(message.totalPrice + "元");
					
				}
				else{
					
					$.messager.alert("错误", message, "warning");
				}
				
			},"json");
		});
	}
	
	//判断份数是否小于0
	function isOutOfIndex(value){
		
		value =  parseInt(value);
		if(value == 0)
			return true;
		else
			return false;
	}
	
	//拉取购物车的份数和价格
	$.post("/order/store/car_getShoppingCarPriAndPart.action", function(message){
		
		$("#total").html(message);
	});
	
	function getCommentByType(type)
	{
		
		if(type == "差评")
			type = "bad";
		else if(type == "中评")
			type = "medium";
		else if(type == "好评")
			type = "good";
		else 
			type = "";
		
		$.post("/order/store/comment_showComments.action", {"type":type}, function(result){
			
			//更新 评价体
			$("#commentBoxs").html(result);
		});
	}
	
	$("#dishesMenu a").on("click", function(){
		
		var type = $(this).text();
		if(type == "菜品")
			showDishes("", "monthSales");
		else if(type == "评价"){
			
			$.ajax({
				   type: "POST",
				   url: "/order/store/comment_showCommentDiv.action",
				   dataType: "json",
				   async: false,
				   success: function(result){
						
					   var type = result.type;
						var message = result.message;
						
						if("Failure" == type){
							alert("服务器开小差了，请稍候再试！");
						}
						else if("Success" == type){
							
							$("#context").html(message);
						}
						//绑定 中 差 好评按钮
						$(".typeLi").on("click", function(){
							
							
							var type = $(this).text();
							getCommentByType(type);
						});
						 
					}
				});
			
		}
		
		
			 
	});
	
	showDishes("", "monthSales");
});