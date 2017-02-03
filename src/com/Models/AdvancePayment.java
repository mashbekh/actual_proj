package com.Models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="advance_payment")
public class AdvancePayment {
	
	private Date payment_date;
	private double payment_amount;
	private String purpose_title;
	private String purpose_desc;
	//object of payment mode
	private String payment_mode;
	//object of payment account
	private String payment_account;
	private double tdsrate;
	private double tdsamount;
	
	@ManyToOne
	@JoinColumn(name="PO_number",referencedColumnName="PO_number")
	@JsonBackReference
	private PurchaseOrder po;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)     //per company-better
	private long id;

	public Date getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}

	public double getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_amount(double payment_amount) {
		this.payment_amount = payment_amount;
	}

	public String getPurpose_title() {
		return purpose_title;
	}

	public void setPurpose_title(String purpose_title) {
		this.purpose_title = purpose_title;
	}

	public String getPurpose_desc() {
		return purpose_desc;
	}

	public void setPurpose_desc(String purpose_desc) {
		this.purpose_desc = purpose_desc;
	}

	public String getPayment_mode() {
		return payment_mode;
	}

	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getPayment_account() {
		return payment_account;
	}

	public void setPayment_account(String payment_account) {
		this.payment_account = payment_account;
	}

	public double getTdsrate() {
		return tdsrate;
	}

	public void setTdsrate(double tdsrate) {
		this.tdsrate = tdsrate;
	}

	public double getTdsamount() {
		return tdsamount;
	}

	public void setTdsamount(double tdsamount) {
		this.tdsamount = tdsamount;
	}

	public PurchaseOrder getPo() {
		return po;
	}

	public void setPo(PurchaseOrder po) {
		this.po = po;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AdvancePayment(Date payment_date, double payment_amount, String purpose_title, String purpose_desc,
			String payment_mode, String payment_account, double tdsrate, double tdsamount, PurchaseOrder po) {
		super();
		this.payment_date = payment_date;
		this.payment_amount = payment_amount;
		this.purpose_title = purpose_title;
		this.purpose_desc = purpose_desc;
		this.payment_mode = payment_mode;
		this.payment_account = payment_account;
		this.tdsrate = tdsrate;
		this.tdsamount = tdsamount;
		this.po = po;
	}

	public AdvancePayment() {
		
	}

	@Override
	public String toString() {
		return "AdvancePayment [payment_date=" + payment_date + ", payment_amount=" + payment_amount
				+ ", purpose_title=" + purpose_title + ", purpose_desc=" + purpose_desc + ", payment_mode="
				+ payment_mode + ", payment_account=" + payment_account + ", tdsrate=" + tdsrate + ", tdsamount="
				+ tdsamount + ", po=" + po + ", id=" + id + "]";
	}

	

}
