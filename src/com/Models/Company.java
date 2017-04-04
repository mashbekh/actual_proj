package com.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

import java.util.*;

@Entity("company")
public class Company {
	
	private BusinessDetails businessDetails;
	private ContactDetails  contactDetails;
	private TaxDetails taxDetails;
	private List<AccountDetails> accountDetails;
	private POSettings poSettings;
	private EstimateSettings estimateSettings;
	private InvoiceSettings invoiceSettings;
	private ColumnSettings colSettings;
	private List<User> users;
	
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;


	public Company(BusinessDetails businessDetails, ContactDetails contactDetails, TaxDetails taxDetails,
			List<AccountDetails> accountDetails, POSettings poSettings, EstimateSettings estimateSettings,
			InvoiceSettings invoiceSettings, ColumnSettings colSettings, List<User> users) {
		super();
		this.businessDetails = businessDetails;
		this.contactDetails = contactDetails;
		this.taxDetails = taxDetails;
		this.accountDetails = accountDetails;
		this.poSettings = poSettings;
		this.estimateSettings = estimateSettings;
		this.invoiceSettings = invoiceSettings;
		this.colSettings = colSettings;
		this.users = users;
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


	public POSettings getPoSettings() {
		return poSettings;
	}


	public void setPoSettings(POSettings poSettings) {
		this.poSettings = poSettings;
	}


	public EstimateSettings getEstimateSettings() {
		return estimateSettings;
	}


	public void setEstimateSettings(EstimateSettings estimateSettings) {
		this.estimateSettings = estimateSettings;
	}


	public InvoiceSettings getInvoiceSettings() {
		return invoiceSettings;
	}


	public void setInvoiceSettings(InvoiceSettings invoiceSettings) {
		this.invoiceSettings = invoiceSettings;
	}


	public ColumnSettings getColSettings() {
		return colSettings;
	}


	public void setColSettings(ColumnSettings colSettings) {
		this.colSettings = colSettings;
	}


	public ObjectId getId() {
		return id;
	}


	public void setId(ObjectId id) {
		this.id = id;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	

	

}
