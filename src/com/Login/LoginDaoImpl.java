package com.Login;

import org.jose4j.lang.JoseException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.JwtTokens.JwtToken;
import com.Models.JWTConfig;
import com.Models.User;
import com.Register.RegisterHelperFunctions;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;


public class LoginDaoImpl {

	//return token + name + userid
	public String verifyUser(String email, String pwd , int type) throws EntityException
	{
		Datastore ds = Morphiacxn.getInstance().getMORPHIADB("test");
		User user = null;
		Key<JWTConfig> jwtKey = null;
		String data = null;
		String token = null;

		try
		{
			if(type == 1 )
			{
				RegisterHelperFunctions helper = new RegisterHelperFunctions();
				String pwdHex = helper.generateHex(pwd); //excption
				Query<User> query = ds.createQuery(User.class).field("userStatus").equal(1).field("type").equal(1).field("userEmail").equal(email).field("password").equal(pwdHex);
				user = query.get(); 
				if(user == null)
					throw new EntityException(404, "not found", null, null);
			
			}

			else if(type == 2)
			{
				Query<User> query = ds.createQuery(User.class).field("userStatus").equal(1).field("type").equal(2).field("userEmail").equal(email);
				user = query.get(); 
				if(user == null)
					throw new EntityException(404, "not found", null, null);
				
			}
			
			JwtToken jwt = new JwtToken();
			token = jwt.generateToken(user.getId().toString(), user.getUserEmail(), user.getPassword());
			
			if(token  == null)
				throw new EntityException(512,"could not generate token",null,null);
			
			//save the token (if already existing, update token)
			JWTConfig config = new JWTConfig(user.getId().toString(),token);
			jwtKey = ds.save(config);
			if(jwtKey == null)
				throw new AppException(513,"could not save",null,null);
			
			data = user.getUserName() + "*" + user.getId().toString() + "*" + token;
		

		}
		catch(MongoException e)
		{
			if(user == null)
				throw new EntityException(514,"get failed", null, null);
			
			if(user != null && jwtKey == null)
				throw new EntityException(515, "save failed", null, null);

		}
		catch(JoseException e)
		{
			throw new EntityException(512,"could not generate token",null,null);
		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Throwable e)
		{

			if(user == null)
				throw new EntityException(500, e.getMessage(),null, null);
			
			if(user != null && token  == null)                                                //maybe exception caused while gen token
				throw new EntityException(512,"could not generate token",null,null);
			
			if(user != null && token  != null && jwtKey == null)
				throw new EntityException(500, e.getMessage(), null, null);
		}
		return data;

	}

}
