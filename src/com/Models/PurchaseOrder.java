package com.Models;


import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;


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
	private double advance_amount;
	private double balance;
	private int bill_status;
	private long po_id; //auto increment within company- do it in code, desc limit 1 , if null - insert 1
	@Id
	private String PO_number;  //combination of company id and po_id
	
	@OneToMany(targetEntity=PurchaseOrderDetails.class, mappedBy="po")
	@JsonManagedReference
	//@JsonDeserialize(using=JsonCollectionDeserializer.class)
	private List<PurchaseOrderDetails> pod ; //bi directional

	
	
	@OneToMany(targetEntity=AdvancePayment.class, mappedBy="po")
	@JsonManagedReference
	private List<AdvancePayment> adv_payment ; //bi directional
	
	
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
	
	
	
	public double getAdvance_amount() {
		return advance_amount;
	}

	public void setAdvance_amount(double advance_amount) {
		this.advance_amount = advance_amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getBill_status() {
		return bill_status;
	}

	public void setBill_status(int bill_status) {
		this.bill_status = bill_status;
	}

	public List<AdvancePayment> getAdv_payment() {
		return adv_payment;
	}

	public void setAdv_payment(List<AdvancePayment> adv_payment) {
		this.adv_payment = adv_payment;
	}

	public PurchaseOrder(String vendor_id, String company_id, Date date, double amount, double tax_amount,
			double grand_total, String note, long po_id, String pO_number, List<PurchaseOrderDetails> pod,List<AdvancePayment> adv_payment,
			double advance_amount, double balance, int bill_status) {
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
		this.adv_payment = adv_payment;
		this.advance_amount = advance_amount;
		this.balance = balance;
		this.bill_status = bill_status;
		
	}

	public PurchaseOrder() {
		//super();
	}


	

}
