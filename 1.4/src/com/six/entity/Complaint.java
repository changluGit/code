package com.six.entity;

import java.sql.Timestamp;

/**
 * Complaint entity. @author MyEclipse Persistence Tools
 */

public class Complaint implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Businessman businessman;
	private String content;
	private String status;
	private String processResult;
	private Timestamp creatTime;
	private Timestamp processTime;

	// Constructors

	/** default constructor */
	public Complaint() {
	}

	/** full constructor */
	public Complaint(User user, Businessman businessman, String content,
			String status, String processResult, Timestamp creatTime,
			Timestamp processTime) {
		this.user = user;
		this.businessman = businessman;
		this.content = content;
		this.status = status;
		this.processResult = processResult;
		this.creatTime = creatTime;
		this.processTime = processTime;
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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessResult() {
		return this.processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public Timestamp getCreatTime() {
		return this.creatTime;
	}

	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}

	public Timestamp getProcessTime() {
		return this.processTime;
	}

	public void setProcessTime(Timestamp processTime) {
		this.processTime = processTime;
	}

}