package ru.strela.mail;

import java.util.*;

public class MailTask {

	private Collection<String> emails;
	private String subject;
	private String body;
	private Map<String, byte[]> attachments;

	public Collection<String> getEmails() {
		return emails;
	}
	
	public void setEmails(Collection<String> emails) {
		this.emails = emails;
	}
	
	public void addEmail(String email) {
		if (emails == null) {
			emails = new HashSet<String>();
		}
		emails.add(email);
	}
	
	public void addEmails(List<String> emailList) {
		if (emails == null) {
			emails = new HashSet<String>();
		}
		emails.addAll(emailList);
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public void addAttachment(String attachmentName, byte[] attachment) {
		if (attachments == null) {
			attachments = new HashMap<String, byte[]>();
		}
		attachments.put(attachmentName, attachment);
	}

	public Map<String, byte[]> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, byte[]> attachments) {
		this.attachments = attachments;
	}
	
}
