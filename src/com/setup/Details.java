package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("details")
public class Details {
	
	private String name;
	private int age;
	
	@Id
	private ObjectId id;

	public Details(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public Details() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	

}
