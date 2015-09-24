/*
 * 完成地址的切换
 * 828477
 */

$(document).ready(function(){
	
	$("#logout").on("click", function(){
		
		$.post("",function(){
			
			location.href = "/order/login.jsp";
		});
	});
	
});