package com.paj2ee.library.service;

public interface EmailSenderService {

	void sendSimpleEmail(String to, String subject, String body);

}
