package com.ErrorHandling;

public class AppException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private int statusCode;
	private String devMessage;
	private String exceptionMsg;
	private String trace;

	public AppException(int code, String devMessage, String exceptionMsg, String trace) {
		super();
		this.devMessage = devMessage;
		this.exceptionMsg = exceptionMsg;
		this.statusCode= code;
		this.trace = trace;
	}
	
	public AppException() {
		
	}

	

	public String getDevMessage() {
		return devMessage;
	}

	public void setDevMessage(String devMessage) {
		this.devMessage = devMessage;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public int getstatusCode() {
		return statusCode;
	}

	public void setstatusCode(int code) {
		this.statusCode = code;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	
	
	
}
