package com.setup;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import com.Models.ttlTry;


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
		
		Query<Taxes> userQueryDS = datastore1.createQuery(Taxes.class);
		userQueryDS.field("rate").equal(30);
		Taxes a = userQueryDS.get();
		
		Addr a1  =  new Addr("pflayout", "bengaluru", "312",a);
		
		Morphiatry m = new Morphiatry("heyy",a1, a);
		datastore1.save(m);
		
		Query<Morphiatry> userQueryDS1 = datastore1.createQuery(Morphiatry.class).disableValidation().field("t.d.name").equal("sumukh");
		
		Morphiatry b = userQueryDS1.get();
		System.out.println(b.toString());
		return b;
		
	}
	
	@GET
	@Path("/check")
	public void p()
	{
		
	}
	
	@GET
	@Path("/tax")
	public void createtax()
	{
		
		 datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		 Query<Details> q = datastore1.createQuery(Details.class);
		 q.field("name").equal("sumukh");
		 Details d = q.get();
		Taxes t = new Taxes("vat", 40,d);
		datastore1.save(t);
		
	}
	
	@GET
	@Path("/detail")
	public void createdetails()
	{
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		Details d = new Details("sumukh",23);
		datastore1.save(d);
	}

	//update a kind of tax
	@GET
	@Path("/update")
	public String updatetax()
	{
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		Query<Taxes> userQueryDS = datastore1.createQuery(Taxes.class).field("rate").equal(30);
		Taxes a = userQueryDS.get();
		a.setName("GST");
		datastore1.merge(a);
		return a.toString();
	}
	
	@GET
	@Path("/ttl")
	public String tryp()
	{
		ttlTry a  =  new ttlTry(new Date(), 0);
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		datastore1.save(a);
		return "done";
		
	}
	
	@GET
	@Path("/t")
	public String x()
	{
		
		ttlTry a  =  new ttlTry(null, 1);
		datastore1 = Morphiacxn.getInstance().getMORPHIADB("test");
		datastore1.save(a);
		return "done";
		
	}
	
}


