package com.six.entity;

import java.sql.Timestamp;

/**
 * ConsumptionRecord entity. @author MyEclipse Persistence Tools
 */

public class ConsumptionRecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private PetCard petCard;
	private Integer consumptionAmount;
	private Timestamp occurrenceTime;

	// Constructors

	/** default constructor */
	public ConsumptionRecord() {
	}

	/** full constructor */
	public ConsumptionRecord(PetCard petCard, Integer consumptionAmount,
			Timestamp occurrenceTime) {
		this.petCard = petCard;
		this.consumptionAmount = consumptionAmount;
		this.occurrenceTime = occurrenceTime;
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

	public Integer getConsumptionAmount() {
		return this.consumptionAmount;
	}

	public void setConsumptionAmount(Integer consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public Timestamp getOccurrenceTime() {
		return this.occurrenceTime;
	}

	public void setOccurrenceTime(Timestamp occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

}