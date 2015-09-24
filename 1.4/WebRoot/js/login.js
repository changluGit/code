/*828477*/

$(document).ready(function(){
	
	
	$("#loginButton").on("click", function(){
		
		var email =	 $("#login-email").val();
		var pwd = $("#login-password").val();
		console.log($("#J-login-form").serialize());
		//验证输入格式
		if(undefined == email || "" == email){
			
			$("#J-normal-login-tip").show();
			$("#J-normal-login-tip").html("邮箱不能为空");
			
			return;
		}
		
		//判断邮箱格式是否正确
		 var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		    if (!reg.test(email)) {
		    	$("#J-normal-login-tip").show();
				$("#J-normal-login-tip").html("邮箱格式不正确");
				return;
		  }
		
		if(undefined == pwd || "" == pwd){
			
			$("#J-normal-login-tip").show();
			$("#J-normal-login-tip").html("密码不能为空");
			
			return;
		}
		
		//ajax 登录
		
		$.post("/order/core/login_loginCheck.action", {"user.email":email, "user.pwd":pwd}, function(result){
			
			var type = result.type;
			var message = type.message;
			
			 if("Success" == type)
	            {
				 	//出现异常，以后解决
	                location.href = "/order/core/goto_home.action";
	            }
	            else if("Failure" == type){
	                
	            	$("#J-normal-login-tip").show();
	    			$("#J-normal-login-tip").html(result.message);
	            }
		},"json");
		
		
	});
});