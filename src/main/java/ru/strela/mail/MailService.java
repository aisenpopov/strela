package ru.strela.mail;

import java.io.File;
import java.util.Map;

public interface MailService {

	void sendMail(String address, String subject, String body);

	void sendHTML(String address, String subject, String body);

	void sendHTML(String address, String subject, String body, byte[] attachment, String attachmentName);

	void sendHTML(String address, String subject, String body, File attachment, String attachmentNameg);

	void sendHTML(String address, String subject, String body, Map<String, byte[]> attachments);

	void sendHTML(String address, String subject, String filename, String[] keys, String[] values);

	void sendHTML(String address, String subject, String body, String[] urlsInline);

	void sendMail(String address, String subject, String filename, String[] keys, String[] values);

}
