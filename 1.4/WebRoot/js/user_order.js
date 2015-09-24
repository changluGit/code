$(function(){
	$('form').submit(function() {
	  formData = $(this).serialize();
	  formData += "&content="+$(this).children("textarea").val();
	  var formThis =$(this);
	  $.ajax({
 		 type:"POST",
 		 url:"/order/core/orderEvaluate_evaluateOrder.action",
 		 data:formData,
 		 dataType: "json",
 		 success:function(data){
 			if(data.type=="Success"){
 		 		
 		 		$(formThis).children(".fontB").text("已评价");
 		 		$(formThis).children("input[name='bt']").remove();
 		 		$(formThis).children("textarea").attr("readonly","readonly");
 		 		$(".detailBody").hide();
 		 		
 		 		alert(data.message);
 		 	} else {
 		 		alert(data.message);
 		 	}
 		 },
 		 error:function() {
 		 	alert("error");
 		 }
 	  });
	  return false;
	});

	$(".detailBody").hide();
	$(".orderBody").click(function(){
		if($(this).next(".detailBody").is(":hidden")) {
			$(".detailBody").hide();
			$(this).next(".detailBody").show();
		} else {
			$(".detailBody").hide();
		}
	});
});

function cancelOrder(e,orderId) {
	$(".detailBody").hide();
	$.ajax({
 		type:"POST",
 		url:"/order/core/order_cancelOrder.action",
 		data:{
 		orderId:orderId
 		},
 		dataType: "json",
 		success:function(data){
 			if(data.type=="Success"){
 				alert(data.message);
 				$(e).siblings(".state").html("状态:取消订单中");
 				e.remove();
 				$(".detailBody").hide();
 		 	} else {
 		 		alert(data.message);
 		 	}
 		},
 		error:function() {
 			alert("error");
 		}
 	});
}

function stateStr(stateInt) {
	if(stateInt==1) {
		return "已取消";
	} else if(stateInt==2) {
		return "送达";
	} else if(stateInt==3) {
		return "派送中";
	} else if(stateInt==4) {
		return "已确认";
	} else if(stateInt==5) {
		return "取消订单中";
	} else if(stateInt==6) {
		return "已下单";
	} else {
		return "状态有误";
	}
}
