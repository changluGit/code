package com.six.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Component;

import com.six.constant.ResultType;
import com.six.entity.Businessman;
import com.six.entity.DishesCategory;
import com.six.entity.OrderEvaluate;
import com.six.entity.Store;
import com.six.service.DishesCategoryService;
import com.six.util.JSONUtil;

@Component
public class DishesCategoryAction extends BaseAction{
	
    private DishesCategoryService categoryService;
	/**
	 * 添加菜品类别
	 * 
	 * @param dishesCategory
	 */
	public void addDishesCategory(DishesCategory dishesCategory) {
		
	}

	/**
	 * 删除某条菜品类别
	 * 
	 * @param dishesCategory
	 * 
	 */
	public void deleteDishes(DishesCategory dishesCategory) {
		
	}

	/**
	 * 修改菜品信息
	 * 
	 * @param dishesCategory
	 * 
	 */
	public void updateDishes(DishesCategory dishesCategory) {
		
	}

	/**
	 * 查找某个店铺的所有菜品类别
	 * 
	 * @return 菜品类别列表 json格式
	 */
	public void getAllDishesCategory() {
		Store store = (Store) getRequest().getSession().getAttribute("loginedStore");
		if(null != store){
			List<DishesCategory> list = categoryService.findAllDishesCategory(store);
			JsonConfig jc = new JsonConfig();
			jc.setExcludes(new String[] { "store", "disheses"});
			JSONArray jsonArray = JSONArray.fromObject(list, jc);
			toWrite(jsonArray.toString());
			System.out.println(jsonArray.toString());
		}
		
		
	}
	
	/* 商家菜品类别主页面 */
	public String gotoDishesCategoryPage() {
		return "dishesCategoryPage";
	}
	
	/* 显示商家菜品类别 */
	public void showDishesCategories() {
		Businessman businessman = (Businessman) getRequest().getSession().getAttribute("loginedBussinessman");
		if(businessman==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "商家未登录"));
			return;
		}
		Store store = businessman.getStore();
		Integer pageNum = Integer.parseInt(getRequest().getParameter("page"));
		Integer pageSize = Integer.parseInt(getRequest().getParameter("rows"));
		List<DishesCategory> dishesCategories = categoryService.findAllDishesCategory(store);
		List<DishesCategory> dc = dishesCategories.subList((pageNum-1)*pageSize, (pageNum*pageSize)<dishesCategories.size()?(pageNum*pageSize):dishesCategories.size());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", dishesCategories.size());
		map.put("rows", dc);
		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[]{"store","disheses"});
		toWrite(JSONObject.fromObject(map,jc).toString());
	}
	
	/* 添加菜品类别 */
	public void addCategory() {
		Businessman businessman = (Businessman) getRequest().getSession().getAttribute("loginedBussinessman");
		if(businessman==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "商家未登录"));
			return;
		}
		Store store = businessman.getStore();
		String name = getRequest().getParameter("name");
		DishesCategory dishesCategory = new DishesCategory();
		if(categoryService.getDishesByNameAndStore(store, name)!=null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "类别名重复"));
			return;
		}
		
		dishesCategory.setName(name);
		dishesCategory.setStore(store);
		categoryService.save(dishesCategory);
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "保存成功"));
	}
	/* 编辑菜品类别 */
	public void editCategory() {
		Businessman businessman = (Businessman) getRequest().getSession().getAttribute("loginedBussinessman");
		if(businessman==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "商家未登录"));
			return;
		}
		Store store = businessman.getStore();
		Integer id = Integer.parseInt(getRequest().getParameter("id"));
		String name = getRequest().getParameter("name");
		String type = categoryService.editCategory(id, store, name);
		if(type.equals("Success")) {
			toWrite(JSONUtil.returnResultJson(type, "更改成功"));
		} else {
			toWrite(JSONUtil.returnResultJson(type, "类报名已存在"));
		}
	}
	
	/* 删除类别 */
	public void removeCategory() {
		Businessman businessman = (Businessman) getRequest().getSession().getAttribute("loginedBussinessman");
		if(businessman==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "商家未登录"));
			return;
		}
		Integer id = Integer.parseInt(getRequest().getParameter("id"));
		categoryService.deleteDishes(categoryService.findById(id));
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "删除成功"));
	}
	

	public DishesCategoryService getCategoryService() {
		return categoryService;
	}

	@Resource
	public void setCategoryService(DishesCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public Object getModel() {
		return null;
	}
  
	
	
}
