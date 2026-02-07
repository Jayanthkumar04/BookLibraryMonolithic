package com.org.jayanth.exceptions;

public class WrongUserCredentialsException extends RuntimeException{

	public WrongUserCredentialsException(String message) {
		super(message);
	}
}
