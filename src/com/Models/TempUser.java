package com.Models;

import java.util.Date;


import com.setup.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.jongo.marshall.jackson.oid.ObjectId;

@Entity("TempUser")
public class TempUser {
	
	
	private String userName;
	private String userEmail;
	private String mobileNumber;
	private String password;
	private String emailHexcode;
	private int mobileOtp;
	
	private Date creationTime;
	
	private boolean emailSentstatus;
	private Date emailSenttime;
	private boolean emailVerified;
	private Date emailVerfiedtime;
	
	private boolean otpSentstatus;
	private Date otpSenttime;
	private boolean mobileVerified;
	private Date mobileVerfiedtime;
	
	private int userType;

	
	
	@ObjectId
	@JsonSerialize(using = NoObjectIdSerializer.class)
	private String id;



	public TempUser(String userName, String userEmail, String mobileNumber, String password, String emailHexcode,
			int mobileOtp, Date creationTime , boolean emailSentstatus, Date emailSenttime, boolean emailVerified, Date emailVerfiedtime,
			boolean otpSentstatus, Date otpSenttime, boolean mobileVerified, Date mobileVerfiedtime,
			int userType) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.emailHexcode = emailHexcode;
		this.mobileOtp = mobileOtp;
		this.creationTime  = creationTime;
		this.emailSentstatus = emailSentstatus;
		this.emailSenttime = emailSenttime;
		this.emailVerified = emailVerified;
		this.emailVerfiedtime = emailVerfiedtime;
		this.otpSentstatus = otpSentstatus;
		this.otpSenttime = otpSenttime;
		this.mobileVerified = mobileVerified;
		this.mobileVerfiedtime = mobileVerfiedtime;
		this.userType = userType;
	}

	


	public TempUser() {
		
	}
	
	




	public Date getCreationTime() {
		return creationTime;
	}




	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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



	public String getEmailHexcode() {
		return emailHexcode;
	}



	public void setEmailHexcode(String emailHexcode) {
		this.emailHexcode = emailHexcode;
	}



	public int getMobileOtp() {
		return mobileOtp;
	}



	public void setMobileOtp(int mobileOtp) {
		this.mobileOtp = mobileOtp;
	}



	public boolean isEmailSentstatus() {
		return emailSentstatus;
	}



	public void setEmailSentstatus(boolean emailSentstatus) {
		this.emailSentstatus = emailSentstatus;
	}



	public Date getEmailSenttime() {
		return emailSenttime;
	}



	public void setEmailSenttime(Date emailSenttime) {
		this.emailSenttime = emailSenttime;
	}



	public boolean isEmailVerified() {
		return emailVerified;
	}



	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}



	public Date getEmailVerfiedtime() {
		return emailVerfiedtime;
	}



	public void setEmailVerfiedtime(Date emailVerfiedtime) {
		this.emailVerfiedtime = emailVerfiedtime;
	}



	public boolean isOtpSentstatus() {
		return otpSentstatus;
	}



	public void setOtpSentstatus(boolean otpSentstatus) {
		this.otpSentstatus = otpSentstatus;
	}



	public Date getOtpSenttime() {
		return otpSenttime;
	}



	public void setOtpSenttime(Date otpSenttime) {
		this.otpSenttime = otpSenttime;
	}



	public boolean isMobileVerified() {
		return mobileVerified;
	}



	public void setMobileVerified(boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
	}



	public Date getMobileVerfiedtime() {
		return mobileVerfiedtime;
	}



	public void setMobileVerfiedtime(Date mobileVerfiedtime) {
		this.mobileVerfiedtime = mobileVerfiedtime;
	}



	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public int getUserType() {
		return userType;
	}



	public void setUserType(int userType) {
		this.userType = userType;
	}



	

	

	

}
