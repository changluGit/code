package com.six.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.six.dao.BaseDao;
import com.six.entity.Dishes;
import com.six.entity.Store;

@Component
public class DishesManageService {
	private BaseDao baseDao;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 添加菜品
	 * 
	 * @param dishes
	 */
	public void addDishes(Dishes dishes) {
		if (dishes != null) {
			baseDao.save(dishes);
		}
	}

	/**
	 * 删除某条菜品
	 * 
	 * @param dishes
	 * 
	 */
	public void deleteDishes(Dishes dishes) {
		if (dishes != null) {
			baseDao.delete(dishes);
		}
	}

	/**
	 * 修改菜品信息
	 * 
	 * @param dishes
	 * 
	 */
	public void updateDishes(Dishes dishes) {
		if (dishes != null) {
			baseDao.update(dishes);
		}

	}

	/**
	 * 查找某个店铺的所有菜品
	 * 
	 * @param store
	 * @return List 菜品列表
	 */
	public List<Dishes> findAllDishes(Store store) {
		if (store == null) {
			return null;
		} else {
			return baseDao.findByProperty(Dishes.class, "store", store);
		}

	}
	
	
	/**
	 * 根据菜品名和状态进行查询菜品信息，若无，返回null
	 * 
	 * @param store,name,status
	 * @return Dishes
	 */
	public List getDishesByNameAndStatus(Store store,String name,String status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("store", store);
		map.put("name", name+"%");
		map.put("status", status);
		//Dishes dishes = (Dishes) baseDao.findByPropertiesUnique(Dishes.class, map);
//		if (null != dishes) {
//			return dishes;
//		} else {
//			return null;
//		}
		List<Dishes> list = baseDao.fuzzySearchByProperties(Dishes.class, map,0,5);
		if (null != list) {
			return list;
		} else {
			return null;
		}
		
	}
	
	
	/**
	 * 根据菜品名进行查询菜品信息，若无，返回null
	 * 
	 * @param store,name
	 * @return Dishes
	 */
	public Dishes getDishesByName(Store store,String name) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("store", store);
		map.put("name", name);
		Dishes dishes = (Dishes) baseDao.findByPropertiesUnique(Dishes.class, map);
		if (null != dishes) {
			return dishes;
		} else {
			return null;
		}
	}

	/**
	 * 根据菜品名进行模糊查询菜品信息，若无，返回null
	 * 
	 * @param store,name,maxResult
	 * @return dishesList,
	 */
	public List getDishesByfuzzy(Store store,String name,int maxResult) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("store", store);
		map.put("name", name+"%");
		List<Dishes> dishesList = baseDao.fuzzySearchByProperties(Dishes.class, map, 0, maxResult);
		if (null != dishesList && dishesList.size() != 0) {
			return dishesList;
		} else {
			return null;
		}
	}

	/**
	 * 根据id,查询一条菜品信息，若无，返回null
	 * 
	 * @param id
	 * @return dishes
	 */
	public Dishes getDishesById(int id) {
		Dishes dishes = (Dishes) baseDao.get(Dishes.class, id);
		if (null != dishes) {
			return dishes;
		} else {
			return null;
		}
	}

	/**
	 * 根据状态查询菜品信息
	 * 
	 * @return dishesList
	 */
	public List<Dishes> getDishesByStatus(Store store,String status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("store", store);
		map.put("status", status);
		List<Dishes> dishesList = baseDao.findByProperties(Dishes.class, map);
		return dishesList;
	}

}
