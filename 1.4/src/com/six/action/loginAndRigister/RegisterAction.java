package com.six.action.loginAndRigister;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.User;
import com.six.service.AddressManageService;
import com.six.service.CardManageService;
import com.six.service.logAndRigService.RegisterService;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;

@Component
public class RegisterAction extends BaseAction
{
	private User user;
	private RegisterService registerService;
	private CardManageService cardManageService;
	private AddressManageService addressManageService;
	@Override
	public Object getModel()
	{
		if(null == user)
			user = new User();
		
		return user;
	}
	
	/**
	 * 处理用户注册
	 */
	public void userRegister()
	{
		//验证输入信息是否有误
		if(!validateInput())
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "您输入的注册信息有误，请重新输入！"));
			return;
		}
		//判断邮箱是否被注册
		if(registerService.isEmailRegistered(user.getEmail()))
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "您输入的邮箱已被注册，请确定后输入！"));
			return;
		}
		//保存注册信息
		String result = registerService.register(user);
		//初始化 用户的卡信息
		cardManageService.createPetCard(user);
		//初始化 用户的地址
		String name = getRequest().getParameter("name");
		String tel = getRequest().getParameter("tel");
		String address = getRequest().getParameter("address");
		String addressDetail = getRequest().getParameter("addressDetail");
		addressManageService.addAddress(user, name, tel, address,addressDetail, true);
		
		toWrite(result);
		
	}
	
	private boolean validateInput()
	{
		boolean tag = true;
		
		if(StringUtils.isBlank(user.getEmail()))
			tag = false;
		if(StringUtils.isBlank(user.getName()))
			tag = false;
		if(StringUtils.isBlank(user.getPwd() ))
			tag = false;
		if(!StringUtil.emailFormat(user.getEmail()))
			tag = false;
		
		return tag;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public RegisterService getRegisterService()
	{
		return registerService;
	}
	@Resource
	public void setRegisterService(RegisterService registerService)
	{
		this.registerService = registerService;
	}

	public CardManageService getCardManageService()
	{
		return cardManageService;
	}

	@Resource
	public void setCardManageService(CardManageService cardManageService)
	{
		this.cardManageService = cardManageService;
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
	
	
	
}
