package com.six.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Complaint;
import com.six.entity.User;
import com.six.util.JSONUtil;

@Component
public class ComplaintServer {
	private BaseDao baseDao;
	//fiand all Complaint
	public List<Complaint> findAll(User user)
	{
		System.out.println(user.getName());
		List<Complaint> complaints =baseDao.findByProperty(Complaint.class,"user",user);
		System.out.print("....");
		if(null == complaints || complaints.size() == 0)
			return null;
		else
			return complaints;
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
