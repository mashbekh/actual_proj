package com.OutwardFlow;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.ErrorHandling.EntityException;
import com.Models.BusinessPlayers;
import com.Models.Company;
import com.Models.OutwardEntity;
import com.Models.POBillDetails;
import com.Models.Products;
import com.Models.Tax;
import com.setup.Morphiacxn;

public class BillDaoImpl {

	/*
	 * extract PO number 
	 * check if vendor has changed - see if he exists
	 * check if bill number with this vendor exists
	 * check product and tax of items exist
	 * set all bill properties
	 */

	public OutwardEntity createPoBill(OutwardEntity bill, String companyId, String newvendorId) throws EntityException
	{

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
		OutwardEntity purchaseOrder = null;
		OutwardEntity existingBill = null;
		boolean vendorChange = false;


		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			// check if PO exists

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).filter("isBill", false).field("poNumber").equal(bill.getPoNumber());
			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);

			//check if vendor changed, if yes then see if he exists

			if(!(purchaseOrder.getVendor().getId().toString().equals(newvendorId)))
			{
				vendorChange = true;
				vendorOid  = new ObjectId(newvendorId);
				Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(vendorOid).field("isDeleted").equal(false);
				vendor = bpquery.get();
				if(vendor == null)
					throw new EntityException(513, "vendor not found", null, null);
			}



			//check if bill with same number & vendor exists for company

			poquery = ds.createQuery(OutwardEntity.class).filter("company",cmp).filter("vendor", vendor).field("billNumber").equal(bill.getBillNumber());
			existingBill = poquery.get();
			if(existingBill != null)
				throw new EntityException(514, "bill number found", null, null);



			//collect new bill details -  can have deleted entries (2 queries per product)
			List<POBillDetails> billDetails = bill.getBillDetails();

			for(POBillDetails bdetail : billDetails)
			{

				if(bdetail.getTax().getId() != null && bdetail.getTax().isDeleted() == false)
				{

					taxOid = new ObjectId(bdetail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
				}

				//check  tax exists, if  deleted
				if(bdetail.getTax().getId() != null && bdetail.getTax().isDeleted() == true)
				{

					taxOid = new ObjectId(bdetail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
				}

				if(bdetail.getProduct().isDeleted() == false)  //product cant be null
				{

					prodOid = new ObjectId(bdetail.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
				}

				if(bdetail.getProduct().isDeleted() == true)
				{

					prodOid = new ObjectId(bdetail.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
				}


				//each pod needs Id
				bdetail.setId(new ObjectId());
				bdetail.setTax(tax);
				bdetail.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}


			purchaseOrder.setisBill(true);
			if(vendorChange == true)	
				purchaseOrder.setVendor(vendor);   //set new vendor
			purchaseOrder.setBillNumber(bill.getBillNumber());
			purchaseOrder.setBillDate(bill.getBillDate());
			purchaseOrder.setBillDueinterval(bill.getBillDueinterval());
			purchaseOrder.setBillDuedate(bill.getBillDuedate());
			purchaseOrder.setBillSubtotal(bill.getBillSubtotal());
			purchaseOrder.setBillTaxtotal(bill.getBillTaxtotal());
			purchaseOrder.setBillGrandtotal(bill.getBillGrandtotal());
			purchaseOrder.setisBilldeleted(false);
			purchaseOrder.setBillDetails(billDetails);

			java.math.BigDecimal poAdvance = purchaseOrder.getPoAdvancetotal();

			purchaseOrder.setBillAdvancetotal(poAdvance);  //copy PO advance amount
			purchaseOrder.setBillBalance(bill.getBillGrandtotal().subtract(poAdvance));

			key = ds.merge(purchaseOrder);
			if(key == null)
				throw new EntityException(515, "could not update", null, null);

		}
		catch(EntityException e)
		{

			throw e;
		}
		catch(Exception e)
		{

			throw new EntityException(500, null , e.getMessage(), null);
		}
		return purchaseOrder;

	}
	
	// without PO ref
	public OutwardEntity createBill(OutwardEntity bill, String companyId, String vendorId) throws EntityException
	{
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
		OutwardEntity existingBill = null;
	


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
				throw new EntityException(513, "vendor not found", null, null);
			
			//check if bill with same number & vendor exists for company

			Query<OutwardEntity> poquery = ds.createQuery(OutwardEntity.class).filter("company",cmp).filter("vendor", vendor).field("billNumber").equal(bill.getBillNumber());
			existingBill = poquery.get();
			if(existingBill != null)
				throw new EntityException(514, "bill number found", null, null);
			
			List<POBillDetails> billDetails = bill.getBillDetails();

			for(POBillDetails bdetail : billDetails)
			{

				if(bdetail.getTax().getId() != null)
				{
					taxOid = new ObjectId(bdetail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);
				}


				prodOid = new ObjectId(bdetail.getProduct().getId().toString());
				Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
				prod = prodquery.get();
				if(prod == null)
					throw new EntityException(514, "product not found", null, null);

				//each pod needs Id
				bdetail.setId(new ObjectId());
				bdetail.setTax(tax);
				bdetail.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}
			
			bill.setBillDetails(billDetails);
			bill.setCompany(cmp);
			bill.setVendor(vendor);
			key = ds.save(bill);
			if(key == null)
				throw new EntityException(515, "could not create", null, null);

			bill.setId(new ObjectId(key.getId().toString()));
			
			
			
		}
		catch(EntityException e)
		{

			throw e;
		}
		catch(Exception e)
		{

			throw new EntityException(500, null , e.getMessage(), null);
		}
		
		return bill;
		
	}
	
	

}
