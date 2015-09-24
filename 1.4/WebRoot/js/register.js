
/**
 * 828477
 * 注册 js,完成输入验证，和注册提交
 */

$(document).ready(function(){

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
	//绑定注册按钮事件
	$("#registerButton").on("click", function(){
		
		
		var email = $("input[name='email']").val();
		var username = $("input[name='username']").val();
		var password = $("input[name='password']").val();
		var password2 = $("input[name='password2']").val();
		var name = $("input[name='name']").val();
		var tel = $("input[name='tel']").val(); 
		var addressDetail = $("input[name='addressDetail']").val();
		$("#span1").html("");
		$("#span2").html("");
		$("#span3").html("");
		$("#span4").html("");
		$("#span5").html("");
		$("#span6").html("");
		$("#span7").html("");
		
		var tag = true;
		//验证输入的格式是否正确
		if(undefined == email || "" == email){
			
			$("#span1").html("邮箱不能为空");
			tag = false;
		}
		//验证邮箱的格式是否正确
		 var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		 if (!reg.test(email)){
			 $("#span1").html("邮箱格式不正确");
			 tag = false;
		 }
			
		   
		 //验证name 是否为空
		 if(undefined == username || "" == username){
			 
			 $("#span2").html("用户名不能为空");
			 tag = false;
		 }
		 //验证密码不为空
		 if(undefined == password || "" == password){
			 
			 $("#span3").html("密码不能为空");
			 tag = false;
		 }
		 
		 //验证再次输入密码不为空
		 if(undefined == password2 || "" == password2){
			 
			 $("#span4").html("密码不能为空");
			 tag = false;
		 }
		 if(password != password2){
			 $("#span4").html("两次密码输入不一致");
			 tag = false;
		 }
		 
		 //验证 真实姓名是否为空
		 if(undefined == name || "" == name){
			 
			 $("#span5").html("真实姓名不能为空");
			 tag = false;
		 }
		 //验证手机号
		 if(undefined == tel || "" == tel){
			 
			 $("#span6").html("手机号不能为空");
			 tag = false;
		 }
		 var re = /^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
		 if (!re.test(tel)) {
			 $("#span6").html("手机号格式不对");
			 tag = false;
		  } 
		 
		 //判断详细地址不能为空
		 if(undefined == addressDetail || "" == addressDetail){
			 
			 $("#span7").html("详细地址不能为空");
			 tag = false;
		 }
		 if(!tag)
			 return;
		 
		 $.post("/order/core/register_userRegister.action", {"user.email":email, "user.pwd":password,"user.name":username,"address":getAreaNamebyID( getAreaID()),"name":name, "tel":tel,"addressDetail":addressDetail}, function(result){
				
				var type = result.type;
				var message = type.message;
				
				 if("Success" == type)
		            {
					 	$.messager.alert("消息", "注册成功，你可以登陆后点餐了，点击确认后回到登陆页面。", "info",function(){
					 		
					 		location.href = "/order/login.jsp";
					 	});
		                
		            }
		            else if("Failure" == type){
		                
		            	
		    			$("#span1").html(result.message);
		            }
			},"json");
		 
	});
});