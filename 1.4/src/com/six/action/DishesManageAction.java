package com.six.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


import com.six.constant.ResultType;
import com.six.entity.Dishes;
import com.six.entity.DishesCategory;
import com.six.entity.Store;
import com.six.service.DishesCategoryService;
import com.six.service.DishesManageService;
import com.six.service.ImgService;
import com.six.util.JSONUtil;
import com.six.util.StringUtil;

public class DishesManageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DishesManageService dishesManageService;
	private DishesCategoryService categoryService;

	private List<File> file;

	private List<String> fileFileName;

	private Store store;

	/**
	 * 添加菜品
	 * 
	 */
	public void addDishes() throws Exception {
		// 菜品名
		String name = "";
		// 价格
		int price = 0;
		// 菜品图片地址
		String pictureAddress = "";
		// 菜品类别名
		String dcName = "";
		// 详细介绍
		String detail = "";
		// 口味
		String taste = "";

		store = (Store) getRequest().getSession().getAttribute("loginedStore");

		if (null != store) {
			if (null != getRequest().getParameter("name")
					&& null != getRequest().getParameter("price")
					&& null != getRequest().getParameter("dishesCategory")) {
				name = getRequest().getParameter("name");
				if (null == dishesManageService.getDishesByName(store, name)) {
					if (StringUtil
							.isInteger(getRequest().getParameter("price"))) {
						price = Integer.parseInt(getRequest().getParameter(
								"price"));
					}
					detail = getRequest().getParameter("detail");
					taste = getRequest().getParameter("taste");
					dcName = getRequest().getParameter("dishesCategory");
					DishesCategory dishesCategory = categoryService
							.getDishesByNameAndStore(store, dcName);

					Dishes dishes = new Dishes();
					dishes.setName(name);
					dishes.setPrice(price);
                    dishes.setGoodNum(0);
                    dishes.setMonthSales(0);
                    dishes.setStatus("在售");
					dishes.setStore(store);
					dishes.setDetail(detail);
					dishes.setTaste(taste);
					dishes.setDishesCategory(dishesCategory);
					
					if (null != file && file.size() != 0) {
						pictureAddress = ImgService.uploadImg(store, file);
					}
					dishes.setPictureAddress(pictureAddress);
					dishesManageService.addDishes(dishes);
					toWrite(JSONUtil.returnResultJson(
							ResultType.Success.toString(), "添加成功"));
				} else {
					toWrite(JSONUtil.returnResultJson(
							ResultType.Failure.toString(), "已有同名的菜品"));
				}

			}

		}

	}

	/**
	 * 删除某条菜品
	 * 
	 */
	public void deleteDishes() {
		if (null != getRequest().getParameter("ids")) {
			String ids = getRequest().getParameter("ids");
			String[] idArray = ids.split(",");
			for (int i = 0; i < idArray.length; i++) {
				int id = Integer.parseInt(idArray[i]);
				Dishes dishes = new Dishes();
				dishes.setId(id);
				dishesManageService.deleteDishes(dishes);
			}
			toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(),
					"删除成功"));
		}
	}

	/**
	 * 修改菜品信息
	 * 
	 */
	public void updateDishes() throws Exception{
		System.out.println("enter updateDishes");
		int id = 0;
		// 菜品名
		String name = "";
		// 价格
		int price = 0;
		// 菜品图片地址
		String pictureAddress = "";
		// 菜品类别名
		String dcName = "";
		// 详细介绍
		String detail = "";
		// 口味
		String taste = "";
		store = (Store) getRequest().getSession().getAttribute("loginedStore");
		if (null != store) {
			if (null != getRequest().getParameter("name")
					&& null != getRequest().getParameter("price")
					&& null != getRequest().getParameter("id")
					&& null != getRequest().getParameter("dishesCategory")) {
				detail = getRequest().getParameter("detail");
				taste = getRequest().getParameter("taste");
				name = getRequest().getParameter("name");

				if (StringUtil.isInteger(getRequest().getParameter("price"))) {
					price = Integer
							.parseInt(getRequest().getParameter("price"));
				}

				dcName = getRequest().getParameter("dishesCategory");
				DishesCategory dishesCategory = categoryService
						.getDishesByNameAndStore(store, dcName);
				id = Integer.parseInt(getRequest().getParameter("id"));
				Dishes dishesByid = dishesManageService.getDishesById(id);
				Dishes dishesByName = dishesManageService.getDishesByName(store, name);
				
				if (null == dishesByName || dishesByid.getId() == dishesByName.getId()) {
					dishesByid.setName(name);
					dishesByid.setPrice(price);
					dishesByid.setDetail(detail);
					dishesByid.setTaste(taste);
					dishesByid.setDishesCategory(dishesCategory);
					if (null != file && file.size() != 0) {
						pictureAddress = ImgService.uploadImg(store, file);
						dishesByid.setPictureAddress(pictureAddress);
					}
					
					dishesManageService.updateDishes(dishesByid);
					toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(), "修改成功"));
				} else {
					toWrite(JSONUtil.returnResultJson(ResultType.Failure.toString(), "已有同名的菜品"));
				}

			}

		}
	}

	/**
	 * 修改菜品状态
	 * 
	 */
	public void upDateStatus() {
		if (null != getRequest().getParameter("ids")) {
			String ids = getRequest().getParameter("ids");
			System.out.println("ids=" + ids);
			String[] idArray = ids.split(",");
			for (int i = 0; i < idArray.length; i++) {
				System.out.println("idArray[i]=" + idArray[i]);
				int id = Integer.parseInt(idArray[i]);
				Dishes dishes = dishesManageService.getDishesById(id);
				if (null != getRequest().getParameter("status")
						&& !"".equals(getRequest().getParameter("status"))) {
					String status = getRequest().getParameter("status");
					dishes.setStatus(status);
					dishesManageService.updateDishes(dishes);
				}

			}
			toWrite(JSONUtil.returnResultJson(ResultType.Success.toString(),
					"状态更改成功"));
		}
	}

	/**
	 * 查找菜品结果
	 * 
	 * @return List 菜品列表 json
	 */
	public void getSearchResult(List<Dishes> list,int page,int rows) {

		if (null != list) {
			for(int i=0;i<list.size();i++){
				DishesCategory category = list.get(i).getDishesCategory();
				String dishesCategoryName = category.getName();
				list.get(i).setDishesCategoryName(dishesCategoryName);
				
			}
			List<Dishes> templist;
			if (list.size() < page * rows) {
				templist = list.subList((page - 1) * rows, list.size());
			} else {
				templist = list.subList((page - 1) * rows, page * rows);
			}

			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("rows", templist);
			map.put("total", list.size());
			JsonConfig jc = new JsonConfig();
			jc.setExcludes(new String[] { "store", "dishesCategory","collections", "orderDisheses" });
			JSONObject jsonObject = JSONObject.fromObject(map, jc);
			toWrite(jsonObject.toString());
		}

	}

	/**
	 * 根据菜品名或状态查询菜品信息
	 * 
	 * @return json格式
	 */
	public void getDishesByNameOrStatus() {

		System.out.println("enter getDishesByNameOrStatus");
		int page = Integer.parseInt(getRequest().getParameter("page"));
		int rows = Integer.parseInt(getRequest().getParameter("rows"));
		String name = getRequest().getParameter("name");
		String status = getRequest().getParameter("status");
		System.out.println("page:"+ page);
		System.out.println("name:"+ name);
		System.out.println("status:"+ status);
		store = (Store) getRequest().getSession().getAttribute("loginedStore");
		//根据名字进行模糊查询
		if ("".equals(status) || null == status) {
			if (!"".equals(name) && null != name) {
				List<Dishes> list = dishesManageService.getDishesByfuzzy(store, name, 25);
				getSearchResult(list, page, rows);
				
			}
		}
		//根据状态进行查询
		if ("".equals(name) || null == name) {
			if (!"".equals(status) && null != status) {
				System.out.println("根据状态进行查询");
				List<Dishes> list = dishesManageService.getDishesByStatus(store, status);
				getSearchResult(list, page, rows);
			}
		}
		//根据状态和菜品名进行联合查询
		if(!"".equals(status) && null != status && !"".equals(name) && null != name){
			System.out.println("根据状态和菜品名进行联合查询");
			List<Dishes> list = dishesManageService.getDishesByNameAndStatus(store, name, status);
			getSearchResult(list, page, rows);
		}
		
		//若name和status均为空，则得到全部菜品
		if(null == status && null ==name || ("".equals(name) && "".equals(status))){
			System.out.println("name和status均为空字符串");
			List<Dishes> list = dishesManageService.findAllDishes(store);
			getSearchResult(list, page, rows);
		}

	}
	

	public String init() {
		return "input";
	}

	public DishesManageService getDishesManageService() {
		return dishesManageService;
	}

	@Resource
	public void setDishesManageService(DishesManageService dishesManageService) {
		this.dishesManageService = dishesManageService;
	}

	public DishesCategoryService getCategoryService() {
		return categoryService;
	}

	@Resource
	public void setCategoryService(DishesCategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public List<File> getFile() {
		return file;
	}

	public void setFile(List<File> file) {
		this.file = file;
	}

	public List<String> getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}

	@Override
	public Object getModel() {
		return null;
	}

}
