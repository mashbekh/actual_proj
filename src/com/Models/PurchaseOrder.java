package com.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.setup.JsonCollectionDeserializer;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
	
	//these two will be FK
	private String vendor_id; //(many to one vendor object) or just maintain unidir one-one cos creation of vendor is independent of PO
	private String company_id;
	//@Type(type="date")
	//JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00")
	private Date order_date;
	private double amount;
	private double tax_amount;
	private double grand_total;
	private String note;
	private long po_id; //auto increment within company- do it in code, desc limit 1 , if null - insert 1
	@Id
	private String PO_number;  //combination of company id and po_id
	
	@OneToMany(targetEntity=PurchaseOrderDetails.class, mappedBy="po")
	@JsonManagedReference
	//@JsonDeserialize(using=JsonCollectionDeserializer.class)
	private List<PurchaseOrderDetails> pod ; //bi directional
	
	
	
	public List<PurchaseOrderDetails> getPod() {
		return pod;
	}
	
	public long getPo_id() {
		return po_id;
	}

	public void setPo_id(long po_id) {
		this.po_id = po_id;
	}

	public void setPod(List<PurchaseOrderDetails> pod) {
		this.pod = pod;
	}
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
	
	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
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
	public String getPO_number() {
		return PO_number;
	}
	public void setPO_number(String pO_number) {
		PO_number = pO_number;
	}
	
	
	
	public PurchaseOrder(String vendor_id, String company_id, Date date, double amount, double tax_amount,
			double grand_total, String note, long po_id, String pO_number, List<PurchaseOrderDetails> pod) {
		super();
		this.vendor_id = vendor_id;
		this.company_id = company_id;
		this.order_date = date;
		this.amount = amount;
		this.tax_amount = tax_amount;
		this.grand_total = grand_total;
		this.note = note;
		this.po_id = po_id;
		PO_number = pO_number;
		this.pod = pod;
	}

	public PurchaseOrder() {
		//super();
	}

	@Override
	public String toString() {
		
		String a = company_id + "***" + vendor_id + "**" + order_date + "**" + grand_total + "**" + po_id + "**" + PO_number +"***";
		String b = "items are " + "----";
		for( PurchaseOrderDetails x : pod)
		{
			 b += "--NEW" + "--";
			 b += "auto inc ID is"  + x.getId() +"*" +   x.getProduct_id() + "__"  + x.getPo().getPo_id() + "__" + x.getPo().getPO_number() + "__" + x.getAmount();
			 
			 
		}
		
		
		return a + b;
	}

	

}
