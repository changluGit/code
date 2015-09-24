package com.six.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.six.dao.CollectDao;
import com.six.entity.Businessman;
import com.six.entity.Dishes;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.Store;
import com.six.util.DateUtil;
@Component
public class StatisticsChatService {
	private CollectDao collectdao;
	List<Dishes> dishes;
	List<Order> orders;
	List<Order> orderstime;
	private Map<String, Object> mapChatcolumn;
	private Map<String, Object> mapChatline;
	private Map<String, Object> mapChatNumPie;
	private JSONArray pieRoot;
	private JSONArray pieRoot1;
	public JSONArray getPieRoot1() {
		return pieRoot1;
	}

	public void setPieRoot1(JSONArray pieRoot1) {
		this.pieRoot1 = pieRoot1;
	}

	public JSONArray getPieRoot() {
		return pieRoot;
	}

	public void setPieRoot(JSONArray pieRoot) {
		this.pieRoot = pieRoot;
	}

	public Map<String, Object> getMapChatNumPie() {
		return mapChatNumPie;
	}

	public void setMapChatNumPie(Map<String, Object> mapChatNumPie) {
		this.mapChatNumPie = mapChatNumPie;
	}
	private Map<String, Object> mapChatDishes;
	public Map<String, Object> getMapChatcolumn() {
		return mapChatcolumn;
	}

	public void setMapChatcolumn(Map<String, Object> mapChatcolumn) {
		this.mapChatcolumn = mapChatcolumn;
	}

	public Map<String, Object> getMapChatline() {
		return mapChatline;
	}

	public void setMapChatline(Map<String, Object> mapChatline) {
		this.mapChatline = mapChatline;
	}

	public Map<String, Object> getMapChatDishes() {
		return mapChatDishes;
	}

	public void setMapChatDishes(Map<String, Object> mapChatDishes) {
		this.mapChatDishes = mapChatDishes;
	}

	public List<Dishes> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dishes> dishes) {
		this.dishes = dishes;
	}

	public CollectDao getCollectdao() {
		return collectdao;
	}
	@Resource
	public void setCollectdao(CollectDao collectdao) {
		this.collectdao = collectdao;
	}
    //销量
	@SuppressWarnings("unchecked")
	public String getStoreByBussiness(Businessman businessman,Timestamp start,Timestamp end,int state) {
		
		int pienum=0;
		HashMap<Object, Object>  dishNumMap=new HashMap<Object, Object>();
	
		Store store = businessman.getStore();
		
		
		if (collectdao.findByProperty(Dishes.class, "store", store).size()==0)
		{
			return null;
		} 
		else
		{
			
			dishes=collectdao.findByProperty(Dishes.class,
					"store", store);
			
			orders=(List<Order>) collectdao.findByStoreAndCurTime(Order.class,store,start,end,state);
		    
			//获得每个菜品的销量，以及总销量
			for(int i=0; i<orders.size(); i++) {
				Set<OrderDishes> ods = orders.get(i).getOrderDisheses();
				Iterator<OrderDishes> it = ods.iterator();
				while(it.hasNext()) {
					OrderDishes od = (OrderDishes) it.next();
					Integer orgNum = (Integer) dishNumMap.get(od.getDishes());
					System.out.println(orgNum);
					orgNum = (orgNum==null?0:orgNum);
					dishNumMap.put(od.getDishes(), orgNum+od.getNum());
					pienum += od.getNum();
				}
			}
			
			
			
		    }
		
		return chatDishes(dishNumMap,pienum);
		
	}
	
	//绘制销量-柱状图、饼图
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String chatDishes(HashMap<Object, Object> mapOrderDish,int pienum)
	{
		
		mapChatcolumn=new HashMap<String, Object>();
		mapChatDishes=new HashMap<String, Object>();
		mapChatline=new HashMap<String, Object>();
	    pieRoot = new JSONArray();
		Map<String, Object> columnChat1 = new HashMap<String, Object>();
		Map<String, Object> Title1 = new HashMap<String, Object>();
		Map<String, Object> xAxis1 = new HashMap<String, Object>();
		Map<String, Object> xAxis2 = new HashMap<String, Object>();
		Map<String, Object> yAxis1 = new HashMap<String, Object>();
		Map<String, Object> yAxis2 = new HashMap<String, Object>();
		Map<String, Object> series1 = new HashMap<String, Object>();
		Map<String, Object> plot1 = new HashMap<String, Object>();
		Map<String, Object> plot2 = new HashMap<String, Object>();
		Map<String, Object> credits = new HashMap<String, Object>();
		ArrayList name=new ArrayList();
		ArrayList salenumber=new ArrayList();
		ArrayList dataSeries=new ArrayList();
		//柱状图数据
		columnChat1.put("type","column" );
		columnChat1.put("renderTo","container");
		Title1.put("text","菜品月销量分布图");
		Set disheses =  mapOrderDish.keySet();
		Iterator it = disheses.iterator();
		while(it.hasNext()) {
			//获得柱状图需要的数据
			Dishes dishes = (Dishes)it.next();
			Integer num = (Integer)mapOrderDish.get(dishes);
			
			name.add(dishes.getName());
			salenumber.add(num);
			//获得饼图数据
			JSONArray numJSON = new JSONArray();
			numJSON.add(dishes.getName());
			DecimalFormat format = new DecimalFormat("0.00");
			Double numPercentage1 = 0.00;
			if(pienum!=0) {
				numPercentage1  =  Double.valueOf(format.format((double)num/pienum));
			}
			numJSON.add(numPercentage1);
			pieRoot.add(numJSON);
			
		}
		//获得绘制柱状图的属性以及值
		xAxis2.put("text", "菜品种类");
		xAxis1.put("categories",name);
		xAxis1.put("title",xAxis2);
		yAxis2.put("text","销量(份)");
		yAxis1.put("title", yAxis2);
		series1.put("data",salenumber);
		series1.put("name", "月销量");
		series1.put("color", "#AFD8F8");
		dataSeries.add(series1);
		plot1.put("pointWidth", 30);
		plot2.put("series", plot1);
		credits.put("enabled", false);
		mapChatcolumn.put("chart",columnChat1 );
		mapChatcolumn.put("title",Title1);
		mapChatcolumn.put("xAxis",xAxis1);
		mapChatcolumn.put("yAxis", yAxis1);
		mapChatcolumn.put("series",dataSeries);
		mapChatcolumn.put("plotOptions",plot2);
		mapChatcolumn.put("credits",credits);
		mapChatDishes.put("chatcolumn", mapChatcolumn);
		mapChatDishes.put("chatpie", pieRoot);//添加饼图数据
		JSONObject jsonObject = JSONObject.fromObject(mapChatDishes);
		return jsonObject.toString();
	}
	//销售额
		@SuppressWarnings("unchecked")
		public String getsaleByBussiness(Businessman businessman,Timestamp start,Timestamp end,int state) {
			
			int piesum=0;
			LinkedHashMap<Object, Object>  dishsumMap=new LinkedHashMap<Object, Object>();
		
			Store store = businessman.getStore();
			
			if (collectdao.findByProperty(Dishes.class, "store", store).size()==0)
			{
				return null;
			} 
			else
			{
				
				dishes=collectdao.findByProperty(Dishes.class,
						"store", store);
			
				//获得月销售额
				while(!start.equals(end))
				{
					Timestamp startfirst=DateUtil.addMonth(start,1);
					orders=(List<Order>) collectdao.findByStoreAndCurTime(Order.class,store,start,startfirst,state);
					int sum = 0;
			
					for(int i=0; i<orders.size(); i++) {
						Set<OrderDishes> ods = orders.get(i).getOrderDisheses();
						Iterator<OrderDishes> it = ods.iterator();
						while(it.hasNext()) {
							OrderDishes od = (OrderDishes) it.next();
							sum+=od.getNum()*od.getDishes().getPrice();
							
						}
						
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
					String start1=sdf.format(start);
					dishsumMap.put(start1, sum);
					start=DateUtil.addMonth(start,1);
					piesum += sum;
				}
			}
				
			
			return chatSales(dishsumMap,piesum);
			
		}
	//绘制销售额-折线图、饼图
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String chatSales(HashMap<Object, Object> mapSaleDish,int piesum)
	{
		
		mapChatDishes=new HashMap<String, Object>();
		mapChatline=new HashMap<String, Object>();
		pieRoot1 = new JSONArray();
		Map<String, Object> lineChat1 = new HashMap<String, Object>();
		Map<String, Object> Title1 = new HashMap<String, Object>();
		Map<String, Object> xAxis1 = new HashMap<String, Object>();
		Map<String, Object> xAxis2 = new HashMap<String, Object>();
		Map<String, Object> yAxis1 = new HashMap<String, Object>();
		Map<String, Object> yAxis2 = new HashMap<String, Object>();
		Map<String, Object> series1 = new HashMap<String, Object>();
		Map<String, Object> plot1 = new HashMap<String, Object>();
		Map<String, Object> plot2 = new HashMap<String, Object>();
		Map<String, Object> credits = new HashMap<String, Object>();
		ArrayList name=new ArrayList();
		ArrayList salenumber=new ArrayList();
		ArrayList dataSeries=new ArrayList();
		lineChat1.put("type","line" );
		lineChat1.put("renderTo","container");
		Title1.put("text","不同月份销售额分布图");
		Set saleses =  mapSaleDish.keySet();
		Iterator it = saleses.iterator();
		while(it.hasNext()) {
			String month =(String)it.next();
			name.add(month);
			Integer sum = (Integer)mapSaleDish.get(month);
			salenumber.add(sum);
			//获得饼图数据
			
			JSONArray sumJSON = new JSONArray();
			sumJSON.add(month);
			DecimalFormat format = new DecimalFormat("0.00");
			Double sumPercentage1 = 0.00;
			if(piesum!=0) {
				sumPercentage1  =  Double.valueOf(format.format((double)sum/piesum));
			}
			sumJSON.add(sumPercentage1);
			pieRoot1.add(sumJSON);
			
		}
		
		xAxis2.put("text", "月份");
		xAxis1.put("categories",name);
		xAxis1.put("title",xAxis2);
		
		yAxis2.put("text","销售额(元)");
		yAxis1.put("title", yAxis2);
		series1.put("data",salenumber);
		series1.put("name", "销售额");
		series1.put("color", "#AFD8F8");
		dataSeries.add(series1);
		plot1.put("pointWidth", 30);
		plot2.put("series", plot1);
		credits.put("enabled", false);
		mapChatline.put("chart",lineChat1 );
		mapChatline.put("title",Title1);
		mapChatline.put("xAxis",xAxis1);
		mapChatline.put("yAxis",yAxis1);
		mapChatline.put("series",dataSeries);
		mapChatline.put("series",dataSeries);
		mapChatline.put("plotOptions",plot2);
		mapChatline.put("credits",credits);
		mapChatDishes.put("chatline", mapChatline);
		mapChatDishes.put("chatpie", pieRoot1);//添加饼图数据
		JSONObject jsonObject = JSONObject.fromObject(mapChatDishes);
		return jsonObject.toString();
	}
	
	

}
