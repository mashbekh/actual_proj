package com.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="purchase_orderDetail")
public class PurchaseOrderDetails {
	
	private String product_id; //many to one product object (tax and cost apart from product , can be separate entities)
	private double quantity;
	private double cost;
	private String tax_id;  //many to one tax object
	private double amount;
	private double tax_amount;
	
	@ManyToOne
	@JoinColumn(name="PO_number",referencedColumnName="PO_number")
	@JsonBackReference
	private PurchaseOrder po;  //foreign key
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)     //per company-better
	private long id;

	public PurchaseOrderDetails(String product_id, double quantity, double cost, String tax_id, double amount,
			double tax_amount, PurchaseOrder po) {
		super();
		this.product_id = product_id;
		this.quantity = quantity;
		this.cost = cost;
		this.tax_id = tax_id;
		this.amount = amount;
		this.tax_amount = tax_amount;
		this.po = po;
	}

	
	
	public PurchaseOrderDetails() {
		//super();
	}



	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getTax_id() {
		return tax_id;
	}

	public void setTax_id(String tax_id) {
		this.tax_id = tax_id;
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




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		//check equality based on ID only 
		PurchaseOrderDetails other = (PurchaseOrderDetails) obj;
		if (id != other.id)
			return false;
		return true;
	}

	
	

}
