package com.six.service.store;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Dishes;
import com.six.entity.Store;

@Component
public class ShoppingCarService
{
	private BaseDao baseDao;

	
	public String getShoppingCarHtml(LinkedHashMap<Dishes, Integer> temOrderDish)
	{
		
		StringBuilder builder = new StringBuilder();
		
		Set<Entry<Dishes, Integer>> entrySet = temOrderDish.entrySet();
		for(Entry<Dishes, Integer> entry : entrySet)
		{
			builder.append("<tr class='orderList'>"+
					
					"<td class='nameCar'>"+ entry.getKey().getName()+"</td>"+   //显示名称
					"<td class='partCar'><a class='sub'>-</a>&nbsp;<font class='partValue'>"+ entry.getValue()+"</font>&nbsp;<a class='add'>+</a></td>"+			//显示份数
					"<td class='priceCar'>￥"+ entry.getKey().getPrice() * entry.getValue()+"</td>"+ 	//显示价格和份数
				"<input type='hidden' name='dishId' value='"+ entry.getKey().getId() +"'></tr>");		//传递一个id
		}
		
		return builder.toString();
	}
	
	/**
	 * 通过id,查找到一个store
	 * @param id
	 * @return
	 */
	public Store getStoreById(Integer id)
	{
		return (Store)baseDao.get(Store.class, id);
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
