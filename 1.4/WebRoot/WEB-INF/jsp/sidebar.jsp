<!-- SideBar -->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<div class="sidebarBox">
	<div class="sideAttrBox"><a href="/order/core/userInfo_listUser_info.action" target="main">我的账户</a></div>
	<div class="sideAttrBox"><a>我的订单</a></div>
	<div class="sideAttrChildBox"><a href="/order/core/order_findLatest.action" target="main">最新订单</a></div>
	<div class="sideAttrChildBox"><a href="/order/core/order_findHistory.action" target="main">历史订单</a></div>
	<div class="sideAttrBox"><a href="/order/core/petcard_getPetCardAndConm.action"
						target="main">我的储蓄卡</a></div>
	<div class="sideAttrBox"><a href="" target="main">我的收藏</a></div>
	<div class="sideAttrChildBox"><a href="/order/core/storecollect.action" target="main">店铺收藏</a></div>
	<div class="sideAttrChildBox"><a href="/order/core/vegcollect.action" target="main">菜品收藏</a></div>
	<div class="sideAttrBox"><a href="/order/core/address_findAllAddress.action"
						target="main">地址管理</a></div>
	<div class="sideAttrBox"><a href="/order/core/complaint_listComp.action" target="main">意见反馈</a></div>
</div>