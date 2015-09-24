package com.six.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.entity.Taste;
import com.six.entity.User;
import com.six.entity.UserTaste;
import com.six.service.TasteService;
import com.six.service.UserTasteService;
import com.six.util.JSONUtil;
/**
 * 
 * @author 828513
 * 订单Action类
 */
@Component
public class UserTasteAction extends BaseAction
{
	private Logger logger = Logger.getLogger(UserTasteAction.class);
	
	private UserTasteService userTasteService;
	private TasteService tasteService;

	/**
	 * 跳转到用户口味页面
	 * @return
	 */
	public String gotoUserTastePage() {
				  
		User user = (User) getRequest().getSession().getAttribute("loginedUser");
		if(user==null) return null;
		List<UserTaste> userTastes = userTasteService.findByUser(user);
		List<Taste> tastes = (List<Taste>)tasteService.findAll();
		getRequest().setAttribute("userTastes", userTastes);
		getRequest().setAttribute("tastes", tastes);
		return "userTastePage";
	}
	
	/**
	 * 添加或移除用户口味
	 */
	public void addOrRemoveUserTaste() {
		User user = (User) getRequest().getSession().getAttribute("loginedUser");
		String tasteIdStr =  getRequest().getParameter("tasteId");
		if(tasteIdStr==null) {
			toWrite(JSONUtil.returnResultJson("Failure", "口味未选择"));
		}
		Integer tasteId = Integer.parseInt(tasteIdStr);
		String res = userTasteService.addOrRemoveUserTaste(user, tasteId);
		toWrite(res);
	}
	
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public UserTasteService getUserTasteService() {
		return userTasteService;
	}
	@Resource
	public void setUserTasteService(UserTasteService userTasteService) {
		this.userTasteService = userTasteService;
	}

	public TasteService getTasteService() {
		return tasteService;
	}
	@Resource
	public void setTasteService(TasteService tasteService) {
		this.tasteService = tasteService;
	}
	
}
