package com.six.action.businessman;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.six.action.BaseAction;
import com.six.action.ComplaintAction;
import com.six.entity.Businessman;
import com.six.entity.PetCard;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.BusinessInfoServie;
import com.six.service.UserInfoService;

@Component
public class BusinessInfoAction extends BaseAction{
	
	private Logger logger = Logger.getLogger(ComplaintAction.class);
	
	private BusinessInfoServie businessInfoServie;
	private Businessman businessman;
	private Store store;
	private PetCard petcard;

	@Override
	public Object getModel()
	{
		if(null == businessman)
			businessman = new Businessman();
		return businessman;
	}
	//按照登录商家查找
	public String showBusinessInfo() throws Exception
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		businessman= (Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		
		if(null!=businessman.getStore())
		{
			store=businessman.getStore();
			request.setAttribute("store", store);
		}
		if(null!=businessman.getPetCard())
		{
			petcard=businessman.getPetCard();
			request.setAttribute("card", petcard);
		}
		request.setAttribute("businessman", businessman);
		return "info";

	}
	public String businessmodify() throws Exception
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		businessman= (Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		request.setAttribute("businessman", businessman);
		return "info";

	}
	
	public BusinessInfoServie getBusinessInfoServie() {
		return businessInfoServie;
	}
	@Resource
	public void setBusinessInfoServie(BusinessInfoServie businessInfoServie) {
		this.businessInfoServie = businessInfoServie;
	}

}
