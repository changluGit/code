package com.six.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.User;

@Component
public class OrderDishesDao extends BaseDao<Serializable>
{
	public List<OrderDishes> findByUserAndAddressAndStoreStateAndDishesStatus(User user, String address, String storeState, String dishesStatus) {
		String hql = "from OrderDishes od where od.order.user = ? and od.order.store.businessAddress = ? and od.order.store.state = ? and od.dishes.status = ?";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter(0, user);
		query.setParameter(1, address);
		query.setParameter(2, storeState);
		query.setParameter(3, dishesStatus);
		
		return (List<OrderDishes>)query.list();
	}
}
