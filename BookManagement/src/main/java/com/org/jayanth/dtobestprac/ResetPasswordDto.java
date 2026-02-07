package com.org.jayanth.dtobestprac;

public class ResetPasswordDto {

	private String token;
	
	private String newPassword;

	public ResetPasswordDto() {
		super();
		}

	public ResetPasswordDto(String token, String newPassword) {
		super();
		this.token = token;
		this.newPassword = newPassword;
	}

	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ResetPasswordDto [token=" + token + ", newPassword=" + newPassword + "]";
	}

	

}
