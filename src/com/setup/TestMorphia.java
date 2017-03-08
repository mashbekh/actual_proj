package com.setup;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Path("/yes")
public class TestMorphia {
	
	@GET
	@Path("/yo")
	public String test()
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
		
		Morphia morphia = new Morphia(); 
		ServerAddress addr = new ServerAddress("127.0.0.1", 27017);
		String databaseName = "test";
		MongoClient mongoClient = new MongoClient(addr);
		Datastore datastore = morphia.createDatastore(mongoClient, databaseName);
		
		Query<Morphiatry> userQueryDS = datastore.createQuery(Morphiatry.class);
		userQueryDS.field("name").equal("vaish");
		Morphiatry a = userQueryDS.get();
		
		
		
		return a.toString();
	}

}
