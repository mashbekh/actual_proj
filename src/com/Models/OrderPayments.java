package com.Models;

import java.math.BigDecimal;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Embedded
public class OrderPayments {
	
	private Date paymentDate;
	private java.math.BigDecimal paymentAmount;
	private String purposeTitle;
	private String purposeDescription;
	private String paymentMode;
	private String paymentAccount;
	private java.math.BigDecimal tdsRate;
	private java.math.BigDecimal tdsAmount;
	private boolean isTdsReceived;
	
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public OrderPayments(Date paymentDate, BigDecimal paymentAmount, String purposeTitle, String purposeDescription,
			String paymentMode, String paymentAccount, BigDecimal tdsRate, BigDecimal tdsAmount, boolean isTdsReceived,
			ObjectId id) {
		super();
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.purposeTitle = purposeTitle;
		this.purposeDescription = purposeDescription;
		this.paymentMode = paymentMode;
		this.paymentAccount = paymentAccount;
		this.tdsRate = tdsRate;
		this.tdsAmount = tdsAmount;
		this.isTdsReceived = isTdsReceived;
		this.id = id;
	}

	public OrderPayments() {
		
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public java.math.BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(java.math.BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPurposeTitle() {
		return purposeTitle;
	}

	public void setPurposeTitle(String purposeTitle) {
		this.purposeTitle = purposeTitle;
	}

	public String getPurposeDescription() {
		return purposeDescription;
	}

	public void setPurposeDescription(String purposeDescription) {
		this.purposeDescription = purposeDescription;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public java.math.BigDecimal getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(java.math.BigDecimal tdsRate) {
		this.tdsRate = tdsRate;
	}

	public java.math.BigDecimal getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(java.math.BigDecimal tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public boolean isTdsReceived() {
		return isTdsReceived;
	}

	public void setisTdsReceived(boolean isTdsReceived) {
		this.isTdsReceived = isTdsReceived;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	
	

}
