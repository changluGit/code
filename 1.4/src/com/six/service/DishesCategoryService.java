package com.six.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.DishesCategory;
import com.six.entity.Store;

@Component
public class DishesCategoryService {
	private BaseDao baseDao;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 添加菜品类别
	 * 
	 * @param dishesCategory
	 */
	public void addDishesCategory(DishesCategory dishesCategory) {
		baseDao.save(dishesCategory);
	}

	/**
	 * 删除某条菜品类别
	 * 
	 * @param dishesCategory
	 * 
	 */
	public void deleteDishes(DishesCategory dishesCategory) {
		baseDao.delete(dishesCategory);
	}

	/**
	 * 修改菜品信息
	 * 
	 * @param dishesCategory
	 * 
	 */
	public void updateDishes(DishesCategory dishesCategory) {
		baseDao.update(dishesCategory);
	}

	/**
	 * 查找某个店铺的所有菜品类别
	 * 
	 * @param store
	 * @return List 菜品类别列表
	 */
	public List<DishesCategory> findAllDishesCategory(Store store) {
		return baseDao.findByProperty(DishesCategory.class, "store", store);
	}

	/**
	 * 根据商店和菜品类别名,查询一条菜品类别信息，若无，返回null
	 * 
	 * @return DishesCategory
	 */
	public DishesCategory getDishesByNameAndStore(Store store,String name) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("store", store);
		map.put("name", name);
		DishesCategory dishesCategory = (DishesCategory) baseDao
				.findByPropertiesUnique(DishesCategory.class, map);
		if (null != dishesCategory) {
			return dishesCategory;
		} else {
			return null;
		}
	}
	

	/* 保存 */
	public void save(DishesCategory dishesCategory) {
		baseDao.save(dishesCategory);
	}
	
	/* 编辑类别 */
	public String editCategory(Integer id, Store store, String name) {
		DishesCategory dishesCategory = (DishesCategory) baseDao.get(DishesCategory.class, id);
		DishesCategory tmp = (DishesCategory) baseDao.findByPropertyUnique(DishesCategory.class, "name", name);
		if(tmp==null||id.equals(tmp.getId())) {
			dishesCategory.setName(name);
			baseDao.update(dishesCategory);
			return "Success";
		} else {
			return "Failure";
		}
	}
	
	/* 通过ID寻找 */
	public DishesCategory findById(Integer id) {
		return (DishesCategory)baseDao.get(DishesCategory.class, id);
	}

}
