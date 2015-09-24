package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Businessman entity. @author MyEclipse Persistence Tools
 */

public class Businessman implements java.io.Serializable {

	// Fields

	private Integer id;
	private PetCard petCard;
	private Store store;
	private Integer businessmanNum;
	private String username;
	private String email;
	private String tell;
	private String password;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String idCard;
	private Set orderEvaluates = new HashSet(0);
	private Set petCards = new HashSet(0);
	private Set complaints = new HashSet(0);
	private Set stores = new HashSet(0);

	// Constructors

	/** default constructor */
	public Businessman() {
	}

	/** full constructor */
	public Businessman(PetCard petCard, Store store, Integer businessmanNum,
			String username, String email, String tell, String password,
			Timestamp createTime, Timestamp updateTime, String idCard,
			Set orderEvaluates, Set petCards, Set complaints, Set stores) {
		this.petCard = petCard;
		this.store = store;
		this.businessmanNum = businessmanNum;
		this.username = username;
		this.email = email;
		this.tell = tell;
		this.password = password;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.idCard = idCard;
		this.orderEvaluates = orderEvaluates;
		this.petCards = petCards;
		this.complaints = complaints;
		this.stores = stores;
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

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Integer getBusinessmanNum() {
		return this.businessmanNum;
	}

	public void setBusinessmanNum(Integer businessmanNum) {
		this.businessmanNum = businessmanNum;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTell() {
		return this.tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Set getOrderEvaluates() {
		return this.orderEvaluates;
	}

	public void setOrderEvaluates(Set orderEvaluates) {
		this.orderEvaluates = orderEvaluates;
	}

	public Set getPetCards() {
		return this.petCards;
	}

	public void setPetCards(Set petCards) {
		this.petCards = petCards;
	}

	public Set getComplaints() {
		return this.complaints;
	}

	public void setComplaints(Set complaints) {
		this.complaints = complaints;
	}

	public Set getStores() {
		return this.stores;
	}

	public void setStores(Set stores) {
		this.stores = stores;
	}

}