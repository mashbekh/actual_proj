package com.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

import java.math.BigDecimal;
import java.util.*;


/*
 * structure for PO and Bill
 */

@Entity("outward")
public class OutwardEntity {
	
	@Reference
	@JsonIgnore
	private BusinessPlayers vendor;
	
	@Reference
	@JsonIgnore
	private Company company;
	
	private boolean isPurchaseOrder;
	private Long poSerialId;
	private String poNumber;   //prefix + no + suffix
	private Date poDate;
	private String poNotes;
	private java.math.BigDecimal poSubtotal;
	private java.math.BigDecimal poTaxtotal;
	private java.math.BigDecimal poGrandtotal;
	private java.math.BigDecimal poAdvancetotal;
	private java.math.BigDecimal poBalance;
	private boolean isPodeleted;
	
	
	private boolean isBill;
	private String billNumber;
	private Date billDate;
	private int billDueinterval;
	private Date billDuedate;
	private java.math.BigDecimal billSubtotal;
	private java.math.BigDecimal billTaxtotal;
	private java.math.BigDecimal billGrandtotal;
	private java.math.BigDecimal billAdvancetotal;
	private java.math.BigDecimal billBalance;
	private boolean isBilldeleted;
	
	@Embedded
	private List<POBillDetails> poDetails;
	
	@Embedded
	private List<POBillDetails> billDetails;
	
	@Embedded
	private List<POBillPayments> advancePayments;
	
	@Embedded
	private List<POBillPayments> billPayments;
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public OutwardEntity(BusinessPlayers vendor, Company company, boolean isPurchaseOrder, Long poSerialId,
			String poNumber, Date poDate, String poNotes, BigDecimal poSubtotal, BigDecimal poTaxtotal,
			BigDecimal poGrandtotal, BigDecimal poAdvancetotal, BigDecimal poBalance, boolean isPodeleted,
			boolean isBill, String billNumber, Date billDate, int billDueinterval, Date billDuedate,
			BigDecimal billSubtotal, BigDecimal billTaxtotal, BigDecimal billGrandtotal, BigDecimal billAdvancetotal,
			BigDecimal billBalance, boolean isBilldeleted, List<POBillDetails> poDetails,
			List<POBillDetails> billDetails, List<POBillPayments> advancePayments, List<POBillPayments> billPayments) {
		
		this.vendor = vendor;
		this.company = company;
		this.isPurchaseOrder = isPurchaseOrder;
		this.poSerialId = poSerialId;
		this.poNumber = poNumber;
		this.poDate = poDate;
		this.poNotes = poNotes;
		this.poSubtotal = poSubtotal;
		this.poTaxtotal = poTaxtotal;
		this.poGrandtotal = poGrandtotal;
		this.poAdvancetotal = poAdvancetotal;
		this.poBalance = poBalance;
		this.isPodeleted = isPodeleted;
		this.isBill = isBill;
		this.billNumber = billNumber;
		this.billDate = billDate;
		this.billDueinterval = billDueinterval;
		this.billDuedate = billDuedate;
		this.billSubtotal = billSubtotal;
		this.billTaxtotal = billTaxtotal;
		this.billGrandtotal = billGrandtotal;
		this.billAdvancetotal = billAdvancetotal;
		this.billBalance = billBalance;
		this.isBilldeleted = isBilldeleted;
		this.poDetails = poDetails;
		this.billDetails = billDetails;
		this.advancePayments = advancePayments;
		this.billPayments = billPayments;
	}

	
	
	public OutwardEntity() {
		
	}



	public BusinessPlayers getVendor() {
		return vendor;
	}

	public void setVendor(BusinessPlayers vendor) {
		this.vendor = vendor;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isPurchaseOrder() {
		return isPurchaseOrder;
	}

	public void setisPurchaseOrder(boolean isPurchaseOrder) {
		this.isPurchaseOrder = isPurchaseOrder;
	}

	public Long getPoSerialId() {
		return poSerialId;
	}

	public void setPoSerialId(Long poSerialId) {
		this.poSerialId = poSerialId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public String getPoNotes() {
		return poNotes;
	}

	public void setPoNotes(String poNotes) {
		this.poNotes = poNotes;
	}

	public java.math.BigDecimal getPoSubtotal() {
		return poSubtotal;
	}

	public void setPoSubtotal(java.math.BigDecimal poSubtotal) {
		this.poSubtotal = poSubtotal;
	}

	public java.math.BigDecimal getPoTaxtotal() {
		return poTaxtotal;
	}

	public void setPoTaxtotal(java.math.BigDecimal poTaxtotal) {
		this.poTaxtotal = poTaxtotal;
	}

	public java.math.BigDecimal getPoGrandtotal() {
		return poGrandtotal;
	}

	public void setPoGrandtotal(java.math.BigDecimal poGrandtotal) {
		this.poGrandtotal = poGrandtotal;
	}

	public java.math.BigDecimal getPoAdvancetotal() {
		return poAdvancetotal;
	}

	public void setPoAdvancetotal(java.math.BigDecimal poAdvancetotal) {
		this.poAdvancetotal = poAdvancetotal;
	}

	public java.math.BigDecimal getPoBalance() {
		return poBalance;
	}

	public void setPoBalance(java.math.BigDecimal poBalance) {
		this.poBalance = poBalance;
	}

	public boolean isPodeleted() {
		return isPodeleted;
	}

	public void setisPodeleted(boolean isPodeleted) {
		this.isPodeleted = isPodeleted;
	}

	public boolean isBill() {
		return isBill;
	}

	public void setisBill(boolean isBill) {
		this.isBill = isBill;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public int getBillDueinterval() {
		return billDueinterval;
	}

	public void setBillDueinterval(int billDueinterval) {
		this.billDueinterval = billDueinterval;
	}

	public Date getBillDuedate() {
		return billDuedate;
	}

	public void setBillDuedate(Date billDuedate) {
		this.billDuedate = billDuedate;
	}

	public java.math.BigDecimal getBillSubtotal() {
		return billSubtotal;
	}

	public void setBillSubtotal(java.math.BigDecimal billSubtotal) {
		this.billSubtotal = billSubtotal;
	}

	public java.math.BigDecimal getBillTaxtotal() {
		return billTaxtotal;
	}

	public void setBillTaxtotal(java.math.BigDecimal billTaxtotal) {
		this.billTaxtotal = billTaxtotal;
	}

	public java.math.BigDecimal getBillGrandtotal() {
		return billGrandtotal;
	}

	public void setBillGrandtotal(java.math.BigDecimal billGrandtotal) {
		this.billGrandtotal = billGrandtotal;
	}

	public java.math.BigDecimal getBillAdvancetotal() {
		return billAdvancetotal;
	}

	public void setBillAdvancetotal(java.math.BigDecimal billAdvancetotal) {
		this.billAdvancetotal = billAdvancetotal;
	}

	public java.math.BigDecimal getBillBalance() {
		return billBalance;
	}

	public void setBillBalance(java.math.BigDecimal billBalance) {
		this.billBalance = billBalance;
	}

	public boolean isBilldeleted() {
		return isBilldeleted;
	}

	public void setisBilldeleted(boolean isBilldeleted) {
		this.isBilldeleted = isBilldeleted;
	}

	public List<POBillDetails> getPoDetails() {
		return poDetails;
	}

	public void setPoDetails(List<POBillDetails> poDetails) {
		this.poDetails = poDetails;
	}

	public List<POBillDetails> getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(List<POBillDetails> billDetails) {
		this.billDetails = billDetails;
	}

	public List<POBillPayments> getAdvancePayments() {
		return advancePayments;
	}

	public void setAdvancePayments(List<POBillPayments> advancePayments) {
		this.advancePayments = advancePayments;
	}

	public List<POBillPayments> getBillPayments() {
		return billPayments;
	}

	public void setBillPayments(List<POBillPayments> billPayments) {
		this.billPayments = billPayments;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	

}
