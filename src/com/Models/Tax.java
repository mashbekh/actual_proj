package com.Models;

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Entity("tax")
public class Tax {

	private String taxType;
	private String taxName;
	private String taxAbbreviation;
	private String taxDescription;
	private java.math.BigDecimal taxRate;
	private boolean isDeleted;
	
	@Reference
	@JsonIgnore
	Company company;
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public Tax(String taxType,String taxName, String taxAbbreviation, String taxDescription, BigDecimal taxRate, Boolean isDeleted, Company company) {
		this.taxType = taxType;
		this.taxName = taxName;
		this.taxAbbreviation = taxAbbreviation;
		this.taxDescription = taxDescription;
		this.taxRate = taxRate;
		this.isDeleted = isDeleted;
		this.company = company;
	}

	public Tax() {
		
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getTaxAbbreviation() {
		return taxAbbreviation;
	}

	public void setTaxAbbreviation(String taxAbbreviation) {
		this.taxAbbreviation = taxAbbreviation;
	}

	public String getTaxDescription() {
		return taxDescription;
	}

	public void setTaxDescription(String taxDescription) {
		this.taxDescription = taxDescription;
	}

	public java.math.BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(java.math.BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setisDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
	
	
}
