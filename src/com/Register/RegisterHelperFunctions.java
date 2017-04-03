package com.Register;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
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
public class RegisterHelperFunctions {
	
	//generates Random hexadecimal based on String given
		public String generateHex(String valuate) throws NoSuchAlgorithmException
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(valuate.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		}
		
		//generate random 6 digit integer
		public int generateRandomInt()
		{
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			return n;

		}
		
		
		 public String sendMail(String hexcode, String userId, String email) throws UnsupportedEncodingException, MessagingException
		   {
			 
			 if(hexcode== null || userId ==null || email == null)
				 throw new MessagingException();
			 
			   final String username = "simplifytax17@gmail.com";
			   final String password = "asdfghjkl17";

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
				
					Message message = new MimeMessage(session);
					InternetAddress from  = new InternetAddress("simplifytax17@gmail.com","Abc");
					message.setFrom(from);
					InternetAddress to  = new InternetAddress(email);
					message.setRecipient(Message.RecipientType.TO, to);
					message.setSubject("Mail Confirmation");
					Multipart multipart = new MimeMultipart("alternative");
					MimeBodyPart textPart = new MimeBodyPart();
					String textContent = "Hi,  Click on the below link to confirm account !";
					textPart.setText(textContent);
					MimeBodyPart htmlPart = new MimeBodyPart();
					String link;
					String body;
					if(!hexcode.equals("empty"))
					{
						link = "<a href=\"http://localhost:8080/Accounting/VerifyUser.html?email=" +  email + "&verifyCode=" + hexcode  + "-"  + userId + "\">\n" ;
						body = "Congratulations! Your account has been created. Click below to Activate your account!";
					}
					else
					{
						link = "<a href=\"http://localhost:8080/Accounting/verifyOtp.html?email=" +  email + "&identifier=" + userId + "\">\n" ;
						body = "Congratulations! Click below to enter Otp and verify your mobile Number";
					}
					String htmlContent = "<html>\n" +
		                    "<body>Hi<p>" + body + "</p>\n" +
		                    "\n" +
		                    link + 
		                    "Click here !</a>\n" +
		                    "\n" +
		                    "</body>\n" +
		                    "</html>";
					htmlPart.setContent(htmlContent, "text/html");
					multipart.addBodyPart(textPart);
					multipart.addBodyPart(htmlPart);
					message.setContent(multipart);
					Transport.send(message);
					status = "Sent";
					return status;
		   }
}