package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Store entity. @author MyEclipse Persistence Tools
 */

public class Store implements java.io.Serializable {

	// Fields

	private Integer id;
	private Businessman businessman;
	private String storeName;
	private String telephone;
	private String regid;
	private String logoAddress;
	private String businessAddress;
	private String chargePerson;
	private String state;
	private Double rating;
	private Integer foodDeliveryTime;
	private String merchantsAnnouncement;
	private Integer ternover;
	private Integer saleVolume;
	private Timestamp beginSaleHour;
	private Timestamp endSaleTime;
	private Integer collection;
	private Timestamp creatTime;
	private Timestamp modificationTime;
	private String storeCategory;
	private String taste;
	private Set collections = new HashSet(0);
	private Set businessmans = new HashSet(0);
	private Set disheses = new HashSet(0);
	private Set couriers = new HashSet(0);
	private Set orders = new HashSet(0);
	private Set dishesCategories = new HashSet(0);

	// Constructors

	/** default constructor */
	public Store() {
	}

	/** full constructor */
	public Store(Businessman businessman, String storeName, String telephone,
			String regid, String logoAddress, String businessAddress,
			String chargePerson, String state, Double rating,
			Integer foodDeliveryTime, String merchantsAnnouncement,
			Integer ternover, Integer saleVolume, Timestamp beginSaleHour,
			Timestamp endSaleTime, Integer collection, Timestamp creatTime,
			Timestamp modificationTime, String storeCategory, String taste,
			Set collections, Set businessmans, Set disheses, Set couriers,
			Set orders, Set dishesCategories) {
		this.businessman = businessman;
		this.storeName = storeName;
		this.telephone = telephone;
		this.regid = regid;
		this.logoAddress = logoAddress;
		this.businessAddress = businessAddress;
		this.chargePerson = chargePerson;
		this.state = state;
		this.rating = rating;
		this.foodDeliveryTime = foodDeliveryTime;
		this.merchantsAnnouncement = merchantsAnnouncement;
		this.ternover = ternover;
		this.saleVolume = saleVolume;
		this.beginSaleHour = beginSaleHour;
		this.endSaleTime = endSaleTime;
		this.collection = collection;
		this.creatTime = creatTime;
		this.modificationTime = modificationTime;
		this.storeCategory = storeCategory;
		this.taste = taste;
		this.collections = collections;
		this.businessmans = businessmans;
		this.disheses = disheses;
		this.couriers = couriers;
		this.orders = orders;
		this.dishesCategories = dishesCategories;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Businessman getBusinessman() {
		return this.businessman;
	}

	public void setBusinessman(Businessman businessman) {
		this.businessman = businessman;
	}

	public String getStoreName() {
		return this.storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRegid() {
		return this.regid;
	}

	public void setRegid(String regid) {
		this.regid = regid;
	}

	public String getLogoAddress() {
		return this.logoAddress;
	}

	public void setLogoAddress(String logoAddress) {
		this.logoAddress = logoAddress;
	}

	public String getBusinessAddress() {
		return this.businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getChargePerson() {
		return this.chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Double getRating() {
		return this.rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Integer getFoodDeliveryTime() {
		return this.foodDeliveryTime;
	}

	public void setFoodDeliveryTime(Integer foodDeliveryTime) {
		this.foodDeliveryTime = foodDeliveryTime;
	}

	public String getMerchantsAnnouncement() {
		return this.merchantsAnnouncement;
	}

	public void setMerchantsAnnouncement(String merchantsAnnouncement) {
		this.merchantsAnnouncement = merchantsAnnouncement;
	}

	public Integer getTernover() {
		return this.ternover;
	}

	public void setTernover(Integer ternover) {
		this.ternover = ternover;
	}

	public Integer getSaleVolume() {
		return this.saleVolume;
	}

	public void setSaleVolume(Integer saleVolume) {
		this.saleVolume = saleVolume;
	}

	public Timestamp getBeginSaleHour() {
		return this.beginSaleHour;
	}

	public void setBeginSaleHour(Timestamp beginSaleHour) {
		this.beginSaleHour = beginSaleHour;
	}

	public Timestamp getEndSaleTime() {
		return this.endSaleTime;
	}

	public void setEndSaleTime(Timestamp endSaleTime) {
		this.endSaleTime = endSaleTime;
	}

	public Integer getCollection() {
		return this.collection;
	}

	public void setCollection(Integer collection) {
		this.collection = collection;
	}

	public Timestamp getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}

	public Timestamp getModificationTime() {
		return this.modificationTime;
	}

	public void setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getStoreCategory() {
		return this.storeCategory;
	}

	public void setStoreCategory(String storeCategory) {
		this.storeCategory = storeCategory;
	}

	public String getTaste() {
		return this.taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public Set getCollections() {
		return this.collections;
	}

	public void setCollections(Set collections) {
		this.collections = collections;
	}

	public Set getBusinessmans() {
		return this.businessmans;
	}

	public void setBusinessmans(Set businessmans) {
		this.businessmans = businessmans;
	}

	public Set getDisheses() {
		return this.disheses;
	}

	public void setDisheses(Set disheses) {
		this.disheses = disheses;
	}

	public Set getCouriers() {
		return this.couriers;
	}

	public void setCouriers(Set couriers) {
		this.couriers = couriers;
	}

	public Set getOrders() {
		return this.orders;
	}

	public void setOrders(Set orders) {
		this.orders = orders;
	}

	public Set getDishesCategories() {
		return this.dishesCategories;
	}

	public void setDishesCategories(Set dishesCategories) {
		this.dishesCategories = dishesCategories;
	}

}