package com.six.service.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.dao.DishesDao;
import com.six.dao.OrderDao;
import com.six.dao.OrderDishesDao;
import com.six.entity.Dishes;
import com.six.entity.DishesCategory;
import com.six.entity.OrderDishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.entity.UserTaste;
import com.six.service.CollectStoreAndVegService;
import com.six.service.UserTasteService;
import com.six.util.JSONUtil;

import freemarker.template.utility.StringUtil;

/**
 * 
 * @author 828477
 *
 */
@Component
public class DishesService
{
	
	private DishesDao dishesDao;
	private CollectStoreAndVegService collectStoreAndVegService;
	private OrderDao orderDao;
	private OrderDishesDao orderDishesDao;
	private UserTasteService userTasteService;
	
	/**
	 * 拿到前台展示菜类类别的html
	 * @param visitedStore
	 * @return
	 */
	public String getDishCategory(Store visitedStore)
	{
		
		if(null == visitedStore)
			return JSONUtil.returnResultJson("Failure", "程序员喝茶去了，请稍候再试！");
		List<DishesCategory> categorys = dishesDao.getCategoryByStore(visitedStore);
		
		if(null == categorys || categorys.size() == 0)
			return JSONUtil.returnResultJson("Failure", "商家没有分类！");
		
		return JSONUtil.returnResultJson("Success", returnDishCategoryHtml(categorys));
	}
	/**
	 * 返回菜品类别在前台展示的html
	 * @param categorys
	 * @return
	 */
	private String returnDishCategoryHtml(List<DishesCategory> categorys)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("<div class='type'><a href='javascript:void(0)'>全部</div>");
		for(DishesCategory category : categorys)
		{
			builder.append("<div class='type'><a href='javascript:void(0)'>");
			builder.append(category.getName());
			builder.append("</a></div>");
		}
		
		return builder.toString();
	}
	
	/**
	 * 得到 前台dish展示的 html
	 * @param visitedStore
	 * @param categoryName
	 * @param sort
	 * @return
	 */
	public String getShowDishHtml(Store visitedStore, String categoryName, String sort, User loginedUser)
	{
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		LinkedHashMap<String, Boolean> linked = new LinkedHashMap<String, Boolean>();
		DishesCategory dishesCategory = null;
		
		//处理是否按菜品分类
		if(StringUtils.isNotBlank(categoryName))
		{
			HashMap<String, Object> categoryHash = new HashMap<String, Object>();
			categoryHash.put("name", categoryName);
			categoryHash.put("store", visitedStore);
			dishesCategory = (DishesCategory)dishesDao.findByPropertiesUnique(DishesCategory.class, categoryHash);
			
			if(dishesCategory == null)
				return JSONUtil.returnResultJson("Failure", "服务器开小差了");
			
			hashmap.put("dishesCategory", dishesCategory);
		}
		//处理是否排序
		if(StringUtils.isNotBlank(sort))
			linked.put(sort, true);
		
		//讲店铺放到检索条件中
		hashmap.put("store", visitedStore);
		
		List<Dishes> allDishes = new ArrayList<Dishes>();
		//首先检索 在售的菜品
		hashmap.put("status", "在售");
		List<Dishes> onSaleDishes = (List<Dishes>)dishesDao.findAllByPropertiesAndSort(Dishes.class, hashmap, linked);
		//然后检索 售罄的菜品
		hashmap.put("status", "售罄");
		List<Dishes> offSaleDishes = (List<Dishes>)dishesDao.findAllByPropertiesAndSort(Dishes.class, hashmap, linked);
		
		allDishes.addAll(onSaleDishes);
		allDishes.addAll(offSaleDishes);
		if(allDishes.size() == 0)
			return JSONUtil.returnResultJson("Failure", "该分类下，商家没有商品。");
		
		return JSONUtil.returnResultJson("Success", returnDishesHtml(categoryName, allDishes, true, loginedUser));
	}
	
	private String returnDishesHtml(String categoryName, List<Dishes> dishesList, boolean sortDiv, User loginedUser)
	{
		
		StringBuilder builder = new StringBuilder();
		//检索区域的拼接
		if(sortDiv)
		{
			builder.append("<div id='dishSort'>"+
   					"<div id='sortFont'><font>"+ categoryName +"</font></div>"+
   					"<div id='sortCondition'>"+
   						"<ul>"+
   							"<li><a href='javascript:void(0)'>默认排序</a></li>"+
   							"<li><a href='javascript:void(0)'>价格</a></li>"+
   							"<li><a href='javascript:void(0)'>销量</a></li>"+
   							"<li><a href='javascript:void(0)'>好评数</a></li>"+
   						"</ul>"+
   					"</div>"+
   				"</div>");
		}
		builder.append("<div id='dishes'>");
		for(Dishes dishes : dishesList)
		{
			String collectResult = "收藏";
			//处理 菜品是否被收藏
			if(null != loginedUser)
			{
				if(null != collectStoreAndVegService.getCollectByDishesAndUser(loginedUser, dishes))
					collectResult = "取消收藏";
			}
			builder.append("<div class='box'>");
			//处理图片显示
			if(null == dishes.getPictureAddress())
				builder.append("<div class='img'><img style='width:150px;height:150px;' src=''></img></div>");
			else
				builder.append("<div class='img'><img style='width:150px' src='"+ dishes.getPictureAddress()+"'></img></div>");
   			builder.append("<div class='dishFont'>"+
   							"<font class='dishName'>"+ dishes.getName() +"</font><br>"+
   							"<font class='saleVolume'>月售"+ dishes.getMonthSales() +"份</font><br>"+
   							"<font class='rmb'>￥<font class='price'>"+ dishes.getPrice() +"</font>/份</font>"+
   						"</div>"+
   						"<div class='buttonDiv'>"+
   						" <input type='hidden' name='dishId' value='"+ dishes.getId() +"'>");
			if(dishes.getStatus().equals("在售"))
				builder.append("<a href='javascript:void(0)' class='buyButton'>立即购买</a>"+
   							"<a href='javascript:void(0)' class='collectButton'>"+ collectResult +"</a>"+
   						"</div>"+
   					"</div>");
			else if(dishes.getStatus().equals("售罄"))
				builder.append("<a href='javascript:void(0)' id='offButton'>已经售罄</a>"+
							"<a href='javascript:void(0)' class='collectButton'>"+ collectResult +"</a>"+
						"</div>"+
					"</div>");
		}
		
		builder.append("</div>");
		
		return builder.toString();
	}
	
	/**
	 * 处理菜品的搜索
	 * @param inputContext
	 * @return
	 */
	public String getSearchDishResult(String inputContext, Store visitedStore, User loginedUser)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "%" + inputContext + "%");
		map.put("store", visitedStore);
		List<Dishes> lists = dishesDao.fuzzySearchByProperties(Dishes.class, map, -1, 10);
		
		if(lists == null || lists.size() == 0)
			return JSONUtil.returnResultJson("Failure", "没有检索到数据，请确认后重试！");
		
		return JSONUtil.returnResultJson("Success", returnDishesHtml("检索结果", lists, false, loginedUser));
	}
	
	/**
	 * 根据id拿到一个dishes
	 * @param dishId
	 * @return
	 */
	public Dishes getDishesByid(Integer dishId)
	{
		return (Dishes)dishesDao.get(Dishes.class, dishId);
	}
	public DishesDao getDishesDao()
	{
		return dishesDao;
	}
	
	/**
	 * 获得推荐菜单（根据用户历史菜单和口味）
	 * @param user 用户
	 * @param turn 推荐轮数
	 * @param address 用户所在地址
	 * @return 推荐菜单列表
	 */
	public List<Dishes> getRecomendDishes(User user, int turn, String address) {
		//获取用户口味
		List<UserTaste> userTastes = userTasteService.findByUser(user);
		
		List<Dishes> disheses = dishesDao.findByAddressAndStoreStateAndDishesStatus(address, "open", "在售");
		Map<Dishes, Integer> map = new LinkedHashMap<Dishes, Integer>();
		for(int i=0; i<disheses.size(); i++) { 
			Dishes dishes = disheses.get(i);
			if(map.get(dishes)==null) {
				map.put(dishes, 1);
			}
		}
		
		//获取用户历史订单中菜品的点餐次数
		List<OrderDishes> orderDisheses = (List<OrderDishes>) orderDishesDao.findByUserAndAddressAndStoreStateAndDishesStatus(user, address, "open", "在售");
		for(int i=0; i<orderDisheses.size(); i++) {
			OrderDishes od = orderDisheses.get(i);
			Dishes dishes = od.getDishes();
			if(map.get(dishes)!=null) {
				map.put(dishes, map.get(dishes)+1);
			}
			
		}
		
		
		
		if(map.size()==0) { 
			return null; 
		}
		
		//对菜品以点菜次数进行排序
		ArrayList<Entry<Dishes, Integer>> list = new ArrayList<Entry<Dishes,Integer>>(map.entrySet());  
		Collections.sort(list, new Comparator<Map.Entry<Dishes, Integer>>() {  
			public int compare(Map.Entry<Dishes, Integer> o1, Map.Entry<Dishes, Integer> o2) {  
				return (o2.getValue() - o1.getValue());  
			}
		});
		//获取历史订单中的最高点餐次数
		Integer total = list.get(0).getValue();
		
		//根据点餐次数和口味对菜品进行评分（点餐次数：口味=10:1*n）
		for(Entry<Dishes,Integer> e : list) {
			Dishes dishes = e.getKey();
			String taste = dishes.getTaste();
			e.setValue(e.getValue()*100/total);
			if(taste!=null) {
				String[] tastes = StringUtil.split(taste, ',');
				for(int index=0; index<tastes.length; index++) {
					String curTaste = tastes[index];
					for(int i=0; i<userTastes.size(); i++) {
						if(curTaste.equals(userTastes.get(i).getTaste().getName())) {
							e.setValue(e.getValue()+10);
							break;
						} 
					}
				}
			}
		}
		//排序菜单
		Collections.sort(list, new Comparator<Map.Entry<Dishes, Integer>>() {  
			public int compare(Map.Entry<Dishes, Integer> o1, Map.Entry<Dishes, Integer> o2) {  
				return (o2.getValue() - o1.getValue());  
			}
		});
		//获取推荐菜单列表
		List<Dishes> dishesList = new ArrayList<Dishes>();
		
		Integer totalNum = list.size();
		System.out.println(list.size());
		turn = turn%((int)Math.ceil(totalNum/10.0))+1;//取模
		for(int i = (turn-1)*10; i<list.size() && i<turn*10; i++) {
			Dishes dishes = list.get(i).getKey();
			dishesList.add(dishes);
		}
		
		return dishesList;
	}
	
	public Dishes getRandomDishes(User user, String address) {
		Integer turn = (int) (Math.random()*100);
		List<Dishes> disheses = getRecomendDishes(user, turn, address);
		int size = disheses.size();
		Random r = new Random();
		int index = r.nextInt(size);
		return disheses.get(index);
	}
	
	@Resource
	public void setDishesDao(DishesDao dishesDao)
	{
		this.dishesDao = dishesDao;
	}
	public CollectStoreAndVegService getCollectStoreAndVegService()
	{
		return collectStoreAndVegService;
	}
	@Resource
	public void setCollectStoreAndVegService(
			CollectStoreAndVegService collectStoreAndVegService)
	{
		this.collectStoreAndVegService = collectStoreAndVegService;
	}
	public OrderDao getOrderDao() {
		return orderDao;
	}
	@Resource
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	public UserTasteService getUserTasteService() {
		return userTasteService;
	}
	@Resource
	public void setUserTasteService(UserTasteService userTasteService) {
		this.userTasteService = userTasteService;
	}
	public OrderDishesDao getOrderDishesDao() {
		return orderDishesDao;
	}
	@Resource
	public void setOrderDishesDao(OrderDishesDao orderDishesDao) {
		this.orderDishesDao = orderDishesDao;
	}
	
	
}
