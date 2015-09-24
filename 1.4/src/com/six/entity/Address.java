package com.six.entity;

/**
 * Address entity. @author MyEclipse Persistence Tools
 */

public class Address implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private String address;
	private String defaultAddress;
	private String shouhuoTell;
	private String shouhuoren;
	private String detailAddress;

	// Constructors

	/** default constructor */
	public Address() {
	}

	/** full constructor */
	public Address(User user, String address, String defaultAddress,
			String shouhuoTell, String shouhuoren, String detailAddress) {
		this.user = user;
		this.address = address;
		this.defaultAddress = defaultAddress;
		this.shouhuoTell = shouhuoTell;
		this.shouhuoren = shouhuoren;
		this.detailAddress = detailAddress;
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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDefaultAddress() {
		return this.defaultAddress;
	}

	public void setDefaultAddress(String defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public String getShouhuoTell() {
		return this.shouhuoTell;
	}

	public void setShouhuoTell(String shouhuoTell) {
		this.shouhuoTell = shouhuoTell;
	}

	public String getShouhuoren() {
		return this.shouhuoren;
	}

	public void setShouhuoren(String shouhuoren) {
		this.shouhuoren = shouhuoren;
	}

	public String getDetailAddress() {
		return this.detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

}