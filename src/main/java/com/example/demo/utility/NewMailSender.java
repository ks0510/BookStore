package com.example.demo.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NewMailSender {
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	SimpleMailMessage simpleMessage;
	
	public void sendMail(String to,String subject,String message) {
		simpleMessage.setTo(to);
		simpleMessage.setSubject(subject);
		simpleMessage.setText(message);
		
		javaMailSender.send(simpleMessage);
	}

}
