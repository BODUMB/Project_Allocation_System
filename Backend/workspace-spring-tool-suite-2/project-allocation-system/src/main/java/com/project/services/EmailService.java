package com.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendEmail(String toEmail, String subject, String body) {
		try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rvy6969@gmail.com"); // <-- Replace with your email
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println(" Email sent to: " + toEmail);
		} catch(Exception e) {
			System.err.println(" Error sending email to " + toEmail + ": " + e.getMessage());
		}
		
	}
}
