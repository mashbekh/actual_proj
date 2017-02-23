package com.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "fileupload")
public class FileUpload {
	
	
	private byte[] file;
	
	@Id
	private int id;
	
	
	
	public FileUpload() {
		
	}

	public FileUpload(byte[] file, int id) {
		super();
		this.file = file;
		this.id = id;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
