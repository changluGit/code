package com.six.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.constant.OrderStateType;
import com.six.dao.AddressDao;
import com.six.dao.OrderDao;
import com.six.dao.OrderDishesDao;
import com.six.entity.Address;
import com.six.entity.Dishes;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.util.JSONUtil;

@Component
public class OrderService extends ManageBaseService<Order>
{
	private Logger logger = Logger.getLogger(OrderService.class);
	
	private OrderDao orderDao;
	private OrderDishesDao OrderDishesDao;
	private AddressDao addressDao;
	
	public OrderDao getOrderDao() {
		return orderDao;
	}
	@Resource
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	public OrderDishesDao getOrderDishesDao() {
		return OrderDishesDao;
	}
	@Resource
	public void setOrderDishesDao(OrderDishesDao orderDishesDao) {
		OrderDishesDao = orderDishesDao;
	}
	public AddressDao getAddressDao() {
		return addressDao;
	}
	@Resource
	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}
	/* 查看用户的最新订单 */
	public List<Order> findLatestByUser(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("state", str2IntForState(OrderStateType.派送中.toString()));
		LinkedHashMap<String, Boolean> linkedMap = new LinkedHashMap<String, Boolean>();
		linkedMap.put("createTime", true);
		List<Order> orders = (List<Order>)orderDao.findAllByPropertiesAndSort(Order.class, map, linkedMap);
		map.put("state", str2IntForState(OrderStateType.已下单.toString()));
		orders.addAll((List<Order>)orderDao.findAllByPropertiesAndSort(Order.class, map, linkedMap));
		map.put("state", str2IntForState(OrderStateType.已确认.toString()));
		orders.addAll((List<Order>)orderDao.findAllByPropertiesAndSort(Order.class, map, linkedMap));
		map.put("state", str2IntForState(OrderStateType.取消订单中.toString()));
		orders.addAll((List<Order>)orderDao.findAllByPropertiesAndSort(Order.class, map, linkedMap));
		return orders;
	}
	
	/* 查看用户的订单 */
	public List<Order> findAllByUser(User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		LinkedHashMap<String, Boolean> linkedMap = new LinkedHashMap<String, Boolean>();
		linkedMap.put("createTime", true);
		return (List<Order>)orderDao.findAllByPropertiesAndSort(Order.class, map, linkedMap);
	}
	
	/* 改变订单状态 */
	public String changeState(int orderId, String state) {

		Order order = (Order)orderDao.get(Order.class, orderId);
		Integer oldStateCode = order.getState();
		Integer newStateCode = str2IntForState(state);
		if(5 == newStateCode)
		{
			if(oldStateCode != 4 && oldStateCode != 6)
				return JSONUtil.returnResultJson("Failure", "非法修改状态，无法取消订单");
		}	
		order.setState(newStateCode);
		orderDao.update(order);
		return JSONUtil.returnResultJson("Success", "已向商家申请取消订单");
	}
	/* 生成订单 */
	public Order createOrder(User user, Store store, Double totalPrice, 
			Integer totalPart, LinkedHashMap<Dishes,Integer> temOrderDish, 
			String message, Address address, String paymentType) {
		
		Date date = new Date();
		Order order = new Order();
		order.setUser(user);
		order.setCreateTime(new Timestamp(date.getTime()));
		order.setState(str2IntForState(OrderStateType.已下单.toString()));
		Integer userId = (user==null?0:user.getId());
		order.setOrderNum(createOrderNum(date,userId));
		order.setStore(store);
		order.setTotalAmount(totalPrice);
		order.setAddress(address.getAddress()+address.getDetailAddress());
		order.setShouhuoren(address.getShouhuoren());
		order.setShouhuoTel(address.getShouhuoTell());
		order.setUserMessage(message);
		order.setPaymentType(paymentType);
		orderDao.save(order);
		Iterator<Dishes> it = temOrderDish.keySet().iterator();
		it = temOrderDish.keySet().iterator(); 
		while(it.hasNext()) {
			Dishes dishes = (Dishes)it.next();
			int num = temOrderDish.get(dishes);
			OrderDishes orderDishes = new OrderDishes();
			orderDishes.setDishes(dishes);
			orderDishes.setNum(num);
			orderDishes.setOrder(order);
			OrderDishesDao.save(orderDishes);
		}
		return order;
	}
	/* 生成订单号 */
	public String createOrderNum(Date date, int userId) {
		return date.getYear()+""+date.getMonth()+date.getDay()+
				date.getHours()+date.getMinutes()+date.getMinutes()+
				userId+(int)(Math.random()*1000);
	}
	/* 转换state类型 */
	public Integer str2IntForState(String stateStr) {
		if(stateStr.equals(OrderStateType.已下单.toString()))
			return 6;
		else if(stateStr.equals(OrderStateType.取消订单中.toString()))
			return 5;
		else if(stateStr.equals(OrderStateType.已确认.toString()))
			return 4;
		else if(stateStr.equals(OrderStateType.派送中.toString()))
			return 3;
		else if(stateStr.equals(OrderStateType.送达.toString()))
			return 2;
		else if(stateStr.equals(OrderStateType.已取消.toString()))
			return 1;
		else
			return 0;
	}
}
