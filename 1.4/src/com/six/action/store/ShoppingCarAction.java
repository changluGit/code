package com.six.action.store;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.action.BaseAction;
import com.six.entity.Address;
import com.six.entity.Dishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.store.DishesService;
import com.six.service.store.ShoppingCarService;
import com.six.util.JSONUtil;

@Component
public class ShoppingCarAction extends BaseAction
{
	private DishesService dishesService;
	private ShoppingCarService shoppingCarService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 跳转到订单确认页面
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void gotoOrderConfirm() throws ServletException, IOException
	{
		
		LinkedHashMap<Dishes, Integer> dishObjectMap = new LinkedHashMap<Dishes, Integer>();
		Map<Integer, Integer> temOrderDish = (LinkedHashMap<Integer, Integer>)getRequest().getSession().getAttribute("temOrderDish");
		
		if(null == temOrderDish || temOrderDish.size() == 0)
			return;
		
		
		
		getRequest().getRequestDispatcher("/WEB-INF/jsp/orderConfirm.jsp").forward(getRequest(), getResponse());
	}
	
	public void oneKeyOrder()
	{
		//首先拿到推荐的菜品
		
	}
	
	/**
	 * 购物车中菜单的加减操作
	 */
	public void subAndAddDishHandle()
	{
		String dishIdStr = getRequest().getParameter("dishId");
		String type = getRequest().getParameter("type");
		
		if(StringUtils.isBlank(dishIdStr))
			return;
		//根据id,从数据库中拿到 菜品
		Integer dishId = Integer.parseInt(dishIdStr);
		Dishes temDish = dishesService.getDishesByid(dishId);
		
		HttpSession session = getRequest().getSession();
		
		//更新temOrderDish
		LinkedHashMap<Dishes, Integer> temOrderDish = null;
		
		//首先判断是否有 temOrderDish 没有就新建
		if(null == (temOrderDish = (LinkedHashMap<Dishes, Integer>)session.getAttribute("temOrderDish")))
		{
			temOrderDish = new LinkedHashMap<Dishes, Integer>();
			session.setAttribute("temOrderDish", temOrderDish);
		}
		
		//遍历 该temOrderDish， 看是否有存在的菜品
		Set<Entry<Dishes, Integer>> entrySet = temOrderDish.entrySet();
		Dishes editedDish = null;
		Integer part = 0; //该菜品的份数
		for(Entry<Dishes, Integer> entry : entrySet)
		{
			if(entry.getKey().getId() == dishId)
				editedDish = entry.getKey();
		}
		if(editedDish == null)
		{
			if("sub".equals(type))
			{
				toWrite(JSONUtil.returnResultJson("Failure", "非法操作！"));
				return;
			}
			temOrderDish.put(temDish, 1);
		}
		else
		{
			if("sub".equals(type))
			{
				if(1 == temOrderDish.get(editedDish))
				{
					temOrderDish.remove(editedDish);
					part = 0;
				}
					
				else
				{
					part = temOrderDish.get(editedDish) - 1;
					temOrderDish.put(editedDish, part);
				}
					
			}
			else
			{
				part = temOrderDish.get(editedDish) + 1;
				temOrderDish.put(editedDish, part);
			}
				
		}
			
		
		Double totalPrice = 0.0;
		Integer totalPart = 0;
		//讲总价放到session中
		if(null == (totalPrice = (Double)session.getAttribute("totalPrice")))
		{
			totalPrice = temDish.getPrice() + 0.0;
			session.setAttribute("totalPrice", totalPrice);
		}
		else
		{
			if("sub".equals(type))
			{
				totalPrice -= temDish.getPrice();
				session.setAttribute("totalPrice", totalPrice  + 0.0);
			}
			else
			{
				totalPrice += temDish.getPrice();
				session.setAttribute("totalPrice", totalPrice+ 0.0);
			}
				
		}
			
		
		//讲总份数放到sesion中
		if(null == (totalPart = (Integer)session.getAttribute("totalPart")))
		{
			
			session.setAttribute("totalPart", 1);
		}
		else
		{
			if("sub".equals(type))
			{
				totalPart = totalPart - 1;
				session.setAttribute("totalPart", totalPart);
			}
			else
			{
				totalPart = totalPart + 1;
				session.setAttribute("totalPart", totalPart );
			}
				
		}
		
		if(StringUtils.isNotBlank(type))
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.element("part", part);
			jsonObject.element("price", editedDish.getPrice() * part);
			jsonObject.element("totalPart", totalPart);
			jsonObject.element("totalPrice", totalPrice);
			
			toWrite(JSONUtil.returnResultJson("Success", jsonObject.toString()));
		}
	}
	
	/**
	 * 处理一键下单
	 * @throws Exception 
	 * @throws ServletException 
	 */
	public void onekeyOrder() throws ServletException, Exception
	{
		String dishIdStr = getRequest().getParameter("dishId");
		
		if(StringUtils.isBlank(dishIdStr))
			return;
		//根据id,从数据库中拿到 菜品
		Integer dishId = Integer.parseInt(dishIdStr);
		Dishes temDish = dishesService.getDishesByid(dishId);
		
		HttpSession session = getRequest().getSession();
		
		//拿到商店 然后保存到 session 中
		Store visitedStore = shoppingCarService.getStoreById(temDish.getStore().getId());
		session.setAttribute("visitedStore", visitedStore);
		
		//更新temOrderDish
		LinkedHashMap<Dishes, Integer> temOrderDish = null;
		
		//清空购物车
		clearShoppingCar();
		
		temOrderDish = new LinkedHashMap<Dishes, Integer>();
		session.setAttribute("temOrderDish", temOrderDish);
		
		temOrderDish.put(temDish, 1);
		session.setAttribute("totalPart", 1);
		session.setAttribute("totalPrice", temDish.getPrice() + 0.0);
		
		toWrite(JSONUtil.returnResultJson("Success", "成功了！"));
	}
	
	/**
	 * 订单确认页面中，一键更换菜品
	 */
	public void changeDishForOnekey()
	{
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		String addressDefault = (String)getRequest().getSession().getAttribute("defaultAddress");
		
		Dishes dishesNew = dishesService.getRandomDishes(loginedUser, addressDefault);
		
		
		HttpSession session = getRequest().getSession();
		
		//拿到正在访问的商店
		Store visitedStore = shoppingCarService.getStoreById(dishesNew.getStore().getId());
		session.setAttribute("visitedStore", visitedStore);
		
		//更新temOrderDish
		LinkedHashMap<Dishes, Integer> temOrderDish = null;
				
		//清空购物车
		clearShoppingCar();
				
		temOrderDish = new LinkedHashMap<Dishes, Integer>();
		session.setAttribute("temOrderDish", temOrderDish);
				
		temOrderDish.put(dishesNew, 1);
		session.setAttribute("totalPart", 1);
		session.setAttribute("totalPrice", dishesNew.getPrice() + 0.0);
				
		toWrite(JSONUtil.returnResultJson("Success", "成功了！"));
	}
	
	/**
	 * 清空购物车
	 */
	public void clearShoppingCar()
	{
		getRequest().getSession().removeAttribute("totalPrice");
		getRequest().getSession().removeAttribute("totalPart");
		getRequest().getSession().removeAttribute("temOrderDish");
	}
	
	/**
	 * 返回购物车总 菜品列单
	 */
	public void getShoppingCarDishes()
	{
		HttpSession session = getRequest().getSession();
		LinkedHashMap<Dishes, Integer> temOrderDish = (LinkedHashMap<Dishes, Integer>)session.getAttribute("temOrderDish");
		if(temOrderDish == null || temOrderDish.size() == 0)
			toWrite("");
		else
		 toWrite(shoppingCarService.getShoppingCarHtml(temOrderDish));
		
	}
	/**
	 * 返回 购物车总份数和总价格
	 */
	public void getShoppingCarPriAndPart()
	{
		HttpSession session = getRequest().getSession();
		Double totalPrice = 0.0;
		Integer totalPart = 0;
		if(null == (totalPart = (Integer)session.getAttribute("totalPart")))
			totalPart = 0;
		if(null == (totalPrice = (Double)session.getAttribute("totalPrice")))
			totalPrice = 0.0;
		toWrite("共<font id='totalPart'>"+ totalPart+"</font>份，总共<font id='totalPrice'>"+ totalPrice+"元</font>");
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

	public ShoppingCarService getShoppingCarService()
	{
		return shoppingCarService;
	}
	@Resource
	public void setShoppingCarService(ShoppingCarService shoppingCarService)
	{
		this.shoppingCarService = shoppingCarService;
	}
	
	
	
}
