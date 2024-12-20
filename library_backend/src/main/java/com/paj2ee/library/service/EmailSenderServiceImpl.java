package com.paj2ee.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

	private static final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

	@Value("${spring.mail.username:#{null}}")
	private String APP_EMAIL;

	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleEmail(String to, String subject, String body) {

		if (null == APP_EMAIL) {
			throw new RuntimeException("Cannot send email. Application email is not set. Please fix.");
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom(APP_EMAIL);

		mailSender.send(message);

		logger.info("Email sent to: " + to);
	}

}
