package com.org.jayanth.exceptions;

public class CartItemNotFoundException extends RuntimeException{

	public CartItemNotFoundException(String msg)
	{
		super(msg);
	}
}
