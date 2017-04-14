package com.OutwardFlow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.ErrorHandling.EntityException;
import com.Models.BusinessPlayers;
import com.Models.Company;
import com.Models.OutwardEntity;
import com.Models.POBillDetails;
import com.Models.POBillPayments;
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

	public OutwardEntity getBill(String companyId, String billId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		OutwardEntity bill = null;
		ObjectId oid = null;
		ObjectId billOid = null;

		try
		{
			System.out.println(billId);
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			//get bill from PO/ not -  bill has no unique number in db, so use internal Id
			billOid = new ObjectId(billId);
			Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true).
					filter("id", billOid);

			bill = billQuery.get();
			if(bill == null)
				throw new EntityException(512, "bill not found", null, null);



		}
		catch(EntityException e){
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return bill;
	}


	//make changes in adv payment list, update poadv total and biladvtotal and bill balance
	public POBillPayments updateAdvPayment(POBillPayments payment, String companyId , String billNo ) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity bill = null;
		ObjectId oid = null;
		ObjectId billOid = null;


		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			//po and bill must be true
			billOid = new ObjectId(billNo);
			Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true).filter("isPurchaseOrder", true)
					.filter("id", billOid);

			bill = billQuery.get();
			if(bill == null)
				throw new EntityException(512, "converted bill not found", null, null);


			//we need to sub old amt, and add new amt, so get old amt

			java.math.BigDecimal prevAmt = null;
			boolean match = false;
			
			//all deleted in between -  there are no adv payments now
			if(bill.getAdvancePayments() == null)
				throw new EntityException(513, "payment not found", null, null);

			for(POBillPayments pay : bill.getAdvancePayments())
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

			java.math.BigDecimal advanceTotal = bill.getPoAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
			java.math.BigDecimal billAdvance = bill.getBillAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());

			//as PO is converted, change only bill balance , PO balance does not get affected

			java.math.BigDecimal balance = bill.getBillBalance().add(prevAmt).subtract(payment.getPaymentAmount());


			//if null then 0 

			if(payment.gettdsRate() == null)
				payment.settdsRate(new BigDecimal(0));

			if(payment.gettdsAmount() == null)
				payment.settdsAmount(new BigDecimal(0));

			//search for nested payment in the list
			billQuery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", billOid).filter("advancePayments.id", new ObjectId(payment.getId().toString()));

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
					.set("billAdvancetotal", billAdvance)
					.set("billBalance", balance);


			UpdateResults result = ds.update(billQuery, ops, false);

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


	/*make changes in bill payment list,  biladvtotal
	 * applies to both kind of bills
	 */
	public POBillPayments updateBillPayment(POBillPayments payment, String billNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity bill = null;
		ObjectId oid = null;
		ObjectId billOid = null;


		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);


			billOid = new ObjectId(billNo);
			Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true)
					.filter("id", billOid);

			bill = billQuery.get();
			if(bill == null)
				throw new EntityException(512, " bill not found", null, null);


			//we need to sub old amt, and add new amt, so get old amt

			java.math.BigDecimal prevAmt = null;
			boolean match = false;

			//all deleted in between
			if(bill.getBillPayments() == null)
				throw new EntityException(513, "payment not found", null, null);
				
				//always fetch payment prev amt from db, it may have changed in between	
			for(POBillPayments pay : bill.getBillPayments())
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


			//payment towards a bill, PO not affected in any way
			java.math.BigDecimal billAdvance = bill.getBillAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
			java.math.BigDecimal balance = bill.getBillBalance().add(prevAmt).subtract(payment.getPaymentAmount());  // or grand total  -  just set advance


			//if null then 0 

			if(payment.gettdsRate() == null)
				payment.settdsRate(new BigDecimal(0));

			if(payment.gettdsAmount() == null)
				payment.settdsAmount(new BigDecimal(0));

			//search for nested payment in the list
			billQuery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", billOid).filter("billPayments.id", new ObjectId(payment.getId().toString()));

			UpdateOperations<OutwardEntity> ops =  ds.createUpdateOperations(OutwardEntity.class)

					.set("billPayments.$.paymentDate", payment.getPaymentDate())
					.set("billPayments.$.paymentAmount", payment.getPaymentAmount())
					.set("billPayments.$.purposeTitle", payment.getPurposeTitle())
					.set("billPayments.$.purposeDescription", payment.getPurposeDescription())
					.set("billPayments.$.paymentMode", payment.getPaymentMode())
					.set("billPayments.$.paymentAccount", payment.getPaymentAccount())
					.set("billPayments.$.tdsRate", payment.gettdsRate())
					.set("billPayments.$.tdsAmount", payment.gettdsAmount())
					.set("billPayments.$.isTdsPaid", payment.isTdsPaid())
					.set("billAdvancetotal", billAdvance)
					.set("billBalance", balance);


			UpdateResults result = ds.update(billQuery, ops, false);

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

	// deleted adv payment from PO, this affects poadvance , bil advance and bil balance
	public String removeAdvPayment(String paymentId, String billNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		OutwardEntity bill = null;
		ObjectId oid = null;
		ObjectId billOid = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			billOid = new ObjectId(billNo);
			Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true).filter("isPurchaseOrder", true)
					.filter("id", billOid);

			bill = billQuery.get();
			if(bill == null)
				throw new EntityException(512, "converted bill not found", null, null);

			//we need to sub old amt, and add new amt, so get old amt


			boolean match = false;
			POBillPayments pymnt = null;
			
			//all deleted in between - make sure there are adv payments to compare and update
			if(bill.getAdvancePayments() == null)
				throw new EntityException(513, "payment not found", null, null);

			for(POBillPayments pay : bill.getAdvancePayments())
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



			java.math.BigDecimal poAdvanceTotal = bill.getPoAdvancetotal().subtract(pymnt.getPaymentAmount());
			java.math.BigDecimal billAdvanceTotal = bill.getBillAdvancetotal().subtract(pymnt.getPaymentAmount());
			java.math.BigDecimal billBalance = bill.getBillBalance().add(pymnt.getPaymentAmount());



			//get Bill, remove particular element of list after fetching it
			billQuery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", billOid);
			UpdateOperations<OutwardEntity> ops =  ds.createUpdateOperations(OutwardEntity.class)
					.removeAll("advancePayments", pymnt)
					.set("poAdvancetotal",poAdvanceTotal)
					.set("billAdvancetotal", billAdvanceTotal)
					.set("billBalance", billBalance);


			UpdateResults result = ds.update(billQuery, ops, false);

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

	
	  // both kinds of bill - deleted bill payment , this affects bil advance and bil balance, not PO
		public String removeBillPayment(String paymentId, String billNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			OutwardEntity bill = null;
			ObjectId oid = null;
			ObjectId billOid = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				billOid = new ObjectId(billNo);
				Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true)
						.filter("id", billOid);

				bill = billQuery.get();
				if(bill == null)
					throw new EntityException(512, "bill not found", null, null);

				//we need to sub old amt, and add new amt, so get old amt


				boolean match = false;
				POBillPayments pymnt = null;
				
				//all deleted in between - make sure there are adv payments to compare and update
				if(bill.getBillPayments() == null)
					throw new EntityException(513, "payment not found", null, null);

				for(POBillPayments pay : bill.getBillPayments())
				{
					//fetch that payment
					if(pay.getId().toString().equals(paymentId))
					{
						match = true;
						pymnt = pay;
						break;
					}

				}

				if(match == false)
					throw new EntityException(513, "payment not found", null, null);  //cant delete


				java.math.BigDecimal billAdvanceTotal = bill.getBillAdvancetotal().subtract(pymnt.getPaymentAmount());
				java.math.BigDecimal billBalance = bill.getBillBalance().add(pymnt.getPaymentAmount());



				//get Bill, remove particular element of list after fetching it
				billQuery = ds.createQuery(OutwardEntity.class).disableValidation().filter("id", billOid);
				UpdateOperations<OutwardEntity> ops =  ds.createUpdateOperations(OutwardEntity.class)
						.removeAll("billPayments", pymnt)
						.set("billAdvancetotal", billAdvanceTotal)
						.set("billBalance", billBalance);


				UpdateResults result = ds.update(billQuery, ops, false);

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

		//exclusively payment for bill only -  both kinds of bill
		public POBillPayments addBillpayment(POBillPayments payment, String billNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			OutwardEntity bill = null;
			ObjectId oid = null;
			Key<OutwardEntity> key = null;
			ObjectId billOid = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				billOid = new ObjectId(billNo);
				Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true)
						.filter("id", billOid);

				bill = billQuery.get();
				if(bill == null)
					throw new EntityException(512, "bill not found", null, null);


				//if tds is null, initialise to 0

				if(payment.gettdsRate() == null)
					payment.settdsRate(new BigDecimal(0));

				if(payment.gettdsAmount() == null)
					payment.settdsAmount(new BigDecimal(0));

				payment.setId(new ObjectId());

				List<POBillPayments> list ;
				
				//may be adding payment for first time, initialise list

				if(bill.getBillPayments() == null)
				{

					list = new ArrayList<>();
					list.add(payment);
				}
				else
				{

					list = bill.getBillPayments();
					list.add(payment);
				}



				bill.setBillPayments(list);
				bill.setBillAdvancetotal(bill.getBillAdvancetotal().add(payment.getPaymentAmount()));
				bill.setBillBalance(bill.getBillBalance().subtract(payment.getPaymentAmount()));

				key = ds.merge(bill);

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


		//get id from bill, make sure vendor with same number should not exist
		public OutwardEntity updateBill(OutwardEntity entity , String companyId, String vendorId) throws EntityException
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
			OutwardEntity bill = null;
			boolean vendorChange = false;
			boolean billNochange  =  false;
			
			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);
				
				System.out.println("cmp");

				//check if bill exists with object id for update , fetch bill, make changes to it
			
				Query<OutwardEntity> billQuery = ds.find(OutwardEntity.class).filter("company",cmp).filter("isBill", true).
						filter("id",entity.getId());

				bill = billQuery.get();
				if(bill == null)
					throw new EntityException(512, "bill not found", null, null);  //cant update


				System.out.println("bill found");
				
				/*check if vendor changed in update, if so then check if new vendor exists
				 * if vendor hasnt changed bt was deleted - let it be
				 */

				if(!(vendorId.equals(bill.getVendor().getId().toString())))
				{
					vendorChange = true;
					vendorOid  = new ObjectId(vendorId);
					Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(vendorOid).field("isDeleted").equal(false);
					vendor = bpquery.get();
					if(vendor == null)
						throw new EntityException(512, "vendor not found", null, null);


				}
				
				System.out.println("vendor");
				
				/*if bill number is editable, do check if there is new /old vendor + new/old bill number combo unique
				 * if vendor hasnt changed and bill number hasnt chngd -  no prob
				 * if vendor/bill number has chngd do check
				 */
				
				if(!(entity.getBillNumber().equals(bill.getBillNumber())))
					billNochange = true;
				
				System.out.println("bill no");
				
				if(vendorChange == true || billNochange == true)
				{
					/*add query later
					 * if vendor not changed, bill numb has -> chk with bill.vendor and this number
					 * if vendor has changed, bill numb hasnt then -> chk vendor and entity.billno
					 * if both chngd , then vendor and entity.billno
					 */
				}
					
				// front end list cant be null
				List<POBillDetails> billDetails = entity.getBillDetails();

				for(POBillDetails bdetails : billDetails)
				{

					//check  tax exists, if not deleted
					if(bdetails.getTax().getId() != null && bdetails.getTax().isDeleted() == false)
					{

						taxOid = new ObjectId(bdetails.getTax().getId().toString());
						Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
						tax = taxquery.get();
						if(tax == null)
							throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
					}

					//check  tax exists, if  deleted
					if(bdetails.getTax().getId() != null && bdetails.getTax().isDeleted() == true)
					{

						taxOid = new ObjectId(bdetails.getTax().getId().toString());
						Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
						tax = taxquery.get();
						if(tax == null)
							throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
					}

					if(bdetails.getProduct().isDeleted() == false)
					{

						prodOid = new ObjectId(bdetails.getProduct().getId().toString());
						Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
						prod = prodquery.get();
						if(prod == null)
							throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
					}

					if(bdetails.getProduct().isDeleted() == true)
					{

						prodOid = new ObjectId(bdetails.getProduct().getId().toString());
						Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
						prod = prodquery.get();
						if(prod == null)
							throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
					}


					bdetails.setId(new ObjectId());
					bdetails.setTax(tax);
					bdetails.setProduct(prod);

					tax = null;
					taxOid = null;
					prod = null;
					prodOid = null;

				}

				bill.setBillDetails(billDetails);
				bill.setCompany(cmp);
				if(vendorChange ==  true)
					bill.setVendor(vendor);  //set only if vendor has changed
				
				bill.setBillNumber(entity.getBillNumber());
				bill.setBillDate(entity.getBillDate());
				bill.setBillDueinterval(entity.getBillDueinterval());
				bill.setBillDuedate(entity.getBillDuedate());
				
				bill.setBillGrandtotal(entity.getBillGrandtotal());
				bill.setBillSubtotal(entity.getBillSubtotal());
				bill.setBillTaxtotal(entity.getBillTaxtotal());
				java.math.BigDecimal balance = bill.getBillGrandtotal().subtract(bill.getBillAdvancetotal()); //grand total is just set, read prev advance
				bill.setBillBalance(balance);

				key = ds.merge(bill);
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

			return bill;
		}

}
