package com.Models;

import java.util.Date;

import org.jongo.marshall.jackson.oid.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

@Entity("ttl")
public class ttlTry 
{
	@ObjectId
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private String id;
	
	private Date createdAt; 
	private int status;
	
	public ttlTry(Date createdAt, int status) {
		super();
		this.createdAt = createdAt;
		this.status = status;
	}

	public ttlTry() {
		
	}
	
	
}
