package com.six.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.dao.TasteDao;
import com.six.dao.UserTasteDao;
import com.six.entity.Taste;
import com.six.entity.User;
import com.six.entity.UserTaste;
import com.six.util.JSONUtil;

@Component
public class UserTasteService extends ManageBaseService<UserTaste>
{
	private Logger logger = Logger.getLogger(UserTasteService.class);
	
	private UserTasteDao userTasteDao;
	private TasteDao tasteDao;

	public List<UserTaste> findByUser(User user) {
		return (List<UserTaste>)userTasteDao.findByProperty(UserTaste.class, "user", user);
	}
	
	/**
	 * 添加或删除用户口味
	 * @param user
	 * @param tasteId
	 */
	public String addOrRemoveUserTaste(User user, Integer tasteId) {
		if(user==null) return JSONUtil.returnResultJson("Failure", "用户未登录");
		Taste taste = (Taste) tasteDao.get(Taste.class,tasteId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("taste", taste);
		List<UserTaste> userTastes = (List<UserTaste>)tasteDao.findByProperties(UserTaste.class, map);
		if(userTastes.size()==0) {
			UserTaste userTaste = new UserTaste();
			userTaste.setUser(user);
			userTaste.setTaste(taste);
			userTasteDao.save(userTaste);
			return JSONUtil.returnResultJson("Success", "add");
		} else {
			UserTaste userTaste = userTastes.get(0);
			userTasteDao.delete(userTaste);
			return JSONUtil.returnResultJson("Success", "remove");
		}
	}
	
	public TasteDao getTasteDao() {
		return tasteDao;
	}

	@Resource
	public void setUserTasteDao(UserTasteDao userTasteDao) {
		this.userTasteDao = userTasteDao;
	}
	
	public UserTasteDao getUserTasteDao() {
		return userTasteDao;
	}
	
	@Resource
	public void setTasteDao(TasteDao tasteDao) {
		this.tasteDao = tasteDao;
	}
	
	
}
