<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>我的投诉</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="/order/css/public_frame.css" rel="stylesheet">
<link href="/order/qnui/css/sui.min.css" rel="stylesheet">
<script type="text/javascript" src="/order/qnui/js/jquery.js"></script>
<script type="text/javascript" src="/order/qnui/js/sui.min.js"></script>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript" src="/order/js/public_frame.js"></script>
<style type="text/css">
	.base{
		width:800px;
		height:100%;
	}
	.weixin{
		margin-left:100px;
		font-size:12px;
	}
	.left{
		float:left;
		width:200px;
	}
	.right{
		float:left;
		padding-left:30px;
		width:600px;
		border:2px;	
		text-align:left;	
	}
	.submit{
		margin-left:5px;
		margin-top:10px;
		background:#71C671;
		width:70px;
		height:32px;
		border:9px;
		color:#FFFFFF;
	}
	.submit:hover{
		background:#5F9EA0;
	}
	.content{
		font-size:12px;
	}
	.fankui{
		background-color:#96CDCD;
		font-size:14px;
		height:40px;
	}
	#text{
		text-align:left;
	}
	.time{
		margin-left:400px;
		font-size:12px;
	}
	.me{
		font-size:12px;
		line-height:30px;
	}
	.ziti{
		padding-top:10px;
		color:#3B3B3B;
	}
	.all{
		margin:20px;
	}
	.kefu{
		height:40px;
		width:120px;
		float:right;
		margin-top:20px;
		background-color:#FF3030;
	}
	.zhaokefu{
		line-height:40px;
		color:#FFF;
	}
</style>

</head>
<body style="text-align: center;">
  	<!-- 头部  -->
  	<jsp:include page="/WEB-INF/jsp/public/header.jsp"></jsp:include>
  	<!-- 主体  -->
  	<div class="mainBox">
		<div class="bodyBox">
			<!-- 侧边栏  -->
  			<jsp:include page="/WEB-INF/jsp/public/sidebar.jsp"></jsp:include>
  			<!-- 内容 -->
  			<div class="contentBox">
				<div class="base">
					<div class="right">
						<form name="complaint_add" method="post" action="/order/core/complaint_addComplaint.action">
						<div>
							<div class="fankui">
								<h4 class="ziti">&nbsp;&nbsp;&nbsp;反馈通道</h4>
							</div>
							<div style="padding-top:15px">
								<span class="content">客服电话：95338-666</span><span class="weixin">客服微信：wujiande21</span>
							</div>
							<div class="fankui">
								<h4 class="ziti">&nbsp;&nbsp;&nbsp;反馈留言</h4><br>
							</div>
								<textArea id="text" name="complaint_cont" onfocus="if(value=='请输入您的意见'){value='';}" onblur="if (value ==''){value='请输入您的意见';}" cols="94" rows="5">请输入您的意见</textArea><br>
								<input class="submit" type="submit" value="发送"/>
						</div>
						</form>
						<c:forEach var="comp" items="${result}">
						<hr width="98%" size=1 color="#668B8B"></hr>
						<div class="all">
							<span class="me">我：</span><span class="time">${comp.creatTime}</span><br>
							<span class="me">${comp.content}</span><br>
							<span class="me">回复：</span>
							<span class="me">
							<c:if test="${comp.status=='0'}">
								<span class="content">暂时没有回复</span>
							</c:if>
							</span>
							<span class="me">${comp.processResult}</span>
						</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<!-- end 内容 -->
  		</div>
  	</div>
  	<!-- end 主体 -->
	<!-- 脚本 -->
	<jsp:include page="../../footer.jsp"></jsp:include>
</body>
</html>

