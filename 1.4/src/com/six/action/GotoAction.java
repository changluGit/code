package com.six.action;

import java.util.ArrayList;

import javax.annotation.Resource;

import com.six.entity.Address;
import com.six.entity.Dishes;
import com.six.entity.User;
import com.six.service.AddressManageService;
import com.six.service.store.DishesService;

public class GotoAction extends BaseAction
{

	private AddressManageService addressManageService;
	private DishesService dishesService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 跳转到home页面
	 * @return
	 */
	public String home()
	{
		//首先判断用户是否登录
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		if(null != loginedUser)
		{
			//将默认地址写到session中
			Address defaultAddress = addressManageService.getDefaultAddress(loginedUser);
			
			getRequest().getSession().setAttribute("defaultAddress", defaultAddress.getAddress());
			
		}
		//没有登录
		else
		{
			if(null == getRequest().getSession().getAttribute("defaultAddress"))
				return "unRegisterIndex"; //跳转到 地址输入界面
		}
		getRequest().setAttribute("canSwitchAddress", "can");
		return "home";
	}

	/**
	 * 处理默认地址
	 */
	public void noLoginAddressHandle()
	{
		String addressDefault = getRequest().getParameter("addressDefault");
		getRequest().getSession().setAttribute("defaultAddress", addressDefault);
	}

	public AddressManageService getAddressManageService()
	{
		return addressManageService;
	}


	@Resource
	public void setAddressManageService(AddressManageService addressManageService)
	{
		this.addressManageService = addressManageService;
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
	
	
	
}
