package com.setup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


import com.Models.UserVerification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Path("/user")
public class User {
	
	@Path("/email")
	@GET
	public String email_verification(@QueryParam("details") String details) throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException
	{
		System.out.println(details);
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		UserVerification v = objectMapper.readValue(details,UserVerification.class);
		
		String valuate = v.getEmail() + v.getName();
		
		 MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(valuate.getBytes());

	        byte byteData[] = md.digest();

	        //convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }

	        System.out.println("Digest(in hex format):: " + sb.toString());
	        
	        v.setEmail_hexcode(sb.toString());
	        v.setEmail_status(0);
	        
	        
	        
	        System.out.println(v.toString());
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
			em.persist(v);
		em.getTransaction().commit();
		
		//send mail
		String status = send_mail(sb.toString());
		return status + v.toString();
	}
	
	
	
	@Path("/verify")
	@GET
	public String verify(@QueryParam("verify_code") String code)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		Query query=  em.createQuery("SELECT g FROM  UserVerification g  WHERE g.email_hexcode= :code");
		query.setParameter("code", code);
		UserVerification v = (UserVerification) query.getSingleResult();
		em.getTransaction().commit();
		
		if(v!=null)
		{
			v.setEmail_status(1);
			return "Account successfully verified";
		}
		else
			return "Mail verification failed";
		 
	}
	
	
	public String send_mail(String code) throws UnsupportedEncodingException
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
			InternetAddress from  = new InternetAddress("vaishnavirao17@gmail.com","Vaishnavi");
			message.setFrom(from);
			
			
			InternetAddress to  = new InternetAddress("vaishnavirao17@gmail.com");
			
			message.setRecipient(Message.RecipientType.TO, to);
			
			
			message.setSubject("Mail Confirmation");
			
			Multipart multipart = new MimeMultipart("alternative");
			
			MimeBodyPart textPart = new MimeBodyPart();
			String textContent = "Hi,  Click on the below link to confirm account !";
			textPart.setText(textContent);
			
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			String htmlContent = "<html>\n" +
                    "<body>Hi<p>Click here to confirm account!</p>\n" +
                    "\n" +
                    "<a href=\"http://localhost:8080/Accounting/VerifyUser.html?verify_code=" +  code + "\">\n" +
                    "Click here !</a>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";
			htmlPart.setContent(htmlContent, "text/html");
			
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(htmlPart);
			message.setContent(multipart);

			Transport.send(message);

			status = "Done";

		} catch (MessagingException e) {
			
			status="failed" + e.getMessage();
			//throw new RuntimeException(e);
		}
		
		return status;
	}
	
	

	@Path("/send_otp")
	@GET
	public String send_otp()
	{
	   	String otp = "536868";
		
		return "536868";
	}
	

}
