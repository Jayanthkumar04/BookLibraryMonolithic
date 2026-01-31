package com.org.jayanth.service;

public interface EmailService {

	void sendEmail(String to,String subject,String body);
	
	void orderConfirmation(String to,String subject,String body);
	
	void sendOrderCancellation(String to,String subject,String body);
}
