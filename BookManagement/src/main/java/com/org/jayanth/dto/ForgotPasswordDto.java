package com.org.jayanth.dto;

public class ForgotPasswordDto {

	private String email;

	public ForgotPasswordDto(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ForgotPasswordDto [email=" + email + "]";
	}
	
}
