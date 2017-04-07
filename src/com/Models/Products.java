package com.Models;

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Entity("product")
public class Products {
	
	private String productName;
	private String productDescription;
	private String productCategory;
	private java.math.BigDecimal costPrice;
	private java.math.BigDecimal sellingPrice;
	private String productHOI;
	private String productHOE;
	private boolean isDeleted;
	
	@Reference
	private Company company;
	
	@Reference
	private Tax productTax;
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public Products(String productName, String productDescription, String productCategory, BigDecimal costPrice,
			BigDecimal sellingPrice, String productHOI, Company company, Tax productTax, String productHOE, boolean isDeleted) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.productCategory = productCategory;
		this.costPrice = costPrice;
		this.sellingPrice = sellingPrice;
		this.productHOI = productHOI;
		this.company = company;
		this.productTax = productTax;
		this.productHOE = productHOE;
		this.isDeleted = isDeleted;
	}

	public Products() {
		
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public java.math.BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(java.math.BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public java.math.BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(java.math.BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getProductHOI() {
		return productHOI;
	}

	public void setProductHOI(String productHOI) {
		this.productHOI = productHOI;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Tax getProductTax() {
		return productTax;
	}

	public void setProductTax(Tax productTax) {
		this.productTax = productTax;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getProductHOE() {
		return productHOE;
	}

	public void setProductHOE(String productHOE) {
		this.productHOE = productHOE;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setisDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
