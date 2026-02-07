package com.org.jayanth.dtobestprac;

public class ForgotPasswordResponseDto {

	private String email;
	private String url;
	public ForgotPasswordResponseDto() {
		super();
	}
	public ForgotPasswordResponseDto(String email, String url) {
		super();
		this.email = email;
		this.url = url;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ForgotPasswordResponseDto [email=" + email + ", url=" + url + "]";
	}

	
}
