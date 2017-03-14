package com.Models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("file")
public class FileUpload {
	
	
	private byte[] file;
	
	@Id
	private ObjectId id;
	
	
	private int idp;
	
	
	
	public FileUpload() {
		
	}

	public FileUpload(byte[] file, int id) {
		super();
		this.file = file;
		this.idp = id;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int getId() {
		return idp;
	}

	public void setId(int id) {
		this.idp = id;
	}
	
	

}
