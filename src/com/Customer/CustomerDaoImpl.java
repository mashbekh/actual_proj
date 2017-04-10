package com.Customer;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.ErrorHandling.EntityException;
import com.Models.BusinessPlayers;
import com.Models.Company;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class CustomerDaoImpl {
	
	public BusinessPlayers createCustomer(BusinessPlayers bplayer , String companyId ) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		Key<BusinessPlayers> key = null;
		ObjectId oid = null;
		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);
			
			//check condition if already exists among not deleted
			bplayer.setCompany(cmp);
			key = ds.save(bplayer);
			if(key == null)
				throw new EntityException(512, "could not create", null, null);

			
			bplayer.setId(new ObjectId(key.getId().toString()));


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && key == null)
				throw new EntityException(514,"create failed",null,null);

		}
		catch(Throwable e)
		{

			if(oid == null)
				throw new EntityException(500,"invalid id", "invalid" + e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(cmp != null && key == null)
				throw new EntityException(500,"create failed","create" + e.getMessage(),null);
		}


		return bplayer;
	}
	
	
	public List<BusinessPlayers> getCustomers(String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		List<BusinessPlayers> customerList = new ArrayList<>();

		try
		{

			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			Query<BusinessPlayers> vendorquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("isCustomer").equal(true).field("isDeleted").equal(false);
			customerList = vendorquery.asList();


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(512, "get failed", null, null);

			if(cmp != null && customerList.isEmpty() == true)
				throw new EntityException(513, "get list failed", null, null);

		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);

			if(oid != null && cmp == null)
				throw new EntityException(500, "get failed", "get" + e.getMessage(), null);

			if(cmp != null && customerList.isEmpty() == true)
				throw new EntityException(500, "get list failed", "getlist" + e.getMessage(), null);

		}
		return customerList;
	}

}
