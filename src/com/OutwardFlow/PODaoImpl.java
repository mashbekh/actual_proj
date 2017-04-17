package com.OutwardFlow;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.math.BigDecimal;
import java.util.*;
import com.ErrorHandling.EntityException;
import com.Models.BusinessPlayers;
import com.Models.Company;
import com.Models.OutwardEntity;
import com.Models.POBillDetails;
import com.Models.POBillPayments;
import com.Models.Products;
import com.Models.Tax;
import com.setup.Morphiacxn;

public class PODaoImpl {

	public OutwardEntity createPO(OutwardEntity po, String companyId, String vendorId) throws EntityException
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

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);


			vendorOid  = new ObjectId(vendorId);
			Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(vendorOid).field("isVendor").equal(true).field("isDeleted").equal(false);
			vendor = bpquery.get();
			if(vendor == null)
				throw new EntityException(512, "vendor not found", null, null);

			//check if po with same serialNo  - converted/ not  exists within company - overall -  irrespective of prefix and suffix

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).field("poSerialId").equal(po.getPoSerialId());
			purchaseOrder = poquery.get();
			if(purchaseOrder != null)
				throw new EntityException(516, "po found", null, null);


			List<POBillDetails> poDetails = po.getPoDetails();

			for(POBillDetails pod : poDetails)
			{

				//search if product and tax exists
				if(pod.getTax().getId() != null)
				{
					taxOid = new ObjectId(pod.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);
				}


				prodOid = new ObjectId(pod.getProduct().getId().toString());
				Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
				prod = prodquery.get();
				if(prod == null)
					throw new EntityException(514, "product not found", null, null);

				//each pod needs Id
				pod.setId(new ObjectId());
				pod.setTax(tax);
				pod.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}

			po.setPoDetails(poDetails);
			po.setCompany(cmp);
			po.setVendor(vendor);
			key = ds.save(po);
			if(key == null)
				throw new EntityException(515, "could not create", null, null);

			po.setId(new ObjectId(key.getId().toString()));



		}
		catch(EntityException e)
		{

			throw e;
		}
		catch(Exception e)
		{

			throw new EntityException(500, null , e.getMessage(), null);
		}
		return po;

	}

	//amomg PO - converted/ not converted get the largest+1
	public Long getPoNumber(String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		OutwardEntity purchaseOrder = null;
		ObjectId oid = null;
		Long number = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).order("-poSerialId");
			purchaseOrder = poquery.get();

			if(purchaseOrder == null)
				number = Long.valueOf(1);
			else
				number = purchaseOrder.getPoSerialId() + 1;
		}
		catch(EntityException e){
			throw e;
		}
		catch(Exception e )
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}
		System.out.println(number);
		return number;

	}

	//maybe converted/not 
	public OutwardEntity getPurchaseOrder(String companyId, String PONumber) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		OutwardEntity purchaseOrder = null;
		ObjectId oid = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);


			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false).filter("poNumber", PONumber);

			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);



		}
		catch(EntityException e){
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return purchaseOrder;
	}

	//exclusively payment for PO only
	public POBillPayments addPayment(POBillPayments payment, String poNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity purchaseOrder = null;
		ObjectId oid = null;
		Key<OutwardEntity> key = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false).filter("isBill", false).filter("poNumber", poNo);

			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);

			//if tds is null, initialise to 0

			if(payment.gettdsRate() == null)
				payment.settdsRate(new BigDecimal(0));

			if(payment.gettdsAmount() == null)
				payment.settdsAmount(new BigDecimal(0));

			payment.setId(new ObjectId());

			List<POBillPayments> list ;
			
			//may be adding payment for first time, initialise list

			if(purchaseOrder.getAdvancePayments() == null)
			{

				list = new ArrayList<>();
				list.add(payment);
			}
			else
			{

				list = purchaseOrder.getAdvancePayments();
				list.add(payment);
			}



			purchaseOrder.setAdvancePayments(list);
			purchaseOrder.setPoAdvancetotal(purchaseOrder.getPoAdvancetotal().add(payment.getPaymentAmount()));
			purchaseOrder.setPoBalance(purchaseOrder.getPoBalance().subtract(payment.getPaymentAmount()));

			key = ds.merge(purchaseOrder);

			if(key == null)
				throw new EntityException(513, "update failed", null, null);


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return payment;
	}


	public POBillPayments updatePayment(POBillPayments payment, String poNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity purchaseOrder = null;
		ObjectId oid = null;


		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false).filter("isBill", false).filter("poNumber", poNo);

			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);

			//we need to sub old amt, and add new amt, so get old amt

			java.math.BigDecimal prevAmt = null;
			boolean match = false;
			
			//all deleted in between
			if(purchaseOrder.getAdvancePayments() == null)
				throw new EntityException(513, "payment not found", null, null);

			for(POBillPayments pay : purchaseOrder.getAdvancePayments())
			{

				if(pay.getId().toString().equals(payment.getId().toString()))
				{
					match = true;
					prevAmt = pay.getPaymentAmount();
					break;
				}

			}

			if(match == false)
				throw new EntityException(513, "payment not found", null, null);

			java.math.BigDecimal advanceTotal = purchaseOrder.getPoAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
			java.math.BigDecimal balance = purchaseOrder.getPoBalance().add(prevAmt).subtract(payment.getPaymentAmount());

			//if null then 0 

			if(payment.gettdsRate() == null)
				payment.settdsRate(new BigDecimal(0));

			if(payment.gettdsAmount() == null)
				payment.settdsAmount(new BigDecimal(0));

			poquery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", purchaseOrder.getId()).filter("advancePayments.id", new ObjectId(payment.getId().toString()));

			UpdateOperations<OutwardEntity> ops =  ds.createUpdateOperations(OutwardEntity.class)

					.set("advancePayments.$.paymentDate", payment.getPaymentDate())
					.set("advancePayments.$.paymentAmount", payment.getPaymentAmount())
					.set("advancePayments.$.purposeTitle", payment.getPurposeTitle())
					.set("advancePayments.$.purposeDescription", payment.getPurposeDescription())
					.set("advancePayments.$.paymentMode", payment.getPaymentMode())
					.set("advancePayments.$.paymentAccount", payment.getPaymentAccount())
					.set("advancePayments.$.tdsRate", payment.gettdsRate())
					.set("advancePayments.$.tdsAmount", payment.gettdsAmount())
					.set("advancePayments.$.isTdsPaid", payment.isTdsPaid())
					.set("poAdvancetotal",advanceTotal)
					.set("poBalance", balance);


			UpdateResults result = ds.update(poquery, ops, false);

			if(result.getUpdatedCount() == 0)
				throw new EntityException(512, "update failed", null, null);



		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}
		return payment;
	}


	public String removePayment(String paymentId, String poNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity purchaseOrder = null;
		ObjectId oid = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false).filter("isBill", false).filter("poNumber", poNo);

			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);

			//we need to sub old amt, and add new amt, so get old amt


			boolean match = false;
			POBillPayments pymnt = null;
			
			//all deleted in between
			if(purchaseOrder.getAdvancePayments() == null)
				throw new EntityException(513, "payment not found", null, null);

			for(POBillPayments pay : purchaseOrder.getAdvancePayments())
			{

				if(pay.getId().toString().equals(paymentId))
				{
					match = true;
					pymnt = pay;
					break;
				}

			}

			if(match == false)
				throw new EntityException(513, "payment not found", null, null);  //cant delete



			java.math.BigDecimal advanceTotal = purchaseOrder.getPoAdvancetotal().subtract(pymnt.getPaymentAmount());
			java.math.BigDecimal balance = purchaseOrder.getPoBalance().add(pymnt.getPaymentAmount());



			//remove particular element of list
			poquery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", purchaseOrder.getId());
			UpdateOperations<OutwardEntity> ops =  ds.createUpdateOperations(OutwardEntity.class)
					.removeAll("advancePayments", pymnt)
					.set("poAdvancetotal",advanceTotal)
					.set("poBalance", balance);


			UpdateResults result = ds.update(poquery, ops, false);

			if(result.getUpdatedCount() == 0)
				throw new EntityException(512, "update failed", null, null);


		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}
		return "success";
	}



	/* 
	 * inspect each prod/tax of item and if deleted, query if product is actually deleted and has this id
	 * 
	 * else query for product not deleted and with this id
	 */
	public OutwardEntity updatePO( OutwardEntity po, String companyId, String vendorId) throws EntityException
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
		boolean vendorChange = false;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			//check if PO exists for update only when not converted to bill

			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false).filter("isBill", false).filter("poNumber", po.getPoNumber());

			purchaseOrder = poquery.get();
			if(purchaseOrder == null)
				throw new EntityException(512, "po not found", null, null);  //cant update


			/*check if vendor changed in update, if so then check if new vendor exists
			 * if vendor hasnt changed bt was deleted - let it be
			 */

			if(!(vendorId.equals(purchaseOrder.getVendor().getId().toString())))
			{
				vendorChange = true;
				vendorOid  = new ObjectId(vendorId);
				Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(vendorOid).field("isVendor").equal(true).field("isDeleted").equal(false);
				vendor = bpquery.get();
				if(vendor == null)
					throw new EntityException(512, "vendor not found", null, null);


			}


			// front end list cant be null
			List<POBillDetails> poDetails = po.getPoDetails();

			for(POBillDetails pod : poDetails)
			{

				//check  tax exists, if not deleted
				if(pod.getTax().getId() != null && pod.getTax().isDeleted() == false)
				{

					taxOid = new ObjectId(pod.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
				}

				//check  tax exists, if  deleted
				if(pod.getTax().getId() != null && pod.getTax().isDeleted() == true)
				{

					taxOid = new ObjectId(pod.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
				}

				if(pod.getProduct().isDeleted() == false)
				{

					prodOid = new ObjectId(pod.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
				}

				if(pod.getProduct().isDeleted() == true)
				{

					prodOid = new ObjectId(pod.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
				}


				pod.setId(new ObjectId());
				pod.setTax(tax);
				pod.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}

			purchaseOrder.setPoNotes(po.getPoNotes());
			purchaseOrder.setPoDetails(poDetails);
			purchaseOrder.setCompany(cmp);
			if(vendorChange ==  true)
				purchaseOrder.setVendor(vendor);  //set only if vendor has changed
			
			purchaseOrder.setPoGrandtotal(po.getPoGrandtotal());
			purchaseOrder.setPoSubtotal(po.getPoSubtotal());
			purchaseOrder.setPoTaxtotal(po.getPoTaxtotal());
			java.math.BigDecimal balance = purchaseOrder.getPoGrandtotal().subtract(purchaseOrder.getPoAdvancetotal()); //grand total is just set, read prev advance
			purchaseOrder.setPoBalance(balance);

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
			System.out.println(e.getMessage() + e.getClass());
			throw new EntityException(500, null , e.getMessage(), null);
		}

		return purchaseOrder;
	}


	// send all PO, front end if also bill then hold hyperlink
	public List<OutwardEntity> getPolist( String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		ObjectId oid = null;
		List<OutwardEntity> poList = new ArrayList<>();

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);


			Query<OutwardEntity> poquery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isPurchaseOrder", true).
					filter("isPodeleted", false);

			poList = poquery.asList();
			if(poList.isEmpty() == true)
				throw new EntityException(512, "could not retreive", null, null);

		}
		catch(EntityException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return poList;
	}


}




