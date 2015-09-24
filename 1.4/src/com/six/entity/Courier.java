package com.six.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Courier entity. @author MyEclipse Persistence Tools
 */

public class Courier implements java.io.Serializable {

	// Fields

	private Integer id;
	private Store store;
	private String courierName;
	private String courierTel;
	private Set orders = new HashSet(0);

	// Constructors

	/** default constructor */
	public Courier() {
	}

	/** full constructor */
	public Courier(Store store, String courierName, String courierTel,
			Set orders) {
		this.store = store;
		this.courierName = courierName;
		this.courierTel = courierTel;
		this.orders = orders;
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

	public String getCourierName() {
		return this.courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierTel() {
		return this.courierTel;
	}

	public void setCourierTel(String courierTel) {
		this.courierTel = courierTel;
	}

	public Set getOrders() {
		return this.orders;
	}

	public void setOrders(Set orders) {
		this.orders = orders;
	}

}