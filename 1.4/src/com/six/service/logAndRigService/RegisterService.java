package com.six.service.logAndRigService;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.constant.ResultType;
import com.six.dao.BaseDao;
import com.six.entity.User;
import com.six.util.JSONUtil;

@Component
public class RegisterService
{
	private BaseDao baseDao;
	/**
	 * 用户注册
	 * @return 返回操作结果
	 */
	public String register(User registeringUser)
	{
		try
		{
			registeringUser.setCreateTime(new Timestamp(new Date().getTime()));
			baseDao.save(registeringUser);
		} catch (Exception e)
		{
			return JSONUtil.returnResultJson(ResultType.Failure.toString(), "注册失败，请重试！");
		}
		
		return  JSONUtil.returnResultJson(ResultType.Success.toString(), "注册成功！");
	}
	
	public boolean isEmailRegistered(String email)
	{
		if(null == (baseDao.findByPropertyUnique(User.class, "email", email)))
			return false;
		else
			return true;
	}
	public BaseDao getBaseDao()
	{
		return baseDao;
	}
	@Resource
	public void setBaseDao(BaseDao baseDao)
	{
		this.baseDao = baseDao;
	}
	
	
}
