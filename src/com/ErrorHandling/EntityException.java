package com.ErrorHandling;

public class EntityException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String message;
	private Object entity;
	private String devMsg;
	
	public EntityException(int statusCode, String message, Object entity, String devMsg) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.entity = entity;
		this.devMsg  = devMsg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public String getDevMsg() {
		return devMsg;
	}

	public void setDevMsg(String devMsg) {
		this.devMsg = devMsg;
	}
	
	
	

}
