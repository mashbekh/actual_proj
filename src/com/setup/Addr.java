package com.setup;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Addr {
	
	private String street;
	private String city;
	private String num;
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
	public Addr(String street, String city, String num) {
		super();
		this.street = street;
		this.city = city;
		this.num = num;
	}
	public Addr() {
		//super();
	}
	@Override
	public String toString() {
		return "Addr [street=" + street + ", city=" + city + ", num=" + num + "]";
	}
	
	
	

}
