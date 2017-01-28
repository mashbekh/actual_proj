package com.Models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tax_settings")
public class Taxes {
	
	private String tax_name;
	private String tax_abbreviation;
	private String tax_desc;
	private java.math.BigDecimal tax_rate;
	private boolean tax_recoverable;
	private String company_id;
	private long id;
	
	@Id
	private String tax_id; //unique for each company  - auto inc
	
	
	
	@Override
	public String toString() {
		return "Taxes [tax_name=" + tax_name + ", tax_abbreviation=" + tax_abbreviation + ", tax_desc=" + tax_desc
				+ ", tax_rate=" + tax_rate + ", tax_recoverable=" + tax_recoverable + ", company_id=" + company_id
				+ ", tax_id=" + tax_id + "]";
	}
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getTax_name() {
		return tax_name;
	}
	public void setTax_name(String tax_name) {
		this.tax_name = tax_name;
	}
	public String getTax_abbreviation() {
		return tax_abbreviation;
	}
	public void setTax_abbreviation(String tax_abbreviation) {
		this.tax_abbreviation = tax_abbreviation;
	}
	public String getTax_desc() {
		return tax_desc;
	}
	public void setTax_desc(String tax_desc) {
		this.tax_desc = tax_desc;
	}
	public java.math.BigDecimal getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(java.math.BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getTax_id() {
		return tax_id;
	}
	public void setTax_id(String tax_id) {
		this.tax_id = tax_id;
	}
	public boolean isTax_recoverable() {
		return tax_recoverable;
	}
	public void setTax_recoverable(boolean tax_recoverable) {
		this.tax_recoverable = tax_recoverable;
	}
	
	public Taxes(String tax_name, String tax_abbreviation, String tax_desc, BigDecimal tax_rate, String company_id,
			String tax_id, boolean tax_recoverable, long id) {
		//super();
		this.tax_name = tax_name;
		this.tax_abbreviation = tax_abbreviation;
		this.tax_desc = tax_desc;
		this.tax_rate = tax_rate;
		this.company_id = company_id;
		this.tax_id = tax_id;
		this.tax_recoverable = tax_recoverable;
		this.id=id;
	}
	
	
	
	public Taxes() {
		//super();
	}
	
	//tax will have company object (to be added later)  - many to one
	
	
	
	
	

}
