package com.org.jayanth.dto;

public class GenericSucessResponseDto {

	private String message;

	public GenericSucessResponseDto(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GenericSucessResponseDto [message=" + message + "]";
	}

	
	
}
