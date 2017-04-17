package com.Models;

import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Embedded
public class OrderDetails {
	
	@Reference 
	private Products product;
	
	private String description;
	private Long quantity;
	private java.math.BigDecimal price;
	private java.math.BigDecimal discount;
	
	@Reference
	private Tax tax;
	
	private java.math.BigDecimal subTotal;
	private java.math.BigDecimal taxTotal;
	
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public OrderDetails(Products product, String description, Long quantity, BigDecimal price, BigDecimal discount,
			Tax tax, BigDecimal subTotal, BigDecimal taxTotal , ObjectId id) {
		super();
		this.product = product;
		this.description = description;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.tax = tax;
		this.subTotal = subTotal;
		this.taxTotal = taxTotal;
		this.id = id;
	}

	public OrderDetails() {
	
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.math.BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(java.math.BigDecimal discount) {
		this.discount = discount;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public java.math.BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(java.math.BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public java.math.BigDecimal getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(java.math.BigDecimal taxTotal) {
		this.taxTotal = taxTotal;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	

}
