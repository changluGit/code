package com.six.action.loginAndRigister;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.Businessman;
import com.six.entity.User;
import com.six.service.logAndRigService.BusinessmanLoginService;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;

public class BusinessmanLoginAction extends BaseAction
{
	private BusinessmanLoginService businessmanLoginService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 登录检查
	 * 
	 * @throws Exception
	 */
	public void loginCheck() throws Exception
	{
		String email = getRequest().getParameter("email");
		String pwd = getRequest().getParameter("pwd");
		
		String validateResult = "";
		//验证客户端输入格式是否正确
		if(!"Success".equals(validateResult = validateInput(email, pwd)))
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), validateResult));
			return;
		}
			
		Businessman loginedBussinessman = businessmanLoginService.loginCheck(email, pwd);
		
	
		if(null == loginedBussinessman)
		{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "登录名或者密码错误"));
			return;
		}
			
		
		//讲登录成功的用户写到session中
		getRequest().getSession().setAttribute("loginedBussinessman", loginedBussinessman);
		getRequest().getSession().setAttribute("loginedStore", businessmanLoginService.getStoreOfBusinessman(loginedBussinessman.getId()));
		
		//跳转到
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "/order/home.jsp"));
			
	}

	public String gotoBusinessManage()
	{
		return "businessManage";
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
		getRequest().getRequestDispatcher("/home.jsp").forward(getRequest(),
				getResponse());
	}


	/**
	 * 登录表单项格式的验证
	 */
	private String validateInput(String email, String pwd)
	{
		
		if(StringUtils.isBlank(email))
			return "邮箱不能为空";
		
		if(!StringUtil.emailFormat(email))
			return "邮箱的格式不正确，请重新填写";
		
		if(StringUtils.isBlank(pwd))
			return "密码不能为空";
		
		return "Success";
		
	}

	public BusinessmanLoginService getBusinessmanLoginService()
	{
		return businessmanLoginService;
	}

	@Resource
	public void setBusinessmanLoginService(
			BusinessmanLoginService businessmanLoginService)
	{
		this.businessmanLoginService = businessmanLoginService;
	}

	
	
}
