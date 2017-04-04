package com.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;

/*
 * token generated with user ID as key 
 * token stored in db against userId 
 */
@Entity("jwttoken")
public class JWTConfig {
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;
	private String userID;
	private String tokenValue;
	
	public JWTConfig(String userID, String tokenValue) {
		super();
		this.userID = userID;
		this.tokenValue = tokenValue;
	}
	
	public JWTConfig(){
		
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	
	

}
