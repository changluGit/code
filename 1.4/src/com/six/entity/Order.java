package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Order entity. @author MyEclipse Persistence Tools
 */

public class Order implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Store store;
	private Courier courier;
	private String orderNum;
	private Double totalAmount;
	private Timestamp createTime;
	private Timestamp completeTime;
	private Integer state;
	private String userMessage;
	private String address;
	private String shouhuoTel;
	private String shouhuoren;
	private String paymentType;
	private Set orderEvaluates = new HashSet(0);
	private Set orderDisheses = new HashSet(0);

	// Constructors

	/** default constructor */
	public Order() {
	}

	/** full constructor */
	public Order(User user, Store store, Courier courier, String orderNum,
			Double totalAmount, Timestamp createTime, Timestamp completeTime,
			Integer state, String userMessage, String address,
			String shouhuoTel, String shouhuoren, Set orderEvaluates,
			Set orderDisheses) {
		this.user = user;
		this.store = store;
		this.courier = courier;
		this.orderNum = orderNum;
		this.totalAmount = totalAmount;
		this.createTime = createTime;
		this.completeTime = completeTime;
		this.state = state;
		this.userMessage = userMessage;
		this.address = address;
		this.shouhuoTel = shouhuoTel;
		this.shouhuoren = shouhuoren;
		this.orderEvaluates = orderEvaluates;
		this.orderDisheses = orderDisheses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Courier getCourier() {
		return this.courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getCompleteTime() {
		return this.completeTime;
	}

	public void setCompleteTime(Timestamp completeTime) {
		this.completeTime = completeTime;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getUserMessage() {
		return this.userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShouhuoTel() {
		return this.shouhuoTel;
	}

	public void setShouhuoTel(String shouhuoTel) {
		this.shouhuoTel = shouhuoTel;
	}

	public String getShouhuoren() {
		return this.shouhuoren;
	}

	public void setShouhuoren(String shouhuoren) {
		this.shouhuoren = shouhuoren;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Set getOrderEvaluates() {
		return this.orderEvaluates;
	}

	public void setOrderEvaluates(Set orderEvaluates) {
		this.orderEvaluates = orderEvaluates;
	}

	public Set getOrderDisheses() {
		return this.orderDisheses;
	}

	public void setOrderDisheses(Set orderDisheses) {
		this.orderDisheses = orderDisheses;
	}

}