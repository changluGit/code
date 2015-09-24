package com.six.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Businessman;
import com.six.entity.PetCard;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.util.JSONUtil;

@Component
public class BusinessInfoServie  {
	private BaseDao baseDao;
	//fiand all Businessman
	public List<Businessman> findAll(Businessman businessnan)
	{
		List<Businessman> businessmans =baseDao.findByProperty(Businessman.class,"user",businessnan);
		
		if(null == businessmans || businessmans.size() == 0)
			return null;
		else
			return businessmans;
	}
		//fiand Businessman by name
			public Businessman findBusinessman(String propertyName,Object propertyValue)
			{
				Businessman businessman =(Businessman)baseDao.findByPropertyUnique(Businessman.class,propertyName,propertyValue);
				if(null == businessman)
					return null;
				else
					return businessman;
			}
		//fiand Store by id
		public Store findStore(int storeId)
		{
			Store store =(Store)baseDao.findByPropertyUnique(Store.class,"id",storeId);
			if(null == store)
				return null;
			else
				return store;
		}
		//fiand Store by name
		public Store findStore(String name)
		{
			Store store =(Store)baseDao.findByPropertyUnique(Store.class,"storeName",name);
			if(null == store)
				return null;
			else
				return store;
		}
		//fiand  petcard
		public PetCard findCard(int cardId)
		{
			PetCard petCard =(PetCard)baseDao.findByPropertyUnique(PetCard.class,"id",cardId);
			if(null == petCard)
				return null;
			else
				return petCard;
		}
		
	//更新用户信息
	public void updateBusinessman(Businessman u){
		baseDao.update(u);
	}
	public void save(Businessman c){
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

