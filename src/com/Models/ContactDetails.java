package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class ContactDetails {

	private String addrLine1;
	private String addrLine2;
	private String state;
	private String city;
	private String locality;
	private boolean addrVStatus;
	private String businessEmail;
	private boolean mailVStatus;
	private String website;
	private String mobileNo;
	private boolean mobileVStatus;
	private String landlineNo;

	public ContactDetails(String addrLine1, String addrLine2, String state, String city,
			String locality, boolean addrVStatus, String businessEmail, boolean mailVStatus, String website,
			String mobileNo, boolean mobileVStatus, String landlineNo) {

		this.addrLine1 = addrLine1;
		this.addrLine2 = addrLine2;
		this.state = state;
		this.city = city;
		this.locality = locality;
		this.addrVStatus = addrVStatus;
		this.businessEmail = businessEmail;
		this.mailVStatus = mailVStatus;
		this.website = website;
		this.mobileNo = mobileNo;
		this.mobileVStatus = mobileVStatus;
		this.landlineNo = landlineNo;
	}

	public ContactDetails() {

	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public boolean isAddrVStatus() {
		return addrVStatus;
	}

	public void setAddrVStatus(boolean addrVStatus) {
		this.addrVStatus = addrVStatus;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public boolean isMailVStatus() {
		return mailVStatus;
	}

	public void setMailVStatus(boolean mailVStatus) {
		this.mailVStatus = mailVStatus;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public boolean isMobileVStatus() {
		return mobileVStatus;
	}

	public void setMobileVStatus(boolean mobileVStatus) {
		this.mobileVStatus = mobileVStatus;
	}

	public String getLandlineNo() {
		return landlineNo;
	}

	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}




}
