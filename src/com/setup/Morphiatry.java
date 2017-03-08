package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Morphiatry {

	
	@Id
	private ObjectId id;
	
	private String name;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Morphiatry(ObjectId id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Morphiatry() {
		
	}

	@Override
	public String toString() {
		return "Morphiatry [id=" + id + ", name=" + name + "]";
	}
	
	
}
