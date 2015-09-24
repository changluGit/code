package com.six.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.dao.TasteDao;
import com.six.entity.Taste;

@Component
public class TasteService extends ManageBaseService<Taste>
{
	private Logger logger = Logger.getLogger(TasteService.class);
	
	private TasteDao tasteDao;
	
	public List<Taste> findAll() {
		return (List<Taste>)tasteDao.findAll(Taste.class);
	}
	

	public TasteDao getTasteDao() {
		return tasteDao;
	}
	@Resource
	public void setTasteDao(TasteDao tasteDao) {
		this.tasteDao = tasteDao;
	}
	
	
}
