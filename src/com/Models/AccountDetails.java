package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class AccountDetails {

	private String accountNo;
	private String accountType;
	private String bankName;
	private String ifscCode;
	
	
	public AccountDetails(String accountNo, String accountType, String bankName, String ifscCode) {
		super();
		this.accountNo = accountNo;
		this.accountType = accountType;
		this.bankName = bankName;
		this.ifscCode = ifscCode;
	}

	public AccountDetails()
	{
		
	}

	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getIfscCode() {
		return ifscCode;
	}


	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	
	
	
}
