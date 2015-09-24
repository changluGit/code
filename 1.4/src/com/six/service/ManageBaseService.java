package com.six.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import com.six.dao.BaseDao;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;

public class ManageBaseService<T>
{
	private BaseDao<Serializable> baseDao;
	
	public Serializable save(T o)
	{
		return baseDao.save(o);
	}

	
	public void delete(T o)
	{
		baseDao.delete(o);
		
	}

	
	public void update(T o)
	{
		baseDao.update(o);
		
	}

	
	public void saveOrUpdate(T o)
	{
		baseDao.saveOrUpdate(o);
		
	}

	/**
	 * 得到要查询对象的所有记录
	 * @param clazz
	 * @return
	 */
	public List<T> getAll(Class<T> clazz)
	{
		return (List<T>) baseDao.findAll(clazz);
	}
	
	/**
	 * 分页查询需要的记录
	 * @param clazz
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> getByPage(Class<T> clazz,int page, int rows)
	{
		int firstResult = (page - 1) * rows;
		
		return (List<T>)baseDao.findByPage(clazz, firstResult, rows);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param clazz
	 */
	public void deleteSome(String ids, Class<T> clazz)
	{
		List<String> list = (List<String>)StringUtil.getValues(ids, ",");
		
		for(int i = 0; i < list.size(); i++)
		{
			Object obj = null;
			try
			{
				obj = clazz.newInstance();
				Method setIdMethod = clazz.getDeclaredMethod("setId", Integer.class);
				setIdMethod.invoke(obj, Integer.valueOf(list.get(i)));
			} catch (Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			
			baseDao.delete(obj);
		}
	}
	
	/**
	 * 得到 user datagrid rows json 数据
	 */
	public String getDateGridRows(int tatal, List<T> dataList, List<String> exclude)
	{
		JSONUtil<T> jsonUtil = new JSONUtil<T>();
		
		return jsonUtil.getDatagridRow(tatal, dataList, exclude);
	}
	
	
	public BaseDao<Serializable> getBaseDao()
	{
		return baseDao;
	}
	
	@Resource
	public void setBaseDao(BaseDao<Serializable> baseDao)
	{
		this.baseDao = baseDao;
	}
	
	
	
}
