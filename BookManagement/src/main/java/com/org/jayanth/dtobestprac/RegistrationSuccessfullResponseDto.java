package com.org.jayanth.dtobestprac;

public class RegistrationSuccessfullResponseDto {

	private String email;
	private String message;
	public RegistrationSuccessfullResponseDto(String email, String message) {
		super();
		this.email = email;
		this.message = message;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "RegistrationSuccessfullResponseDto [email=" + email + ", message=" + message + "]";
	}
	
}
