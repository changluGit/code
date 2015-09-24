$(function(){
	var orgCol = "#426AB3";
	var chgCol = "#CA8A88";
	var orgBgCol = "white";
	var chgBgCol = "#426AB3";
	$(".tasteDetail").bind("mouseleave",function() {
		var flag = $(this).attr("flag");
		if(flag=="0") {
			$(this).css("color",orgCol);
		} else {
			$(this).css("color","white");
		}
		$(this).css("border-color",orgCol);
	});
	$(".tasteDetail").bind("mousemove",function() {
		var flag = $(this).attr("flag");
		if(flag=="0") {
			$(this).css("color",chgCol);
			$(this).css("border-color",chgCol);
		} else {
			$(this).css("color","#FFE48D");
			$(this).css("border-color",chgCol);
		}
	});
	$(".tasteDetail").bind("click",function() {
		var tasteId = $(this).attr("myVal");
		var thisObject = $(this);
		var flag = $(this).attr("flag");
		if(flag=="0") {
			$(thisObject).css("background-color",chgBgCol);
			$(thisObject).css("color","white");
			$(thisObject).attr("flag","1");
		} else {
			$(thisObject).css("background-color",orgBgCol);
			$(thisObject).css("color",orgCol);
			$(thisObject).attr("flag","0");
		}
		$.ajax({
			url:"/order/core/userTaste_addOrRemoveUserTaste.action",
			data:{tasteId:tasteId},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.type=="Success") {
					if(data.message=="add") {
						$(thisObject).css("background-color",chgBgCol);
						$(thisObject).css("color","white");
						$(thisObject).attr("flag","1");
					} else {
						$(thisObject).css("background-color",orgBgCol);
						$(thisObject).css("color",orgCol);
						$(thisObject).attr("flag","0");
					}
				}
			}
		});
	});
});
