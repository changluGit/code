package com.six.service.logAndRigService;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.constant.ResultType;
import com.six.dao.LogAndRigDao;
import com.six.entity.User;
import com.six.util.DateUtil;
import com.six.util.JSONUtil;

@Component
public class LoginService
{
	private LogAndRigDao logAndRigDao;
	
	public User getLoginingUser(User user)
	{
		
		return(User)logAndRigDao.findByPropertyUnique(User.class, "email", user.getEmail());
	}

	public LogAndRigDao getLogAndRigDao()
	{
		return logAndRigDao;
	}

	@Resource
	public void setLogAndRigDao(LogAndRigDao logAndRigDao)
	{
		this.logAndRigDao = logAndRigDao;
	}
	
	
}
