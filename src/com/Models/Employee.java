package com.Models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Employee {
	
	private int id;
	private String name;
	private boolean permanent;
	private Date date;
	private List<Address> address;
	private long[] phoneNumbers;
	private String role;
	private List<String> cities;
	private Map<String, String> properties;
	
	
	
	public Employee() {
		//super();
	}
	public Employee(int id, String name, boolean permanent, Date date, List<Address> address, long[] phoneNumbers,
			String role, List<String> cities, Map<String, String> properties) {
		super();
		this.id = id;
		this.name = name;
		this.permanent = permanent;
		this.date = date;
		this.address = address;
		this.phoneNumbers = phoneNumbers;
		this.role = role;
		this.cities = cities;
		this.properties = properties;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPermanent() {
		return permanent;
	}
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public long[] getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(long[] phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("***** Employee Details *****\n");
		sb.append("ID="+getId()+"\n");
		sb.append("Name="+getName()+"\n");
		sb.append("Permanent="+isPermanent()+"\n");
		sb.append("date="+getDate()+"\n");
		sb.append("Role="+getRole()+"\n");
		sb.append("Phone Numbers="+Arrays.toString(getPhoneNumbers())+"\n");
		sb.append("Address="+getAddress()+ address.size() + "\n");
		sb.append("Cities="+Arrays.toString(getCities().toArray())+"\n");
		sb.append("Properties="+getProperties()+"\n");
		sb.append("*****************************");
		
		return sb.toString();
	}
	public List<String> getCities() {
		return cities;
	}
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}

