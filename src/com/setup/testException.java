package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.Models.User;
import com.mongodb.MongoException;
import com.mongodb.MongoExecutionTimeoutException;

public class testException {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Datastore ds;
		 boolean get = false;
		 String p =null;
		try
		{ 
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
		   
			Morphiatry a = new Morphiatry("vaish",null,null);
			Key<Morphiatry> key = ds.save(a);
			if(key == null)
				System.out.println("couldnt create");
			get = true;
			p = key.getId().toString();
			System.out.println(p + get);

	}
		catch(MongoException e)
		{
			
			if(p == null)
				System.out.println("create didnt work");
		}
		catch(Exception e)
		{
			//System.out.println(e.getMessage() + e.getStackTrace());
		}

}
}