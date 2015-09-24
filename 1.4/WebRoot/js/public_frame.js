$(function(){
	//头部链接颜色变化
	$(".hU a").bind("mousemove",function(){
		$(this).css("color","darkseagreen"); 
	});
	$(".hU a").bind("mouseleave",function(){
		$(this).css("color","white");
	});
	$(".hD2D a").bind("mousemove",function(){
		$(this).css("color","darkslateblue"); 
	});
	$(".hD2D a").bind("mouseleave",function(){
		$(this).css("color","#808080");
	});
	//侧边栏高度调整
	var sidebarBoxMinHeight = parseInt($(".sidebarBox").css("height"));
	var tmpHeight = parseInt($(".contentBox").css("height"));
	
	if(sidebarBoxMinHeight<tmpHeight) {
		$(".sidebarBox").css("height",tmpHeight+"px");
	}
	//高度调整
	$(".contentBox").bind("click",function(){
		tmpHeight = parseInt($(".contentBox").css("height"));
		if(sidebarBoxMinHeight<tmpHeight) {
			$(".sidebarBox").css("height",tmpHeight+"px");
		} else {
			$(".sidebarBox").css("height",sidebarBoxMinHeight+"px");
		}
	});
	//侧边栏颜色变化
	$(".sideAttrBox a").parent().bind("mousemove",function(){
		$(this).css("background-color","#426AB3");
		$(this).children("a").css("color","white");
	});
	$(".sideAttrBox a").parent().bind("mouseleave",function(){ 
		$(this).css("background-color","#ECEDEE");
		$(this).children("a").css("color","#2F4F4F");
	});
	$(".sideAttrChildBox").bind("mousemove",function(){ 
		$(this).css("background-color","#426AB3");
		$(this).children("a").css("color","white");
	});
	$(".sideAttrChildBox").bind("mouseleave",function(){ 
		$(this).css("background-color","#ECEDEE");
		$(this).children("a").css("color","#788681");
	});
});