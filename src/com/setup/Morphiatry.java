package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Morphiatry {

	
	@Id
	@JsonIgnore
	private ObjectId id;
	
	private String name;
	
	@Embedded
	private Addr addr;
	
	@Reference
	private Taxes t;

	public Addr getAddr() {
		return addr;
	}

	public void setAddr(Addr addr) {
		this.addr = addr;
	}

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



	public Taxes getT() {
		return t;
	}

	public void setT(Taxes t) {
		this.t = t;
	}

	public Morphiatry( String name, Addr addr, Taxes t) {
		super();
		//this.id = id;
		this.name = name;
		this.addr = addr;
		this.t= t;
	}

	public Morphiatry() {
		
	}

	@Override
	public String toString() {
		return "Morphiatry [id=" + id + ", name=" + name + ", addr=" + addr.toString() + ",tax=" + t.toString() + "    ]";
	}

	
	
	
}
