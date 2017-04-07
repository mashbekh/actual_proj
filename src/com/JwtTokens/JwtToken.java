package com.JwtTokens;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.JwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.ErrorHandling.AppException;
import com.Models.JWTConfig;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class JwtToken {

	public String generateToken(String userId, String email, String password, int type) throws JoseException{

		
		String jwt = null;
		RsaJsonWebKey rsaJsonWebKey = null;

			rsaJsonWebKey =  RsaJwkGenerator.generateJwk(2048);
			String keyId   = userId;
			rsaJsonWebKey.setKeyId(keyId);


			JwtClaims claims = new JwtClaims();
			//claims.setIssuer("Issuer");  // who creates the token and signs it
			//claims.setAudience("Audience"); // to whom the token is intended to be sent
			claims.setExpirationTimeMinutesInTheFuture(1440); 
			claims.setGeneratedJwtId(); 
			claims.setIssuedAtToNow(); 
			claims.setClaim("email",email); 
			claims.setClaim("password", password); 
			claims.setClaim("type", type); 
			claims.setSubject("user"); 

			JsonWebSignature jws = new JsonWebSignature();
			jws.setPayload(claims.toJson());
			jws.setKey(rsaJsonWebKey.getPrivateKey());
			jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
			jwt = jws.getCompactSerialization();

		return jwt;

	}


	public String validateToken(String id,String token) throws AppException
	{
		Datastore ds ;
		JWTConfig config = null;
		List<JsonWebKey> jsonWebKeys = new ArrayList<>();
		PublicJsonWebKey jsonWebKey = null;
		JwtClaims jwtClaims = null;

		try
		{
			ds =  Morphiacxn.getInstance().getMORPHIADB("test");
			ObjectId oid = new ObjectId(id);
			Query<JWTConfig> query = ds.createQuery(JWTConfig.class).field("id").equal(oid);
			config = query.get(); 
			if(config == null)
				throw new AppException(404,"not found",null,null);

			String publicKey = config.getTokenValue();

			jsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(publicKey);                 //jose exception
			RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) jsonWebKey.getPublicKey());
			jsonWebKeys.add(rsaJsonWebKey);


			JwksVerificationKeyResolver jwksVerificationKeyResolver = new JwksVerificationKeyResolver(jsonWebKeys);

			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime()
					.setRequireSubject()
					.setVerificationKeyResolver(jwksVerificationKeyResolver)
					.build();
			jwtClaims = jwtConsumer.processToClaims(token);                    //invalid token
			//open claims and do db check later
			
		}
		catch(JoseException e)
		{
			throw new AppException(512,"jose exception",null,null);
		}
		catch(InvalidJwtException e)
		{
			throw new AppException(513,"invalid jwt",e.getMessage(),null);
		}
		catch(MongoException e)
		{
			if(config == null)
				throw new AppException(514,"get failed",e.getMessage(),null);
		}
		catch(Throwable e)
		{
			throw new AppException(500,"validation issues",e.getMessage(),null);
		}

		return "verified";
	}

}
