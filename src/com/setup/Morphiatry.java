package com.setup;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Morphiatry {

	
	@Id
	@JsonIgnore
	private ObjectId id;
	
	private String name;
	
	@Embedded
	@Indexed
	private List<Addr> adrlist;
	
	/*@Reference
	private Taxes t;
*/
	


	public ObjectId getId() {
		return id;
	}


	
	public List<Addr> getAdrlist() {
		return adrlist;
	}



	public void setAdrlist(List<Addr> adrlist) {
		this.adrlist = adrlist;
	}



	public Morphiatry(String name, List<Addr> adrlist) {
		super();
		this.name = name;
		this.adrlist = adrlist;
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



	public Morphiatry() {
		
	}

	
	
}
