package com.six.action.loginAndRigister;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.springframework.stereotype.Component;


import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.User;
import com.six.service.logAndRigService.LoginService;
import com.six.util.DateUtil;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;
import com.mchange.util.Base64Encoder;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 处理登录和退出action
 * @author 828477
 *
 */
@Component
public class LoginAction extends BaseAction
{
	private User user = new User();
	private LoginService loginService;

	@Override
	public Object getModel()
	{
		return user;
	}

	/**
	 * 登录检查
	 * 
	 * @throws Exception
	 */
	public void loginCheck() throws Exception
	{
		String validateResult = "";
		//验证客户端输入格式是否正确
		if(!"Success".equals(validateResult = validateInput()))
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), validateResult));
		
		User loginingUser = loginService.getLoginingUser(user);
		
		if(null == loginingUser)
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "该邮箱还没有注册，请注册后登录"));
			return;
		}
			
		if(!user.getPwd().equals(loginingUser.getPwd()))
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "登录名或者密码错误"));
			return;
		}
			
		
		//讲登录成功的用户写到session中
		getRequest().getSession().setAttribute("loginedUser", loginingUser);
		
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "/order/home.jsp"));
			
	}

	

	/**
	 * 处理用户退出
	 * 
	 * @throws Exception
	 * @throws ServletException
	 */
	public void logout() throws ServletException, Exception
	{
		HttpSession session = getRequest().getSession();
	
		session.invalidate();

		// 跳转到登录界面
		getRequest().getRequestDispatcher("/login.jsp").forward(getRequest(),
				getResponse());
	}

	
	

	/**
	 * 登录表单项格式的验证
	 */
	private String validateInput()
	{
		String email = user.getEmail();
		String pwd = user.getPwd();
		
		if(StringUtils.isBlank(email))
			return "邮箱不能为空";
		
		if(!StringUtil.emailFormat(email))
			return "邮箱的格式不正确，请重新填写";
		
		if(StringUtils.isBlank(pwd))
			return "密码不能为空";
		
		return "Success";
		
	}

	public String gotoPersonPage()
	{
		return "personPage";
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public LoginService getLoginService()
	{
		return loginService;
	}
	
	@Resource
	public void setLoginService(LoginService loginService)
	{
		this.loginService = loginService;
	}

	
}
