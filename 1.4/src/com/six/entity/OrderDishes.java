package com.six.entity;

/**
 * OrderDishes entity. @author MyEclipse Persistence Tools
 */

public class OrderDishes implements java.io.Serializable {

	// Fields

	private Integer id;
	private Dishes dishes;
	private Order order;
	private Integer num;

	// Constructors

	/** default constructor */
	public OrderDishes() {
	}

	/** full constructor */
	public OrderDishes(Dishes dishes, Order order, Integer num) {
		this.dishes = dishes;
		this.order = order;
		this.num = num;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Dishes getDishes() {
		return this.dishes;
	}

	public void setDishes(Dishes dishes) {
		this.dishes = dishes;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}