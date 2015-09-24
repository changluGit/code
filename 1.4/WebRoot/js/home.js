/*
 * 主要完成store 数据的请求
 */

$(document).ready(function(){
	
	/**
	 * 弹出框
	 */
	
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
	//切换默认地址的弹出框
	$('#dd').dialog({   
	    title: '切换地址',    
	    closed: true,   
	    cache: false,   
	    modal: true,
	    buttons:[{
			text:'确定',
			handler:function(){
				
				var radioInput = $(".addressItem input[name='addressHeader']:checked");
				var address = radioInput.next(".addressText").text();
				var addressId = radioInput.val();
				
				$("#font1").html(address);
				
				//设置默认地址
				$.post("/order/core/address_setDefaultAddr.action", {"addressId":addressId},function(){
					
					//设置成功后 处理检索数据、
					getDate(storeType, "saleVolume");
					
					//处理推荐菜品列表
					var turn = $("#oneKeyDishDiv input[name = 'turn']").val(); //拿到轮数
					if(undefined == turn || turn == "")
						turn = 0;
					getRecommendDishes(turn);
				});
				
				$('#dd').dialog("close");
			}
		},{
			text:'取消',
			handler:function(){
				
				$('#dd').dialog("close");
			}
		}]

	});   
	
	//处理新增地址
	$('#dd2').dialog({   
	    title: '新增地址',   
	    closed: true,   
	    cache: false,
	    modal: true  
	});   
	
	$("#addAddressButton2").on("click", function(){
		
		console.log("zhangsan");
		var shouhuoren = $(".dd2Input input[name='name']").val();
		var shouhuo_tell = $(".dd2Input input[name='tel']").val();
		var address = getAreaNamebyID(getAreaID());
		var addressDetail = $(".dd2Input input[name='addressDetail']").val();
		
		$.post("/order/core/address_add_Address.action", {"shouhuoren":shouhuoren, "shouhuo_tell":shouhuo_tell, "address":address,"detailAddress":addressDetail},function(result){
			
			var resultType = result.type;
			var message = result.message;
			if("Success" == resultType){
				
				$("#dd3").html(message);
				
				$('#dd3').dialog({   
				    title: '消息',   
				    width: 400,   
				    height: 200,   
				    closed: true,   
				    cache: false,   
				    modal: true,
				    buttons:[{
						text:'确定',
						handler:function(){
							
							$("#font1").html(address);
							
							//设置成功后 处理检索数据、
							getDate(storeType, "saleVolume");
							//拿到推荐的菜品
							var turn = $("#oneKeyDishDiv input[name = 'turn']").val(); //拿到轮数
							if(undefined == turn || turn == "")
								turn = 0;
							getRecommendDishes(turn);
							
							$("#dd3").dialog("close");
							$("#dd2").dialog("close");
							$("#dd").dialog("close");
						}
					}]
				});
				
				$("#dd3").dialog("open");
			}
		},"json");
	});
	
	//绑定 地址切换
	$("#switchAddress").on("click",function(){
		
			$.post("/order/store/show_addressSwitch.action", function(result){
			
			var type = result.type;
			var message = result.message;
			
			//登录用户
			if("Success" == type){
				
				var addressHtml = "";
				$(message).each(function(){
					
					if(this.defaultAddress == "1")
						addressHtml += "<div class = 'addressItem'><input type='radio' name='addressHeader' checked='checked' value='"+this.id+"'/><font class='addressText'>"+ this.address+"</font></div>";
					else
						addressHtml += "<div class = 'addressItem'><input type='radio' name='addressHeader' value='"+this.id+"'/><font class='addressText'>"+this.address +"</font></div>";
				});
				
				addressHtml += "<div><a href='javascript:void(0)' id='addAddressButton'>添加新的地址</a></div>";
				$("#dd").html(addressHtml);
			
				//绑定新增地址按钮的绑定
				$("#addAddressButton").on("click", function(){
					
					$("#dd2").dialog("open");
				});
				$("#dd").dialog("open");
			}
			else if("Failure" == type){//非登录用户
				//切换地址会直接跳转到 地址输入界面
				location.href = "/order/unRegisterIndex.jsp";
			}
		},"json");
	});
	
	//---------------------------菜品推荐区域-------------------------------
	
	var onekeyDishId = "";
	
	$(".recommendDiv a").on("click", function(){
		
		onekeyDishId = $(this).prev("input[name='dishId']").val();
		
		//所有的颜色 处理成默认的背景和字体颜色
		$(".recommendDiv a").css({backgroundColor:'white', color:'#426AB3'});
		$(this).css({backgroundColor:'#426AB3', color:'white'});
	});
	
	//远端请求推荐菜品
	function getRecommendDishes(turn){
		
		$.post("/order/store/show_getDishRecommend.action", {"turn":turn}, function(result){
			
			$("#oneKeyDishDiv").html(result);
			
			//绑定 推荐菜品的点击事件
			
			$(".recommendDiv a").on("click", function(){
				
				onekeyDishId = $(this).prev("input[name='dishId']").val();
				
				//所有的颜色 处理成默认的背景和字体颜色
				$(".recommendDiv a").css({backgroundColor:'white', color:'#426AB3'});
				$(this).css({backgroundColor:'#426AB3', color:'white'});
			});
		});
	}
	//跟换一批按钮事件绑定
	$("#changeButton").on("click", function(){
		
		var turn = $("#oneKeyDishDiv input[name = 'turn']").val();
		getRecommendDishes(turn);
	});
	
	//绑定一键下单
	$("#placeButton").on("click", function(){
		
		console.log(onekeyDishId);
		if(undefined == onekeyDishId || onekeyDishId == "")
			return;
		
		$.post("/order/store/car_onekeyOrder.action", {"dishId":onekeyDishId}, function(result){
			
			var type = result.type;
			console.log(type);
			if("Success" == type){
				
				location.href = "/order/store/car_gotoOrderConfirm.action?onekey=true";
			}
		}, "json");
	});
	
	//请求远端返回推荐的菜品
	getRecommendDishes(0);
	//---------------------------------------------------------------------------
	var storeType = "";
	var sort;
	
	//请求 store 数据的方法
	function getDate(storeType, sort){
		
		$.post("/order/store/show_showStore.action", {"type":storeType, "sort":sort}, function(result){
			
			var type = result.type;
			var message = result.message;
			
			 if("Success" == type)
	            {
				 	$("#storeDiv").html(message);
	            }
	            else if("Failure" == type){
	                
	            	
	    			$("#span1").html(result.message);
	            }
		},"json");
	}
	
	//商店类别 点击事件绑定
	$(".footDl").on("click", function(){
		
		storeType = $(this).children(".footDd").html();
		
		console.log(storeType);
		if(storeType == "全部")
			storeType = "";
		
		getDate(storeType, "saleVolume");
	});
	
	//商店 检索条件 点击事件绑定
	$("#sort a").on("click", function(){
	
		var text = $(this).text();
		
		if(text == "默认排序"){
			
			sort = "saleVolume";
		}
		else if(text == "销量"){
			sort = "saleVolume";
		}
		else if(text == "评价"){
			sort = "rating";
		}
		else if(text == "送餐速度"){
			sort = "foodDeliveryTime";
		}
		
		getDate(storeType, sort);
	});
	
	/*
	 * 检索框输入检索
	 * */
	$("#searchButton").on("click", function(){
		
		var searchContext = $("#searchText").val();
		console.log(searchContext);
		
		//如果输入框中内容为空，则弹出提示
		if(undefined == searchContext || "" == searchContext){
			
			$.messager.alert("注意", '请在搜索框中输入内容后在搜索！', "warning");
			return;
		}
		
		//post 请求，检索商店
		$.post("/order/store/show_inputSearchHandle.action", {"storeName": searchContext}, function(result){
			
			var type = result.type;
			var message = result.message;
			
			 if("Success" == type)
	            {
				 	$("#storeDiv").html(message);
	            }
	            else if("Failure" == type){
	                
	            	$.messager.show({
	    				title:'消息',
	    				msg:"<font style='color:red'>"+message+"</font>",
	    				showType:'fade',
	    				style:{
	    					top:200
	    				}
	    			});
	            }
		},"json");
		
	});
	
	//首次访问默认按 中餐和月销量排序
	getDate(storeType, "saleVolume");
});