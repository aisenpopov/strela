package ru.strela.mail;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.activation.*;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MailServiceImpl implements MailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	private Session session;

	@Value("${mail.smtp.host}")
	private String smtpHost;

	@Value("${mail.smtp.user}")
	private String smtpUser;
	
	@Value("${mail.smtp.password}")
	private String smtpPassword;
	
	private int smtpPort = 465;
	
	private String fromPerson = "Strela Team";
	private InternetAddress internetAddress;

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public void setFromPerson(String fromPerson) {
		this.fromPerson = fromPerson;
	}
	
	@PostConstruct
	public void postConstruct() throws Exception {
		if (StringUtils.isBlank(smtpHost) ) {
			LOGGER.warn("Mail server not configured - insert mail.smtp.host attribute in lconfig.xml");
			return;
		}
		
		Properties props = new Properties();
		Boolean needAuth = !StringUtils.isBlank(smtpUser) && !StringUtils.isBlank(smtpPassword);
		props.setProperty("mail.smtps.auth", String.valueOf(needAuth));
		props.setProperty("mail.smtps.host", smtpHost);
		props.setProperty("mail.smtps.port", String.valueOf(smtpPort));
		props.setProperty("mail.user", smtpUser);
		props.setProperty("mail.password", smtpPassword);
		
		session = Session.getDefaultInstance(props);
		
		internetAddress = new InternetAddress(smtpUser, fromPerson, "utf-8");
	}

	@Override
	public void sendMail(String address, String subject, String body) {
		send(address, subject, body, "text/plain");
	}

	@Override
	public void sendHTML(String address, String subject, String body) {
		send(address, subject, body, "text/html");
	}

	private void send(String address, String subject, String body, String contentType) {
		if (session == null) {
			LOGGER.info("Send [" + subject + "]=" + body);
			return;
		}

		if (StringUtils.isEmpty(address)) {
			return;
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject, "UTF-8");
			message.setFrom(internetAddress);

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, contentType + "; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			message.saveChanges();

			sendMessage(message);
		} catch (MessagingException ex) {
			LOGGER.error("Error delivering message", ex);
		} catch (Throwable th) {
			th.printStackTrace();
			LOGGER.error("Error sending message", th);
		}
	}

	private void sendMessage(MimeMessage message) throws Throwable {
		Transport transport = session.getTransport("smtps");
		transport.connect(smtpUser, smtpPassword);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	private MimeMultipart createMultipart(String type) {
		String subType;
		try {
			subType = new MimeType("multipart/" + type).getSubType();
		} catch (MimeTypeParseException me) {
			subType = type;
		}
		return new MimeMultipart(subType);
	}

	@Override
	public void sendHTML(String address, String subject, String body, byte[] attachment, String attachmentName) {
		ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, "application/octet-stream");
		sendHTML(address, subject, body, dataSource, attachmentName);
	}
	
	@Override
	public void sendHTML(String address, String subject, String body, File attachment, String attachmentName) {
		FileDataSource dataSource = new FileDataSource(attachment);
		sendHTML(address, subject, body, dataSource, attachmentName);
	}
	
	private void sendHTML(String address, String subject, String body, DataSource attachment, String attachmentName) {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		dataSources.put(attachmentName, attachment);
		sendHTMLMain(address, subject, body, dataSources);
	}
	
	@Override
	public void sendHTML(String address, String subject, String body, Map<String, byte[]> attachments) {
		Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
		for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
			ByteArrayDataSource dataSource = new ByteArrayDataSource(entry.getValue(), "application/octet-stream");
			dataSources.put(entry.getKey(), dataSource);
		}
		sendHTMLMain(address, subject, body, dataSources);
	}
	
	private void sendHTMLMain(String address, String subject, String body, Map<String, DataSource> attachments) {
		if (session == null) {
			LOGGER.info("Send [" + subject + "]=" + body);
			return;
		}

		if (StringUtils.isEmpty(address)) {
			return;
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject, "UTF-8");
			message.setFrom(internetAddress);

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

			MimeMultipart mixed = createMultipart("mixed");
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(MimeUtility.fold(0, body), "text/html; charset=UTF-8");
			messageBodyPart.setHeader("Content-Transfer-Encoding", "8bit");
			mixed.addBodyPart(messageBodyPart);

			for (Map.Entry<String, DataSource> entry : attachments.entrySet()) {
				String attachmentName = entry.getKey();
				if (StringUtils.isBlank(attachmentName)) {
					attachmentName = "undefined";
				}
				DataSource attachment = entry.getValue();
				
				BodyPart attachmentPart = new MimeBodyPart();
	
				MimetypesFileTypeMap map = new MimetypesFileTypeMap();
				String contentType = map.getContentType(attachmentName);
				if (contentType == null  ) contentType = "application/octet-stream";
	
				attachmentPart.setDataHandler(new DataHandler(attachment));
				attachmentPart.addHeader("Content-Type", contentType);
				attachmentPart.setFileName(MimeUtility.encodeText(attachmentName, "UTF-8", "Q"));
				attachmentPart.setDisposition(Part.ATTACHMENT);
				mixed.addBodyPart(attachmentPart);
			}
			message.setContent(mixed);
			message.saveChanges();

			sendMessage(message);
		} catch (MessagingException ex) {
			LOGGER.error("Error delivering message", ex);
		} catch (Throwable th) {
			th.printStackTrace();
			LOGGER.error("Error sending message", th);
		}
	}

	@Override
	public void sendHTML(String address, String subject, String filename, String[] keys, String[] values) {
		send(address, subject, filename, keys, values, true);
	}

	@Override
	public void sendMail(String address, String subject, String filename, String[] keys, String[] values) {
		send(address, subject, filename, keys, values, false);
	}

	public void send(String address, String subject, String filename, String[] keys, String[] values, boolean isHTML) {
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			String body = IOUtils.toString(in, "UTF-8");
			body = fillTemplate(body, keys, values);
			if (isHTML) {
				sendHTML(address, subject, body);
			} else {
				sendMail(address, subject, body);
			}
		} catch (Throwable th) {
			LOGGER.error("Cannot send mail", th);
		} finally {
			try {
				if (in != null) in.close();
			} catch (Throwable th) {}
		}
	}
	
	@Override
	public void sendHTML(String address, String subject, String body, String[] urlsInline) {
		if (session == null) {
			LOGGER.info("Send [" + subject + "]=" + body);
			return;
		}

		if (StringUtils.isEmpty(address)) {
			return;
		}

		try {
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject, "UTF-8");
			message.setFrom(internetAddress);

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			for (String urlString : urlsInline) {
				MimeBodyPart part = new MimeBodyPart();
				URL url = new URL(urlString);
				String name = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);
				part.setFileName(name);
				part.setDisposition(Part.INLINE);
				byte buffer[] = new byte[1024];
				int countReadByte;
				byte[] image = new byte[0];
				InputStream is = url.openStream();
				while((countReadByte = is.read(buffer)) > 0){
					image = ArrayUtils.addAll(image, ArrayUtils.subarray(buffer, 0, countReadByte));
				}
				part.setContentID(name);
				DataSource ds = new AttachmentDataSource(url.openStream(), name);
				part.setDataHandler(new DataHandler( ds ) );
				part.setContent(image, ds.getContentType());
				multipart.addBodyPart(part);
			}
			
			message.setContent(multipart);
			message.saveChanges();
			
			sendMessage(message);
		} catch (MessagingException ex) {
			LOGGER.error("Error delivering message", ex);
		} catch (Throwable th) {
			th.printStackTrace();
			LOGGER.error("Error sending message", th);
		}
	}
	
	private String fillTemplate(String body, String[] keys, String[] values) {
		for (int i = 0; i < keys.length; i++) {
			int ndx = body.indexOf(keys[i]);
			if (ndx >= 0) {
				body = body.replace(keys[i], values[i]);
				i--;
			}
		}
		return body;
	}
	
	private class AttachmentDataSource implements DataSource {
		private InputStream is;
		private String name;

		private AttachmentDataSource(InputStream is, String name) {
			this.is = is;
			this.name = name;
		}

		public InputStream getInputStream() throws IOException {
			return is;
		}

		public OutputStream getOutputStream() throws IOException {
			// not supposed to call
			throw new IllegalArgumentException();
		}

		public String getContentType() {
			// check if content type is broken
			return "image/gif";
		}

		public String getName() {
			return name;
		}
	}
}
