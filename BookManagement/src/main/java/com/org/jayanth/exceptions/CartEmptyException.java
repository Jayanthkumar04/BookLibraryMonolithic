package com.org.jayanth.exceptions;

public class CartEmptyException extends RuntimeException{

	public CartEmptyException(String message)
	{
		super(message);
	}
}
