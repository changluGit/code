package com.six.entity;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */

public class Collection implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Dishes dishes;
	private Store store;

	// Constructors

	/** default constructor */
	public Collection() {
	}

	/** full constructor */
	public Collection(User user, Dishes dishes, Store store) {
		this.user = user;
		this.dishes = dishes;
		this.store = store;
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

	public Dishes getDishes() {
		return this.dishes;
	}

	public void setDishes(Dishes dishes) {
		this.dishes = dishes;
	}

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}