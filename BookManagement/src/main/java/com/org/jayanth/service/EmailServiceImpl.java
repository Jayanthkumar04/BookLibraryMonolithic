package com.org.jayanth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;
	@Override
	public void sendEmail(String to, String subject, String body) {
		
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setText(body);
		message.setSubject(subject);
		
		javaMailSender.send(message);
		
	}
	@Override
	public void orderConfirmation(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setText(body);
		message.setSubject(subject);
		javaMailSender.send(message);
	}
	@Override
	public void sendOrderCancellation(String to, String subject, String body) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body+" please reset this is expire in 15 minutes from now");
		javaMailSender.send(message);
	}

}
