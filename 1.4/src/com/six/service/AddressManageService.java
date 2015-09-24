package com.six.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Component;

import com.six.dao.BaseDao;
import com.six.entity.Address;
import com.six.entity.User;

@Component
public class AddressManageService {

	private BaseDao baseDao;
  
	public BaseDao getBaseDao() {
		return baseDao;
	}

	@Resource
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * 添加新地址
	 * 
	 * @param user,shouhuoren,shouhuo_tell,address,b
	 * @return 添加成功为true,失败为false
	 */
	public boolean addAddress(User user,String shouhuoren,String shouhuo_tell,String address,String detailAddress,boolean b) {
		if(user != null && shouhuoren != null && shouhuo_tell != null && address!= null && detailAddress!= null && shouhuo_tell.length()<13){
			Address addr = new Address();
			addr.setUser(user);
			addr.setAddress(address);
			addr.setShouhuoren(shouhuoren);
			addr.setShouhuoTell(shouhuo_tell);
			addr.setDetailAddress(detailAddress);
			//若用户收货地址表为空，则将该地址设为默认地址
			if(null == findAllAddress(user) || findAllAddress(user).size() == 0 || b){
				Address defaultAddr = getDefaultAddress(user);
				if (null != defaultAddr) {
					defaultAddr.setDefaultAddress("0");
					baseDao.update(defaultAddr);
				}
				addr.setDefaultAddress("1");
			}else {
				addr.setDefaultAddress("0");
			}		
			baseDao.save(addr);
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 通过id得到一条地址
	 * @param id
	 * @return
	 */
	public Address getAddress(int id){
		Address address = (Address) baseDao.get(Address.class, id);
		if(null != address){
			return address;
		}else{
			return null;
		}
	}

	/**
	 * 查找用户所有地址
	 * 
	 * @param user
	 * @return addressList 地址列表
	 */
	public List<Address> findAllAddress(User user) {
		return baseDao.findByProperty(Address.class, "user", user);
	}

	/**
	 * 返回所有地址的json数据
	 * @param user
	 * @return
	 */
	public String getJSONOfAllAddress(User user)
	{
		List<Address> addressList = findAllAddress(user);
		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[]{"user","orders"});
		JSONArray jsonArray = JSONArray.fromObject(addressList,jc);		
		return jsonArray.toString();
	}
	
	/**
	 * 删除某条地址
	 * 
	 * @param address
	 *            地址对象
	 */
	public void deleteAddress(Address address) {
		System.out.println("deleteAddress call...");
		baseDao.delete(address);
	}

	/**
	 * 设置默认地址,并返回该对象
	 * 
	 * @param user
	 * @param id
	 */
	public Address setDefaultAddr(User user, int id) {
		// 1表示默认地址
		
		Address defaultAddr = getDefaultAddress(user);
		if (null != defaultAddr) {
			defaultAddr.setDefaultAddress("0");
			baseDao.update(defaultAddr);
		}
		Address address = (Address) baseDao.get(Address.class, id);
		address.setDefaultAddress("1");
		baseDao.update(address);
		return address;
	}

	/**
	 * 得到默认地址,若暂时无默认地址，返回null
	 * 
	 * @return defaultAddress 
	 */
	public Address getDefaultAddress(User user) {
		// "1"表示默认地址
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("defaultAddress", "1");
		Address defaultAddress = (Address) baseDao.findByPropertiesUnique(Address.class, map);
		if(null != defaultAddress){
			return defaultAddress;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 判断是否为默认地址
	 * @param address
	 * @return
	 */
	public boolean isDefaultAddr(Address address){
		if(null != address){
			if("1".equals(address.getDefaultAddress())){
				return true;
			}
		}
		return false;
	}
	
	
}
