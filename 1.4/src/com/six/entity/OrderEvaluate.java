package com.six.entity;

import java.sql.Timestamp;

/**
 * OrderEvaluate entity. @author MyEclipse Persistence Tools
 */

public class OrderEvaluate implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Businessman businessman;
	private Order order;
	private String evaluateContent;
	private String evaluateCategory;
	private String state;
	private String businessmanReply;
	private Timestamp updateTime;

	// Constructors

	/** default constructor */
	public OrderEvaluate() {
	}

	/** full constructor */
	public OrderEvaluate(User user, Businessman businessman, Order order,
			String evaluateContent, String evaluateCategory, String state,
			String businessmanReply) {
		this.user = user;
		this.businessman = businessman;
		this.order = order;
		this.evaluateContent = evaluateContent;
		this.evaluateCategory = evaluateCategory;
		this.state = state;
		this.businessmanReply = businessmanReply;
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

	public Businessman getBusinessman() {
		return this.businessman;
	}

	public void setBusinessman(Businessman businessman) {
		this.businessman = businessman;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getEvaluateContent() {
		return this.evaluateContent;
	}

	public void setEvaluateContent(String evaluateContent) {
		this.evaluateContent = evaluateContent;
	}

	public String getEvaluateCategory() {
		return this.evaluateCategory;
	}

	public void setEvaluateCategory(String evaluateCategory) {
		this.evaluateCategory = evaluateCategory;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBusinessmanReply() {
		return this.businessmanReply;
	}

	public void setBusinessmanReply(String businessmanReply) {
		this.businessmanReply = businessmanReply;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}