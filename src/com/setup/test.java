package com.setup;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import com.Models.Student;



@Path("/hello")
public class test {
	
	@GET
	@Path("/test")
	@Produces({MediaType.APPLICATION_JSON})
	public Response test1() throws AppEx
	{
		
		Student a = new Student(22,"vaish",10);
		Student b = new Student(22,"abhgg",10);
		List<Student> c = new ArrayList<>();
		c.add(a);
		c.add(b);
		String abc="abc";
		if(abc == "abcd")
		{
			AppEx ae =  new AppEx(Response.Status.BAD_REQUEST.getStatusCode(),"empty string");
			return Response.status(ae.getStatus())
				       .entity(ae.getMsg()).build();
		}
		return Response.status(Response.Status.OK)
				       .entity(c).build();
		
	}
	
	
	@GET
	@Path("/token")
	@Produces(MediaType.APPLICATION_JSON)
	public String generate_token() throws JoseException
	{
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
	
	
	@GET
	@Path("/connect")
	@Produces(MediaType.APPLICATION_JSON)
	public String getRemoteConnection() {
		
		String status=null;
		String dbName = System.getProperty("RDS_DB_NAME");
		  String userName = System.getProperty("RDS_USERNAME");
		  String password = System.getProperty("RDS_PASSWORD");
		  String hostname = System.getProperty("RDS_HOSTNAME");
		  String port = System.getProperty("RDS_PORT");
		  String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
		    port + "/" + dbName + "?user=" + userName + "&password=" + password;
		  
		try {
			status +="Loading driver..." + " ";
		    Class.forName("com.mysql.jdbc.Driver");
		    status+= "Driver loaded!" + " " + jdbcUrl;
		  } catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		  }
  
		try {
			Connection con = DriverManager.getConnection(jdbcUrl);
			status += "Connected to db";
			
			  Statement setupStatement = null;
			 
			  try {
			    // Create connection to RDS DB instance
			    //conn = DriverManager.getConnection(jdbcUrl);
			    
			    // Create a table and write two rows
			    setupStatement = con.createStatement();
			    String createTable = "CREATE TABLE Beanstalk (Resource char(50));";
			    String insertRow1 = "INSERT INTO Beanstalk (Resource) VALUES ('EC2 Instance');";
			   
			    
			    setupStatement.addBatch(createTable);
			    setupStatement.addBatch(insertRow1);
			   // setupStatement.addBatch(insertRow2);
			    setupStatement.executeBatch();
			    setupStatement.close();
			    
			  } catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			  } finally {
			    status += "Closing the connection.";
			    if (con != null) try { con.close(); } catch (SQLException ignore) {}
			  }
			  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			status += "could not connect" + e.getMessage();
		}
		

		return status;
	  }
		
	
	@GET
	@Path("/jpa")
	@Produces(MediaType.APPLICATION_JSON)
	public String test_jpa()
	{
		String ans= "";
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
	    List<Student> result = em
	        .createQuery("SELECT g FROM Student g")
	        .getResultList();
	    for (Student g : result) {
	     	      ans+=    g.getAge() + " " + g.getName() + " ";
	         
	    }
	    em.getTransaction().commit();
	    em.close();
		return ans;
	}
	
	
	
}
