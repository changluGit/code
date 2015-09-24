package com.six.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.six.entity.User;
import com.six.service.UserInfoService;

@Component
public class UserInfoAction extends BaseAction{
	
	private Logger logger = Logger.getLogger(ComplaintAction.class);
	
	private UserInfoService infoService;
	private User user;

	@Override
	public Object getModel()
	{
		if(null == user)
			user = new User();
		return user;
	}
	//按照登录用户查找
	public String listUserInfo() throws Exception
	{
		logger.info("in");
		HttpServletRequest request = ServletActionContext.getRequest();
		User user= (User)getRequest().getSession().getAttribute("loginedUser");
		//user = user_infoService.findUser("id", 2);
		request.setAttribute("result", user);
		return "input";

	}
	//修改用户名
	
	public String nameM() throws Exception{
		String userName=getRequest().getParameter("userName");
		HttpServletRequest request = ServletActionContext.getRequest();
		user= (User)getRequest().getSession().getAttribute("loginedUser");
		//user = user_infoService.findUser("id", 2);
		user.setName(userName);
		infoService.updateUser(user);
		request.setAttribute("result", user);
		request.setAttribute("tishi", "恭喜修改用户名成功！");
		return "input";
	}
	
	//修改手机号码
	public String telM(){
		String tel=getRequest().getParameter("telMo");
		HttpServletRequest request = ServletActionContext.getRequest();
		user= (User)getRequest().getSession().getAttribute("loginedUser");
		//user = user_infoService.findUser("id", 2);
		user.setTel(tel);
		infoService.updateUser(user);
		request.setAttribute("result", user);
		request.setAttribute("tishi", "恭喜修改手机成功！");
		return "input";
	}
	
	//添加手机号码
	public String telA(){
		String tel=getRequest().getParameter("telA");
		HttpServletRequest request = ServletActionContext.getRequest();
		user= (User)getRequest().getSession().getAttribute("loginedUser");
		//user = user_infoService.findUser("id", 2);
		user.setTel(tel);
		infoService.updateUser(user);
		request.setAttribute("result", user);
		request.setAttribute("tishi", "恭喜添加电话成功！");
		return "input";
	}
	//修改Email
	public String emailM(){
		String email=getRequest().getParameter("useremail");
		HttpServletRequest request = ServletActionContext.getRequest();
		user= (User)getRequest().getSession().getAttribute("loginedUser");
		//user = user_infoService.findUser("id", 2);
		User e=infoService.findUser("email", email);
		//System.out.println("emile--:"+e.getEmail());
		if(null == e)
		{
			user.setEmail(email);
			request.setAttribute("result", user);
			request.setAttribute("tishi", "恭喜修改Email成功！");
			
		}
		else{

			request.setAttribute("tishi", "修改失败，邮箱已被占用！");
		}
		request.setAttribute("result", user);
		return "input";
	}
	//修改密码
		public String pswM(){
			String pwd1=getRequest().getParameter("pwdNew1");
			String pwd2=getRequest().getParameter("pwdNew2");
			String pwdOld=getRequest().getParameter("pwdOld");
			HttpServletRequest request = ServletActionContext.getRequest();
			user= (User)getRequest().getSession().getAttribute("loginedUser");
			//user = user_infoService.findUser("id", 2);
			if(pwdOld.equals(user.getPwd()))
			{
				if(pwd1.equals(pwd2)){
					user.setPwd(pwd1);
					infoService.updateUser(user);
					request.setAttribute("tishi", "恭喜修改密码成功！");	
				}
				else{
					request.setAttribute("tishi", "两次密码不相同,修改密码失败");
				}
			}
			else{
				request.setAttribute("tishi", "旧密码错误,修改密码失败");
			}
			request.setAttribute("result", user);
			return "input";
		}
	
	public UserInfoService getUserInfoService() {
		return infoService;
	}
	@Resource
	public void setUserInfoService(UserInfoService userinfoService) {
		this.infoService = userinfoService;
	}

}
