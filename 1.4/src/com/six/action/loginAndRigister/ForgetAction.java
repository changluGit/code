package com.six.action.loginAndRigister;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.springframework.stereotype.Component;


import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.User;
import com.six.service.MailUtil;
import com.six.service.UserInfoService;
import com.six.service.logAndRigService.LoginService;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;

/**
 * 处理登录和退出action
 * @author 828477
 *
 */
@Component
public class ForgetAction extends BaseAction
{
	private User user = new User();
	private LoginService loginService;
	private UserInfoService userInfo;

	@Override
	public Object getModel()
	{
		return user;
	}

	/**
	 * 邮箱检查
	 * 
	 * @throws Exception
	 */
	public void emailCheck() throws Exception
	{
		String validateResult = "";
		
		HttpServletRequest request = ServletActionContext.getRequest();
		//验证客户端输入格式是否正确
		if(!"Success".equals(validateResult = validateInput()))
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), validateResult));
		
		User forgetUser = userInfo.findUser("email", user.getEmail());
		if(null == forgetUser)
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "密码没找回，该邮箱还没有注册"));
			return;
		}
		if(!user.getName().equals(forgetUser.getName()))
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "密码没找回，用户名填写错误"));
			return;
		}
		//如果修改成功，跳到登录页面
		//这个类主要是设置邮件  
		  String pass=randomString(8);
		  forgetUser.setPwd(pass);
		  userInfo.updateUser(forgetUser);
		 // request.setAttribute("forget", pass);
		  toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "已重置您的密码："+pass+"，      (建议您登陆后尽快修密码)"));
		  System.out.println(pass);
		  
		 /* SendMail sm=new SendMail();
		  boolean b=sm.sendMails(user.getEmail(), "test",pass);
		  System.out.print(b);
		  
		  用QQ邮箱可以发送邮件成功
		  MailUtil.sendmail(forgetUser.getEmail(), "六六外卖网账号密码重置", "你好，这是你的新密码，建议你尽快登录并修改账号密码："+pass);
		  */

		  toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "/order/login.jsp"));
			
	}
	/**
	 * 表单项格式的验证
	 */
	private String validateInput()
	{
		String email = user.getEmail();
		String name = user.getName();
		
		if(StringUtils.isBlank(email))
			return "邮箱不能为空";
		
		if(!StringUtil.emailFormat(email))
			return "邮箱的格式不正确，请重新填写";
		if(StringUtils.isBlank(name))
			return "用户名不能为空";
		return "Success";
		
	}

	/**
	* 产生随机字符串
	* */
	private static Random randGen = null;
	private static char[] numbersAndLetters = null;

	public static final String randomString(int length) {
		if (length < 1) {
		return null;
		}
		if (randGen == null) {
		randGen = new Random();
		numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char [] randBuffer = new char[length];
		for (int i=0; i<randBuffer.length; i++) {
		randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
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

	private UserInfoService getUserInfo() {
		return userInfo;
	}
	@Resource
	private void setUserInfo(UserInfoService userInfo) {
		this.userInfo = userInfo;
	}

	
}
