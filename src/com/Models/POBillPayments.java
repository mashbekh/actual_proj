package com.Models;

import java.math.BigDecimal;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Embedded
public class POBillPayments {
	
	private Date paymentDate;
	private java.math.BigDecimal paymentAmount;
	private String purposeTitle;
	private String purposeDescription;
	private String paymentMode;
	private String paymentAccount;
	private java.math.BigDecimal tdsRate;
	private java.math.BigDecimal tdsAmount;
	private boolean isTdsPaid;
	
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public POBillPayments(Date paymentDate, BigDecimal paymentAmount, String purposeTitle, String purposeDescription,
			String paymentMode, String paymentAccount, BigDecimal tdsRate, BigDecimal tdsAmount, boolean isTdsPaid,
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
		this.isTdsPaid = isTdsPaid;
		this.id = id;
	}

	public POBillPayments() {
	
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

	public java.math.BigDecimal gettdsRate() {
		return tdsRate;
	}

	public void settdsRate(java.math.BigDecimal tdsRate) {
		this.tdsRate = tdsRate;
	}

	public java.math.BigDecimal gettdsAmount() {
		return tdsAmount;
	}

	public void settdsAmount(java.math.BigDecimal tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public boolean isTdsPaid() {
		return isTdsPaid;
	}

	public void setisTdsPaid(boolean isTdsPaid) {
		this.isTdsPaid = isTdsPaid;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "POBillPayments [paymentDate=" + paymentDate + ", paymentAmount=" + paymentAmount + ", purposeTitle="
				+ purposeTitle + ", purposeDescription=" + purposeDescription + ", paymentMode=" + paymentMode
				+ ", paymentAccount=" + paymentAccount + ", tdsRate=" + tdsRate + ", tdsAmount=" + tdsAmount
				+ ", isTdsPaid=" + isTdsPaid + ", id=" + id + "]";
	}

	
	
	

}
