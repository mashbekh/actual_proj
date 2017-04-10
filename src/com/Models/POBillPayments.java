package com.Models;

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
	
	
	

}
