package com.setup;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;


public class Morphiacxn {


	private MongoClient mongoClient = null;
	private String mongoUrl="mongodb://127.0.0.1:27017";    //https:docs.mongodb.com/manual/reference/connection-string/
	private static Morphiacxn morphiacxn;
	private static final Morphia MORPHIA = new Morphia();
	private static Datastore ds;

	private Morphiacxn()
	{

	}

	public static Morphiacxn getInstance()
	{
		if(morphiacxn == null)
			morphiacxn = new Morphiacxn();
		return morphiacxn;

	}

	public Datastore getMORPHIADB(String dbName) {
		if (mongoClient == null) {

			init(dbName);
		}
		return ds;
	}

	@PostConstruct
	public void init(String dbName) {
		try {

			MongoClientOptions.Builder options = new MongoClientOptions.Builder()
					.connectionsPerHost(100);  
			MongoClientURI mongoClientURI = new MongoClientURI(mongoUrl, options);
			mongoClient = new MongoClient(mongoClientURI);
			ds = MORPHIA.createDatastore(mongoClient, dbName);
		} catch (MongoClientException e) {

			System.out.println(e.getMessage());
		}
	}

	@PreDestroy
	public synchronized void closeConnection() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}
}
