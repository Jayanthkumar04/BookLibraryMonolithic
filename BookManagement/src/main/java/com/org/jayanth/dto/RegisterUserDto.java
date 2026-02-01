package com.org.jayanth.dto;

public class RegisterUserDto {

	private String name;
	
	private String email;

	public RegisterUserDto(String name, String email) {
		super();
		this.name = name;
		this.email = email;
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

	@Override
	public String toString() {
		return "RegisterUserDto [name=" + name + ", email=" + email + "]";
	}
	
	
}
