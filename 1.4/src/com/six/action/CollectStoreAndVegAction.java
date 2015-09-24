package com.six.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.six.entity.Collection;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.CollectStoreAndVegService;
import com.six.service.store.StoreShowService;

@Component
public class CollectStoreAndVegAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CollectStoreAndVegService collectStoreAndVegService;

	private Collection collect;
	@SuppressWarnings("rawtypes")
	private List collects;

	@SuppressWarnings("rawtypes")
	public List getCollects() {
		return collects;
	}

	public void setCollects(@SuppressWarnings("rawtypes") List collects) {
		this.collects = collects;
	}

	private User user;
	private Store store;
	private StoreShowService storeShowService;
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer userid;
	private Integer storeid;
	private Integer dishid;

	public Integer getDishid() {
		return dishid;
	}

	public void setDishid(Integer dishid) {
		this.dishid = dishid;
	}

	private Logger logger = Logger.getLogger(CollectStoreAndVegAction.class);

	public Collection getCollect() {
		return collect;
	}

	public void setCollect(Collection collect) {
		this.collect = collect;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getStoreid() {
		return storeid;
	}

	public void setStoreid(Integer storeid) {
		this.storeid = storeid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public CollectStoreAndVegService getCollectStoreAndVegService() {
		return collectStoreAndVegService;
	}

	@Resource
	public void setCollectStoreAndVegService(
			CollectStoreAndVegService collectStoreAndVegService) {
		this.collectStoreAndVegService = collectStoreAndVegService;
	}

	public StoreShowService getStoreShowService() {
		return storeShowService;
	}

	@Resource
	public void setStoreShowService(StoreShowService storeShowService) {
		this.storeShowService = storeShowService;
	}

	/* 查找收藏的菜品的所有记录 */
	@SuppressWarnings({})
	public String findCollectVeg() {

		User loginingUser = (User) getRequest().getSession().getAttribute(
				"loginedUser");
		collects = collectStoreAndVegService.findAllCollect(loginingUser);

		if (collects.size() == 0) {
			getRequest().setAttribute("beNull", "true");

		}

		else {
			for (int i = collects.size() - 1; i >= 0; i--) {
				if (((Collection) collects.get(i)).getDishes() == null) {
					collects.remove(collects.get(i));
				}
			}
			if (collects.size() == 0) {
				getRequest().setAttribute("beNull", "true");

			}

		}

		return SUCCESS;
	}

	/* 查找收藏的餐厅的所有记录 */
	@SuppressWarnings({})
	public String findCollectStore()

	{
		User loginingUser = (User) getRequest().getSession().getAttribute(
				"loginedUser");
		collects = collectStoreAndVegService.findAllCollect(loginingUser);

		if (collects.size() == 0) {
			getRequest().setAttribute("beNull", "true");

		} else {

			for (int j = collects.size() - 1; j >= 0; j--) {
				if (((Collection) collects.get(j)).getStore() == null) {
					collects.remove(collects.get(j));
				}

			}
			if (collects.size() == 0) {
				getRequest().setAttribute("beNull", "true");

			}

		}

		return SUCCESS;
	}

	/* 在我的收藏中删除一条收藏餐厅的记录 */
	public String deleteCollectStore() {

		collectStoreAndVegService.deleteCollectStore(storeid);
		return SUCCESS;
	}

	/* 在我的收藏中删除一条收藏菜品的记录 */
	public String deleteCollectVeg() {
		collectStoreAndVegService.deleteCollectVeg1(dishid);
		return SUCCESS;
	}

}
