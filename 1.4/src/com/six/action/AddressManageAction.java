package com.six.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.constant.ResultType;
import com.six.entity.Address;
import com.six.entity.User;
import com.six.service.AddressManageService;
import com.six.util.JSONUtil;

@Component
public class AddressManageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AddressManageService addressManageService;
	private User user;

	public AddressManageService getAddressManageService() {
		return addressManageService;
	}

	@Resource
	public void setAddressManageService(
			AddressManageService addressManageService) {
		this.addressManageService = addressManageService;
	}

	/**
	 * 添加新地址
	 *
	 */
	public String addAddress() throws UnsupportedEncodingException {
		/** 收货人姓名 */
		String shouhuoren = getRequest().getParameter("shouhuoren");
		/** 收货人手机 */
		String shouhuo_tell = getRequest().getParameter("shouhuo_tell");
		/** 收货人地址 */
		String address = getRequest().getParameter("address");
		/** 收货人详细地址 */
		String detailAddress = getRequest().getParameter("detailAddress");
		System.out.println("detailAdress:" + detailAddress);
		user = (User) getRequest().getSession().getAttribute("loginedUser");
	
	    addressManageService.addAddress(user,shouhuoren,shouhuo_tell,address,detailAddress,false);	
		findAllAddress();
		return SUCCESS;
	}
	
	
	
	/**
	 * 添加新地址并设置为默认
	 *  
	 */
	public void addAddressForPage(){
		/** 收货人姓名 */
		String shouhuoren = getRequest().getParameter("shouhuoren");
		/** 收货人手机 */
		String shouhuo_tell = getRequest().getParameter("shouhuo_tell");
		/** 收货人地址 */
		String address = getRequest().getParameter("address");
		/** 收货人详细地址 */
		String detailAddress = getRequest().getParameter("detailAddress");
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		if( addressManageService.addAddress(user,shouhuoren,shouhuo_tell,address,detailAddress,true)){

			if(StringUtils.isNotBlank(address)){
		    	getRequest().getSession().setAttribute("defaultAddress", address);
		    }	
			toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(),"添加成功")) ;
		}	else{
			toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(),"添加失败，信息错误")) ;
		}
	        
		
	}

	/**
	 * 查询所有地址
	 * @return
	 */
	public String findAllAddress() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		List<Address> addressList = new ArrayList<Address>();
		addressList = addressManageService.findAllAddress(user);
		getRequest().setAttribute("addressList", addressList);
		return SUCCESS;
	}

	/**
	 * 删除地址 
	 * @return
	 */
	public String deleteAddress() {
		int id = 0;
		if (getRequest().getParameter("addressId") != null) {
			id = Integer.parseInt(getRequest().getParameter("addressId"));
			Address address = addressManageService.getAddress(id);
			if(!addressManageService.isDefaultAddr(address)){
				addressManageService.deleteAddress(address);
			}else{
				getRequest().setAttribute("msg", "默认地址无法删除");
			}
		}
		findAllAddress();
		return SUCCESS;
	}
	
	/**
	 * 设置默认地址
	 * @return
	 */
	public String setDefaultAddr(){
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		int id = 0;
		if (getRequest().getParameter("addressId") != null) {
			id = Integer.parseInt(getRequest().getParameter("addressId"));
			Address adress = addressManageService.setDefaultAddr(user,id);
			getRequest().getSession().setAttribute("defaultAddress", adress.getAddress());
		}
		
		findAllAddress();
		return SUCCESS;
	}
	
	
	/**
	 * 得到默认地址，若暂时无默认地址，返回null
	 * 
	 * @return defaultAddress json格式
	 */
	public void getDefaultAddress() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");
		Address defaultAddress = addressManageService.getDefaultAddress(user);
		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[]{"user","orders"});
		JSONObject jsonObject = JSONObject.fromObject(defaultAddress,jc);				
		toWrite(jsonObject.toString());
	}

	/**
	 * 得到所有地址的json 数据
	 * @return json格式
	 */
	public void getAllAddress() {
		user = (User) getRequest().getSession().getAttribute("loginedUser");	
		toWrite(addressManageService.getJSONOfAllAddress(user));
	}
	
	@Override
	public Object getModel() {
		return null;
	}

}
