package com.tibco.automation.oiag.common.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailUtil {

	private final static Log logger = LogFactory.getLog(MailUtil.class);

	public static void sendMail(String sub, String msg, String to, String from) {
		try {
			logger.info("Sneding message with subject: " + sub + "\n content: " + msg);
			Properties props = new Properties();
			props.putAll(ConfigurationConverter.getMap(PropertiesUtil.getBundle().subset("mailer")));
			System.out.println("host:  " + props.getProperty("mail.smtp.host"));
			final String username = props.getProperty("username");
			final String password = props.getProperty("password");

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(sub);
			message.setContent(msg, "text/html");
			Transport.send(message);

			logger.info("Mail sent successfully!...");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			logger.warn("Unable to send e-mail", e);
		}
	}

	public static void main(String[] args) {

	}
}
