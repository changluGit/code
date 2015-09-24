package com.six.action;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.constant.OrderEvaluateCategoryType;
import com.six.constant.ResultType;
import com.six.entity.Businessman;
import com.six.entity.Order;
import com.six.entity.OrderDishes;
import com.six.entity.OrderEvaluate;
import com.six.service.OrderEvaluateService;
import com.six.util.JSONUtil;
/**
 * 
 * @author 828513
 * 订单Action类
 */
@Component
public class OrderEvaluateAction extends BaseAction
{
	private Logger logger = Logger.getLogger(OrderEvaluateAction.class);
	
	private OrderEvaluateService orderEvaluateService;

	public OrderEvaluateService getOrderEvaluateService() {
		return orderEvaluateService;
	}

	@Resource
	public void setOrderEvaluateService(OrderEvaluateService orderEvaluateService) {
		this.orderEvaluateService = orderEvaluateService;
	}


	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void test()
	{
		toWrite("This Order Test");
	}
	
	/* 评价订单 */
	public void evaluateOrder() {
		int orderId = Integer.parseInt(getRequest().getParameter("orderId"));
		String category = getRequest().getParameter("category");
		String content = getRequest().getParameter("content");
		if(category==null) {
			toWrite(JSONUtil.returnResultJson("Failure", "评价等级未选择"));
		} else if(category.equals(OrderEvaluateCategoryType.good.toString())||
			category.equals(OrderEvaluateCategoryType.medium.toString())||
			category.equals(OrderEvaluateCategoryType.bad.toString())){
			String message = orderEvaluateService.evaluateOrder(orderId, category, content);
			toWrite(message);
		} else {
			toWrite(JSONUtil.returnResultJson("Failure", "评价仅限于好中差"));
		}
		
	}
	
	/* 商家回复评价页面 */
	public String gotoReplyPage() {
		return "replyPage";
	}
	
	/* 显示商家全部的用户评价 */
	public void showEvaluates() {
		Businessman businessman = (Businessman) getRequest().getSession().getAttribute("loginedBussinessman");
		if(businessman==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "商家未登录"));
			return;
		}
		Integer pageNum = Integer.parseInt(getRequest().getParameter("page"));
		Integer pageSize = Integer.parseInt(getRequest().getParameter("rows"));
		String sort = getRequest().getParameter("sort");
		String order = getRequest().getParameter("order");
		String res = orderEvaluateService.getBussinessEvaluates(businessman,pageNum,pageSize,sort,order);
		
		toWrite(res);
	}
	/* 显示细节 */
	public void showDetail() {
		String idStr = getRequest().getParameter("id");
		if(idStr==null) {
			return;
		}
		Integer id = Integer.parseInt(idStr);
		String html = orderEvaluateService.getDetailHtml(id);
		toWrite(html);
	}
	/* 保存回复 */
	public void reply() {
		String idStr = getRequest().getParameter("id");
		Integer id = Integer.parseInt(idStr);
		String reply = getRequest().getParameter("reply");
		if(idStr==null||reply==null) {
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "提交失败"));
			return;
		}
		OrderEvaluate orderEvaluate = orderEvaluateService.getById(id);
		orderEvaluate.setState("已回复");
		orderEvaluate.setBusinessmanReply(reply);
		orderEvaluateService.update(orderEvaluate);
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "提交成功"));
	}
	
}
