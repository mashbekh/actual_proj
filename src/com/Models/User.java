package com.Models;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.setup.NoObjectIdSerializer;


@Entity("User")
public class User {
	
	private String userName;
	private String userEmail;
	private String mobileNumber;
	private String password;
	private int type;
	private Date createdAt;
	private String emailHexcode;
	private int otp;
	private int emailVerified;
	private int mobileVerified;
	private int userStatus;
	
	
	@Id
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private ObjectId id;


	public User(String userName, String userEmail, String mobileNumber, String password, int type, Date createdAt,
			String emailHexcode, int otp, int emailVerified, int mobileVerified, int userStatus ) {
		
		this.userName = userName;
		this.userEmail = userEmail;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.type = type;
		this.createdAt = createdAt;
		this.emailHexcode = emailHexcode;
		this.otp = otp;
		this.emailVerified = emailVerified;
		this.mobileVerified = mobileVerified;
		this.userStatus = userStatus;	}


	public User() {
		
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getEmailHexcode() {
		return emailHexcode;
	}


	public void setEmailHexcode(String emailHexcode) {
		this.emailHexcode = emailHexcode;
	}


	public int getOtp() {
		return otp;
	}


	public void setOtp(int otp) {
		this.otp = otp;
	}


	public int getEmailVerified() {
		return emailVerified;
	}


	public void setEmailVerified(int emailVerified) {
		this.emailVerified = emailVerified;
	}


	public int getMobileVerified() {
		return mobileVerified;
	}


	public void setMobileVerified(int mobileVerified) {
		this.mobileVerified = mobileVerified;
	}


	public int getUserStatus() {
		return userStatus;
	}


	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}


	public ObjectId getId() {
		return id;
	}


	public void setId(ObjectId id) {
		this.id = id;
	}

	
}


