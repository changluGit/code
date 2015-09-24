package com.six.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

@Component
public class BaseDao<PK extends java.io.Serializable>
{
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	protected Session getCurrentSession()
	{
		return getSessionFactory().getCurrentSession();
	}
	
	
	public Object get(Class<? extends Object> class1, Serializable pk)
	{
		return getCurrentSession().get(class1, pk);
	}
	/**
	 * 保存对象
	 * 
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PK save(Object t)
	{
		return (PK) getCurrentSession().save(t);
	}

	/**
	 * 保存或者更新
	 * 
	 * @param t
	 */
	public void saveOrUpdate(Object t)
	{
		getCurrentSession().saveOrUpdate(t);
	}

	/**
	 * ����
	 */
	public void update(Object t)
	{
		getCurrentSession().update(t);
	}

	/**
	 * 删除对象通过id
	 * @param id
	 */
	public void delete(Serializable  id)
	{
		getCurrentSession().delete(id);
	}
	/**
	 * 删除对象
	 * @param t
	 */
	public void delete(Object t)
	{
		getCurrentSession().delete(t);

	}
	/**
	 * 通过id判断对象是否被持久化
	 * @param clazz
	 * @param id
	 * @return 
	 */
	public boolean exist(Class<?> clazz, int id)
	{
		return getCurrentSession().get(clazz, id) == null ? false : true;
	}
	/**
	 * 加载一个对象到内存中
	 * @param clazz
	 * @param id
	 * @return
	 * @throws HibernateException
	 */
	public Object load(Class<?> clazz, Serializable  id) throws HibernateException
	{
		return getCurrentSession().load(clazz, id);
	}

	/**
	 * 统计记录数
	 * @param clazz
	 * @return
	 */
	public int countAll(Class<?> clazz)
	{
		return Integer.valueOf(getCurrentSession().createCriteria(clazz)
				.setProjection(Projections.rowCount()).uniqueResult()
				.toString());
	}

	/**
	 * 读取某实体的所有记录
	 * @param clazz
	 * @return
	 */
	public List<?> findAll(Class<?> clazz)
	{
		return getCurrentSession().createCriteria(clazz).list();
	}
	/**
	 * 读取某实体的所有记录，通过指定的排序
	 * @param clazz 实体类class
	 * @param linkedMap 需要排序的字段，key为字段，value为是否正序
	 * @return
	 */
	public List<?> findAll(Class<?> clazz,
			LinkedHashMap<String, Boolean> linkedMap)
	{
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		Iterator<String> iterator;

		for (iterator = (Iterator<String>) linkedMap.keySet().iterator(); iterator
				.hasNext();)
		{
			String key = iterator.next();
			Boolean isAsc = linkedMap.get(key);

			if (isAsc)
				criteria.addOrder(Order.asc(key));
			else
				criteria.addOrder(Order.desc(key));
		}

		return criteria.list();
	}
	
	/**
	 * 根据属性和排序 返回查询结果
	 * @param clazz
	 * @param map
	 * @param linkedMap linkedMap 需要排序的字段，key为字段，value为是否正序（true, 为正序，false为反序）
	 * @return
	 */
	public List<?> findAllByPropertiesAndSort(Class<?> clazz, Map<String, Object> map, LinkedHashMap<String, Boolean> linkedMap)
	{
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		
		if(null != map)
			criteria.add(Restrictions.allEq(map));
		if(null != linkedMap)
		{
			Iterator<String> iterator;

			for (iterator = (Iterator<String>) linkedMap.keySet().iterator(); iterator
					.hasNext();)
			{
				String key = iterator.next();
				Boolean isAsc = linkedMap.get(key);

				if (!isAsc)
					criteria.addOrder(Order.asc(key));
				else
					criteria.addOrder(Order.desc(key));
			}

		}
		
		return criteria.list();
			
	}
	/**
	 * 根据一个属性来查找多个记录
	 * @param clazz
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public List<?> findByProperty(Class<?> clazz, String propertyName,
			Object propertyValue)
	{
		return getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq(propertyName, propertyValue)).list();
	}

	/**
	 * 根据一个属性来查找一个记录
	 * @param clazz
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * @throws HibernateException
	 */
	public Object findByPropertyUnique(Class<?> clazz, String propertyName,
			Object propertyValue) throws HibernateException
	{
		if(null == propertyName)
			return this.findAll(clazz);
		
		Object result = new Object();
		//如果结果得到的结果不唯一 使得result = null
		try
		{
			result= getCurrentSession().createCriteria(clazz)
					.add(Restrictions.eq(propertyName, propertyValue))
					.uniqueResult();
		} catch (HibernateException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			result = null;
		}
		
		
		return result;
	}
	
	/**
	 * 根据多个属性来返回一条记录
	 * @param clazz
	 * @param map
	 * @return
	 * @throws HibernateException
	 */
	public Object findByPropertiesUnique(Class<?> clazz, Map<String, Object> map) throws HibernateException
	{
		if(null == map)
			return this.findAll(clazz);
		return getCurrentSession().createCriteria(clazz)
				.add(Restrictions.allEq(map)).uniqueResult();
	}
	/**
	 * 根据说个属性值，来查找多个记录。可能返回为0或一条
	 * @param clazz
	 * @param map
	 * @return
	 */
	public List<?> findByProperties(Class<?> clazz, Map<String, Object> map)
	{
		if(null == map)
			return this.findAll(clazz);
		return getCurrentSession().createCriteria(clazz)
				.add(Restrictions.allEq(map)).list();
	}
	/**
	 * 分页查询记录
	 * @param clazz
	 * @param firstResult 开始记录的位置
	 * @param maxResult 最大的记录数
	 * @return
	 */
	public List<?> findByPage(Class<?> clazz, int firstResult, int maxResult)
	{
		return getCurrentSession().createCriteria(clazz)
				.setFirstResult(firstResult).setMaxResults(maxResult).list();
	}
	
	/**
	 * 根据一个属性键值对来分页查询
	 * @param clazz
	 * @param firstResult
	 * @param maxResult
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public List<?> findByPageAndProperty(Class<?> clazz, int firstResult, int maxResult, String propertyName,
			Object propertyValue)
	{
		
		return getCurrentSession().createCriteria(clazz).setFirstResult(firstResult).setMaxResults(maxResult)
				.add(Restrictions.eq(propertyName, propertyValue)).list();
	}
	/**
	 * 根据多个属性来分页查询
	 * @param clazz
	 * @param firstResult
	 * @param maxResult
	 * @param map
	 * @return
	 */
	public List<?> findByPageAndProperties(Class<?> clazz, int firstResult,
			int maxResult, Map<String, Object> map)
	{
		if(map == null)
			return findByPage(clazz, firstResult, maxResult);
		return getCurrentSession().createCriteria(clazz)
				.add(Restrictions.allEq(map)).setFirstResult(firstResult)
				.setMaxResults(maxResult).list();
	}
	
	
	
	/**
	 * 根据多个属性来模糊查询,提供分页功能，
	 * @param clazz
	 * @param map key要查询的字段，value的格式为"test%"
	 * @param firstResult 数值小于 0 取消分页
	 * @return
	 */
	public List<?> fuzzySearchByProperties(Class<?> clazz, Map<String, Object> map, int firstResult,int maxResult)
	{
		if(null == map)
			return this.findAll(clazz);
		
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		
		Set<Map.Entry<String, Object>> entrys = map.entrySet();
		
		for(Map.Entry<String, Object> entry : entrys)
		{
			criteria.add(Restrictions.like(entry.getKey(), entry.getValue()));
		}
		
		if(firstResult > 0)
		{
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResult);
		}
		return criteria.list();
	}

}
