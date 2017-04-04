package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class BusinessDetails {
	
	private String businessName;
	private boolean bnameVStatus;
	private String contactPerson;
	private boolean contactVStatus;
	private String businessSector;
	private boolean bsectorVStatus;
	private String businessType;
	private boolean btypeVStatus;
	private byte[] logo;
	
	public BusinessDetails(String businessName, boolean bnameVStatus, String contactPerson, boolean contactVStatus,
			String businessSector, boolean bsectorVStatus, String businessType, boolean btypeVStatus, byte[] logo) {
		super();
		this.businessName = businessName;
		this.bnameVStatus = bnameVStatus;
		this.contactPerson = contactPerson;
		this.contactVStatus = contactVStatus;
		this.businessSector = businessSector;
		this.bsectorVStatus = bsectorVStatus;
		this.businessType = businessType;
		this.btypeVStatus = btypeVStatus;
		this.logo = logo;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public boolean isBnameVStatus() {
		return bnameVStatus;
	}

	public void setBnameVStatus(boolean bnameVStatus) {
		this.bnameVStatus = bnameVStatus;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public boolean isContactVStatus() {
		return contactVStatus;
	}

	public void setContactVStatus(boolean contactVStatus) {
		this.contactVStatus = contactVStatus;
	}

	public String getBusinessSector() {
		return businessSector;
	}

	public void setBusinessSector(String businessSector) {
		this.businessSector = businessSector;
	}

	public boolean isBsectorVStatus() {
		return bsectorVStatus;
	}

	public void setBsectorVStatus(boolean bsectorVStatus) {
		this.bsectorVStatus = bsectorVStatus;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public boolean isBtypeVStatus() {
		return btypeVStatus;
	}

	public void setBtypeVStatus(boolean btypeVStatus) {
		this.btypeVStatus = btypeVStatus;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
	
	

}
