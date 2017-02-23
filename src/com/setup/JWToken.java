package com.setup;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

import com.Models.Configuration;

@Path("/token")
public class JWToken {
	
	@GET
	@Path("/generate_token")
	@Produces(MediaType.APPLICATION_JSON)
	public String generate_token() throws JoseException
	{
		//MODIFY TO TAKE IN A USER_ID
		Long userid= (long) 4;
		RsaJsonWebKey rsaJsonWebKey =  RsaJwkGenerator.generateJwk(2048);
	    String key_id   = "Key" + userid;
	    // Give the JWK a Key ID (kid), which is just the polite thing to do
	    rsaJsonWebKey.setKeyId(key_id);

	    // Create the Claims, which will be the content of the JWT
	    JwtClaims claims = new JwtClaims();
	    //claims.setIssuer("Issuer");  // who creates the token and signs it
	    //claims.setAudience("Audience"); // to whom the token is intended to be sent
	    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
	    claims.setGeneratedJwtId(); // a unique identifier for the token
	    claims.setIssuedAtToNow();  // when the token was issued/created (now)
	    claims.setClaim("email","mail@example.com");  //add details of the user (like email, pwd etc)
	    claims.setSubject("subject"); // the subject/principal is whom the token is about
	    
	    
	    JsonWebSignature jws = new JsonWebSignature();
	    jws.setPayload(claims.toJson());
	    jws.setKey(rsaJsonWebKey.getPrivateKey());
	    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
	    String jwt = jws.getCompactSerialization();
	    //System.out.println(rsaJsonWebKey.toJson());
	    //System.out.println("JWT: " + jwt);
		
	    return jwt;
	}
	
	
	@Path("/validateToken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String verify_token(@QueryParam("name1") String token) throws Exception
	{
		 List<JsonWebKey> jsonWebKeys = new ArrayList<>();
		 Map<String, String> properties = new HashMap();
	
		    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
			        "Demo", properties);
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			Query query=  em.createQuery("SELECT g FROM  Configuration g  WHERE g.key_name= :name");
			query.setParameter("name", "PUBLIC_KEY");
			List<Configuration> publicKeys =  query.getResultList();
			em.getTransaction().commit();
			em.close();
	        //String status=null;
			
			for (Configuration publicKey : publicKeys) {
	            PublicJsonWebKey jsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(publicKey.getValue());
	            RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) jsonWebKey.getPublicKey());
	            

	            rsaJsonWebKey.setKeyId(publicKey.getNodeId());
	            jsonWebKeys.add(rsaJsonWebKey);
				
				
	        }
	  
			 //System.out.println(status);
			 
			//System.out.println(isValid(token, jsonWebKeys));
			return isValid(token, jsonWebKeys);
	}
	
	public String isValid(String token, List<JsonWebKey> jsonWebKeys) throws Exception {

        JwksVerificationKeyResolver jwksVerificationKeyResolver = new JwksVerificationKeyResolver(jsonWebKeys);

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime()
            .setRequireSubject()
            .setVerificationKeyResolver(jwksVerificationKeyResolver)
            .build();

        try {
        	JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
           return "JWT validation succeeded! " + jwtClaims;
        } catch (InvalidJwtException e) {
            return e.getMessage();
        }
    }
	
	
	
	
	

}
