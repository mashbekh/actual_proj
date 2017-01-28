package com.Models;

import java.util.Date;

public class POfrontEnd {
	
	private String vendor_id;
	private String company_id;
	private Date date;
	private double amount;
	private double tax_amount;
	private double grand_total;
	private String note;
	
	public String getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTax_amount() {
		return tax_amount;
	}
	public void setTax_amount(double tax_amount) {
		this.tax_amount = tax_amount;
	}
	public double getGrand_total() {
		return grand_total;
	}
	public void setGrand_total(double grand_total) {
		this.grand_total = grand_total;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "POfrontEnd [vendor_id=" + vendor_id + ", company_id=" + company_id + ", date=" + date + ", amount="
				+ amount + ", tax_amount=" + tax_amount + ", grand_total=" + grand_total + ", note=" + note + "]";
	}
	
	

}
