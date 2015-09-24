package com.six.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Taste entity. @author MyEclipse Persistence Tools
 */

public class Taste implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Set userTastes = new HashSet(0);

	// Constructors

	/** default constructor */
	public Taste() {
	}

	/** full constructor */
	public Taste(String name, Set userTastes) {
		this.name = name;
		this.userTastes = userTastes;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set getUserTastes() {
		return this.userTastes;
	}

	public void setUserTastes(Set userTastes) {
		this.userTastes = userTastes;
	}

}