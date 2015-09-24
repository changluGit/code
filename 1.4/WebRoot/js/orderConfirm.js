
$(document).ready(function(){
	
	var payParam = {};
	var name = "";
	var tel = "";
	var addressDetail = "";
	
	//处理新增地址
	$('#dd2').dialog({   
	    title: '新增地址',   
	    closed: true,   
	    cache: false,
	    modal: true  
	}); 
	
	$.post("/order/core/address_getAllAddress.action", function(json){
		
		var addressHtml = "";
		$(json).each(function(){
			if(this.defaultAddress == "1")
				addressHtml += "<input type='radio' name='address' value='"+ this.id+"' checked='checked'>收货人："+ this.shouhuoren+" 电话号码："+this.shouhuoTell+" <br><font style='margin-left=10px'>&nbsp;&nbsp;&nbsp;&nbsp;地址：" + this.address+"&nbsp"+ this.detailAddress +"</input></font><br><hr>";
			else
				addressHtml += "<input type='radio' name='address' value='"+ this.id+"' >收货人："+ this.shouhuoren+" 电话号码："+this.shouhuoTell+" <br>&nbsp;&nbsp;&nbsp;&nbsp;地址：" + this.address+"&nbsp"+ this.detailAddress +"</input><br><hr>";
		});
		if(""!= addressHtml)
			$("#del_address").html(addressHtml);
	},"json");
	
	$("#pay_tatal_button a").on("click", function(){
		
		var addressId = $("#del_address input[name='address']:checked").val();
		var message = $("#del_message input").val();
		var payType = $("#payment_type input[name='paymentType']:checked").val();
		if(undefined != addressId && addressId != ""){
			
			
			payParam = {"addressId":addressId, "message":message, "payType":payType};
		}
		else{
			payParam = {"shouhuoren":name,"shouhuoTel":tel,"addressDetail":addressDetail, "message":message, "payType":payType};
		}
			
		$.post("/order/core/order_placeOrder.action", payParam, function(result){
			
			var type = result.type;
			var message = result.message;
			
			if("Success" == type){
				
				$.messager.alert("消息", "下单成功，点击确认后调整到首页", "info", function(){
					
					//跳转到首页
					location.href = "/order/core/goto_home.action";
				});
			}
			else if("Failure" == type){
				
				$.messager.alert("错误", message, "warning");
			}
		}, "json");
	
	});
	
	$("#onekeyChange").on("click", function(){
		
		console.log("zhangsan");
		$.post("/order/store/car_changeDishForOnekey.action",function(result){
			
			var type = result.type;
			var message = result.message;
			if("Success" == type){
				
				location.href = "/order/store/car_gotoOrderConfirm.action?onekey=true";
			}
		}, "json");
	});
	
	
	$("#addNewAddress").on("click", function(){
		
		$("#dd2").dialog("open");
	});

	$("#addAddressButton2").on("click", function(){
		
		name = $("#dd2 input[name='name']").val();
		tel = $("#dd2 input[name='tel']").val();
		addressDetail = $("#dd2 input[name='addressDetail']").val();
		
		var addressHtml = "";
		addressHtml += "<div id='addressNewDiv'>姓名："+name+"&nbsp;&nbsp;电话："+ tel+"&nbsp;&nbsp;<br>详细地址："+ addressDetail+"</div>";
		
		$("#del_address").html(addressHtml);
		
		$("#dd2").dialog("close");
		
		payParam = {"shouhuoren":name,"shouhuoTel":tel,"addressDetail":addressDetail, "message":message, "payType":payType};
	});
});