package com.setup;

public class AppEx extends Exception {
	
	private Integer status;
	private String msg;
	
	
	public AppEx(Integer code, String msg) {
		super();
		this.status = code;
		this.msg = msg;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
