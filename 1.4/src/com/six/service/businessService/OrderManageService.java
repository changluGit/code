package com.six.service.businessService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.constant.OrderStateType;
import com.six.dao.BaseDao;
import com.six.dao.OrderManageDao;
import com.six.entity.Businessman;
import com.six.entity.Dishes;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.Store;
import com.six.service.CardManageService;
import com.six.util.DateUtil;
import com.six.util.JSONUtil;


/**
 * 
 * @author 828477
 *
 */
@Component
public class OrderManageService
{
	private OrderManageDao orderManageDao;
	private BaseDao baseDao;
	private CardManageService cardManageService;
	private Logger logger = Logger.getLogger(OrderManageService.class);
	
	
	public Order getOrderById(String id)
	{
		if(StringUtils.isBlank(id))
			return null;
		return (Order)baseDao.get(Order.class, Integer.parseInt(id));
	}
	
	/**
	 * 返回前台展示order控件datagrid 所需要的json数据
	 * @param queryCondition
	 * @param firstResult
	 * @param max
	 * @return
	 */
	public String getDatagridJsonByQuery(Map<String, Object> queryCondition, Integer firstResult, Integer max)
	{
		List<Order> orders = new ArrayList<Order>();
		LinkedHashMap<String, Boolean> linkedHashMap = new LinkedHashMap<String, Boolean>();
		
		Integer totalNumber = 0;
		
		if(queryCondition.get("state") == null)
		{
			orders = (List<Order>)orderManageDao.findByGroupAndCondition(queryCondition);
			
		}
		else
		{
			linkedHashMap.put("createTime", true);
			orders = (List<Order>)orderManageDao.findByPageAndCondition(queryCondition, linkedHashMap);
		}
		
		//处理分页
		totalNumber = orders.size();
		List<Order> pageOrders = null;
		if((firstResult + max + 1) > totalNumber)
			pageOrders = orders.subList(firstResult, orders.size());
		else
			pageOrders = orders.subList(firstResult, firstResult + max);
			
		return getJsonOforders(pageOrders, totalNumber);
	
		
	}
	
	/**
	 * 返回 一个order 集合前台所需要的json 字符串
	 * @param orders
	 * @param totalNumber
	 * @return
	 */
	private String getJsonOforders(List<Order> orders, int totalNumber)
	{
		
		JSONObject jsonObject = new JSONObject();
		if(orders.size() != 0 && null != orders)
			jsonObject.element("total", totalNumber);
		else
			jsonObject.element("total", 0);
		JSONArray jsonArray = new JSONArray();
		
		if(orders.size() != 0 && null != orders)
		{
			for(Order temOrder : orders)
			{
				JSONObject orderJSON = getSingleOrderJSON(temOrder);
				
				jsonArray.add(orderJSON);
			}
		}
		
		
		jsonObject.element("rows", jsonArray);
		return jsonObject.toString();
	}

	/**
	 * 拿到单个 order 的前台json数据
	 * @param temOrder
	 * @return
	 */
	private JSONObject getSingleOrderJSON(Order temOrder)
	{
		JSONObject orderJSON = new JSONObject();
		
		orderJSON.element("id",temOrder.getId());
		orderJSON.element("orderNum", temOrder.getOrderNum());
		orderJSON.element("tel", temOrder.getShouhuoTel());
		if(null != temOrder.getUser())
		{
			orderJSON.element("userName", temOrder.getUser().getName());
		}
		else
			orderJSON.element("userName", "未注册用户购买！");
		
		orderJSON.element("address", temOrder.getAddress());
		
		String cteateTimeStr = DateUtil.formatDateTime(new Date(temOrder.getCreateTime().getTime()));
		orderJSON.element("createTime", cteateTimeStr);
		orderJSON.element("totalAmount", "￥ " + temOrder.getTotalAmount());
		
		//讲状态码翻译成汉字
		Integer stateThis = temOrder.getState();
		String translateState = "";
		if(stateThis == 6)
			translateState = OrderStateType.已下单.toString();
		else if(stateThis == 5)
			translateState = OrderStateType.取消订单中.toString();
		else if(stateThis == 4)
			translateState = OrderStateType.已确认.toString();
		else if(stateThis ==3)
			translateState = OrderStateType.派送中.toString();
		else if(stateThis ==2)
			translateState = OrderStateType.送达.toString();
		else if(stateThis == 1)
			translateState = OrderStateType.已取消.toString();
		
		orderJSON.element("state", translateState);
		orderJSON.element("userMessage", temOrder.getUserMessage());
		return orderJSON;
	}

	/**
	 * 商家改变订单状态 处理器
	 * @param orderId
	 * @param newStateCode
	 * @return
	 */
	public String handleOrderState(Integer orderId, Integer newStateCode)
	{
		Order handlingOrder = (Order)orderManageDao.get(Order.class, orderId);
		
		//判断修改的状态是否非法
		Integer oldStateCode = handlingOrder.getState();
		boolean errorTag = false;
		if(6 == oldStateCode)
		{
			if(newStateCode != 4)
				errorTag = true;
		}	
		else if(5 == oldStateCode)
		{
			if(newStateCode != 1)
				errorTag = true;
		}
		else
		{
			if(oldStateCode - newStateCode != 1)
				errorTag = true;
		}
		
		if(errorTag)
			return JSONUtil.returnResultJson("Failure", "非法修改状态，请确认后修改");
		handlingOrder.setState(newStateCode);
		
		try
		{
			orderManageDao.saveOrUpdate(handlingOrder);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			return JSONUtil.returnResultJson("Failure", "处理失败，请重试");
		}
		
		return JSONUtil.returnResultJson("Success", getSingleOrderJSON(handlingOrder).toString());
	}
	
	/**
	 * 显示订单 菜品的详情
	 * @param orderId
	 * @return
	 */
	public String returnDishDetailHtml(Integer orderId)
	{
		//首先拿到订单
		Order handlingOrder = (Order)orderManageDao.get(Order.class, orderId);
		//拿到该订单中的所有菜品
		List<OrderDishes> orderDishesGroup = (List<OrderDishes>)orderManageDao.findByProperty(OrderDishes.class, "order", handlingOrder);
		
		
		JSONObject jsonObject = new JSONObject();
		if(orderDishesGroup.size() != 0 && null != orderDishesGroup)
			jsonObject.element("total", orderDishesGroup.size());
		else
			jsonObject.element("total", 0);
		JSONArray jsonArray = new JSONArray();
		
		if(orderDishesGroup.size() != 0 && null != orderDishesGroup)
		{
			for(OrderDishes temOrderDishes : orderDishesGroup)
			{
				Dishes temDishes = temOrderDishes.getDishes();
				JSONObject detailObject = new JSONObject();
				detailObject.element("name", temDishes.getName());
				detailObject.element("num", temOrderDishes.getNum());
				detailObject.element("totalPrice", "￥ " + temDishes.getPrice() * temOrderDishes.getNum());
				
				jsonArray.add(detailObject);
			}
		}
		
		
		jsonObject.element("rows", jsonArray);
		return jsonObject.toString();
		
	}
	
	/**
	 * 接受订单
	 * @param orderId
	 * @return
	 */
	public String acceptOrder(Store store, Integer orderId) {
		String handleRes = handleOrderState(orderId, 4);//改变为已确认状态
		Order order = (Order) orderManageDao.get(Order.class, orderId);
		JSONObject jo = JSONObject.fromObject(handleRes);
		if(!jo.get("type").equals("Success")) {
			return handleRes;
		}
		return JSONUtil.returnResultJson("Success", getSingleOrderJSON(order).toString());
	}
	
	/**
	 * 取消订单
	 * @param orderId
	 * @return
	 */
	public String cancelOrder(Store store, Integer orderId) {
		
		Order order = (Order) orderManageDao.get(Order.class, orderId);
		
		String handleRes = handleOrderState(orderId, 1);//改变为已取消状态
		JSONObject jo = JSONObject.fromObject(handleRes);
		if(!jo.get("type").equals("Success")) {
			return handleRes;
		}
		if(order.getPaymentType().equals("on")) {
			Set<OrderDishes> orderDisheses = order.getOrderDisheses();
			Iterator<OrderDishes> it = orderDisheses.iterator();
			Integer sales = 0;//本次订单菜单总价
			while(it.hasNext()) {
				OrderDishes od = it.next();
				sales += od.getNum()*od.getDishes().getPrice();
			}
			cardManageService.recharge(order.getUser(), sales);
		}
		return JSONUtil.returnResultJson("Success", getSingleOrderJSON(order).toString());
	}
	
	/**
	 * 送达，完成订单
	 * @param store
	 * @param orderId
	 * @return
	 */
	public String completeOrder(Store store, Integer orderId) {
		store = (Store) baseDao.get(Store.class, store.getId());
		String handleRes = handleOrderState(orderId, 2);//改变为送达状态
		JSONObject jo = JSONObject.fromObject(handleRes);
		if(!jo.get("type").equals("Success")) {
			return handleRes;
		}
		Order order = (Order) orderManageDao.get(Order.class, orderId);
		
		//更新菜品月销量和商店月销量，销售额
		Set<OrderDishes> orderDisheses = order.getOrderDisheses();
		Iterator<OrderDishes> it = orderDisheses.iterator();
		Integer totalSales = (store.getSaleVolume()==null?0:store.getSaleVolume());
		Integer sales = 0;//本次订单菜单总价
		while(it.hasNext()) {
			OrderDishes orderDishes = (OrderDishes) it.next();
			Dishes dishes = orderDishes.getDishes();
			Integer dishesNum = orderDishes.getNum()==null?0:orderDishes.getNum();
			sales += dishesNum*dishes.getPrice();
			Integer monthSales = (dishes.getMonthSales()==null?0:dishes.getMonthSales());
			monthSales += dishesNum;
			totalSales += dishesNum;
			dishes.setMonthSales(monthSales);
			try {
				baseDao.update(dishes);
			} catch(Exception e) {
				e.printStackTrace();
				return JSONUtil.returnResultJson("Failure", "处理失败，请重试");
			}
		}
		store.setSaleVolume(totalSales);
		Integer preTernover = store.getTernover();
		preTernover = preTernover==null?0:preTernover;
		store.setTernover(preTernover+sales);
		//商家储蓄卡充钱
		if(order.getPaymentType().equals("on")) {
			cardManageService.recharge((Businessman)store.getBusinessmans().iterator().next(), sales);
		}
		//更新订单完成时间
		Timestamp completeTime = new Timestamp(System.currentTimeMillis());
		order.setCompleteTime(completeTime);
		orderManageDao.update(order);
		//更新店送餐速度
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("store", store);
		map.put("state", 2);
		Integer orderNum = orderManageDao.findByProperties(Order.class, map).size();
		Integer preSpeed = store.getFoodDeliveryTime()==null?0:store.getFoodDeliveryTime();
		Integer curSpeed = (int) ((completeTime.getTime()-order.getCreateTime().getTime())/60000);
		Integer aveSpeed = (preSpeed*(orderNum-1)+curSpeed)/orderNum;
		store.setFoodDeliveryTime(aveSpeed);
		
		try {
			baseDao.update(store);
		} catch(Exception e) {
			e.printStackTrace();
			return JSONUtil.returnResultJson("Failure", "处理失败，请重试");
		}
		
		return JSONUtil.returnResultJson("Success", getSingleOrderJSON(order).toString());
	}
	
	public OrderManageDao getOrderManageDao()
	{
		return orderManageDao;
	}
	
	@Resource
	public void setOrderManageDao(OrderManageDao orderManageDao)
	{
		this.orderManageDao = orderManageDao;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public CardManageService getCardManageService() {
		return cardManageService;
	}
	@Resource
	public void setCardManageService(CardManageService cardManageService) {
		this.cardManageService = cardManageService;
	}

	
}
