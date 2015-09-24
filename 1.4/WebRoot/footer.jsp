<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'footer.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="/order/css/footer.css">
	

  </head>
  
  <body>
    <body>
		 <div class="page-footer">
      <div class="footer-wrap">
        <div class="column fl help control">
          <div class="title">用户帮助</div>
          <ul>
            <li><a href="" class="ca-darkgrey" target="_blank" rel="nofollow">常见问题</a></li>
            <li><a href="" class="ca-darkgrey" target="_blank" rel="nofollow">用户反馈</a></li>
            <li><a href="" class="ca-darkgrey" target="_blank" rel="nofollow">诚信举报</a></li>
          </ul>
        </div>
        <div class="column fl corp control">
          <div class="title">商务合作</div>
          <ul> 
            <li><a href="/order/businessmanLogin.jsp" class="ca-darkgrey" target="_blank" rel="nofollow">我要开店</a></li>
            <li><a href="#" class="ca-darkgrey" target="_blank" rel="nofollow">配送合作申请入口</a></li>
          </ul>
        </div>
        <div class="column fl update control">
          <div class="title">公司信息</div>
          <ul>
            <li><a href="#" class="ca-darkgrey" target="_blank" rel="nofollow">关于乐丰</a></li>
            <li><a href="#" class="ca-darkgrey" target="_blank" rel="nofollow">媒体报道</a></li>
            <li><a href="#" class="ca-darkgrey" target="_blank" rel="nofollow">加入我们</a></li>
          </ul>
        </div>
         <div class="column fl update control">
          <div class="title">友情链接</div>
          <ul>
            <li><a href="http://www.sf-express.com/cn/sc/" class="ca-darkgrey" target="_blank" rel="nofollow">顺丰官网</a></li>
            <li><a href="http://www.sfbest.com/" class="ca-darkgrey" target="_blank" rel="nofollow">顺丰优选</a></li>
            <li><a href="http://www.sf-airlines.com/" class="ca-darkgrey" target="_blank" rel="nofollow">顺丰航空</a></li>
          </ul>
        </div>
        <div class="column fr service control">
          <div><i class="icon i-service-avatar" ></i></div>
          <div class="details">
            <p class="w1">乐丰外卖客服电话</p>
            <p class="w2">***********</p>
            <!-- <p class="w2">4008508888</p> -->
            <!-- <p class="w2">010-56652722</p> -->
            
              <p class="w3">周一到周日 10:00-23:00</p>
            
            <p class="w3">客服不受理商务合作</p>
          </div>
        </div>
        
        <div class="clear"></div>
        <div class="copyright">&copy;2015 lefeng.com <a target="_blank" href="#">It-Class</a> 第六小组</div>
        <br/>
      </div>
    </div>
	</body>
  </body>
</html>
