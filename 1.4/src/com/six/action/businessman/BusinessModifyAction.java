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
public class BusinessModifyAction extends BaseAction{
	
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
		return "input";
	}
	//修改用户名
	public String businessNamemodify() throws Exception
	{
		String userName=getRequest().getParameter("userName");
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
		if(null!=userName && (userName.length()<16 && userName.length()>3)){
			businessman.setUsername(userName);
			businessInfoServie.updateBusinessman(businessman);
			request.setAttribute("tishi", "恭喜更改用户名成功！");
		}
		else{
			request.setAttribute("tishi", "用户名不能小于3位或超过16位！");
		}
		request.setAttribute("businessman", businessman);
		return "name";

	}
	
	//修改email
		public String businessEmailmodify() throws Exception
		{
			String email=getRequest().getParameter("myemail");
			Businessman b=businessInfoServie.findBusinessman("email",email);
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
			if(null!=email && null == b){
				businessman.setEmail(email);
				businessInfoServie.updateBusinessman(businessman);
				request.setAttribute("tishi", "恭喜修改email成功！");
			}
			else{
				request.setAttribute("tishi", "请填写正确的邮箱格式或邮箱已被占用");
			}
			request.setAttribute("businessman", businessman);
			return "email";

		}
		//修改身份证号码
		public String businessIdcardmodify() throws Exception
		{
			String idcard=getRequest().getParameter("idCard");
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
			if(null!=idcard && idcard.length()==18){
				businessman.setIdCard(idcard);
				businessInfoServie.updateBusinessman(businessman);
				request.setAttribute("tishi", "恭喜修改身份证号成功！");
			}
			else{
				request.setAttribute("tishi", "请填写正确的身份证号");
			}
			request.setAttribute("businessman", businessman);
			return "idcard";

		}
		//修改手机号码
		public String businessTelmodify() throws Exception
		{
			String tel=getRequest().getParameter("tel");
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
			if(null!=tel && tel.length()==11){
				businessman.setTell(tel);
				businessInfoServie.updateBusinessman(businessman);
				request.setAttribute("tishi", "恭喜修改电话号成功！");
			}
			else{
				request.setAttribute("tishi", "请填写正确的电话号");
			}
			request.setAttribute("businessman", businessman);
			return "tel";

		}
		//修改店铺
		public String businessStoremodify() throws Exception
		{
			String storeName=getRequest().getParameter("storeName");
			String oldstoreName=getRequest().getParameter("oldstoreName");
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

			if(null!=storeName){
				store.setStoreName(storeName);
				businessman.setStore(store);
				businessInfoServie.updateBusinessman(businessman);
				request.setAttribute("tishi", "恭喜修改店铺名成功！");
			}
			else{
				request.setAttribute("tishi", "请填写正确的店铺名称(长度6~16位)");
			}
			request.setAttribute("businessman", businessman);
			return "store";

		}
		
		//修改店铺
		public String businessPassmodify() throws Exception
		{
			String oldpwd=getRequest().getParameter("pwdold");
			String pwd1=getRequest().getParameter("pwd1");
			String pwd2=getRequest().getParameter("pwd2");
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

			if(businessman.getPassword().equals(oldpwd)){
				if(pwd1.equals(pwd2))
				{
					businessman.setPassword(pwd2);
					businessInfoServie.updateBusinessman(businessman);
					request.setAttribute("tishi", "恭喜修改密码成功！");
				}
				else{
					request.setAttribute("tishi", "两次输入的新密码不相同");
				}
			}
			else{
				request.setAttribute("tishi", "旧密码输入不正确");
			}
			request.setAttribute("businessman", businessman);
			return "pwd";

		}
	public BusinessInfoServie getBusinessInfoServie() {
		return businessInfoServie;
	}
	@Resource
	public void setBusinessInfoServie(BusinessInfoServie businessInfoServie) {
		this.businessInfoServie = businessInfoServie;
	}

}
