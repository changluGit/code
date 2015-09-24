package com.six.service;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Businessman;
import com.six.entity.Store;

@Component
public class StoreService  {
	private BaseDao baseDao;
	
		//fiand Store by id
		public Store findStore(Businessman businessman)
		{
			Store store =(Store)baseDao.findByPropertyUnique(Store.class,"businessman",businessman);
			if(null == store)
				return null;
			else
				return store;
		}
	//创建文件路径	
	public boolean makeDir(String path){
		File folder = new File(path);
		boolean i = false;
		if(!folder.exists())
		{
			i=folder .mkdir();
		}
		return i;
	}	
	
	public Store get(Integer id) {
		return (Store)baseDao.get(Store.class, id);
	}
	
	//更新store信息
	public void updateStore(Store u){
		baseDao.update(u);
	}
	public BaseDao getBasedao() {
		return baseDao;
	}
	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	

}

