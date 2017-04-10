package com.OutwardFlow;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import java.util.*;
import com.ErrorHandling.EntityException;
import com.Models.BusinessPlayers;
import com.Models.Company;
import com.Models.OutwardEntity;
import com.Models.POBillDetails;
import com.Models.Products;
import com.Models.Tax;
import com.setup.Morphiacxn;

public class PODaoImpl {
	
	public OutwardEntity createPO(OutwardEntity po, String companyId, String vendorId) throws EntityException
	{
		System.out.println("hiiii");
		Datastore ds = null;
		Company cmp = null;
		BusinessPlayers vendor = null;
		Products prod = null;
		Tax tax = null;
		Key<OutwardEntity> key = null;
		ObjectId oid = null;
		ObjectId vendorOid = null;
		ObjectId prodOid = null;
		ObjectId taxOid = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);
			
			
			vendorOid  = new ObjectId(vendorId);
			Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(vendorOid).field("isDeleted").equal(false);
			vendor = bpquery.get();
			if(vendor == null)
				throw new EntityException(512, "vendor not found", null, null);
			
			//check if po with same number exists within company
			
			
			
			List<POBillDetails> poDetails = po.getPoDetails();
					
			for(POBillDetails pod : poDetails)
			{
				System.out.println("yo");
				//search if product and tax exists
				taxOid = new ObjectId(pod.getTax().getId().toString());
				Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
				tax = taxquery.get();
				if(tax == null)
					throw new EntityException(513, "tax not found", null, null);
				
				
				
				prodOid = new ObjectId(pod.getProduct().getId().toString());
				Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
				prod = prodquery.get();
				if(prod == null)
					throw new EntityException(514, "product not found", null, null);
				
				pod.setTax(tax);
				pod.setProduct(prod);
				
				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;
				
			}
			
			po.setPoDetails(poDetails);
			
			System.out.println("pp");
			po.setCompany(cmp);
			po.setVendor(vendor);
			key = ds.save(po);
			if(key == null)
				throw new EntityException(515, "could not find", null, null);
			
			po.setId(new ObjectId(key.getId().toString()));
			
			
			
		}
		catch(EntityException e)
		{
			System.out.println("in");
			throw e;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage() + e.getClass());
			throw new EntityException(500, null , e.getMessage(), null);
		}
		return po;
	
	}
}
