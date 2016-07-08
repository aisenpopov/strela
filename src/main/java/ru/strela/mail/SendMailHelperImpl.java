package ru.strela.mail;

import org.apache.commons.collections.list.SynchronizedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import ru.strela.util.localization.Dictionary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@ImportResource("classpath:mail-templates.xml")
public class SendMailHelperImpl implements SendMailHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(SendMailHelperImpl.class);

	private static final String LOCALE ="ru";
	
	@Autowired
	private MailService mailService;
	
	@Value("${domain}")
	private String domain;
	
	@Value("mail-templates.xml")
	private String mailTemplates;
	
	@SuppressWarnings("unchecked")
	private List<MailTask> tasks = SynchronizedList.decorate(new ArrayList<MailTask>());
	private SendMailThread thread;
	private boolean isShutdown;
	
	private class SendMailThread extends Thread {
		
		@Override
		public void run() {
			while(!isShutdown) {
				try{
					if(tasks.size() > 0) {
						sendMail(tasks.remove(0));
					} else {
						synchronized (this) {
							this.wait();
						}
					}
				} catch (Exception e) {
					LOG.error("Error send notification: ", e);
				}
			}
		}
		
	}
	
	private void addSendMailTask(MailTask task) {
		synchronized (thread) {
			tasks.add(task);
			thread.notify();
		}
	}
	
	private void sendMail(MailTask task) {
		if (task.getEmails() != null) {
			int i = 0;
			Collection<String> emails = task.getEmails();
			LOG.info("Start send mails " + emails.size());
			for(String email : emails) {
				String subject = task.getSubject();
				String body = task.getBody();
				if (task.getAttachments() != null) {
					mailService.sendHTML(email, subject, body, task.getAttachments());
				} else {
					mailService.sendHTML(email, subject, body);
				}
				
				if (i % 100 == 0) {
					LOG.info("Send mails " +  i + " of " + emails.size());
				}
				i++;
			}
			LOG.info("End send mails " + emails.size());
		}
	}
	
		
	@PostConstruct
	public void postConstruct() throws Exception {
		thread = new SendMailThread();
		thread.start();
	}
	
	@PreDestroy
	public void preDestroy() throws Exception {
		isShutdown = true;
		synchronized (tasks) {
			tasks.notify();
		}
	}

	private String getMail(String item) {
		Dictionary templates = new Dictionary();
		templates.init(mailTemplates, LOCALE);
		return templates.localize(item + "-mail", null);
	}

	private String getSubject(String item) {
		Dictionary templates = new Dictionary();
		templates.init(mailTemplates, LOCALE);
		return templates.localize(item + "-subject", null);
	}
	
	private String replaceVariables(String mail, Map<String, String> replaceMap) {
		replaceMap.put("<DOMAIN/>", domain);
		for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
			String value = entry.getValue() != null ? entry.getValue() : "";
			mail = mail.replace(entry.getKey(), value);
		}
		return mail;
	}

	@Override
	public void sendRecoveryMail(String name, String email, String recoveryCode) {
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("<NAME/>", name);
		replaceMap.put("<EMAIL/>", email);
		replaceMap.put("<CODE/>", recoveryCode);
		replaceMap.put("<DOMAIN/>", domain);

		String subject = replaceVariables(getSubject("recovery"), replaceMap);
		String mail = replaceVariables(getMail("recovery"), replaceMap);

		MailTask task = new MailTask();
		task.setSubject(subject);
		task.setBody(mail);
		task.setEmails(Collections.singletonList(email));
		addSendMailTask(task);
	}
}
