package com.six.action.businessManage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.six.action.BaseAction;
import com.six.entity.Store;
import com.six.service.businessService.OrderManageService;
import com.six.util.JSONUtil;

public class OrderManageAction extends BaseAction
{

	private OrderManageService orderManageService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String gotoOrderManagePage()
	{
		return "orderMpage";
	}
	
	public void getDatagridJSON()
	{
		Map<String, Object> queryConditions = new HashMap<String, Object>();
		
		String orderStateStr = getRequest().getParameter("state");
		Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		
		queryConditions.put("store", loginedStore);
		if(StringUtils.isNotBlank(orderStateStr))
			queryConditions.put("state", Integer.parseInt(orderStateStr));
		
		Integer firstResult = (page - 1) * 10;
		toWrite(orderManageService.getDatagridJsonByQuery(queryConditions, firstResult, rows));
	}

	/**
	 * 上海处理订单，改变订单的状态
	 */
	public void handleOrderState()
	{
		Integer orderId = Integer.parseInt(getRequest().getParameter("orderId"));
		Integer newStateCode = Integer.parseInt(getRequest().getParameter("newStateCode"));
		
		toWrite(orderManageService.handleOrderState(orderId, newStateCode));
				
	}

	/**
	 * 返回订单中的菜品明细
	 */
	public void getOrderDetail()
	{
		
		String orderIdStr = getRequest().getParameter("orderId");
		
		if(StringUtils.isBlank(orderIdStr))
			return;
		Integer orderId = Integer.parseInt(orderIdStr);
		
		toWrite(orderManageService.returnDishDetailHtml(orderId));
	}
	
	public OrderManageService getOrderManageService()
	{
		return orderManageService;
	}

	@Resource
	public void setOrderManageService(OrderManageService orderManageService)
	{
		this.orderManageService = orderManageService;
	}
	
	/**
	 * 接受订单
	 */
	 public void acceptOrder() {
		 String idStr = getRequest().getParameter("orderId");
		 Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		 if(idStr==null || StringUtils.isBlank(idStr)) {
			 toWrite(JSONUtil.returnResultJson("Failure", "未选择订单，请重试"));
			 return;
		 }
		 Integer orderId = Integer.parseInt(idStr);
		 String res = orderManageService.acceptOrder(loginedStore,orderId);
		 toWrite(res);
	 }
	/**
	 * 取消订单
	 */
	 public void cancelOrder() {
		 String idStr = getRequest().getParameter("orderId");
		 Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		 if(idStr==null || StringUtils.isBlank(idStr)) {
			 toWrite(JSONUtil.returnResultJson("Failure", "未选择订单，请重试"));
			 return;
		 }
		 Integer orderId = Integer.parseInt(idStr);
		 String res = orderManageService.cancelOrder(loginedStore,orderId);
		 toWrite(res);
	 }
	/**
	 * 送达，完成订单
	 */
	 public void completeOrder() {
		 String idStr = getRequest().getParameter("orderId");
		 Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		 if(idStr==null || StringUtils.isBlank(idStr)) {
			 toWrite(JSONUtil.returnResultJson("Failure", "未选择订单，请重试"));
			 return;
		 }
		 Integer orderId = Integer.parseInt(idStr);
		 String res = orderManageService.completeOrder(loginedStore,orderId);
		 toWrite(res);
	 }
}
