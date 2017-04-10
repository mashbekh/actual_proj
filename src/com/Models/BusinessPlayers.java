package com.Models;

import java.math.BigDecimal;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Entity("businessPlayers")
public class BusinessPlayers {
//includes both vendors and customers -  true or false
	
	private BusinessDetails businessDetails;
	private ContactDetails  contactDetails;
	private TaxDetails taxDetails;
	private List<AccountDetails> accountDetails;
	private boolean isVendor;
	private boolean isCustomer;
	private boolean ismatchFound;
	private boolean ismatchVerified;
	private java.math.BigDecimal vendorDueAmt;
	private java.math.BigDecimal customerDueAmt;
	private boolean isDeleted;
	
	@Reference
	@JsonIgnore
	private Company company;
	
	@Reference
	@JsonIgnore
	private Company primaryCompany;
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	
	public BusinessPlayers(BusinessDetails businessDetails, ContactDetails contactDetails, TaxDetails taxDetails,
			List<AccountDetails> accountDetails, boolean isVendor, boolean isCustomer, boolean ismatchFound,
			boolean ismatchVerified, BigDecimal vendorDueAmt, BigDecimal customerDueAmt, Company company,
			Company primaryCompany, boolean isDeleted) {
		
		this.businessDetails = businessDetails;
		this.contactDetails = contactDetails;
		this.taxDetails = taxDetails;
		this.accountDetails = accountDetails;
		this.isVendor = isVendor;
		this.isCustomer = isCustomer;
		this.ismatchFound = ismatchFound;
		this.ismatchVerified = ismatchVerified;
		this.vendorDueAmt = vendorDueAmt;
		this.customerDueAmt = customerDueAmt;
		this.company = company;
		this.primaryCompany = primaryCompany;
		this.isDeleted = isDeleted;
	}

	public BusinessPlayers() {
		
	}
	
	

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setisDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public java.math.BigDecimal getVendorDueAmt() {
		return vendorDueAmt;
	}

	public void setVendorDueAmt(java.math.BigDecimal vendorDueAmt) {
		this.vendorDueAmt = vendorDueAmt;
	}

	public java.math.BigDecimal getCustomerDueAmt() {
		return customerDueAmt;
	}

	public void setCustomerDueAmt(java.math.BigDecimal customerDueAmt) {
		this.customerDueAmt = customerDueAmt;
	}

	public BusinessDetails getBusinessDetails() {
		return businessDetails;
	}

	public void setBusinessDetails(BusinessDetails businessDetails) {
		this.businessDetails = businessDetails;
	}

	public ContactDetails getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(ContactDetails contactDetails) {
		this.contactDetails = contactDetails;
	}

	public TaxDetails getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(TaxDetails taxDetails) {
		this.taxDetails = taxDetails;
	}

	public List<AccountDetails> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(List<AccountDetails> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public boolean isVendor() {
		return isVendor;
	}

	public void setisVendor(boolean isVendor) {
		this.isVendor = isVendor;
	}

	public boolean isCustomer() {
		return isCustomer;
	}

	public void setisCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

	public boolean ismatchFound() {
		return ismatchFound;
	}

	public void setismatchFound(boolean ismatchFound) {
		this.ismatchFound = ismatchFound;
	}

	public boolean ismatchVerified() {
		return ismatchVerified;
	}

	public void setismatchVerified(boolean ismatchVerified) {
		this.ismatchVerified = ismatchVerified;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getPrimaryCompany() {
		return primaryCompany;
	}

	public void setPrimaryCompany(Company primaryCompany) {
		this.primaryCompany = primaryCompany;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	
}
