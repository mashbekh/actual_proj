package com.setup;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/mail")
public class Email {
	
	@GET
	@Path("/send")
	public String send_mail()
	{
		final String username = "vaishnavirao17@gmail.com";
		final String password = "amandambaht*17";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
String status;
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("vaishnavirao17@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("vaishnavirao17@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Hello! :)");

			Transport.send(message);

			status = "Done";

		} catch (MessagingException e) {
			
			status="failed" + e.getMessage();
			//throw new RuntimeException(e);
		}
		
		return status;
	}

}
