package com.Models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Entity("inward")
public class InwardEntity {
	
	@Reference
	private BusinessPlayers customer;
	
	@Reference
	private Company company;
	
	private boolean isEstimate;
	private Long estimateSerialId;
	private String estimateNumber;   //prefix + no + suffix
	private Date estimateDate;
	private String estimateNotes;
	private java.math.BigDecimal estimateSubtotal;
	private java.math.BigDecimal estimateTaxtotal;
	private java.math.BigDecimal estimateGrandtotal;
	private java.math.BigDecimal estimateAdvancetotal;
	private java.math.BigDecimal estimateBalance;
	private boolean isEstimatedeleted;
	
	
	private boolean isInvoice;
	private Long invoiceSerialId;
	private String invoiceNumber;
	private Date invoiceDate;
	private int invoiceDueinterval;
	private Date invoiceDuedate;
	private String invoiceNotes;
	private java.math.BigDecimal invoiceSubtotal;
	private java.math.BigDecimal invoiceTaxtotal;
	private java.math.BigDecimal invoiceGrandtotal;
	private java.math.BigDecimal invoiceAdvancetotal;
	private java.math.BigDecimal invoiceBalance;
	private boolean isInvoicedeleted;
	
	@Embedded
	private List<OrderDetails> estimateDetails;
	
	@Embedded
	private List<OrderDetails> invoiceDetails;
	
	@Embedded
	private List<OrderPayments> estimatePayments;
	
	@Embedded
	private List<OrderPayments> invoicePayments;
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;

	public InwardEntity(BusinessPlayers customer, Company company, boolean isEstimate, Long estimateSerialId,
			String estimateNumber, Date estimateDate, String estimateNotes, BigDecimal estimateSubtotal,
			BigDecimal estimateTaxtotal, BigDecimal estimateGrandtotal, BigDecimal estimateAdvancetotal,
			BigDecimal estimateBalance, boolean isEstimatedeleted, boolean isInvoice, Long invoiceSerialId,
			String invoiceNumber, Date invoiceDate, int invoiceDueinterval, Date invoiceDuedate, String invoiceNotes,
			BigDecimal invoiceSubtotal, BigDecimal invoiceTaxtotal, BigDecimal invoiceGrandtotal,
			BigDecimal invoiceAdvancetotal, BigDecimal invoiceBalance, boolean isInvoicedeleted,
			List<OrderDetails> estimateDetails, List<OrderDetails> invoiceDetails, List<OrderPayments> estimatePayments,
			List<OrderPayments> invoicePayments) {
		super();
		this.customer = customer;
		this.company = company;
		this.isEstimate = isEstimate;
		this.estimateSerialId = estimateSerialId;
		this.estimateNumber = estimateNumber;
		this.estimateDate = estimateDate;
		this.estimateNotes = estimateNotes;
		this.estimateSubtotal = estimateSubtotal;
		this.estimateTaxtotal = estimateTaxtotal;
		this.estimateGrandtotal = estimateGrandtotal;
		this.estimateAdvancetotal = estimateAdvancetotal;
		this.estimateBalance = estimateBalance;
		this.isEstimatedeleted = isEstimatedeleted;
		this.isInvoice = isInvoice;
		this.invoiceSerialId = invoiceSerialId;
		this.invoiceNumber = invoiceNumber;
		this.invoiceDate = invoiceDate;
		this.invoiceDueinterval = invoiceDueinterval;
		this.invoiceDuedate = invoiceDuedate;
		this.invoiceNotes = invoiceNotes;
		this.invoiceSubtotal = invoiceSubtotal;
		this.invoiceTaxtotal = invoiceTaxtotal;
		this.invoiceGrandtotal = invoiceGrandtotal;
		this.invoiceAdvancetotal = invoiceAdvancetotal;
		this.invoiceBalance = invoiceBalance;
		this.isInvoicedeleted = isInvoicedeleted;
		this.estimateDetails = estimateDetails;
		this.invoiceDetails = invoiceDetails;
		this.estimatePayments = estimatePayments;
		this.invoicePayments = invoicePayments;
	}

	public InwardEntity() {
		
	}

	public BusinessPlayers getCustomer() {
		return customer;
	}

	public void setCustomer(BusinessPlayers customer) {
		this.customer = customer;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isEstimate() {
		return isEstimate;
	}

	public void setisEstimate(boolean isEstimate) {
		this.isEstimate = isEstimate;
	}

	public Long getEstimateSerialId() {
		return estimateSerialId;
	}

	public void setEstimateSerialId(Long estimateSerialId) {
		this.estimateSerialId = estimateSerialId;
	}

	public String getEstimateNumber() {
		return estimateNumber;
	}

	public void setEstimateNumber(String estimateNumber) {
		this.estimateNumber = estimateNumber;
	}

	public Date getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(Date estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getEstimateNotes() {
		return estimateNotes;
	}

	public void setEstimateNotes(String estimateNotes) {
		this.estimateNotes = estimateNotes;
	}

	public java.math.BigDecimal getEstimateSubtotal() {
		return estimateSubtotal;
	}

	public void setEstimateSubtotal(java.math.BigDecimal estimateSubtotal) {
		this.estimateSubtotal = estimateSubtotal;
	}

	public java.math.BigDecimal getEstimateTaxtotal() {
		return estimateTaxtotal;
	}

	public void setEstimateTaxtotal(java.math.BigDecimal estimateTaxtotal) {
		this.estimateTaxtotal = estimateTaxtotal;
	}

	public java.math.BigDecimal getEstimateGrandtotal() {
		return estimateGrandtotal;
	}

	public void setEstimateGrandtotal(java.math.BigDecimal estimateGrandtotal) {
		this.estimateGrandtotal = estimateGrandtotal;
	}

	public java.math.BigDecimal getEstimateAdvancetotal() {
		return estimateAdvancetotal;
	}

	public void setEstimateAdvancetotal(java.math.BigDecimal estimateAdvancetotal) {
		this.estimateAdvancetotal = estimateAdvancetotal;
	}

	public java.math.BigDecimal getEstimateBalance() {
		return estimateBalance;
	}

	public void setEstimateBalance(java.math.BigDecimal estimateBalance) {
		this.estimateBalance = estimateBalance;
	}

	public boolean isEstimatedeleted() {
		return isEstimatedeleted;
	}

	public void setisEstimatedeleted(boolean isEstimatedeleted) {
		this.isEstimatedeleted = isEstimatedeleted;
	}

	public boolean isInvoice() {
		return isInvoice;
	}

	public void setisInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public Long getInvoiceSerialId() {
		return invoiceSerialId;
	}

	public void setInvoiceSerialId(Long invoiceSerialId) {
		this.invoiceSerialId = invoiceSerialId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public int getInvoiceDueinterval() {
		return invoiceDueinterval;
	}

	public void setInvoiceDueinterval(int invoiceDueinterval) {
		this.invoiceDueinterval = invoiceDueinterval;
	}

	public Date getInvoiceDuedate() {
		return invoiceDuedate;
	}

	public void setInvoiceDuedate(Date invoiceDuedate) {
		this.invoiceDuedate = invoiceDuedate;
	}

	public String getInvoiceNotes() {
		return invoiceNotes;
	}

	public void setInvoiceNotes(String invoiceNotes) {
		this.invoiceNotes = invoiceNotes;
	}

	public java.math.BigDecimal getInvoiceSubtotal() {
		return invoiceSubtotal;
	}

	public void setInvoiceSubtotal(java.math.BigDecimal invoiceSubtotal) {
		this.invoiceSubtotal = invoiceSubtotal;
	}

	public java.math.BigDecimal getInvoiceTaxtotal() {
		return invoiceTaxtotal;
	}

	public void setInvoiceTaxtotal(java.math.BigDecimal invoiceTaxtotal) {
		this.invoiceTaxtotal = invoiceTaxtotal;
	}

	public java.math.BigDecimal getInvoiceGrandtotal() {
		return invoiceGrandtotal;
	}

	public void setInvoiceGrandtotal(java.math.BigDecimal invoiceGrandtotal) {
		this.invoiceGrandtotal = invoiceGrandtotal;
	}

	public java.math.BigDecimal getInvoiceAdvancetotal() {
		return invoiceAdvancetotal;
	}

	public void setInvoiceAdvancetotal(java.math.BigDecimal invoiceAdvancetotal) {
		this.invoiceAdvancetotal = invoiceAdvancetotal;
	}

	public java.math.BigDecimal getInvoiceBalance() {
		return invoiceBalance;
	}

	public void setInvoiceBalance(java.math.BigDecimal invoiceBalance) {
		this.invoiceBalance = invoiceBalance;
	}

	public boolean isInvoicedeleted() {
		return isInvoicedeleted;
	}

	public void setisInvoicedeleted(boolean isInvoicedeleted) {
		this.isInvoicedeleted = isInvoicedeleted;
	}

	public List<OrderDetails> getEstimateDetails() {
		return estimateDetails;
	}

	public void setEstimateDetails(List<OrderDetails> estimateDetails) {
		this.estimateDetails = estimateDetails;
	}

	public List<OrderDetails> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<OrderDetails> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public List<OrderPayments> getEstimatePayments() {
		return estimatePayments;
	}

	public void setEstimatePayments(List<OrderPayments> estimatePayments) {
		this.estimatePayments = estimatePayments;
	}

	public List<OrderPayments> getInvoicePayments() {
		return invoicePayments;
	}

	public void setInvoicePayments(List<OrderPayments> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	

}
