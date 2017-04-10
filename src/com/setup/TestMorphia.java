package com.setup;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.Models.Products;
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
	public int test()
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
		
		/*
		Query<Taxes> userQueryDS = datastore1.createQuery(Taxes.class);
		userQueryDS.field("rate").equal(30);
		Taxes a = userQueryDS.get();
		List<Addr> adrlist = new ArrayList<>();
		Addr a1  =  new Addr("pflayout", "bengaluru", "312",new ObjectId(), a);
		Addr a2  =  new Addr("pflayojhghg", "hybd", "676",new ObjectId(),a);
		adrlist.add(a1);
		adrlist.add(a2);
		
		Morphiatry m = new Morphiatry("heyy",adrlist);
		datastore1.save(m);
		*/
		//"58eb28f54c3e9d38ec176406"
		Query<Morphiatry> userQueryDS1 = datastore1.createQuery(Morphiatry.class).disableValidation().filter("id", new ObjectId("58eb28f54c3e9d38ec176406")).filter("adrlist.id", new ObjectId("58eb28f54c3e9d38ec176404"));
		UpdateOperations<Morphiatry> ops =  datastore1.createUpdateOperations(Morphiatry.class).set("adrlist.$.num", "444");
		UpdateResults result = datastore1.update(userQueryDS1, ops, false);
		
		return result.getUpdatedCount();
		
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


