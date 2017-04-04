package com.setup;
/*
@Path("/token")
public class JWToken {
	/*
	@GET
	@Path("/generate_token")
	@Produces(MediaType.APPLICATION_JSON)
	public String generate_token() throws JoseException
	{
		
		Long userid = (long) 5;
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
	    
	    
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(new Configuration(userid,"PUBLIC_KEY",rsaJsonWebKey.toJson(),key_id));
		em.getTransaction().commit();
		em.close();
	    
	    JsonWebSignature jws = new JsonWebSignature();
	    jws.setPayload(claims.toJson());
	    jws.setKey(rsaJsonWebKey.getPrivateKey());
	    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
	    String jwt = jws.getCompactSerialization();
	  
	    System.out.println(rsaJsonWebKey.getPrivateKey());

	    System.out.println(rsaJsonWebKey.getKeyId());
	    return jwt;
	}
	
	
	@Path("/validateToken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String verify_token(@QueryParam("name1") String token) throws JoseException 
	{
		 List<JsonWebKey> jsonWebKeys = new ArrayList<>();
		
		    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			Query query=  em.createQuery("SELECT g FROM  Configuration g  WHERE g.key_name= :name");
			query.setParameter("name", "PUBLIC_KEY");
			Configuration publicKey =  (Configuration) query.getSingleResult();
			em.getTransaction().commit();
			em.close();
	       
	            PublicJsonWebKey jsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(publicKey.getValue());
	            
	        
	            
	           // System.out.println(jsonWebKey.getPrivateKey());
	            
	            RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey((RSAPublicKey) jsonWebKey.getPublicKey());
	            

	            //rsaJsonWebKey.setKeyId(publicKey.getNodeId());
	            
	            //System.out.println(rsaJsonWebKey.getKeyId());
	            
	            jsonWebKeys.add(rsaJsonWebKey);
			
		System.out.println(isValid(token, jsonWebKeys, rsaJsonWebKey));
			return isValid(token, jsonWebKeys, rsaJsonWebKey);
	}
	
	public String isValid(String token, List<JsonWebKey> jsonWebKeys, RsaJsonWebKey a) {
		
		//System.out.println(a.getKeyId());
		//System.out.println(a.getPrivateKey());

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
        	
        	//check doc jwt tokens for better check
        	
        	//if at all expired, generate a new one, and also store it in db, call generate token method
            return e.getMessage();
        }
    }
	
	
	
	*/
	


