package com.Models;

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

}
