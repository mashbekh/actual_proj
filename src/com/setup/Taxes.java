package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("taxes")
public class Taxes {
	
	private String name;
	private double rate;
	
	@Id
	private ObjectId id;

	public Taxes(String name, double rate) {
		super();
		this.name = name;
		this.rate = rate;
	}
	
	public Taxes() {
		
	}

	@Override
	public String toString() {
		return "Taxes [name=" + name + ", rate=" + rate + ", id=" + id + "]";
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
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	

}
