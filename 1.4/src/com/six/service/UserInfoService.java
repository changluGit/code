package com.six.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Complaint;
import com.six.entity.User;

@Component
public class UserInfoService {
	private BaseDao baseDao;
	
	//查找用户
	public User findUser(String propertyName,Object propertyValue){
		User user =(User)baseDao.findByPropertyUnique(User.class,propertyName,propertyValue);
		return user;
	}
	//更新用户信息
	public void updateUser(User u){
		baseDao.update(u);
	}
	public void save(Complaint c){
		baseDao.save(c);
	}
	public BaseDao getBasedao() {
		return baseDao;
	}
	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	

}
