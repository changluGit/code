<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>找回密码</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="inc.jsp"></jsp:include>

<link rel="stylesheet" type="text/css" href="/order/css/login.css">
<script type="text/javascript">
	$(document).ready(function(){
	
	$("#loginButton").on("click", function(){
		
		var email =	 $("#login-email").val();
		var name = $("#login-name").val();
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
		
		//ajax 登录
		
		$.post("/order/core/forget_emailCheck.action", {"user.email":email,"user.name":name}, function(result){
			
			var type = result.type;
			var message = type.message;
			
			 if("Success" == type)
	            {
	                //location.href = "/order/login.jsp"; 
	            	$("#J-normal-login-tip").show();
	    			$("#J-normal-login-tip").html(result.message);
	    			$("#try").html("去登陆");
	            }
	            else if("Failure" == type){ 
	            	$("#J-normal-login-tip").show();
	    			$("#J-normal-login-tip").html(result.message);
	            }
		},"json");
		
		
	});
});
</script>
</head>

<body id="forget" class="pg-unitive-login theme--www">

        <header id="site-mast" class="site-mast site-mast--mini">
            <div class="site-mast__branding cf">
               <!-- <a class="site-logo" href="#" gaevent="header/logo">丰乐网</a>-->
            </div>
        </header><div class="site-body-wrapper" id="yui_3_16_0_1_1438340464163_112"> 
    <div class="site-body cf" id="yui_3_16_0_1_1438340464163_111" style="position: static;">
        <div class="promotion-banner">
            <img alt="名人名言" src="images/4.png" width="480" height="370"></div>
        <div data-component="unitlogin-section" class="component-unitlogin-section component-unitlogin-section--page mt-component--booted" mt-scope="[]" data-component-params="{&quot;service&quot;:&quot;www&quot;,&quot;isDialog&quot;:false}" data-component-config="{&quot;lazyRender&quot;:false}" id="yui_3_16_0_1_1438340464163_14"><div class="unit-login-section">
        <div class="J-login-block login-block login-block--pc">
    <div class="validate-info" style="visibility:hidden"></div>
    <h2 class="login-type-wrapper">
        <a href="#" id="J-login-link" gaevent="loginemotion/nav/normal" style="display:none">普通方式登录<i></i></a>
    找回密码
    </h2>
    <form id="J-login-form" action="#" method="post" class="form form--stack J-wwwtracker-form"><span style="display:none"><input type="hidden" name="csrf" value="SUXWndMdy9fiSbKgavRcHEu23wKCuaX59JYL4WkfUwnWqp6HA3qG6gO7u8rmAmv4"></span>
        <div id="J-normal-login-tip" class="common-tip common-tip--login" style="display:none"></div>
        <div class="form-field form-field--icon">
            <i class="icon icon-user"></i>
			<input type="text" id="login-email" class="f-text" name="user.email" placeholder="登录邮箱" value="">
        </div>
        <div class="form-field form-field--icon">
            <i class="icon icon-user"></i>
			<input type="text" id="login-name" class="f-text" name="user.name" placeholder="真实姓名" value="">
        </div>
        <div class="form-field form-field--icon" style="color:red">
       &nbsp &nbsp我们将会重置您的密码，发送至你填写的邮箱,您可以登陆再做修改
       </div>
       <div class="form-field form-field--auto-login cf">
				<a tabindex="-1" id ="try" href="login.jsp" target="_top" class="forget-password">你还想尝试登录？</a>
           
        </div>
       
        <div class="form-field form-field--ops">
           <a href="javascript:void(0)" id="loginButton">一键找回</a>
        </div>
    </form>

    <p class="signup-guide">还没有账号？<a href="/order/register.jsp" target="_top">免费注册</a></p>
    
    <p class="J-treaty-block treaty-block" style="display:none;">
    提示：未注册乐丰账号的手机号，登录时将自动注册乐丰账号，且代表您已同意<a href="#" target="_blank">《用户协议》</a>
    </p>
    
    <div class="oauth-wrapper">
        <h3 class="title-wrapper"><span class="title"></span></h3>
        
            <div class="oauth cf">
                <a class="oauth__link oauth__link--qq" gaevent="OutLink|Click|loginemotion/oauth/qq" href="https://passport.meituan.com/account/connect/tencent" target="_top" data-mtevent="{&quot;la&quot;:&quot;oauth/qq&quot;}"></a>
                <a class="oauth__link oauth__link--alipay" gaevent="OutLink|Click|loginemotion/oauth/alipay" href="https://passport.meituan.com/account/connect/alipay" target="_top" data-mtevent="{&quot;la&quot;:&quot;oauth/aplipay&quot;}"></a>
                <a class="oauth__link oauth__link--weibo" gaevent="OutLink|Click|loginemotion/oauth/sina" href="https://passport.meituan.com/account/connect/sina" target="_top" data-mtevent="{&quot;la&quot;:&quot;oauth/sina&quot;}"></a>
                <a class="oauth__link oauth__link--collapse oauth__link--tuan800" gaevent="OutLink|Click|loginemotion/oauth/tuan800" href="https://passport.meituan.com/account/connect/tuan800" target="_top" data-mtevent="{&quot;la&quot;:&quot;oauth/tuan800&quot;}"></a>
            </div>
    </div>
</div>

</div>

</div>
           </div>
</div>

        <footer class="site-info-w site-info-w--mini">
            <div class="site-info">
                
                <div class="site-info-nav cf">
                    <ul>
                        <li class="first"><a rel="nofollow" href="#">关于我们</a></li>
                  
                        <li><a rel="nofollow" href="#" gaevent="footer/job">加入我们</a></li>
                        <li><a rel="nofollow" href="#">商家入驻</a></li>
                        <li><a rel="nofollow" href="#">帮助中心</a></li>
                        
                    </ul>
                   
                </div>
                <div class="copyright">
                    <p>
                        ©<span>2015</span>
                        <a href="#">乐丰网团购</a>
                        lefeng.com
                        <a href="#" target="_blank">京ICP证070791号</a>
                        京公网安备11010502025545号<a href="#" target="_blank"> 电子公告服务规则</a>
                    </p>
                </div>
            </div>
        </footer>

</body>
</html>
