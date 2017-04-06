package com.CompanyProfile;

import java.util.*;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.Key;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.ColumnSettings;
import com.Models.Company;
import com.Models.EstimateSettings;
import com.Models.InvoiceSettings;
import com.Models.POSettings;
import com.Models.User;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class CompanyDaoImpl {

	public String createCompany(Company company, String userId)  throws AppException, EntityException //return company Id
	{
		Datastore ds = null;
		User user = null;
		Key<Company> key = null;
		String companyId = null;
		ObjectId oid = null;
		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(userId);
			Query<User> query = ds.createQuery(User.class).field("id").equal(oid);
			user = query.get();
			if(user == null)
				throw new EntityException(404, "not found", null, null);

			List<User> users = new ArrayList<>();
			users.add(user);
			company.setUsers(users);

			key = ds.save(company);
			if(key == null)
				throw new EntityException(512, "could not create", null, null);

			companyId = key.getId().toString()  + "*" + company.getBusinessDetails().getBusinessName();
		}
		catch(MongoException e)
		{
			if(oid == null)
				throw new EntityException(514, "invalid id", null, null);

			if( oid != null && user == null)
				throw new EntityException(513, "get failed", null, null);

			if(user != null && companyId == null)
				throw new EntityException(512, "could not create", null, null);

		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Throwable e)
		{
			if(user == null)
				throw new EntityException(500, "get failed", "get" + e.getMessage(), null);

			if(user != null && companyId == null)
				throw new EntityException(500, "create", "create" + e.getMessage(), null);

		}
		return companyId;
	}


	public Company getCompany(String cmpId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(cmpId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

		}
		catch(MongoException e)
		{
			if(oid == null)
				throw new EntityException(513,"invalid id",null,null);

			if(oid!=null && cmp == null)
				throw new EntityException(512,"get failed",null,null);
		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500,"invalid id",e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed",e.getMessage(),null); //string is an object
		}
		return cmp;
	}


	public Company updateFormatSettings(POSettings po, EstimateSettings es, InvoiceSettings inv, ColumnSettings col, String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		Key<Company> key = null;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);
			
			po.setPOSeqNo(cmp.getPoSettings().getPOSeqNo());
			es.setEstimateSeqNo(cmp.getEstimateSettings().getEstimateSeqNo());
			inv.setInvoiceSeqNo(cmp.getEstimateSettings().getEstimateSeqNo());
			
			cmp.setPoSettings(po);
			cmp.setEstimateSettings(es);
			cmp.setInvoiceSettings(inv);
			cmp.setColSettings(col);
			key = ds.merge(cmp);
			if(key == null)
				throw new EntityException(513,"update failed",null,null);
			

		}
		catch(MongoException e)
		{
			if(oid == null)
				throw new EntityException(514,"invalid id",null,null);

			if(oid!=null && cmp == null)
				throw new EntityException(512,"get failed",null,null);
			
			if(oid!=null && cmp != null && key == null)
				throw new EntityException(513,"update failed",null,null);
		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500,"invalid id", "invalid" + e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);
			
			if(oid!=null && cmp != null && key == null)
				throw new EntityException(500,"update failed","update" + e.getMessage(),null);
			
		}
		return cmp;
	}
}
