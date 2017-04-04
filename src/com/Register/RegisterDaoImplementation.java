package com.Register;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.mail.MessagingException;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.jose4j.lang.JoseException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.JwtTokens.JwtToken;
import com.Models.JWTConfig;
import com.Models.User;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class RegisterDaoImplementation {

	public String createUser(User temp) throws AppException, EntityException
	{
		Datastore ds;
		Key<User> key;
		String  valuate;
		String objectIdhex = null;
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");

			//check if existing user with either mail/mobile  - status = 1


			Query<User> queryMail = ds.createQuery(User.class).field("userEmail").equal(temp.getUserEmail()).field("userStatus").equal(1);
			User userMail = queryMail.get();

			Query<User> queryMob = ds.createQuery(User.class).field("mobileNumber").equal(temp.getMobileNumber()).field("userStatus").equal(1);
			User userMob = queryMob.get();

			if(userMail != null && userMob != null)
				throw new AppException(432,"user exists with email and mob",null,null);

			else if(userMail !=null && userMob == null)
				throw new AppException(433,"user exists with email",null,null);

			else if(userMail == null && userMob != null)
				throw new AppException(434,"user exists with mob",null,null);

			//if both null - user with status 1 does not exist


			//check if user with mail/mob is under verification/ yet to be deleted

			Query<User> queryVMail = ds.createQuery(User.class).field("userEmail").equal(temp.getUserEmail()).field("userStatus").equal(0);
			User userVMail = queryVMail.get();

			Query<User> queryVMob = ds.createQuery(User.class).field("mobileNumber").equal(temp.getMobileNumber()).field("userStatus").equal(0);
			User userVMob = queryVMob.get();

			if(userVMail != null && userVMob != null)
				throw new AppException(435,"user verification with email and mob",null,null);

			else if(userVMail !=null && userVMob == null)
			{

				throw new AppException(436,"user verification with email",null,null);

			}
			else if(userVMail == null && userVMob != null)
				throw new AppException(437,"user verification with mob",null,null);

			//if both null - can register User
			RegisterHelperFunctions helper =  new RegisterHelperFunctions();

			if(temp.getType() == 1)                                //normal user
			{

				valuate = temp.getUserName() + temp.getUserEmail() + temp.getMobileNumber() + temp.getPassword();
				valuate +=  helper.generateRandomInt();
				String emailHex = helper.generateHex(valuate) ;  //create email hex using name,email,mobile,pwd,random string
				String password = temp.getPassword();                 //generate hash of password for storage
				String passwordHex = helper.generateHex(password);
				int otp  = helper.generateRandomInt();

				temp.setEmailHexcode(emailHex);
				temp.setPassword(passwordHex);
				temp.setOtp(otp);
				temp.setCreatedAt(new Date());
				key = ds.save(temp);
				if(key == null)
					throw new AppException(440,"could not create",null,null);

				objectIdhex = key.getId().toString();               //create successful

				//send mail
				helper.sendMail(temp.getEmailHexcode(), objectIdhex , temp.getUserEmail());         

			}

			if(temp.getType() == 2)   //email verified
			{
				int otp  = helper.generateRandomInt();
				temp.setOtp(otp);
				temp.setCreatedAt(new Date());
				key = ds.save(temp);
				if(key == null)
					throw new AppException(440,"could not create",null,null);
				objectIdhex = key.getId().toString();               //create successful
				return objectIdhex;
			}

		}

		catch (NoSuchAlgorithmException e)
		{ 
			throw new AppException(438,"no such algorithm",e.getMessage(),null);   //not created yet
		}
		catch(MongoException e)
		{
			if(objectIdhex == null)                                              //create failed save, initial GET
				throw new AppException(439,"create failed",e.getMessage(),null);

			//any other kind of DB error (time out) - send id anyway(if null then not created)

			throw new EntityException(516, "unknown db error", objectIdhex, null);

		}
		catch(AppException e)
		{
			throw e;
		}

		catch(UnsupportedEncodingException e)    //mail failed - send userId
		{
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(MessagingException e) //mail failed - send userId
		{
			//maybe invalid address
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(Exception e)
		{
			throw new EntityException(500, "internal error", objectIdhex, null);  //if null - could not create
		}
		catch(Throwable e)
		{
			throw new EntityException(500, "internal error", objectIdhex, null);
		}

		return objectIdhex;

	}


	public String resendMail(String id) throws AppException, EntityException              //don't have to send back user id
	{
		Datastore ds;
		Key<User> key;
		String  valuate;
		String objectIdhex = null;
		String updatedIdhex = null;

		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			ObjectId oid  = new ObjectId(id);
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			User user = query.get();
			if(user == null)                                                            // does not exist/ deleted
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

			objectIdhex = user.getId().toHexString();

			if(user.getUserStatus() == 1)
				throw new AppException(433, "already registered", null, null);

			if(user.getEmailVerified() == 1)
				throw new AppException(434, "email verified", null, null);



			long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

			if( (int)timeDiff > 15)                                           // if within session
				throw new AppException(437,"session time out", null, null);


			valuate = user.getUserName() + user.getUserEmail() + user.getMobileNumber() + user.getPassword();
			RegisterHelperFunctions helper  = new RegisterHelperFunctions();
			valuate +=  helper.generateRandomInt();
			String emailHex = helper.generateHex(valuate) ; 
			user.setEmailHexcode(emailHex);
			key = ds.merge(user);                                          //updates if existing
			if(key == null)
				throw new AppException(440,"could not update",null,null);

			updatedIdhex = key.getId().toString();
			helper.sendMail(user.getEmailHexcode(), objectIdhex , user.getUserEmail()); 


		}
		catch (AppException e)
		{
			throw e;
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new AppException(438,"no such algorithm",e.getMessage(),null);
		}
		catch(MongoException e)
		{
			if(objectIdhex == null)                                                   //GET failed
				throw new AppException(439,"fetch failed",e.getMessage(),null);

			if(objectIdhex != null && updatedIdhex == null)                                                             // update failed
				throw new AppException(440,"could not update",e.getMessage(),null);

			//any other kind of DB error (time out)

			throw new AppException(516,"unknown db error",e.getMessage(),null);

		}
		catch(UnsupportedEncodingException e)                                      //mail failed - send userId
		{
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(MessagingException e)                                               //mail failed - send userId
		{

			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(Exception e)
		{
			throw e;
		}
		catch(Throwable e)
		{
			throw e;
		}

		return objectIdhex;
	}

	public String updateEmail(String id, String newEmail) throws AppException , EntityException   //dont have to return id
	{
		Datastore ds;
		Key<User> key;
		String  valuate;
		String objectIdhex = null;
		String updatedIdhex = null;
		User userMail = null;
		User userVMail = null;
        System.out.println(newEmail);
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");


			ObjectId oid  = new ObjectId(id);                                   //find unverified user
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			User user = query.get(); 
			if(user == null)                                                                                      //doesnt exist/ deleted
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

			objectIdhex = user.getId().toString();

			if(user.getUserStatus() == 1)
				throw new AppException(433,"verified user", objectIdhex,null);

			if(user.getEmailVerified() == 1)
				throw new AppException(434,"email verified otp left",null,null);


			/*
			       updated new mail, bt mail dint work and you wanna give same email again                                                                                  //make sure same email isn't provided again
			if(user.getUserEmail().equals(newEmail))
				throw new AppException(Response.Status.FOUND.getStatusCode(),"same email",null,null);             //same email provided again

			 */



			//check if within session
			long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

			if( (int)timeDiff > 15)                                           
				throw new AppException(435,"session time out",null,null);

			// check if email already exists with verified user
			Query<User> queryMail = ds.createQuery(User.class).field("userEmail").equal(newEmail).field("userStatus").equal(1);

			userMail = queryMail.get();
			if(userMail != null)
				throw new AppException(436,"verified user exists with email",null,null);

			//check if new email is already under verification 
			Query<User> queryVMail = ds.createQuery(User.class).field("userEmail").equal(newEmail).field("userStatus").equal(0).field("id").notEqual(oid);

			userVMail = queryVMail.get();
			if(userVMail != null)
				throw new AppException(437,"user verification with email",null,null);

			//ready to update
			valuate = user.getUserName() + newEmail + user.getMobileNumber() + user.getPassword();
			RegisterHelperFunctions helper  = new RegisterHelperFunctions();
			valuate +=  helper.generateRandomInt();
			String emailHex = helper.generateHex(valuate) ; 

			user.setUserEmail(newEmail);
			user.setEmailHexcode(emailHex);
			key = ds.merge(user);
			if(key == null)                          //merge failed
				throw new AppException(438,"update failed",null,null);
			updatedIdhex = key.getId().toString();
			helper.sendMail(user.getEmailHexcode(), objectIdhex , user.getUserEmail());

		}
		catch (AppException e)
		{
			throw e;
		}
		catch (MongoException e)
		{
			if(objectIdhex == null)
				throw new AppException(439,"get with id failed",null,null);

			if(objectIdhex != null && userMail == null)                           //initial get worked
				throw new AppException(439,"get failed",null,null);

			if(objectIdhex != null && userMail != null && userVMail == null)           //initial get worked
				throw new AppException(439,"get failed",null,null);
			
			if(objectIdhex != null && userMail != null && userVMail != null && updatedIdhex == null)           //initial get worked
				throw new AppException(438,"update failed",null,null);

			throw new AppException(516,"unknown db error",e.getMessage(),null);    //mail could not be sent at the end

		}
		catch(NoSuchAlgorithmException e)
		{
			throw new AppException(440,"no such algorithm",e.getMessage(),null);
		}
		catch(UnsupportedEncodingException e)                                      //mail failed - send userId
		{
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(MessagingException e)                                               //mail failed - send userId
		{

			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(Exception e)
		{
			throw e;
		}
		catch(Throwable e)
		{
			throw e;
		}
		return updatedIdhex;

	}

	public User confirmEmail(String verficationCode) throws EntityException, AppException   //must send user, need phone 
	{
		String[] code = verficationCode.split("-") ;
		String id = code[1];
		String hexcode = code[0];
		Datastore ds;
		Key<User> key;
		String objectIdhex = null;
		String updatedIdhex = null;
		User user = null;

		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");

			ObjectId oid  = new ObjectId(id);                                   //find unverified user
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			user = query.get(); 
			if(user == null)                                                                                      //doesnt exist/ deleted - back to register
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

			objectIdhex = user.getId().toString();

			if(user.getUserStatus() == 1)
				throw new EntityException(433,"verified user", user,null);

			if(user.getEmailVerified() == 1)
				throw new EntityException(434,"email verified otp left",user,null);

			long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

			if( (int)timeDiff > 15)                                           
				throw new EntityException(435,"session time out",user,null);

			if(hexcode.equals(user.getEmailHexcode()))
			{
				user.setEmailVerified(1);
				key = ds.merge(user);
				if(key == null)
					throw new EntityException(440,"could not verify",user,null);
				updatedIdhex =  key.getId().toString();

			}
			else //wrong hexcode
			{
				throw new EntityException(441, "could not match", user, null);
			}
		}
		catch(AppException e)
		{
			throw e;

		}

		catch (EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(objectIdhex == null)   //gt failed
				throw new EntityException(439,"get with id failed",null,null);    //could not do get op, -  retry conf, dont send anythng

			if(objectIdhex !=null && updatedIdhex == null)
				throw new EntityException(440,"could not verify",user,null); 

			throw new EntityException(515,"unknown db error", user, e.getMessage());

		}
		
		catch(Exception e)
		{
			throw new EntityException(500,e.getMessage(),user,null);
		}
		catch(Throwable e)
		{
			throw new EntityException(500,e.getMessage(),user,null);
		}
		return user;


	}
	
	public String sendOtp(String id) throws AppException, EntityException
	{
		Datastore ds;
		String objectIdhex = null;
		
		
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");

			ObjectId oid  = new ObjectId(id);                                   //find unverified user
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			User user = query.get(); 
			if(user == null)                                                                                      //doesnt exist/ deleted
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

			objectIdhex = user.getId().toString();
			
			if(user.getUserStatus() == 1)
				throw new EntityException(433,"verified user", objectIdhex,null);

			                                                                            //status = 0 and mobile verified not possible 
			long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

			if( (int)timeDiff > 15)                                           
				throw new EntityException(435,"session time out", objectIdhex,null);
			
			System.out.println(user.getOtp());
			RegisterHelperFunctions helper = new RegisterHelperFunctions();
			                                                                           //send otp and if that succeeds link in mail, now assume that otp has been sent
			
				helper.sendMail("empty", objectIdhex, user.getUserEmail());
		
		}
		catch(AppException e)
		{
			throw e;
		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(objectIdhex == null)
				throw new AppException(439,"get with id failed",null,null);
			
			throw new AppException(516,"unknown db error", e.getMessage(),null);
		}
		catch(UnsupportedEncodingException e)                                      //mail failed - send userId
		{
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(MessagingException e)                                               //mail failed - send userId
		{

			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(Exception e)
		{
			throw new EntityException(500,e.getMessage(),null,null);
		}
		catch(Throwable e)
		{
			throw new EntityException(500,e.getMessage(),null,null);
		}
		return objectIdhex;
}
	
	
	public String resendOtp(String id) throws AppException, EntityException
	{
		Datastore ds;
		String objectIdhex = null;
		Key<User> key= null;
		String updatedIdhex  = null;
		
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");

			ObjectId oid  = new ObjectId(id);                                   //find unverified user
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			User user = query.get(); 
			if(user == null)                                                                                      //doesnt exist/ deleted
				throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

			objectIdhex = user.getId().toString();
			
			if(user.getUserStatus() == 1)
				throw new EntityException(433,"verified user",  objectIdhex,null);

			                                                                            //status = 0 and mobile verified not possible 
			long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

			if( (int)timeDiff > 15)                                           
				throw new EntityException(435,"session time out", objectIdhex,null);
			
			RegisterHelperFunctions helper = new RegisterHelperFunctions();
			int otp = helper.generateRandomInt();
			user.setOtp(otp); 
			key = ds.merge(user);
			if(key == null)
				throw new AppException(440,"could not update",null,null);
			
			System.out.println(otp);
			helper.sendMail("empty", objectIdhex, user.getUserEmail());
			
			
		}
		catch(AppException e)
		{
			throw e;
		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(objectIdhex == null)
				throw new AppException(439,"get with id failed",null,null);
			
			if(objectIdhex != null && updatedIdhex == null)
				throw new AppException(440,"update failed",null,null);
			
			throw new AppException(516,"unknown db error", e.getMessage(),null);
		}
		catch(UnsupportedEncodingException e)                                      //mail failed - send userId
		{
			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(MessagingException e)                                               //mail failed - send userId
		{

			throw new EntityException(515,"could not send mail",objectIdhex,null);
		}
		catch(Exception e)
		{
			throw new EntityException(500,e.getMessage(),null,null);
		}
		catch(Throwable e)
		{
			throw new EntityException(500,e.getMessage(),null,null);
		}
		return objectIdhex;
}
	
	
	//dont have to send id, but on success send token
	   public String confirmOtp(String id, int otp) throws EntityException, AppException  
	   { 
		   Datastore ds;
			Key<User> key;
			String objectIdhex = null;
			String updatedIdhex = null;
			User user = null;
			String token = null;
			String data = null;
			Key<JWTConfig> jwtKey = null;

			try
			{ 
				ds = Morphiacxn.getInstance().getMORPHIADB("test");

				ObjectId oid  = new ObjectId(id);                                   //find unverified user
				Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
				user = query.get(); 
				if(user == null)                                                                                      //doesnt exist/ deleted - back to register
					throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

				objectIdhex = user.getId().toString();

				if(user.getUserStatus() == 1)
					throw new EntityException(433,"verified user", objectIdhex,null);


				long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

				if( (int)timeDiff > 15)                                           
					throw new EntityException(435,"session time out", objectIdhex,null);

				if(otp == user.getOtp())     //check otp
				{
					user.setMobileVerified(1);
					user.setUserStatus(1);   //successful verification
					user.setCreatedAt(null);  //prevent deletion
					key = ds.merge(user);
					if(key == null)
						throw new EntityException(440,"could not update", objectIdhex,null);
					updatedIdhex =  key.getId().toString();

				}
				else     //wrong OTP entered
				{
					throw new EntityException(441, "could not match", user, null);
				}
				
				//send token
				JwtToken jwt = new JwtToken();
				token = jwt.generateToken(user.getId().toString(), user.getUserEmail(), user.getPassword());
				
				if(token  == null)
					throw new EntityException(512,"could not generate token",null,null);  //verified user, login for token
				
				//save the token 
				JWTConfig config = new JWTConfig(user.getId().toString(),token);
				jwtKey = ds.save(config);
				if(jwtKey == null)
					throw new AppException(513,"could not save",null,null);    //verified user, login for token
				
				data = user.getUserName() + "*" + user.getId().toString() + "*" + token;
				
			}
			catch(AppException e)
			{
				throw e;

			}

			catch (EntityException e)
			{
				throw e;
			}
			catch(JoseException e)
			{
				throw new EntityException(512,"could not generate token",null,null);  //verified user, login for token
			}
			catch(MongoException e)
			{
				if(objectIdhex == null)                                                //get failed
					throw new EntityException(439,"get with id failed",null,null);    //could not do get op, -  retry conf, dont send anythng

				if(objectIdhex !=null && updatedIdhex == null)                          //update failed
					throw new EntityException(440,"could not update",user,null); 
				
				if(objectIdhex !=null && updatedIdhex != null && jwtKey == null)           //create token failed, login for token
					throw new EntityException(512,"could not create token",user,null);

				throw new EntityException(515,"unknown db error", user, e.getMessage());

			}
			
			catch(Throwable e)
			{
				if(objectIdhex == null)   
					throw new EntityException(500,"get with id failed",user,null);    

				if(objectIdhex !=null && updatedIdhex == null)                          
					throw new EntityException(500,"could not update",user,null); 
				
				else                                                                    //if both not null, error in tokenisation  - login
					throw new EntityException(514,"could not create token",user,null);
			}
		   
		   return data;
	   }
	
	   
	   public String updateMobile(String id, String newMobile) throws AppException , EntityException   //dont have to return id
		{
			Datastore ds;
			Key<User> key;
			String objectIdhex = null;
			String updatedIdhex = null;
			User userMob = null;
			User userVMob = null;
			try
			{ 
				ds = Morphiacxn.getInstance().getMORPHIADB("test");


				ObjectId oid  = new ObjectId(id);                                   //find unverified user
				Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
				User user = query.get(); 
				if(user == null)                                                                                      //doesnt exist/ deleted
					throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

				objectIdhex = user.getId().toString();

				if(user.getUserStatus() == 1)
					throw new AppException(433,"verified user", objectIdhex,null);

				                                                                                        //check if within session
				long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

				if( (int)timeDiff > 15)                                           
					throw new AppException(435,"session time out", objectIdhex,null);

				                                                                                       // check if email already exists with verified user
				Query<User> queryMob = ds.createQuery(User.class).field("mobileNumber").equal(newMobile).field("userStatus").equal(1);

				userMob = queryMob.get();
				if(userMob != null)
					throw new AppException(436,"verified user exists with mobile",null,null);

				//check if new email is already under verification 
				Query<User> queryVMob = ds.createQuery(User.class).field("mobileNumber").equal(newMobile).field("userStatus").equal(0).field("id").notEqual(oid);

				userVMob = queryVMob.get();
				if(userVMob != null)
					throw new AppException(437,"user verification with mobile",null,null);

				//ready to update
			
				RegisterHelperFunctions helper  = new RegisterHelperFunctions();
				int otp = helper.generateRandomInt();
				user.setMobileNumber(newMobile);
				user.setOtp(otp);
				key = ds.merge(user);
				if(key == null)                          //merge failed
					throw new AppException(438,"update failed",null,null);
				
				updatedIdhex = key.getId().toString();
				System.out.println("new" + otp);  //send otp if success then send mail
				helper.sendMail("empty", updatedIdhex, user.getUserEmail());

			}
			catch (AppException e)
			{
				throw e;
			}
			catch (MongoException e)
			{
				if(objectIdhex == null)
					throw new AppException(439,"get with id failed",null,null);

				if(objectIdhex != null && userMob == null)                           //initial get worked
					throw new AppException(439,"get failed",null,null);

				if(objectIdhex != null && userMob != null && userVMob == null)           //initial get worked
					throw new AppException(439,"get failed",null,null);
				
				if(objectIdhex != null && userMob != null && userVMob != null && updatedIdhex == null)           //initial get worked
					throw new AppException(438,"update failed",null,null);

				throw new EntityException(516,"unknown db error",updatedIdhex,null);    //mail could not be sent at the end

			}
			
			catch(UnsupportedEncodingException e)                                      //mail failed - send userId, update worked
			{
				throw new EntityException(515,"could not send mail",updatedIdhex,null);
			}
			catch(MessagingException e)                                               //mail failed - send userId, update worked
			{

				throw new EntityException(515,"could not send mail",updatedIdhex,null);   // if null then update failed
			}
			catch(Exception e)
			{
				throw new EntityException(500,e.getMessage(),null,null);
			}
			catch(Throwable e)
			{
				throw new EntityException(500,e.getMessage(),null,null);
			}
			return updatedIdhex;

		}
	   
	   //get user if not verified
	   public User confirmOtpMail(String id) throws AppException, EntityException
	   {
		   Datastore ds;
			String objectIdhex = null;
			User user = null;
		
			try
			{ 
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				ObjectId oid  = new ObjectId(id);                                   //find unverified user
				Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
				user = query.get(); 
				if(user == null)                                                                                      //doesnt exist/ deleted
					throw new AppException(Response.Status.NOT_FOUND.getStatusCode(),"user does not exist",null,null);

				objectIdhex = user.getId().toString();
				
				if(user.getUserStatus() == 1)
					throw new EntityException(433,"verified user", user,null);

				                                                                                        //check if within session
				long timeDiff = (new Date().getTime()/60000) - (user.getCreatedAt().getTime()/60000);

				if( (int)timeDiff > 15)                                           
					throw new EntityException(435,"session time out", user,null);
				

			}
			catch(AppException e)
			{
				throw e;
			}
			catch (MongoException e)
			{
				if(objectIdhex == null)
					throw new AppException(439,"get with id failed",null,null);
				
				throw new EntityException(516,"unknown db error",user,null);  //check if user null
			}
			catch(EntityException e)
			{
				throw e;
			}
			catch(Exception e)
			{
				
				throw new EntityException(500,e.getMessage(),user,null);
			}
			catch(Throwable e)
			{
				throw new EntityException(500,e.getMessage(),user,null);
			}
		
			return user;
}
}

