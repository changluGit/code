
$(document).ready(function(){
	
	
	$("#loginDiv a").on("click", function(){
		
		var email = $("#email").val();
		var pwd  = $("#password").val();
		
		$.post("/order/business/busLogin_loginCheck.action", {"email": email,"pwd":pwd}, function(result){
			
			var type = result.type;
			var message = result.message;
			
			if("Success" == type){
				
				
				location.href = "/order/business/busLogin_gotoBusinessManage.action";
			}
			else if("Failure" == type){
				
				$("#error").html(message);
				$("#error").show();
			}
		}, "json");
	});
});