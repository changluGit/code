package com.six.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.six.entity.Order;

@Component
public class OrderManageDao extends BaseDao
{
	
	/**
	 * 根据 属性和排序 完成分页的查询
	 * @param map
	 * @param linkedMap
	 * @param firstReult
	 * @param max
	 * @return
	 */
	public List<Order> findByPageAndCondition(Map<String, Object> map, LinkedHashMap<String, Boolean> linkedMap)
	{
		Criteria criteria = getCurrentSession().createCriteria(Order.class);
		
		if(null != map)
			criteria.add(Restrictions.allEq(map));
		if(null != linkedMap)
		{
			Iterator<String> iterator;

			for (iterator = (Iterator<String>) linkedMap.keySet().iterator(); iterator
					.hasNext();)
			{
				String key = iterator.next();
				Boolean isAsc = linkedMap.get(key);

				if (!isAsc)
					criteria.addOrder(org.hibernate.criterion.Order.asc(key));
				else
					criteria.addOrder(org.hibernate.criterion.Order.desc(key));
			}

		}
		
		
		return criteria.list();
	}
	
	/**
	 * 根据给出的属性 和 分页然后按照 创建时间和state排序 和state分租查询 order
	 * @param map
	 * @param firstResult
	 * @param max
	 * @return
	 */
	public List<Order> findByGroupAndCondition(Map<String, Object> map)
	{
		Criteria criteria = getCurrentSession().createCriteria(Order.class);
		
		if(null != map)
			criteria.add(Restrictions.allEq(map));
		
		criteria.addOrder(org.hibernate.criterion.Order.desc("state"));
		criteria.addOrder(org.hibernate.criterion.Order.desc("createTime"));
		
		
		return criteria.list();
	}
}
