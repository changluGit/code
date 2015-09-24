package com.six.action.store;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.action.BaseAction;
import com.six.entity.Dishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.store.DishesService;
import com.six.util.JSONUtil;

@Component
public class DishesAction extends BaseAction
{
	private DishesService dishesService;
	
	private String category;
	private String sort;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 返回菜单种类
	 */
	public void getDishCategory()
	{
		
		Store visitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		
		String result = dishesService.getDishCategory(visitedStore);
		toWrite(result);
	}

	/**
	 * 根据菜单种类和检索条件返回dish
	 */
	public void showDishByCategory()
	{
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		Store visitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		String html = dishesService.getShowDishHtml(visitedStore, category, sort, loginedUser);
		
		toWrite(html);
	}

	/**
	 * 菜品的search
	 */
	public void search()
	{
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		String inputContext = getRequest().getParameter("inputContext");
		
		if(StringUtils.isBlank(inputContext))
			return;
		
		Store visitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		toWrite(dishesService.getSearchDishResult(inputContext, visitedStore, loginedUser));
	}
	
	
	public void test()
	{
		toWrite(JSONUtil.returnResultJson("Failure", "testFailure"));
	}
	public DishesService getDishesService()
	{
		return dishesService;
	}

	@Resource
	public void setDishesService(DishesService dishesService)
	{
		this.dishesService = dishesService;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}
	
	
}
