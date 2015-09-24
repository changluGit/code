$(document).ready(function(){
	
	$("#myOrder").on("click",function(){
		
		console.log("zhangsan");
		
		$("#right").load("/order/home.jsp");
	});
});