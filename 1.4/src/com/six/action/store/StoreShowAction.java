package com.six.action.store;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.six.action.BaseAction;
import com.six.constant.ResultType;
import com.six.entity.Address;
import com.six.entity.Collection;
import com.six.entity.Dishes;
import com.six.entity.Store;
import com.six.entity.User;
import com.six.service.AddressManageService;
import com.six.service.CollectStoreAndVegService;
import com.six.service.store.DishesService;
import com.six.service.store.StoreShowService;
import com.six.util.JSONUtil;

/**
 * 处理 首页和选餐页面的商家显示
 * @author 828477
 *
 */
@Component
public class StoreShowAction extends BaseAction
{
	//默认使用销量作为 排序
	private String sort = "saleVolume";
	private String type = "中餐";
	
	private StoreShowService storeShowService;
	private CollectStoreAndVegService collectStoreAndVegService;
	private DishesService dishesService;
	private AddressManageService addressManageService;
	@Override
	public Object getModel()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	//跳转到商家销售界面
	public String gotoStorePage()
	{
		
		
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		
		String storeIdStr = getRequest().getParameter("storeId");
		
		Store oldVisitedStore = (Store)getRequest().getSession().getAttribute("visitedStore");
		if(null != oldVisitedStore)
		{
			if(oldVisitedStore.getId() != Integer.parseInt(storeIdStr))
			{
				//清空购物车
				getRequest().getSession().removeAttribute("totalPrice");
				getRequest().getSession().removeAttribute("totalPart");
				getRequest().getSession().removeAttribute("temOrderDish");
			}
				
		}
	
		Store visitedStore = storeShowService.getStoreById(storeIdStr);
		//异常导致访问的store没被查询到。则返回home页面
		if(visitedStore == null)
			return "home";
		//讲正在访问的store 放到session中
		HttpSession session = getRequest().getSession();
		session.setAttribute("visitedStore", visitedStore);
		
		//处理店铺是否被收藏
		if(loginedUser != null)
		{
	        Collection collect = collectStoreAndVegService.getCollectionByUserAndStore(loginedUser, visitedStore);
			 if(collect!=null){
				  getRequest().setAttribute("change", "[取消收藏]");
	            }
			 else
			 {
				 getRequest().setAttribute("change", "[收藏]");
			 }
		}
		
		return "store";
	}
	
	/**
	 * home页中查询符合条件的store，并返回给前台
	 */
	public void showStore()
	{
		//直接拿到默认的地址
		String addressDefaultStr = (String)getRequest().getSession().getAttribute("defaultAddress");
			
		
		toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), storeShowService.retrievalStore(type, sort,addressDefaultStr)));
	}
	
	/**
	 * 处理用户的输入检索商家
	 */
	public void inputSearchHandle()
	{
		
		String searchContext = getRequest().getParameter("storeName");
		
		toWrite(storeShowService.getStoreByInputSearch(searchContext));
	}
	
	/**
	 * 处理 商店的 收藏点击
	 */
	public void toggleCollectStore()
	{
		
			 
	         User loginingUser=(User)getRequest().getSession().getAttribute("loginedUser");
	         if(null == loginingUser)
	         {
	        	 toWrite(JSONUtil.returnResultJson("Failure", "请登录后收藏"));
	        	 return;
	         }
	        	 
	         Store visitedStore=(Store)getRequest().getSession().getAttribute("visitedStore");
	         
	         toWrite(collectStoreAndVegService.toggleCollectionHandle(loginingUser, visitedStore, null));
		
	}
	
	/**
	 * 处理菜品的 收藏点击
	 */
	public void toggleCollectDish()
	{
		User loginingUser=(User)getRequest().getSession().getAttribute("loginedUser");
        if(null == loginingUser)
        {
       	 toWrite(JSONUtil.returnResultJson("Failure", "请登录后收藏"));
       	 return;
        }
       	 
       //拿到 需要收藏的菜品
        String dishIdStr = getRequest().getParameter("dishId");
        if(StringUtils.isBlank(dishIdStr))
        {
        	 toWrite(JSONUtil.returnResultJson("Failure", "参数非法"));
           	 return;
        }
        Dishes dishes = dishesService.getDishesByid(Integer.parseInt(dishIdStr));
        
        toWrite(collectStoreAndVegService.toggleCollectionHandle(loginingUser, null, dishes));
	}
	
	/**
	 * header 页面的地址切换
	 */
	public void addressSwitch()
	{
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		if(null ==loginedUser)
		{
			toWrite(JSONUtil.returnResultJson("Failure", "没有登录，点击确认后跳转到地址输入界面"));
			return;
		}
		//回写 所有地址的address数据
		toWrite(JSONUtil.returnResultJson("Success", addressManageService.getJSONOfAllAddress(loginedUser)));
	}
	
	/**
	 *home页面中，推荐菜品 跟换一批的相应处理
	 *
	 */
	public void getDishRecommend()
	{
		String turnStr = getRequest().getParameter("turn");
		if(StringUtils.isBlank(turnStr)) {
			turnStr = "0";
		}
		Integer turnNew = Integer.parseInt(turnStr);
		
		User loginedUser = (User)getRequest().getSession().getAttribute("loginedUser");
		//拿到默认地址
		String defaultAddress = (String)getRequest().getSession().getAttribute("defaultAddress");
		
		ArrayList<Dishes> dishesGroup = (ArrayList<Dishes>) dishesService.getRecomendDishes(loginedUser, turnNew,defaultAddress);
		
		//回写到前台 需要的推荐菜品的HTML
		toWrite(storeShowService.getDishRecommendByHtml(dishesGroup, ++turnNew));
	}
	
	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public StoreShowService getStoreShowService()
	{
		return storeShowService;
	}

	@Resource
	public void setStoreShowService(StoreShowService storeShowService)
	{
		this.storeShowService = storeShowService;
	}

	public CollectStoreAndVegService getCollectionService()
	{
		return collectStoreAndVegService;
	}
	
	@Resource
	public void setCollectionService(CollectStoreAndVegService collectionService)
	{
		this.collectStoreAndVegService = collectionService;
	}

	public DishesService getDishesService()
	{
		return dishesService;
	}
	@Resource
	public void setDishesService(DishesService dishesService)
	{
		this.dishesService = dishesService;
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

	public AddressManageService getAddressManageService()
	{
		return addressManageService;
	}
	@Resource
	public void setAddressManageService(AddressManageService addressManageService)
	{
		this.addressManageService = addressManageService;
	}
	
	
	
}
