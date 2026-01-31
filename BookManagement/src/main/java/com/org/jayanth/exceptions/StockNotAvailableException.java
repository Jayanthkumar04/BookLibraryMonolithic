package com.org.jayanth.exceptions;

public class StockNotAvailableException extends RuntimeException{

	public StockNotAvailableException(String msg)
	{
		super(msg);
	}
}
