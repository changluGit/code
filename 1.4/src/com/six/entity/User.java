package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private PetCard petCard;
	private String name;
	private String email;
	private String tel;
	private String pwd;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Integer preferencesId;
	private Set orderEvaluates = new HashSet(0);
	private Set userTastes = new HashSet(0);
	private Set collections = new HashSet(0);
	private Set addresses = new HashSet(0);
	private Set complaints = new HashSet(0);
	private Set orders = new HashSet(0);
	private Set petCards = new HashSet(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(PetCard petCard, String name, String email, String tel,
			String pwd, Timestamp createTime, Timestamp updateTime,
			Integer preferencesId, Set orderEvaluates, Set userTastes,
			Set collections, Set addresses, Set complaints, Set orders,
			Set petCards) {
		this.petCard = petCard;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.pwd = pwd;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.preferencesId = preferencesId;
		this.orderEvaluates = orderEvaluates;
		this.userTastes = userTastes;
		this.collections = collections;
		this.addresses = addresses;
		this.complaints = complaints;
		this.orders = orders;
		this.petCards = petCards;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PetCard getPetCard() {
		return this.petCard;
	}

	public void setPetCard(PetCard petCard) {
		this.petCard = petCard;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getPreferencesId() {
		return this.preferencesId;
	}

	public void setPreferencesId(Integer preferencesId) {
		this.preferencesId = preferencesId;
	}

	public Set getOrderEvaluates() {
		return this.orderEvaluates;
	}

	public void setOrderEvaluates(Set orderEvaluates) {
		this.orderEvaluates = orderEvaluates;
	}

	public Set getUserTastes() {
		return this.userTastes;
	}

	public void setUserTastes(Set userTastes) {
		this.userTastes = userTastes;
	}

	public Set getCollections() {
		return this.collections;
	}

	public void setCollections(Set collections) {
		this.collections = collections;
	}

	public Set getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set addresses) {
		this.addresses = addresses;
	}

	public Set getComplaints() {
		return this.complaints;
	}

	public void setComplaints(Set complaints) {
		this.complaints = complaints;
	}

	public Set getOrders() {
		return this.orders;
	}

	public void setOrders(Set orders) {
		this.orders = orders;
	}

	public Set getPetCards() {
		return this.petCards;
	}

	public void setPetCards(Set petCards) {
		this.petCards = petCards;
	}

}