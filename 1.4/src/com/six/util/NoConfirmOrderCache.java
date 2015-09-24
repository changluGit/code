package com.six.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.id.SequenceIdentityGenerator.NoCommentsInsert;

import com.six.entity.Order;

public class NoConfirmOrderCache
{
	private ArrayList<Order> noConfirm = new ArrayList<Order>();
	private HashMap<Order, Date> laterConfirm = new HashMap<Order, Date>();
	
	/**
	 * 添加一个订单到未确定订单列表中
	 * @param order
	 */
	public void addNoConfirmOrder(Order order)
	{
		getNoConfirm().add(order);
	}
	
	/**
	 * 讲一个order 从未确定订单列表中删除
	 * @param order
	 */
	private void removeNoConfirmOrder(Order order)
	{
		for(Iterator<Order> iterator = noConfirm.iterator();iterator.hasNext();)
		{
			if(iterator.next().getId() == order.getId())
			{
				iterator.remove();
				break;
			}
				
		}
	}
	
	/**
	 * 商家点击稍候确认，讲该订单加入到稍候确认列表中
	 * @param order
	 */
	public void laterConfirm(Order order)
	{
		//添加到 稍候确认列表中
		laterConfirm.put(order, new Date());
	}
	
	/**
	 * 得到一个未确认的订单，然后将该订单从列表中移除
	 * @return 如果 未确定列表和 稍候确定列表中都没有订单就返回 null
	 */
	public Order getNoConfirmoOrder()
	{
		Order OrderOfReturn = null;
		if(laterConfirm.size() != 0)
		{
			Set<Entry<Order, Date>> entrySet = laterConfirm.entrySet();
			//首先查询 稍候确认列表中 事件超过10分钟的订单。
			for(Entry<Order, Date> entry : entrySet)
			{
				if((new Date().getTime() - entry.getValue().getTime() > 10 * 60 * 1000))
				{
					OrderOfReturn =  entry.getKey();
					break;
				}
			}
			
			//从稍候确定列表中移除该order
			if(null != OrderOfReturn)
			{
				laterConfirm.remove(OrderOfReturn);
				return OrderOfReturn;
			}
				
		}
		
		
		if(noConfirm.size() != 0)
		{
			OrderOfReturn =  noConfirm.get(0);
			//移除
			removeNoConfirmOrder(OrderOfReturn);
		}
		
		return OrderOfReturn;
	}


	public ArrayList<Order> getNoConfirm()
	{
		return noConfirm;
	}

	public void setNoConfirm(ArrayList<Order> noConfirm)
	{
		this.noConfirm = noConfirm;
	}

	public HashMap<Order, Date> getLaterConfirm()
	{
		return laterConfirm;
	}

	public void setLaterConfirm(HashMap<Order, Date> laterConfirm)
	{
		this.laterConfirm = laterConfirm;
	}
	
	
}
