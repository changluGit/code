package com.six.action.businessManage;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import ognl.ObjectElementsAccessor;

import org.apache.struts2.util.ServletContextAware;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.six.action.BaseAction;
import com.six.entity.Dishes;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.Store;
import com.six.service.businessService.OrderManageService;
import com.six.util.DateUtil;
import com.six.util.JSONUtil;
import com.six.util.NoConfirmOrderCache;

@Component
public class NoConfirmOrderAction extends BaseAction implements ServletContextAware
{
	private OrderManageService orderManageService;
	
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext arg0)
	{
		this.servletContext = arg0;
		
	}

	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 得到一个未确认的订单
	 */
	public void getNoComfirmOrder()
	{
		//得到商店的id
		Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		
		//从servletContext 中拿到该商店的未确认订单的缓冲
		NoConfirmOrderCache noConfirmOrderCache = (NoConfirmOrderCache)servletContext.getAttribute(loginedStore.getId()+"");
		if(null == noConfirmOrderCache)
		{
			toWrite(JSONUtil.returnResultJson("Failure", "没有数据！"));
			return;
		}
		
		Order noConfirmOrder = noConfirmOrderCache.getNoConfirmoOrder();
		if(null == noConfirmOrder)
		{
			toWrite(JSONUtil.returnResultJson("Failure", "没有数据！"));
			return;
		}
		//有订单 处理该订单 然后返回json 数据
		//重新加载到内存中
		noConfirmOrder = orderManageService.getOrderById(noConfirmOrder.getId() + "");
		JSONObject objectJSON =new JSONObject();
		objectJSON.element("id", noConfirmOrder.getId());
		objectJSON.element("orderNum", noConfirmOrder.getOrderNum());
		objectJSON.element("username", noConfirmOrder.getUser().getName());
		objectJSON.element("totalAmount", noConfirmOrder.getTotalAmount());
		
		objectJSON.element("createTime", DateUtil.formatDateTime(new Date(noConfirmOrder.getCreateTime().getTime())));
		objectJSON.element("userMessage", noConfirmOrder.getUserMessage());
		objectJSON.element("address", noConfirmOrder.getAddress());
		objectJSON.element("shouhuoren", noConfirmOrder.getShouhuoren());
		objectJSON.element("tel", noConfirmOrder.getShouhuoTel());
		
		String dishDetail = "";
		for(OrderDishes temOrderDishes : (Set<OrderDishes>)noConfirmOrder.getOrderDisheses())
		{
			dishDetail += temOrderDishes.getDishes().getName()+ " : " + temOrderDishes.getNum() + "份,";
		}
		dishDetail = dishDetail.substring(0, dishDetail.length() - 1);
		
		objectJSON.element("dishDetail", dishDetail);
		
		toWrite(JSONUtil.returnResultJson("Success", objectJSON.toString()));
	}
	
	/**
	 * 处理稍候确定的订单
	 */
	
	public void handleLaterOrder()
	{
		
		//得到商店的id
		Store loginedStore = (Store)getRequest().getSession().getAttribute("loginedStore");
		
		//从servletContext 中拿到该商店的未确认订单的缓冲
		NoConfirmOrderCache noConfirmOrderCache = (NoConfirmOrderCache)servletContext.getAttribute(loginedStore.getId()+"");
		
		//那的肉需要稍候确认的 订单 id
		String orderIdStr = getRequest().getParameter("orderId");
		
		try
		{
			noConfirmOrderCache.laterConfirm(orderManageService.getOrderById(orderIdStr));
		} catch (Exception e)
		{
			toWrite(JSONUtil.returnResultJson("Failure", "非法参数引起的系统错误，请稍候再试！"));
		}
		
		toWrite(JSONUtil.returnResultJson("Success", "该订单会在10分钟后再次提醒，或者您可以到订单管理中确定该订单"));
		
		
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
	
	
	
}
