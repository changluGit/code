package com.six.service.store;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.FinderException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Businessman;
import com.six.entity.OrderDishes;
import com.six.entity.OrderEvaluate;
import com.six.entity.Store;

@Component
public class StoreCommentShowService
{
	
	private BaseDao baseDao;
	public String getCommentDivHtml(Store visitedStore,String type)
	{
			
		StringBuilder builder = new StringBuilder();
		builder.append("<div id='commentDiv'>"+
			
			"<div id='commentType'>"+
				"<ul>"+
					"<li class='typeLi'>全部评价</li>"+
					"<li class='typeLi'>好评</li>"+
					"<li class='typeLi'>中评</li>"+
					"<li class='typeLi'>差评</li>"+
				"</ul>"+
			"</div>");
		
		builder.append(getCommnetBox(visitedStore, type));
		builder.append("</div>");
		
		return builder.toString();
		
	}
	
	public String getCommnetBox(Store visitedStore,String type)
	{
		Businessman businessman = (Businessman)baseDao.findByPropertyUnique(Businessman.class, "store", visitedStore);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(type))
		{
			map.put("evaluateCategory", type);
		}
		
		map.put("businessman", businessman);
		List<OrderEvaluate> orderEvaluates = (List<OrderEvaluate>)baseDao.findByProperties(OrderEvaluate.class, map);
		
		if(null == orderEvaluates || orderEvaluates.size() == 0)
			return "该商家还没有任何评价";
	
		StringBuilder builder = new StringBuilder();
		builder.append("<div id='commentBoxs'>");
		for(OrderEvaluate temEvaluate : orderEvaluates)
		{
			builder.append("<div class='commentBox'>"+
				"<div class='commentTitle'>"+
					
					temEvaluate.getUser().getName() + "    ");
			//讲中差评 英文翻译成中文
			builder.append(transType(temEvaluate.getEvaluateCategory())+
				"</div>"+
				"<div class='orderDishes'>");
			//处理该订单中所购买的菜品
			StringBuilder orderDishesStr = new StringBuilder();
			for(OrderDishes orderDishes : (List<OrderDishes>)baseDao.findByProperty(OrderDishes.class, "order", temEvaluate.getOrder()))
			{
				orderDishesStr.append(orderDishes.getDishes().getName());
				orderDishesStr.append(",");
			}
			
			builder.append("购买商品: " + orderDishesStr.substring(0, orderDishesStr.length() - 1));
			
			builder.append("</div>"+
				"<div class='commentContext'>"+ temEvaluate.getEvaluateContent()+"</div>"+
				"<div class='storeReply'>商家回复："+ temEvaluate.getBusinessmanReply()+"</div>"+
			"</div>");
			
			
		}
		
		builder.append("</div>");
		return builder.toString();
	}
	
	private String transType(String typeEnglish)
	{
		if(typeEnglish.equals("good"))
			return "好评";
		else if(typeEnglish.equals("medium"))
			return "中评";
		else
			return "差评";
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
