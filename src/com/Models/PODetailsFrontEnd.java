package com.Models;

public class PODetailsFrontEnd {

	private String product_id;
	private double quantity;
	private double cost;
	private String tax_id;
	private double amount;
	private double tax_amount;
	
	
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
	@Override
	public String toString() {
		return "PODetailsFrontEnd [product_id=" + product_id + ", quantity=" + quantity + ", cost=" + cost + ", tax_id="
				+ tax_id + ", amount=" + amount + ", tax_amount=" + tax_amount + "]";
	}
	
	
}
