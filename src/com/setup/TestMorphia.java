package com.setup;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;


@Path("/yes")
public class TestMorphia {
	
	/*Morphia morphia = new Morphia(); 
	ServerAddress addr = new ServerAddress("127.0.0.1", 27017);
	String databaseName = "test";
	MongoClient mongoClient = new MongoClient(addr);
	//MongoClientOptions ap = new MongoClientOptions();
	//option builder etc
	
	
	Datastore datastore = morphia.createDatastore(mongoClient, databaseName);
	*/
	private  Datastore datastore1;
	@GET
	@Path("/embed")
	@Produces(MediaType.APPLICATION_JSON)
	public Morphiatry test()
	{
		/*
		
		Morphia morphia = new Morphia(); 
		String databaseName = "tax";
		ServerAddress addr = new ServerAddress("127.0.0.1", 27017);
		List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
		MongoCredential credentia = MongoCredential.createCredential(
		        "username", databaseName, "password".toCharArray());
		credentialsList.add(credentia);
		MongoClient client = new MongoClient(addr, credentialsList);
		Datastore datastore = morphia.createDatastore(client, databaseName);
		*/
		
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		
		
		System.out.println(datastore1);
		
		Addr a1  =  new Addr("pflayoutjkhh", "bengalmhjhuru", "312");
		Query<Taxes> userQueryDS = datastore1.createQuery(Taxes.class);
		userQueryDS.field("rate").equal(29.5);
		Taxes a = userQueryDS.get();
		Morphiatry m = new Morphiatry("svaishpp",a1, a);
		datastore1.save(m);
		
		Query<Morphiatry> userQueryDS1 = datastore1.createQuery(Morphiatry.class);
		userQueryDS1.field("name").equal("svaishpp");
		Morphiatry b = userQueryDS1.get();
		
		return b;
	}
	
	@GET
	@Path("/ref")
	public String createtax()
	{
		
		 datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		 System.out.println(datastore1);
		Taxes t = new Taxes("vat", 29.5);
		datastore1.save(t);
		
		return null;
	}

}
