package com.six.action;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.stereotype.Component;

import com.six.constant.OrderStateType;
import com.six.constant.ResultType;
import com.six.entity.Address;
import com.six.entity.Dishes;
import com.six.entity.Order;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.AddressManageService;
import com.six.service.CardManageService;
import com.six.service.OrderService;
import com.six.service.StoreService;
import com.six.util.JSONUtil;
import com.six.util.NoConfirmOrderCache;
/**
 * 
 * @author 828513
 * 订单Action类
 */
@Component
public class OrderAction extends BaseAction  implements ServletContextAware
{
	private Logger logger = Logger.getLogger(OrderAction.class);
	
	private OrderService orderService;
	private CardManageService cardManageService;
	private AddressManageService addressManageService;
	private StoreService storeService;
	
	
	private ServletContext servletContext;

	public OrderService getOrderService() {
		return orderService;
	}

	@Resource
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}


	public CardManageService getCardManageService() {
		return cardManageService;
	}
	@Resource
	public void setCardManageService(CardManageService cardManageService) {
		this.cardManageService = cardManageService;
	}

	public AddressManageService getAddressManageService() {
		return addressManageService;
	}
	@Resource
	public void setAddressManageService(AddressManageService addressManageService) {
		this.addressManageService = addressManageService;
	}
	
	public StoreService getStoreService() {
		return storeService;
	}
	@Resource
	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void test()
	{
		toWrite("This Order Test");
	}
	/* 订单页面跳转 */
	public String gotoOrderPage() {
		return "orderPage";
	}
	/* 找到最新的订单 */
	public String findLatest() {
		User user = (User) getRequest().getSession().getAttribute("loginedUser");
		List<Order> orders = orderService.findLatestByUser(user);
		getRequest().setAttribute("orders", orders);
		
		return "orderPage";
			
	}
	/* 找到历史订单 */
	public String findHistory() {
		
		User user = (User) getRequest().getSession().getAttribute("loginedUser");
		List<Order> orders = orderService.findAllByUser(user);
		getRequest().setAttribute("orders", orders);
		return "orderPage";
	}
	
	/* 取消订单 */
	public void cancelOrder() {
		int orderId = Integer.parseInt(getRequest().getParameter("orderId"));
		String res = orderService.changeState(orderId, OrderStateType.取消订单中.toString());
		toWrite(res);
	}
	
	/* 下订单 */
	public void placeOrder() {
		//获取及检验数据
		User user = (User) getRequest().getSession().getAttribute("loginedUser");
		Store store = (Store) getRequest().getSession().getAttribute("visitedStore");
		Double totalPrice = (Double)getRequest().getSession().getAttribute("totalPrice");
		Integer totalPart = (Integer) getRequest().getSession().getAttribute("totalPart");
		String defaultAddress = (String) getRequest().getSession().getAttribute("defaultAddress");
		String addressDetail = (String) getRequest().getParameter("addressDetail");
		String shouhuoren = (String) getRequest().getParameter("shouhuoren");
		String shouhuoTell = (String) getRequest().getParameter("shouhuoTel");
		Address address = null;
		if(addressDetail!=null&&shouhuoren!=null&&shouhuoTell!=null&&!addressDetail.equals("")&&!shouhuoren.equals("")&&!shouhuoTell.equals("")) {
			address = new Address();
			address.setAddress(defaultAddress);
			address.setDetailAddress(addressDetail);
			address.setShouhuoren(shouhuoren);
			address.setShouhuoTell(shouhuoTell);
		}
		LinkedHashMap<Dishes,Integer> temOrderDish = (LinkedHashMap<Dishes,Integer>)getRequest().getSession().getAttribute("temOrderDish");
		if(temOrderDish==null || temOrderDish.size()==0) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "订单为空"));
			return;
		} else if(temOrderDish.size()==1) {
			store = ((Dishes)temOrderDish.keySet().iterator().next()).getStore();
			store = (Store) storeService.get(store.getId());
			
		}
		
		if(store==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "未选择商家"));
			return;
		}
		if(store.getState().equals("close")) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "对不起，商家已打烊"));
			return;
		}
		if(totalPrice<=0||totalPart<=0) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "未购买食品"));
			return;
		}
		
		//判断订单中是否有菜单售罄
		Iterator<Dishes> it = temOrderDish.keySet().iterator();
		it = temOrderDish.keySet().iterator(); 
		Boolean soldOut = false;
		String soldOutDisheses = "";
		while(it.hasNext()) {
			Dishes dishes = (Dishes)it.next();
			if(dishes.getStatus().equals("售罄")) {
				soldOutDisheses += dishes.getName()+" ";
				soldOut = true;
			}
		}
		if(soldOut) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "对不起，你的订单中的"+soldOutDisheses+"已经售罄"));
			return;
		}
		
		String message = getRequest().getParameter("message");
		String paymentType = getRequest().getParameter("payType");
		Integer addressId = null;
		if(getRequest().getParameter("addressId")!=null) {
			addressId =  Integer.parseInt(getRequest().getParameter("addressId"));
			address = (Address)addressManageService.getAddress(addressId);
		} else if(address==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "未选择地址"));
			return;
		}
		if(paymentType==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "对不起，由于未选择支付方式，下单失败"));
			return;
		}
		if(user==null&&!paymentType.equals("off")) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "用户未登录,只能选择货到付款的支付方式"));
			return;
		}
		
		if(paymentType.equals("on")) {//在线付款
			Boolean deDuctSucc = cardManageService.deduct(user, totalPrice.intValue());
			if(!deDuctSucc) {//余额不足
				toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "付款失败，未绑定储蓄卡或余额不足"));
				return;
			}
		} else if((!paymentType.equals("off"))) {//未选择支付状态
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "对不起，由于未选择支付方式，下单失败"));
			return;
		}
		//生成订单
		Order order = orderService.createOrder(user, store, totalPrice, totalPart, temOrderDish, message, address, paymentType);
		//缓存订单
		NoConfirmOrderCache noCOC = (NoConfirmOrderCache) servletContext.getAttribute(store.getId().toString());
		if(noCOC==null) {
			noCOC = new NoConfirmOrderCache();
		}
		noCOC.getNoConfirm().add(order);
		servletContext.setAttribute(store.getId().toString(), noCOC);
		//清空购物车
		clearShoppingCar();
		
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "已下单"));
	}
	
	/**
	 * 得到推荐菜单
	 */
	
	/**
	 * 清空购物车
	 */
	public void clearShoppingCar()
	{
		getRequest().getSession().removeAttribute("totalPrice");
		getRequest().getSession().removeAttribute("totalPart");
		getRequest().getSession().removeAttribute("temOrderDish");
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		this.servletContext = arg0;
		
	}
	
	
}
