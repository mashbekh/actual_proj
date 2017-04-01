package com.Register;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
import javax.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.TempUser;
import com.Models.User;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.WriteConcernException;
import com.setup.Morphiacxn;
import com.setup.Taxes;


public class RegisterDaoImpl {
	
	
	public TempUser create(TempUser temp) throws AppException, EntityException
	{
		/*
		 * hash password for storage
		 * hash valuate to generate hex code for mail confirmation
		 * unique email required
		 * check if gmail/normal user
		 * if mail resent, need different hexcode
		 * Check if mail and phone exists in USER document
		*/
	
		Datastore ds;
		Key<TempUser> key;
		String  valuate;
		
		
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			//step 1 check if entry exists of email/number in USER
			
			Query<User> userQueryDS = ds.createQuery(User.class).field("userEmail").equal(temp.getUserEmail());
			User u = userQueryDS.get();
			if(u != null)
			{
			   throw new AppException(Response.Status.FOUND.getStatusCode(),"user email exists",null,null);	
				
			}
			
			Query<User> query = ds.createQuery(User.class).field("mobileNumber").equal(temp.getMobileNumber());
			User user = userQueryDS.get();
			if(user != null)
			{
			   throw new AppException(Response.Status.FOUND.getStatusCode(),"user mobile exists",null,null);	
				
			}
			
			if(temp.getUserType() == 1)                                //normal user
			{
				valuate = temp.getUserName() + temp.getUserEmail() + temp.getMobileNumber() + temp.getPassword();
				valuate +=  generateRandomInt();
				String emailHex = generateHex(valuate) ;  //create email hex using name,email,mobile,pwd,random string
				String password = temp.getPassword();                 //generate hash of password for storage
				String passwordHex = generateHex(password);
				temp.setEmailHexcode(emailHex);
				temp.setPassword(passwordHex);
				key = ds.save(temp);
		        
		        if(key.getId() == null)
		        {
		        	throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"could not create temp user",null,null);
		        }
		        
		        //temp is local object
		        temp.setId(key.getId().toString());
		        sendMail(temp.getEmailHexcode(), temp.getUserEmail());              //send mail and note time
		        temp.setEmailSentstatus(true);
		        temp.setEmailSenttime(new Date());
		        ds.merge(temp);
			}
			
			else if(temp.getUserType() == 2)
			{
												//email verification not needed
				key = ds.save(temp);
				if(key.getId() == null)
				{
					throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"could not create temp user",null,null);
				}
				
			}
			
	        return temp;
	        
		}
		
		catch(NoSuchAlgorithmException ex)
		{
			throw new AppException();
		}
	      
		
		// if mail fails, send temp
		catch(UnsupportedEncodingException e)
		{
			throw new EntityException(515,"could not send mail",temp,null);
		}
		
		
		catch(MessagingException e)    
		{
			throw new EntityException(515,"could not send mail",temp,null);
			
		}
		
		catch( MongoException e )
		{
			if(e instanceof DuplicateKeyException)
				throw new AppException(Response.Status.CONFLICT.getStatusCode(), "duplicate email/number" ,e.getMessage(), e.getStackTrace().toString());
			
		/*	else if(e instanceof WriteConcernException || e instanceof MongoExecutionTimeoutException)   
			{
				if(temp.getId()!=null)                                 //created but update issue
					throw new EntityException(516,"could not update temp",temp);
				
				else                                                   //create issue
					throw new AppException(517,"could not create",null,null);
			}
			*/
			
			
			//if none of the above  ------  distinguish create/update operations
			if(temp.getId()!=null)                                              //created but update issue
				throw new EntityException(516,"could not update temp",temp,e.getMessage());
			
			else                                                                 //create issue
				throw new AppException(517,"could not create",e.getMessage(),null);
			
		}
		
		catch(Exception ex)
		{
			//if none of the above  ------  distinguish create/update operations
			if(temp.getId() != null)                                              //created but update issue
				throw new EntityException(516,"could not update temp",temp,ex.getMessage());
			
			else                                                                 //create issue
				throw new AppException(517,"could not create",ex.getMessage(),null);
		}
		
		catch(Throwable ex)
		{
			//if none of the above  ------  distinguish create/update operations
			if(temp.getId()!=null)                                              //created but update issue
				throw new EntityException(516,"could not update temp",temp, ex.getMessage());
			
			else                                                                 //create issue
				throw new AppException(517,"could not create",ex.getMessage(),null);
		}
		 
		
	}
	
	
	//public void updateMail
	
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
	
	  
	public TempUser updateGuser(TempUser temp) throws AppException
	{
		Datastore ds;
		TempUser user;
		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			Query<TempUser> userQueryDS = ds.createQuery(TempUser.class).field("userEmail").equal(temp.getUserEmail());
			user = userQueryDS.get();
			user.setMobileNumber(temp.getMobileNumber());
			ds.merge(user);
		}

		catch( MongoException e )
		{
			if(e instanceof DuplicateKeyException)
				throw new AppException(Response.Status.CONFLICT.getStatusCode(), "duplicate mobile" ,e.getMessage(), e.getStackTrace().toString());

			else if(e instanceof WriteConcernException)
				throw new AppException();

			else if(e instanceof MongoExecutionTimeoutException)
				throw new AppException(Response.Status.REQUEST_TIMEOUT.getStatusCode(),"timeout",e.getMessage(),e.getStackTrace().toString());

			
			throw new AppException(Response.Status.FORBIDDEN.getStatusCode(),"unknown mongo error",e.getMessage(),e.getStackTrace().toString());

		}

		catch(Exception ex)
		{
			throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal exception",ex.getMessage(),ex.getStackTrace().toString());
		}

		catch(Throwable ex)
		{
			throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal error",ex.getMessage(),ex.getStackTrace().toString());
		}
		return user;
	}

	
	   public String sendMail(String hexcode, String email) throws UnsupportedEncodingException, MessagingException
	   {
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
				String htmlContent = "<html>\n" +
	                    "<body>Hi<p>Congratulations! Your account has been created. Click here to Activate your account!</p>\n" +
	                    "\n" +
	                    "<a href=\"http://localhost:8080/Accounting/rest/VerifyUser.html?email=" +  email + "&verifyCode=" + hexcode  + "\">\n" +
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
	
	   
	   public TempUser confirmEmail(String email, String hexcode) throws AppException, EntityException
	   {
		    Datastore ds;
		    TempUser temp = null;
		   try
		   {
			   ds = Morphiacxn.getInstance().getMORPHIADB("test");
			   Query<TempUser> query = ds.createQuery(TempUser.class).field("userEmail").equal(email).field("emailHexcode").equal(hexcode);
			   temp  = query.get();  //returns existing/null
			   
			   if(temp == null)   //get worked
			   {
				   throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user with above not found",null,null);
			   }
			   
			   
			   UpdateOperations<TempUser> op = ds.createUpdateOperations(TempUser.class).set("emailVerified", true).set("emailVerifiedtime", new Date());
			   UpdateResults results = ds.update(query, op);
			   int count = results.getUpdatedCount();
			   if(count == 1 )
			   {
				   temp.setEmailVerified(true);
			   }
			   return temp;
		   }
		   
		   catch( MongoException e )
			{
				 if(e instanceof WriteConcernException || temp != null)   //Get succeeded, update dint happen
					 throw new EntityException(515,"try again",null,e.getMessage());
				
				else if(e instanceof MongoExecutionTimeoutException)
					throw new AppException(Response.Status.REQUEST_TIMEOUT.getStatusCode(),"timeout",e.getMessage(),e.getStackTrace().toString());
				
			    if(temp == null)    //get failed
			    	throw new EntityException(515,"try again",null,e.getMessage());
			    
			    //any other
			    throw new AppException(Response.Status.FORBIDDEN.getStatusCode(),"unknown mongo error",e.getMessage(),e.getStackTrace().toString());
			  
			    	
			}
		   
		   catch(Exception ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal exception",ex.getMessage(),ex.getStackTrace().toString());
			}
			
			catch(Throwable ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal error",ex.getMessage(),ex.getStackTrace().toString());
			}
		  
	   }
	   
	   public TempUser sendOtp(TempUser temp) throws AppException
	   {
		   /*
		    * generate random 6 digit int, send it thru SMS and also save it in db
		    * while returning user , remove otp and send
		    */
		   Datastore ds;
		   TempUser user;
		   try
		   {
			   ds = Morphiacxn.getInstance().getMORPHIADB("test");
			   Query<TempUser> userQueryDS = ds.createQuery(TempUser.class).field("userEmail").equal(temp.getUserEmail());
			   user = userQueryDS.get();
			   
			   
			   user.setMobileOtp(123456);
			   ds.merge(user);
		   }
		   catch( MongoException e )
			{
				if(e instanceof DuplicateKeyException)
					throw new AppException(Response.Status.CONFLICT.getStatusCode(), "duplicate entry/index" ,e.getMessage(), e.getStackTrace().toString());
				
				else if(e instanceof WriteConcernException)
					throw new AppException();
				
				else if(e instanceof MongoExecutionTimeoutException)
					throw new AppException(Response.Status.REQUEST_TIMEOUT.getStatusCode(),"timeout",e.getMessage(),e.getStackTrace().toString());
				
				//if none of the above
				throw new AppException(Response.Status.FORBIDDEN.getStatusCode(),"unknown mongo error",e.getMessage(),e.getStackTrace().toString());
				
			}
		   
		   catch(Exception ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal exception",ex.getMessage(),ex.getStackTrace().toString());
			}
			
			catch(Throwable ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal error",ex.getMessage(),ex.getStackTrace().toString());
			}
		   
		   user.setMobileOtp(0);
		   return user;
	   }
	   
	   /*
	   public User confirmMobile(TempUser tempuser, int otp) throws AppException
	   {
		   /* at this stage, email is already verified
		    * if mobile and email status is true, move entry to USER document
		    * generate token
		    * 
		   */
	   /*
		   User user ;
		   Datastore ds;
		   TempUser temp;
		   try
		   {
			   ds = Morphiacxn.getInstance().getMORPHIADB("test");
			   Query<TempUser> userQueryDS = ds.createQuery(TempUser.class).field("userEmail").equal(tempuser.getUserEmail());
			   temp = userQueryDS.get();
			   
			   if(temp.getMobileOtp() != otp)
			   {
				   throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),"otp doesnt match","otp no match",null);
			   }
			   
			   temp.setMobileStatus(true);
			   ds.merge(temp);   //if update failed would anyway throw exception, so continue
			   
			   if(temp.isEmailStatus() == false)
				   throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(),"email not verified","email not verified",null);
			   
			   //if both status  = true, create User
			   user = new User(temp);
			   Key<User> key = ds.save(user);
			   
			   if(key.getId() == null)
				   throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"could not create user",null,null);
				   
			   
		   }
		   catch( MongoException e )
			{
				if(e instanceof DuplicateKeyException)
					throw new AppException(Response.Status.CONFLICT.getStatusCode(), "duplicate entry/index" ,e.getMessage(), e.getStackTrace().toString());
				
				else if(e instanceof WriteConcernException)
					throw new AppException();
				
				else if(e instanceof MongoExecutionTimeoutException)
					throw new AppException(Response.Status.REQUEST_TIMEOUT.getStatusCode(),"timeout",e.getMessage(),e.getStackTrace().toString());
				
				//if none of the above
				throw new AppException(Response.Status.FORBIDDEN.getStatusCode(),"unknown mongo error",e.getMessage(),e.getStackTrace().toString());
				
			}
		   
		   catch(Exception ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal exception",ex.getMessage(),ex.getStackTrace().toString());
			}
			
			catch(Throwable ex)
			{
				throw new AppException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"internal error",ex.getMessage(),ex.getStackTrace().toString());
			}
		   
		   return user;
		   
	   }
*/
}
