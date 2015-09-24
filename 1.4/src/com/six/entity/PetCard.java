package com.six.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * PetCard entity. @author MyEclipse Persistence Tools
 */

public class PetCard implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Businessman businessman;
	private Double balance;
	private String state;
	private String cardNumber;
	private Timestamp createCardTime;
	private Set businessmans = new HashSet(0);
	private Set consumptionRecords = new HashSet(0);
	private Set users = new HashSet(0);

	// Constructors

	/** default constructor */
	public PetCard() {
	}

	/** full constructor */
	public PetCard(User user, Businessman businessman, Double balance,
			String state, String cardNumber, Timestamp createCardTime,
			Set businessmans, Set consumptionRecords, Set users) {
		this.user = user;
		this.businessman = businessman;
		this.balance = balance;
		this.state = state;
		this.cardNumber = cardNumber;
		this.createCardTime = createCardTime;
		this.businessmans = businessmans;
		this.consumptionRecords = consumptionRecords;
		this.users = users;
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

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Timestamp getCreateCardTime() {
		return this.createCardTime;
	}

	public void setCreateCardTime(Timestamp createCardTime) {
		this.createCardTime = createCardTime;
	}

	public Set getBusinessmans() {
		return this.businessmans;
	}

	public void setBusinessmans(Set businessmans) {
		this.businessmans = businessmans;
	}

	public Set getConsumptionRecords() {
		return this.consumptionRecords;
	}

	public void setConsumptionRecords(Set consumptionRecords) {
		this.consumptionRecords = consumptionRecords;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}