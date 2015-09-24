package com.six.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.six.entity.User;

@SuppressWarnings("rawtypes")
@Component
public class CollectDao extends BaseDao{
	 
	@SuppressWarnings("unchecked")
	public List<User> findUser(Map<String, String> map)
	{
		
		String userid1 = map.get("userid");
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		if(!StringUtils.isBlank(userid1))
			
			criteria.add(Restrictions.like("userid", userid1, MatchMode.ANYWHERE));
	 
			return (List<User>)criteria.list();
	}
	
	public List<?> findByStoreAndCurTime(Class<?> clazz, 
			Object storeValue,Timestamp start,Timestamp end,int state)
	{
		
		return getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("store", storeValue))
				.add(Restrictions.between("createTime", start, end))
				.add(Restrictions.eq("state", state))
				.list();
	}

}
