package com.six.action;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.six.entity.Businessman;
import com.six.entity.Dishes;
import com.six.service.StatisticsChatService;
import com.six.util.JSONUtil;
@Component
public class StatisticsChatAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StatisticsChatService statisticsChatservice;
	private List<Dishes> disheses;

	

	public List<Dishes> getDisheses() {
		return disheses;
	}

	public void setDisheses(List<Dishes> disheses) {
		this.disheses = disheses;
	}

	public StatisticsChatService getStatisticsChatservice() {
		return statisticsChatservice;
	}
	@Resource
	public void setStatisticsChatservice(StatisticsChatService statisticsChatservice) {
		this.statisticsChatservice = statisticsChatservice;
	}
  //统计销量 
	@SuppressWarnings({ })
	public void chatDishes() throws ParseException
	{
		
		String start=getRequest().getParameter("beginDate");
		String end=getRequest().getParameter("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date datestart = sdf.parse(start);
		Date dateend = sdf.parse(end);
		Timestamp start1 = new Timestamp(datestart.getTime());
		Timestamp end1 = new Timestamp(dateend.getTime());
		Businessman loginingBusiness=(Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		String s=statisticsChatservice.getStoreByBussiness(loginingBusiness,start1,end1,2);
		if(s==null)
		{
			 toWrite(JSONUtil.returnResultJson("Failure", "暂无订单信息"));
	       	 return; 
		}
		else
		{
			toWrite(s);
		}
		
		 
		 
		
		
	}
	//统计销售额
	@SuppressWarnings({ })
	public void chatsales() throws ParseException
	{
		
		String start=getRequest().getParameter("beginDate");
		String end=getRequest().getParameter("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date datestart = sdf.parse(start);
		Date dateend = sdf.parse(end);
		datestart.setDate(1);
		datestart.setHours(0);
		datestart.setMinutes(0);
		datestart.setSeconds(0);
		dateend.setHours(0);
		dateend.setDate(1);
		dateend.setMinutes(0);
		dateend.setSeconds(0);
		Timestamp start1 = new Timestamp(datestart.getTime());
		Timestamp end1 = new Timestamp(dateend.getTime());
		Businessman loginingBusiness=(Businessman)getRequest().getSession().getAttribute("loginedBussinessman");
		String s=statisticsChatservice.getsaleByBussiness(loginingBusiness,start1,end1,2);
		if(s==null)
		{
			 toWrite(JSONUtil.returnResultJson("Failure", "暂无订单信息"));
	       	 return; 
		}
		else
		{
		toWrite(s);
		}
	
	}
	
	
	public String gotoPage() {
		return "page";
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
