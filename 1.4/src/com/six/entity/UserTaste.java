package com.six.entity;

/**
 * UserTaste entity. @author MyEclipse Persistence Tools
 */

public class UserTaste implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Taste taste;

	// Constructors

	/** default constructor */
	public UserTaste() {
	}

	/** full constructor */
	public UserTaste(User user, Taste taste) {
		this.user = user;
		this.taste = taste;
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

	public Taste getTaste() {
		return this.taste;
	}

	public void setTaste(Taste taste) {
		this.taste = taste;
	}

}