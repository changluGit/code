package com.six.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.CollectDao;
import com.six.entity.Collection;
import com.six.entity.Dishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.util.JSONUtil;

@Component
public class CollectStoreAndVegService {
	private CollectDao collectdao;
	private Collection collect;

	public Collection getCollect() {
		return collect;
	}

	public void setCollect(Collection collect) {
		this.collect = collect;
	}

	public CollectDao getCollectdao() {
		return collectdao;
	}

	@Resource
	public void setCollectdao(CollectDao collectdao) {
		this.collectdao = collectdao;
	}

	/* 收藏餐厅，保存信息 */
	public void addCollect(Collection collect) {

		this.collectdao.save(collect);
	}

	/**
	 * 根据id 得到一个collect
	 * 
	 * @param storeIdStr
	 * @return
	 */
	/* 根据店铺，查找此店铺是否已收藏 */
	@SuppressWarnings("unchecked")
	public Collection getCollectByStore(Store store) {

		if (collectdao.findByProperty(Collection.class, "store", store).size() == 0) {
			return null;
		} else {
			return (Collection) collectdao.findByProperty(Collection.class,
					"store", store).get(0);
		}
	}

	/* 根据菜品，查找此菜品是否已收藏 */
	@SuppressWarnings("unchecked")
	public Collection getCollectByDishes(Dishes dishes) {

		if (collectdao.findByProperty(Collection.class, "dishes", dishes)
				.size() == 0) {
			return null;
		} else {
			return (Collection) collectdao.findByProperty(Collection.class,
					"dishes", dishes).get(0);
		}
	}

	/* 取消收藏，删除信息 */
	public void deleteCollect(Collection collect) {

		this.collectdao.delete(collect);
	}

	/* 查找收藏的所有记录 */
	@SuppressWarnings({ "unchecked" })
	public List<Collection> findAllCollect(User user) {
		return collectdao.findByProperty(Collection.class, "user", user);
		

	}

	
	/* 删除收藏的店铺的记录，店铺收藏量同时减1 */
	@SuppressWarnings("unchecked")
	public void deleteCollectStore(Integer id) {
		
		Collection collect = (Collection) collectdao.findByPropertyUnique(
				Collection.class, "id", id);
		
		Store store1=(Store) collectdao.findByPropertyUnique(
				Store.class, "id", collect.getStore().getId());
		store1.setCollection(store1.getCollection()-1);
		collectdao.save(store1);
		
		this.collectdao.delete(collect);
		
	}

	/* 删除收藏的菜品的记录 */
	@SuppressWarnings("unchecked")
	public void deleteCollectVeg1(Integer id) 
	{
		
		Collection collect = (Collection) collectdao.findByPropertyUnique(
				Collection.class, "id", id);
		
		this.collectdao.delete(collect);
		
	
	}

	/* 判断是否收藏店铺、菜品 ，没有收藏则添加*/
	@SuppressWarnings({ "unchecked" })
	public String toggleCollectionHandle(User user, Store store, Dishes dishes)
	{	System.out.println("345e5er545555");
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(null == dishes&&store!=null)
		{
			map.put("user", user);
			map.put("store", store);
			System.out.print(45555);
		}
		if(null != dishes&&store==null)
		{
			map.put("user", user);
			map.put("dishes", dishes);
			System.out.print(4444);
			
		}
		
		Collection temCollection = (Collection)collectdao.findByPropertiesUnique(Collection.class, map);
	   
		if(null == temCollection)
		{
			temCollection = new Collection();
			temCollection.setDishes(dishes);
			temCollection.setStore(store);
			temCollection.setUser(user);
			collectdao.save(temCollection);
			if(null == dishes&&store!=null)
			{
			List<Collection> collects=collectdao.findByProperty(Collection.class, "store", store);
			Store store1=(Store) collectdao.findByPropertyUnique(
					Store.class, "id", store.getId());
			store1.setCollection(collects.size());
			collectdao.save(store1);
			}
			
			return JSONUtil.returnResultJson("Success", "取消收藏");
		}
		else
		{
			collectdao.delete(temCollection);
			if(null == dishes&&store!=null)
			{
			Store store1=(Store) collectdao.findByPropertyUnique(
					Store.class, "id", temCollection.getStore().getId());
			store1.setCollection(store1.getCollection()-1);
			collectdao.save(store1);
			}
			return JSONUtil.returnResultJson("Success", "收藏");
		}
	}
	//根据用户和菜品，查找该用户所收藏的菜品
	@SuppressWarnings("unchecked")
	public Collection getCollectByDishesAndUser(User user,Dishes dishes) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("dishes", dishes);
		Collection collection = (Collection)collectdao.findByPropertiesUnique(Collection.class, map);
		
			return collection;
		
	}
	//根据用户和店铺查找该用户收藏的店铺
	@SuppressWarnings("unchecked")
	public Collection getCollectionByUserAndStore(User user,Store store) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("store", store);
		Collection collection = (Collection)collectdao.findByPropertiesUnique(Collection.class, map);
		
			return collection;
		
	}
}
