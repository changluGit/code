package com.six.action.store;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


import com.six.action.BaseAction;
import com.six.entity.Store;
import com.six.service.store.StoreCommentShowService;
import com.six.util.JSONUtil;

@Component
public class StoreCommentShowAction extends BaseAction
{

	private StoreCommentShowService storeCommentShowService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void showComments()
	{
		Store visitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		String type = getRequest().getParameter("type");
		
		toWrite(storeCommentShowService.getCommnetBox(visitedStore, type));
	}
	
	public void showCommentDiv()
	{
		Store visitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		String type = getRequest().getParameter("type");
		
		toWrite(JSONUtil.returnResultJson("Success", storeCommentShowService.getCommentDivHtml(visitedStore, type)));
	}


	public StoreCommentShowService getStoreCommentShowService()
	{
		return storeCommentShowService;
	}

	@Resource
	public void setStoreCommentShowService(
			StoreCommentShowService storeCommentShowService)
	{
		this.storeCommentShowService = storeCommentShowService;
	}
	
	
}
