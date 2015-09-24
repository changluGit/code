package com.six.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Component;

import com.six.entity.Order;

@Component
public class OrderDao extends BaseDao<Serializable>
{
	public List<Order> findByPageSort(int pageNum, int pageSize, LinkedHashMap<String, Boolean> linkedMap) {
		Criteria criteria = getCurrentSession().createCriteria(Order.class);
		Iterator<String> iterator;

		for (iterator = (Iterator<String>) linkedMap.keySet().iterator(); iterator
				.hasNext();)
		{
			String key = iterator.next();
			Boolean isAsc = linkedMap.get(key);

			if (isAsc)
				criteria.addOrder(org.hibernate.criterion.Order.asc(key));
			else
				criteria.addOrder(org.hibernate.criterion.Order.desc(key));
		}
		int firstResult = (pageNum-1)*pageSize;
		criteria.setFirstResult(firstResult).setMaxResults(firstResult+pageSize-1);
		return criteria.list();
	}
}
