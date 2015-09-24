package com.six.action.businessman;

import javax.annotation.Resource;

import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.Businessman;
import com.six.entity.Store;
import com.six.service.StoreService;
import com.six.util.JSONUtil;

public class StoreStateAction extends BaseAction{

	private StoreService storeservice;
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	public void storeState() throws Exception
	{
		Store store= (Store)getRequest().getSession().getAttribute("loginedStore");
		String state=getRequest().getParameter("state");
		store.setState(state);
		storeservice.updateStore(store);
		
		String result = "";
		if(state.equals("open"))
			result = "营业中";
		else if(state.equals("close"))
			result = "餐厅已经打样，点击确认后回到登陆界面";
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), result));
	}
	public StoreService getStoreService() {
		return storeservice;
	}
	@Resource
	public void setStoreService(StoreService storeService) {
		this.storeservice = storeService;
	}
	
}
