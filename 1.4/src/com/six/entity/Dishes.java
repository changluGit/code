package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * Dishes entity. @author MyEclipse Persistence Tools
 */

public class Dishes  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Store store;
     private DishesCategory dishesCategory;
     private String name;
     private Integer price;
     private Integer monthSales;
     private Integer goodNum;
     private String pictureAddress;
     private Timestamp creTime;
     private Timestamp modTime;
     private String status;
     private String detail;
     private String taste;
     private String dishesCategoryName;
     private Set collections = new HashSet(0);
     private Set orderDisheses = new HashSet(0);


    // Constructors

    /** default constructor */
    public Dishes() {
    }

    
    /** full constructor */
    public Dishes(Store store, DishesCategory dishesCategory, String name, Integer price, Integer monthSales, Integer goodNum, String pictureAddress, Timestamp creTime, Timestamp modTime, String status, String detail, String taste, Set collections, Set orderDisheses) {
        this.store = store;
        this.dishesCategory = dishesCategory;
        this.name = name;
        this.price = price;
        this.monthSales = monthSales;
        this.goodNum = goodNum;
        this.pictureAddress = pictureAddress;
        this.creTime = creTime;
        this.modTime = modTime;
        this.status = status;
        this.detail = detail;
        this.taste = taste;
        this.collections = collections;
        this.orderDisheses = orderDisheses;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Store getStore() {
        return this.store;
    }
    
    public void setStore(Store store) {
        this.store = store;
    }

    public DishesCategory getDishesCategory() {
        return this.dishesCategory;
    }
    
    public void setDishesCategory(DishesCategory dishesCategory) {
        this.dishesCategory = dishesCategory;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return this.price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMonthSales() {
        return this.monthSales;
    }
    
    public void setMonthSales(Integer monthSales) {
        this.monthSales = monthSales;
    }

    public Integer getGoodNum() {
        return this.goodNum;
    }
    
    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public String getPictureAddress() {
        return this.pictureAddress;
    }
    
    public void setPictureAddress(String pictureAddress) {
        this.pictureAddress = pictureAddress;
    }

    public Timestamp getCreTime() {
        return this.creTime;
    }
    
    public void setCreTime(Timestamp creTime) {
        this.creTime = creTime;
    }

    public Timestamp getModTime() {
        return this.modTime;
    }
    
    public void setModTime(Timestamp modTime) {
        this.modTime = modTime;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return this.detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTaste() {
        return this.taste;
    }
    
    public void setTaste(String taste) {
        this.taste = taste;
    }

    
    
    public String getDishesCategoryName() {
		return dishesCategoryName;
	}


	public void setDishesCategoryName(String dishesCategoryName) {
		this.dishesCategoryName = dishesCategoryName;
	} 


	public Set getCollections() {
        return this.collections;
    }
    
    public void setCollections(Set collections) {
        this.collections = collections;
    }

    public Set getOrderDisheses() {
        return this.orderDisheses;
    }
    
    public void setOrderDisheses(Set orderDisheses) {
        this.orderDisheses = orderDisheses;
    }
   








}