package com.setup;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Reference;

@Embedded
public class Addr {
	
	private String street;
	private String city;
	private String num;
	
	@Reference
	private Taxes t1;
	
	
	
	public Taxes getT1() {
		return t1;
	}
	public void setT1(Taxes t1) {
		this.t1 = t1;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Addr(String street, String city, String num, Taxes t1) {
		super();
		this.street = street;
		this.city = city;
		this.num = num;
		this.t1 = t1;
	}
	public Addr() {
		//super();
	}
	@Override
	public String toString() {
		return "Addr [street=" + street + ", city=" + city + ", num=" + num + "]";
	}
	
	
	

}
