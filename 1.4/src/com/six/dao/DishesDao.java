package com.six.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.six.entity.Dishes;
import com.six.entity.DishesCategory;
import com.six.entity.Store;

@Component
public class DishesDao extends BaseDao
{
	public List<DishesCategory> getCategoryByStore(Store store)
	{
		
		return (List<DishesCategory>)findByProperty(DishesCategory.class, "store", store);
	}
	
	public List<Dishes> findByAddressAndStoreStateAndDishesStatus(String address, String storeState, String dishesStatus) {
		String hql = "from Dishes d where d.store.businessAddress = ? and  d.store.state = ? and d.status = ?";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter(0, address);
		query.setParameter(1, storeState);
		query.setParameter(2, dishesStatus);
		return (List<Dishes>)query.list();
	}
	
}
