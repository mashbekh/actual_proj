package com.Models;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class TaxDetails {

	private String panNo;
	private boolean panVStatus;
	private byte[] panDoc;
	private boolean panDocVStatus;
	
	private String tinNo;
	private boolean tinVStatus;
	private byte[] tinDoc;
	private boolean tinDocVStatus;
	
	private String strnNo;
	private boolean strnVStatus;
	private byte[] strnDoc;
	private boolean strnDocVStatus;
	
	private String tanNo;
	private boolean tanVStatus;
	private byte[] tanDoc;
	private boolean tanDocVStatus;
	
	private String gstNo;
	private boolean gstVStatus;
	private byte[] gstDoc;
	private boolean gstDocVStatus;
	
	private String cinNo;
	private boolean cinVStatus;
	private byte[] cinDoc;
	private boolean cinDocVStatus;
	
	
	public TaxDetails(String panNo, boolean panVStatus, byte[] panDoc, boolean panDocVStatus, String tinNo,
			boolean tinVStatus, byte[] tinDoc, boolean tinDocVStatus, String strnNo, boolean strnVStatus,
			byte[] strnDoc, boolean strnDocVStatus, String tanNo, boolean tanVStatus, byte[] tanDoc,
			boolean tanDocVStatus, String gstNo, boolean gstVStatus, byte[] gstDoc, boolean gstDocVStatus, String cinNo,
			boolean cinVStatus, byte[] cinDoc, boolean cinDocVStatus) {
		
		this.panNo = panNo;
		this.panVStatus = panVStatus;
		this.panDoc = panDoc;
		this.panDocVStatus = panDocVStatus;
		this.tinNo = tinNo;
		this.tinVStatus = tinVStatus;
		this.tinDoc = tinDoc;
		this.tinDocVStatus = tinDocVStatus;
		this.strnNo = strnNo;
		this.strnVStatus = strnVStatus;
		this.strnDoc = strnDoc;
		this.strnDocVStatus = strnDocVStatus;
		this.tanNo = tanNo;
		this.tanVStatus = tanVStatus;
		this.tanDoc = tanDoc;
		this.tanDocVStatus = tanDocVStatus;
		this.gstNo = gstNo;
		this.gstVStatus = gstVStatus;
		this.gstDoc = gstDoc;
		this.gstDocVStatus = gstDocVStatus;
		this.cinNo = cinNo;
		this.cinVStatus = cinVStatus;
		this.cinDoc = cinDoc;
		this.cinDocVStatus = cinDocVStatus;
	}


	public String getPanNo() {
		return panNo;
	}


	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}


	public boolean isPanVStatus() {
		return panVStatus;
	}


	public void setPanVStatus(boolean panVStatus) {
		this.panVStatus = panVStatus;
	}


	public byte[] getPanDoc() {
		return panDoc;
	}


	public void setPanDoc(byte[] panDoc) {
		this.panDoc = panDoc;
	}


	public boolean isPanDocVStatus() {
		return panDocVStatus;
	}


	public void setPanDocVStatus(boolean panDocVStatus) {
		this.panDocVStatus = panDocVStatus;
	}


	public String getTinNo() {
		return tinNo;
	}


	public void setTinNo(String tinNo) {
		this.tinNo = tinNo;
	}


	public boolean isTinVStatus() {
		return tinVStatus;
	}


	public void setTinVStatus(boolean tinVStatus) {
		this.tinVStatus = tinVStatus;
	}


	public byte[] getTinDoc() {
		return tinDoc;
	}


	public void setTinDoc(byte[] tinDoc) {
		this.tinDoc = tinDoc;
	}


	public boolean isTinDocVStatus() {
		return tinDocVStatus;
	}


	public void setTinDocVStatus(boolean tinDocVStatus) {
		this.tinDocVStatus = tinDocVStatus;
	}


	public String getStrnNo() {
		return strnNo;
	}


	public void setStrnNo(String strnNo) {
		this.strnNo = strnNo;
	}


	public boolean isStrnVStatus() {
		return strnVStatus;
	}


	public void setStrnVStatus(boolean strnVStatus) {
		this.strnVStatus = strnVStatus;
	}


	public byte[] getStrnDoc() {
		return strnDoc;
	}


	public void setStrnDoc(byte[] strnDoc) {
		this.strnDoc = strnDoc;
	}


	public boolean isStrnDocVStatus() {
		return strnDocVStatus;
	}


	public void setStrnDocVStatus(boolean strnDocVStatus) {
		this.strnDocVStatus = strnDocVStatus;
	}


	public String getTanNo() {
		return tanNo;
	}


	public void setTanNo(String tanNo) {
		this.tanNo = tanNo;
	}


	public boolean isTanVStatus() {
		return tanVStatus;
	}


	public void setTanVStatus(boolean tanVStatus) {
		this.tanVStatus = tanVStatus;
	}


	public byte[] getTanDoc() {
		return tanDoc;
	}


	public void setTanDoc(byte[] tanDoc) {
		this.tanDoc = tanDoc;
	}


	public boolean isTanDocVStatus() {
		return tanDocVStatus;
	}


	public void setTanDocVStatus(boolean tanDocVStatus) {
		this.tanDocVStatus = tanDocVStatus;
	}


	public String getGstNo() {
		return gstNo;
	}


	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}


	public boolean isGstVStatus() {
		return gstVStatus;
	}


	public void setGstVStatus(boolean gstVStatus) {
		this.gstVStatus = gstVStatus;
	}


	public byte[] getGstDoc() {
		return gstDoc;
	}


	public void setGstDoc(byte[] gstDoc) {
		this.gstDoc = gstDoc;
	}


	public boolean isGstDocVStatus() {
		return gstDocVStatus;
	}


	public void setGstDocVStatus(boolean gstDocVStatus) {
		this.gstDocVStatus = gstDocVStatus;
	}


	public String getCinNo() {
		return cinNo;
	}


	public void setCinNo(String cinNo) {
		this.cinNo = cinNo;
	}


	public boolean isCinVStatus() {
		return cinVStatus;
	}


	public void setCinVStatus(boolean cinVStatus) {
		this.cinVStatus = cinVStatus;
	}


	public byte[] getCinDoc() {
		return cinDoc;
	}


	public void setCinDoc(byte[] cinDoc) {
		this.cinDoc = cinDoc;
	}


	public boolean isCinDocVStatus() {
		return cinDocVStatus;
	}


	public void setCinDocVStatus(boolean cinDocVStatus) {
		this.cinDocVStatus = cinDocVStatus;
	}
	
	
	
	

}
