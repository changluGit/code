package com.six.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.dao.OrderDishesDao;
import com.six.entity.Order;

@Component
public class OrderDishesService extends ManageBaseService<Order>
{
	private Logger logger = Logger.getLogger(OrderDishesService.class);
	
	private OrderDishesDao orderDishesDao;
	
	public OrderDishesDao getOrderDishesDao() {
		return orderDishesDao;
	}
	@Resource
	public void setOrderDishesDao(OrderDishesDao orderDishesDao) {
		this.orderDishesDao = orderDishesDao;
	}

}
