package com.Products;

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
import com.Models.Products;
import com.Models.Tax;
import com.mongodb.MongoException;
import com.setup.Morphiacxn;

public class ProductsDaoImpl {


	public Products addProduct(Products prod, String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		Key<Products> key = null;
		Products product = null;
		Tax tx = null;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			//no product with same name
			Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("productName").equal(prod.getProductName()).field("isDeleted").equal(false);
			product = prodquery.get();
			if(product != null)
				throw new EntityException(515,"product found",null,null);

			
			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(prod.getProductTax().getId()).field("isDeleted").equal(false);
			tx = taxquery.get();
			if(tx == null)
				throw new EntityException(512,"tax not found",null,null);

			prod.setCompany(cmp);
			prod.setProductTax(tx);
			key = ds.save(prod);
			if(key == null)
				throw new EntityException(513,"create failed",null,null);

			prod.setId(new ObjectId(key.getId().toString()));

		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && product == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && product != null && tx == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && tx != null && key == null)
				throw new EntityException(513,"create failed",null,null);

		}
		catch(Throwable e)
		{

			if(oid == null)
				throw new EntityException(500,"invalid id", "invalid" + e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(cmp != null && product == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(oid!=null  && cmp != null &&  product != null && tx == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(oid!=null && cmp != null && tx != null &&  product != null  &&  key == null)
				throw new EntityException(500,"create failed","create" + e.getMessage(),null);
		}


		return prod;

	}

	public List<Products> getProducts(String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		List<Products> productList = new ArrayList<>();

		try
		{

			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			Query<Products> productquery = ds.createQuery(Products.class).field("company").equal(cmp).field("isDeleted").equal(false);
			productList = productquery.asList();


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(512, "get failed", null, null);

			if(cmp != null && productList.isEmpty() == true)
				throw new EntityException(513, "get list failed", null, null);

		}
		catch(Throwable e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);

			if(oid != null && cmp == null)
				throw new EntityException(500, "get failed", "get" + e.getMessage(), null);

			if(cmp != null && productList.isEmpty() == true)
				throw new EntityException(500, "get list failed", "getlist" + e.getMessage(), null);

		}
		return productList;

	}

	public Products updateProduct(Products prod, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		Key<Products> key = null;
		Products product = null;
		Tax tx = null;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			//no other product with same name
			Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("productName").equal(prod.getProductName()).field("id").notEqual(prod.getId());
			product = prodquery.get();
			if(product != null)
				throw new EntityException(515,"product found",null,null);


			Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(prod.getProductTax().getId()).field("isDeleted").equal(false);
			tx = taxquery.get();
			if(tx == null)
				throw new EntityException(512,"tax not found",null,null);

			prod.setCompany(cmp);
			prod.setProductTax(tx);
			key = ds.merge(prod);
			if(key == null)
				throw new EntityException(513,"update failed",null,null);

			prod.setId(new ObjectId(key.getId().toString()));

		}
		catch(EntityException e)
		{ 
			throw e;
		}
		catch(MongoException e)
		{
			if(cmp == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && product == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && product != null && tx == null)
				throw new EntityException(514,"get failed",null,null);

			if(cmp != null && product != null && tx != null && key == null)
				throw new EntityException(513,"merge failed",null,null);

		}
		catch(Throwable e)
		{

			if(oid == null)
				throw new EntityException(500,"invalid id", "invalid" + e.getMessage(),null);

			if(oid!=null && cmp == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(cmp != null && product == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(oid!=null  && cmp != null &&  product != null && tx == null)
				throw new EntityException(500,"get failed","get" + e.getMessage(),null);

			if(oid!=null && cmp != null && tx != null &&  product != null  &&  key == null)
				throw new EntityException(500,"update failed","create" + e.getMessage(),null);
		}
		return prod;

	}

	public String deleteProduct(String productId, String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		ObjectId prodOid  = null;
		int count = 0;

		try{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404,"cmp not found",null,null);

			prodOid = new ObjectId(productId);
			
			Query<Products> productquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid);
			UpdateOperations<Products> ops = ds.createUpdateOperations(Products.class).set("isDeleted", true);
			UpdateResults result = ds.update(productquery, ops, false);
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
		catch(Exception e)
		{
			if(oid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);
			
			if(cmp == null)
				throw new EntityException(500, "get cmp failed", "get" + e.getMessage(), null);
			
			if(prodOid == null)
				throw new EntityException(500, "invalid", "invalid" + e.getMessage(), null);
			
			if(count == 0)
				throw new EntityException(500, "update failed", "update" + e.getMessage(), null);
		}
		return "Successfully deleted";
	}
}