package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("taxes")
public class Taxes {
	
	private String name;
	private double rate;
	
	@Reference
	Details d;
	
	@Id
	private ObjectId taxid;

	public Taxes(String name, double rate, Details d) {
		super();
		this.name = name;
		this.rate = rate;
		this.d = d;
	}
	
	public Taxes() {
		
	}

	@Override
	public String toString() {
		return "Taxes [name=" + name + ", rate=" + rate + ", id=" + taxid + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public ObjectId getId() {
		return taxid;
	}

	public void setId(ObjectId id) {
		this.taxid = id;
	}

	public Details getD() {
		return d;
	}

	public void setD(Details d) {
		this.d = d;
	}
	
	

}
