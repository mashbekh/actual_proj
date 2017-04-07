package com.Tax;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.ErrorHandling.EntityException;
import com.Models.Company;
import com.Models.Tax;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class TaxDaoImpl {

	public Tax addTax(Tax tax, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		Key<Tax> key = null;
		Tax tx = null;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("taxName").equal(tax.getTaxName()).field("taxAbbreviation").equal(tax.getTaxAbbreviation()).field("taxRate").equal(tax.getTaxRate()).field("isDeleted").equal(false);
			tx = taxquery.get();
			if(tx != null)
				throw new EntityException(515,"tax found",null,null);

			tax.setCompany(cmp); 
			key = ds.save(tax);
			if(key == null)
				throw new EntityException(512,"couldnt create tax",null,null);

			tax.setId(new ObjectId(key.getId().toString()));

		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(513,"get failed",null,null);

			if(cmp != null && key == null)
				throw new EntityException(514,"create failed",null,null);

		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500,"invalid id", "invalid" + e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(oid!=null && cmp != null && key == null)
				throw new EntityException(500,"create failed","create" + e.getMessage(),null);
		}


		return tax;
	}


	public List<Tax> getTaxlist(String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;

		List<Tax> taxList = new ArrayList<>();

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("isDeleted").equal(false);
			taxList = taxquery.asList();


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(512, "get failed", null, null);

			if(cmp != null && taxList.isEmpty() == true)
				throw new EntityException(513, "get list failed", null, null);

		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);

			if(oid != null && cmp == null)
				throw new EntityException(500, "get failed", "get" + e.getMessage(), null);

			if(cmp != null && taxList.isEmpty() == true)
				throw new EntityException(500, "get list failed", "getlist" + e.getMessage(), null);

		}
		return taxList;

	}


	public Tax updateTax(Tax tax, String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		Key<Tax> key = null;
		Tax tx = null;
		Tax taxObj = null;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);


			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(tax.getId());
			tx = taxquery.get();
			if(tx == null)                    //can't update non existing tax
				throw new EntityException(512,"tax not found",null,null);
			
			//check if other tax with same details exists
			Query<Tax> txquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("taxName").equal(tax.getTaxName()).field("taxAbbreviation").equal(tax.getTaxAbbreviation())
					.field("taxRate").equal(tax.getTaxRate()).field("isDeleted").equal(false).field("id").notEqual(tax.getId());;
			taxObj = txquery.get();
			
			
			if(taxObj != null)
				throw new EntityException(515,"tax found",null,null);

			tx.setTaxName(tax.getTaxName());
			tx.setTaxType(tax.getTaxType());
			tx.setTaxAbbreviation(tax.getTaxAbbreviation());
			tx.setTaxDescription(tax.getTaxDescription());
			tx.setTaxRate(tax.getTaxRate());

			key = ds.merge(tx);
			if(key == null)
				throw new EntityException(513,"update failed",null,null);



		}
		catch(EntityException e)
		{ 
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(514, "get cmp failed", null, null);
			
			if(cmp != null && tx == null)
				throw new EntityException(514, "get tax failed", null, null);
			
			if(cmp != null && tx != null && taxObj == null)
				throw new EntityException(514, "get tax failed", null, null);
			
			if(cmp != null && tx != null && taxObj != null && key == null)
				throw new EntityException(513, "update failed", null, null);
			
		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);
			
			if(cmp == null)
				throw new EntityException(500, "get cmp failed", "get" + e.getMessage(), null);
			
			if(cmp != null && tx == null)
				throw new EntityException(500, "get tax failed", "get" + e.getMessage(), null);
			
			if(cmp != null && taxObj == null)
				throw new EntityException(500, "get tax failed", "get" + e.getMessage(), null);
			
			if(cmp != null && tx != null &&  taxObj != null && key == null)
				throw new EntityException(500, "update failed", "update" + e.getMessage(), null);
			

		}
		return tx;

	}


	public String deleteTax(String taxId, String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		ObjectId taxOid = null;
		int count = 0;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);
			
			taxOid = new ObjectId(taxId);
			
			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid);
			UpdateOperations<Tax> ops = ds.createUpdateOperations(Tax.class).set("isDeleted", true);
			UpdateResults result = ds.update(taxquery, ops, false);
			count = result.getUpdatedCount();
			if(count == 0)
				throw new EntityException(513,"update failed",null,null);

		}
		catch(EntityException e)
		{ 
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(514, "get cmp failed", null, null);
			
			if(cmp != null  && count == 0)
				throw new EntityException(513, "update failed", null, null);
		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);
			
			if(cmp == null)
				throw new EntityException(500, "get cmp failed", "get" + e.getMessage(), null);
			
			if(cmp != null && taxOid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);
			
			if(cmp != null && count == 0)
				throw new EntityException(500, "update failed", "update" + e.getMessage(), null);

		}
		return "Successfully deleted";

	}


}
