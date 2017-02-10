package com.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "verification")
public class UserVerification {
	
	//assign a timestamp for expiry of hex code

	
	private String name;
	private String email;
	private String password;
	private String email_hexcode;
	private int email_status;
	private String mobile;
	private String otp;
	private int mobile_status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	public UserVerification(String name, String email, String password, String email_hexcode, int email_status,
			String mobile, String otp, int mobile_status) {
		//super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.email_hexcode = email_hexcode;
		this.email_status = email_status;
		this.mobile = mobile;
		this.otp = otp;
		this.mobile_status = mobile_status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail_hexcode() {
		return email_hexcode;
	}

	public void setEmail_hexcode(String email_hexcode) {
		this.email_hexcode = email_hexcode;
	}

	public int getEmail_status() {
		return email_status;
	}

	public void setEmail_status(int email_status) {
		this.email_status = email_status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public int getMobile_status() {
		return mobile_status;
	}

	public void setMobile_status(int mobile_status) {
		this.mobile_status = mobile_status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserVerification() {
		
	}

	@Override
	public String toString() {
		return "UserVerification [name=" + name + ", email=" + email + ", password=" + password + ", email_hexcode="
				+ email_hexcode + ", email_status=" + email_status + ", mobile=" + mobile + ", otp=" + otp
				+ ", mobile_status=" + mobile_status + ", id=" + id + "]";
	}
	
	
	
	
}
