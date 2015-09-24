package com.six.service;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;

@Component
public abstract class BaseService<T>
{
	private BaseDao<Serializable> baseDao;
	
	/**
	 * 保存一个对象
	 * 
	 * @param o
	 *            对象
	 * @return 对象的ID
	 */
	public abstract Serializable save(T o);

	/**
	 * 删除一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public abstract void delete(T o);

	/**
	 * 更新一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public abstract void update(T o);

	/**
	 * 保存或更新一个对象
	 * 
	 * @param o
	 *            对象
	 */
	public abstract void saveOrUpdate(T o);

	public BaseDao<Serializable> getBaseDao()
	{
		return baseDao;
	}
	
	@Resource(name="baseDao")
	public void setBaseDao(BaseDao<Serializable> baseDao)
	{
		this.baseDao = baseDao;
	}

	
}
