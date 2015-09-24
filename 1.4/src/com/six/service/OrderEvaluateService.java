package com.six.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.constant.OrderEvaluateStateType;
import com.six.dao.OrderDao;
import com.six.dao.OrderEvaluateDao;
import com.six.dao.StoreDao;
import com.six.entity.Businessman;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.OrderEvaluate;
import com.six.entity.Store;
import com.six.util.JSONUtil;

@Component
public class OrderEvaluateService extends ManageBaseService<OrderEvaluate>
{
	private Logger logger = Logger.getLogger(OrderEvaluateService.class);
	
	private OrderEvaluateDao orderEvaluateDao;
	private OrderDao orderDao;
	private StoreDao storeDao;
	
	public OrderEvaluateDao getOrderEvaluateDao() {
		return orderEvaluateDao;
	}
	@Resource
	public void setOrderEvaluateDao(OrderEvaluateDao orderEvaluateDao) {
		this.orderEvaluateDao = orderEvaluateDao;
	}
	
	public OrderDao getOrderDao() {
		return orderDao;
	}
	@Resource
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	/* 评价订单 */
	public String evaluateOrder(int orderId, String category, String content) {
		
		Order order = (Order)orderDao.get(Order.class, orderId);
		OrderEvaluate orderEvaluate = (OrderEvaluate) orderEvaluateDao.findByPropertyUnique(OrderEvaluate.class, "order", order);
		if(orderEvaluate!=null) {
			return JSONUtil.returnResultJson("Failure", "已评价");
		}
		if(order.getStore().getBusinessmans().size()==0) {
			return JSONUtil.returnResultJson("Failure", "该店未绑定商家");
		}
		orderEvaluate = new OrderEvaluate();
		orderEvaluate.setOrder(order);
		Businessman businessman = (Businessman)order.getStore().getBusinessmans().iterator().next();
		orderEvaluate.setBusinessman(businessman);
		orderEvaluate.setEvaluateCategory(category);
		orderEvaluate.setEvaluateContent(content);
		orderEvaluate.setState(OrderEvaluateStateType.未回复.toString());
		orderEvaluate.setUser(order.getUser());
		orderEvaluate.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		orderEvaluateDao.saveOrUpdate(orderEvaluate);
		//更新商店好评率
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("businessman", businessman);
		
		map.put("evaluateCategory", "good");
		List<OrderEvaluate> orderEvaluates = (List<OrderEvaluate>) orderEvaluateDao.findByProperties(OrderEvaluate.class, map);
		
		List<OrderEvaluate> orderEvaluatesAll = new ArrayList<OrderEvaluate>();
		orderEvaluatesAll.addAll(orderEvaluates);
		map.put("evaluateCategory", "medium");
		orderEvaluatesAll.addAll((List<OrderEvaluate>) orderEvaluateDao.findByProperties(OrderEvaluate.class, map));
		map.put("evaluateCategory", "bad");
		orderEvaluatesAll.addAll((List<OrderEvaluate>) orderEvaluateDao.findByProperties(OrderEvaluate.class, map));
		double rating = (orderEvaluatesAll.size()==0)?0:(double)orderEvaluates.size()/(double)orderEvaluatesAll.size();
		rating = rating*10;
		Store store = order.getStore();
		store.setRating(rating);
		storeDao.update(store);
		
		return JSONUtil.returnResultJson("Success", "评价成功");
	}
	
	/** 找到商家的所有订单评价 */
	public List<OrderEvaluate> findAllByBusiness(Businessman businessman) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("businessman", businessman);
		LinkedHashMap<String, Boolean> sortMap = new LinkedHashMap<String, Boolean>();
		sortMap.put("state", false);
		return (List<OrderEvaluate>) orderEvaluateDao.findAllByPropertiesAndSort(OrderEvaluate.class, map, sortMap);
	}
	
	/* 找到特定Id的对象 */
	public OrderEvaluate getById(Integer id) {
		return (OrderEvaluate)orderEvaluateDao.get(OrderEvaluate.class, id);
	}
	/* 保存回复 */
	public void saveReply(Integer id, String reply) {
		
	}
	/* 获取评价的订单详情 */
	public String getDetailHtml(Integer id) {
		OrderEvaluate orderEvaluate = (OrderEvaluate) orderEvaluateDao.get(OrderEvaluate.class,id);
		Order order = orderEvaluate.getOrder();
		Set<OrderDishes> orderDisheses = order.getOrderDisheses();
		Iterator<OrderDishes> it = orderDisheses.iterator();
		
		String html = "<div class='dishesBox'>";
		while(it.hasNext()) {
			OrderDishes od = it.next();
			html += "<div class='dishes'>"+od.getDishes().getName()+"  ￥"+
					od.getDishes().getPrice()+"*"+od.getNum()+"</div>";
		}
		html += "</div>";
		return html;
	}
	/* 获取商家的评价 */
	public String getBussinessEvaluates(Businessman businessman,Integer pageNum,
			Integer pageSize, String sort, String order) {
		pageNum = pageNum<=0?1:pageNum;
		pageSize = pageSize<=0?10:pageSize;
		sort = (sort==null?"updateTime":sort);
		order = (order==null?"asc":order);
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		LinkedHashMap<String, Boolean> orderMap = new LinkedHashMap<String, Boolean>();
		propertyMap.put("businessman", businessman);
		orderMap.put(sort, order.equals("asc")?true:false);
		List<OrderEvaluate> orderEvaluatesAll = (List<OrderEvaluate>)
				orderEvaluateDao.findAllByPropertiesAndSort
				(OrderEvaluate.class, propertyMap, orderMap);
		List<OrderEvaluate> orderEvaluates = orderEvaluatesAll.subList((pageNum-1)*pageSize, (pageNum*pageSize)<orderEvaluatesAll.size()?(pageNum*pageSize):orderEvaluatesAll.size());
		List<Map> list = new ArrayList<Map>();
		
		for(int i=0; i<orderEvaluates.size(); i++) {
			OrderEvaluate orderEvaluate = orderEvaluates.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", orderEvaluate.getId());
			
			Timestamp updateTimeTS = orderEvaluate.getUpdateTime();
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String updateTime = sdf.format(updateTimeTS);
			map.put("updateTime", updateTime);
			
			map.put("orderNum", orderEvaluate.getOrder().getOrderNum());
			map.put("userName", orderEvaluate.getUser().getName());
			map.put("evaluateCategory", orderEvaluate.getEvaluateCategory());
			map.put("state", orderEvaluate.getState());
			map.put("evaluateContent", orderEvaluate.getEvaluateContent());
			map.put("reply", orderEvaluate.getBusinessmanReply());
			list.add(map);
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("rows",list);
		resMap.put("total", orderEvaluatesAll.size());
		return JSONObject.fromObject(resMap).toString();
	}
	
	public StoreDao getStoreDao() {
		return storeDao;
	}
	@Resource
	public void setStoreDao(StoreDao storeDao) {
		this.storeDao = storeDao;
	}
	
	
}
