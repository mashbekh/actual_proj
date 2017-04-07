package com.setup;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.ErrorHandling.EntityException;
import com.Models.Company;
import com.Models.Tax;
import com.Models.User;
import com.mongodb.MongoException;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.client.result.UpdateResult;

public class testException {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		boolean get = false;
		String p =null;
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		ObjectId taxOid = null;
		Key<Tax> key = null;
		Tax tx = null;
		int c = 0;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");

			taxOid = new ObjectId("58e7376770587d22ac9633a9");
			Query<Tax> taxquery = ds.createQuery(Tax.class).field("id").equal(taxOid);
			UpdateOperations<Tax> ops = ds.createUpdateOperations(Tax.class).set("isDeletd", true);
			UpdateResults result = ds.update(taxquery, ops,false);
			c = result.getUpdatedCount();
			System.out.println(c);
			

		}
		catch(MongoException e)
		{
			System.out.println(e.getMessage() + "*" + e.getClass());
		}
		catch(Exception e)
		{
			if(taxOid == null)
			{
				
				System.out.println("invalid" + "**" +e.getMessage() + "*" + e.getClass());
			}
			else if(c == 0)
			{
				System.out.println("update" + "**" +e.getMessage() + "*" + e.getClass());
			}
		}
	}
}