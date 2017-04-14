package com.Models;

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

}
