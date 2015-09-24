package com.six.service.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

 

import com.six.constant.ResultType;
import com.six.dao.StoreDao;
import com.six.entity.Dishes;
import com.six.entity.Store;
import com.six.util.JSONUtil;

@Component
public class StoreShowService
{
	private StoreDao storeDao;

	/**
	 * home 页面中 根据 类型和检索来显示 store
	 * @param type
	 * @param sort
	 * @return
	 */
	public String retrievalStore(String type, String sort, String addressDefalult)
	{
		HashMap<String, Object> properties = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Boolean> linkedHashMap = new LinkedHashMap<String, Boolean>();
		
		//首先将检索地址放到 检索条件中
		properties.put("businessAddress", addressDefalult);
		
		if(StringUtils.isNotBlank(type))
			properties.put("storeCategory", type);
		
		//处理排序
		if(sort.equals("foodDeliveryTime"))
			linkedHashMap.put(sort, false);
		else
			linkedHashMap.put(sort, true);
		
		
		List<Store> allStores = new ArrayList<Store>();
		//首先查询营业的商店
		properties.put("state", "open");
		List<Store> openStores = (List<Store>)storeDao.findAllByPropertiesAndSort(Store.class, properties, linkedHashMap);
		//然后查询打样的商店
		properties.put("state", "close");
		List<Store> closeStores = (List<Store>)storeDao.findAllByPropertiesAndSort(Store.class, properties, linkedHashMap);
		
		allStores.addAll(openStores);
		allStores.addAll(closeStores);
		if(allStores.size() == 0)
		{
			return "没有检索到数据";
		}
		return returnHtmlStore(allStores);
	}
	
	private String returnHtmlStore(List<Store> stores)
	{
		StringBuilder builder = new StringBuilder();
		for(Store tem : stores)
		{
			//拼store logo地址
			builder.append("<div class='box'><img src='");
			builder.append(tem.getLogoAddress());
			builder.append("'  width='180px' height='180px'/>");
			
			//store 名称
			builder.append("<div class='storeName'>");
			builder.append(tem.getStoreName());
			builder.append("</div>");
			//具体参数的拼接
			
			builder.append("<div class='storeDemail'>");
			builder.append("月销量:" + tem.getSaleVolume() +"份");
			builder.append("<br>好评度:" + tem.getRating()+ "星");
			builder.append("<br>送餐速度:" + tem.getFoodDeliveryTime()+"min");
			
			//立即选餐按钮
			if(tem.getState().equals("open"))
				builder.append("<br></div><a href='/order/store/show_gotoStorePage.action?storeId="+ tem.getId() +"' id='addShoppingCar'>立即选餐</a></div>");
			else
				builder.append("<br></div><a href='javascript:void(0)' id='closedStore'>商家打烊了</a></div>");
		}
		
		return builder.toString();
	}
	
	/**
	 * 处理用户的输入检索商家
	 * @param searchContext
	 * @return
	 */
	public String getStoreByInputSearch(String searchContext)
	{
		if(StringUtils.isBlank(searchContext))
			return JSONUtil.returnResultJson(ResultType.Failure.toString(), "输入内容非法，请重试");
		
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("storeName","%"+ searchContext+"%");
		List<Store> stores = storeDao.fuzzySearchByProperties(Store.class, map, -1, 10);
		
		if(null == stores || stores.size() == 0)
			return JSONUtil.returnResultJson("Failure", "没有检索到任何商店，请确认后再试！");
		
		return JSONUtil.returnResultJson("Success", returnHtmlStore(stores));
	}
	
	/**
	 * 根据id 得到一个store
	 * @param storeIdStr
	 * @return
	 */
	public Store getStoreById(String storeIdStr)
	{
		Integer storeId;
		try
		{
			storeId = Integer.parseInt(storeIdStr);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		if(storeId < 0)
			return null;
		
		return (Store)storeDao.get(Store.class, storeId);
	}
	
	/**
	 * 返回home页推荐菜品的HTML
	 * @param dishesGroup
	 * @return
	 */
	public String getDishRecommendByHtml(List<Dishes> dishesGroup, Integer turn)
	{
		StringBuilder builder = new StringBuilder();
		
		if(null == dishesGroup)
		{
			builder.append("<div style='margin-left:30px'><font style='font-size:23px;'>您选择的地点还没有商家，换个地方吧！</font></div>");
			return builder.toString();
		}
		
		// 将推荐的轮数写到前端
		builder.append("<input type='hidden' name='turn' value = '"+turn+"'/>");
		for(Dishes temDish : dishesGroup)
		{
			builder.append("<div class='recommendDiv'>"+
							"<input type='hidden' name='dishId' value='"+temDish.getId() +"'/>"+
							"<a href='javascript:void(0)'>"+ temDish.getName()+"</a>"+
						"</div>");
		}
		
		return builder.toString();
	}
	
	
	public StoreDao getStoreDao()
	{
		return storeDao;
	}
	@Resource
	public void setStoreDao(StoreDao storeDao)
	{
		this.storeDao = storeDao;
	}
	
	
	
	
}
