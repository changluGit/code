package com.six.service.logAndRigService;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Businessman;
import com.six.entity.Store;

@Component
public class BusinessmanLoginService
{
	private BaseDao baseDao;

	/**
	 * 根据 email 和pwd 返回一个business
	 * @param email
	 * @param pwd
	 * @return
	 */
	public Businessman loginCheck(String email, String pwd)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password", pwd);
		
		Businessman businessman = (Businessman)baseDao.findByPropertiesUnique(Businessman.class, map);
		
		return businessman;
	}
	
	/**
	 * 根据商人id得到所拥有的商店
	 * @param businessmanId
	 * @return
	 */
	public Store getStoreOfBusinessman(Integer businessmanId)
	{
		 Businessman businessman = (Businessman)baseDao.get(Businessman.class, businessmanId);
		 return businessman.getStore();
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
