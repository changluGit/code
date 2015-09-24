package com.six.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.six.entity.Order;

/**
 * 
 * @author 828477
 *
 */
public class NoConfirmOrderCacheTest
{
	private HashMap<Integer, ArrayList<Order>> noComfirm = new HashMap<Integer, ArrayList<Order>>();
	private HashMap<Integer, LinkedHashMap<Order, Date>> laterConfirm = new LinkedHashMap<Integer, LinkedHashMap<Order,Date>>();
	
	/**
	 * 添加一个未确认的 订单
	 * @param storeId
	 * @param order
	 */
	public void addNoConfirmOrder(Integer storeId,Order order)
	{
		
		ArrayList<Order> noConfirmOrder = null;
		
		if(null == (noConfirmOrder = noComfirm.get(storeId)))
		{
			noConfirmOrder = new ArrayList<Order>();
		}
		
		noConfirmOrder.add(order);
		noComfirm.put(storeId, noConfirmOrder);
	}
	
	/**
	 * 订单已经确认，在未确认列表中移除该订单
	 * @param order
	 */
	public void removeNoConfirmOrder(Integer storeId ,Order order)
	{
		
		ArrayList<Order> noConfirmOrder = noComfirm.get(storeId);
		for(Iterator<Order> iterator = noConfirmOrder.iterator();iterator.hasNext();)
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
	public void laterConfirm(Integer storeId, Order order)
	{
		LinkedHashMap<Order, Date> laterConfirmOrder = null;
		if(null == (laterConfirmOrder = laterConfirm.get(storeId)))
			laterConfirmOrder = new LinkedHashMap<Order,Date>();
		
		laterConfirmOrder.put(order, new Date());
		laterConfirm.put(storeId, laterConfirmOrder);
	}
	
	/**
	 * 得到一个未确认的订单
	 * @return
	 */
	public Order getNoConfirmoOrder(Integer StoreId)
	{
		ArrayList<Order> noConfirmOrder = noComfirm.get(StoreId);
		LinkedHashMap<Order, Date> laterConfirmOrder = laterConfirm.get(StoreId);
		
		if(null != laterConfirmOrder && laterConfirmOrder.size() != 0)
		{
			Set<Entry<Order, Date>> entrySet = laterConfirmOrder.entrySet();
			//首先查询 稍候确认列表中 事件超过10分钟的订单。
			for(Entry<Order, Date> entry : entrySet)
			{
				if((new Date().getTime() - entry.getValue().getTime() > 10 * 60 * 1000))
				{
					return entry.getKey();
				}
			}
		}
		
		
		if(null != noConfirmOrder && noConfirmOrder.size() != 0)
		{
			return noConfirmOrder.get(0);
		}
		
		return null;
	}

	public HashMap<Integer, ArrayList<Order>> getNoComfirm()
	{
		return noComfirm;
	}

	public void setNoComfirm(HashMap<Integer, ArrayList<Order>> noComfirm)
	{
		this.noComfirm = noComfirm;
	}

	public HashMap<Integer, LinkedHashMap<Order, Date>> getLaterConfirm()
	{
		return laterConfirm;
	}

	public void setLaterConfirm(
			HashMap<Integer, LinkedHashMap<Order, Date>> laterConfirm)
	{
		this.laterConfirm = laterConfirm;
	}

	
	
	
}
