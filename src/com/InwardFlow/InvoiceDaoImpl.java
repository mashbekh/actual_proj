package com.InwardFlow;

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
import com.Models.InwardEntity;
import com.Models.OrderDetails;
import com.Models.OrderPayments;

import com.Models.Products;
import com.Models.Tax;
import com.setup.Morphiacxn;

public class InvoiceDaoImpl {

	//among invoices - converted/ not converted get the largest+1
	public Long getInvoiceNumber(String companyId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		InwardEntity invoice = null;
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

			Query<InwardEntity> invoiceQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true).order("-invoiceSerialId");
			invoice = invoiceQuery .get();

			if(invoice == null)
				number = Long.valueOf(1);
			else
				number = invoice.getInvoiceSerialId() + 1;
		}
		catch(EntityException e){
			throw e;
		}
		catch(Exception e )
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return number;

	}

	//fetch relevant estimate and make changes to it
	public InwardEntity createEstimateInvoice(InwardEntity invoice, String companyId, String newcustomerId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		BusinessPlayers customer = null;
		Products prod = null;
		Tax tax = null;
		Key<InwardEntity> key = null;
		ObjectId oid = null;
		ObjectId customerOid = null;
		ObjectId prodOid = null;
		ObjectId taxOid = null;
		InwardEntity estimate = null;
		InwardEntity existingInvoice = null;
		boolean customerChange = false;


		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			// check if estimate exists

			Query<InwardEntity> equery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).filter("isInvoice", false).field("estimateNumber").equal(invoice.getEstimateNumber());
			estimate = equery.get();
			if(estimate == null)
				throw new EntityException(512, "estimate not found", null, null);

			//check if customer changed, if yes then see if he exists

			if(!(estimate.getCustomer().getId().toString().equals(newcustomerId)))
			{
				customerChange = true;
				customerOid  = new ObjectId(newcustomerId);
				Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(customerOid).field("isCustomer").equal(true).field("isDeleted").equal(false);
				customer = bpquery.get();
				if(customer == null)
					throw new EntityException(513, "customer not found", null, null);
			}



			//check if invoice with this serial number exists

			equery = ds.createQuery(InwardEntity.class).filter("company",cmp).field("isInvoice").equal(true).field("invoiceSerialId").equal(invoice.getInvoiceSerialId());
			existingInvoice = equery.get();
			if(existingInvoice != null)
				throw new EntityException(514, "invoice number found", null, null);



			//collect new invoice details -  can have deleted entries (2 queries per product)
			List<OrderDetails> invoiceDetails = invoice.getInvoiceDetails();

			for(OrderDetails detail : invoiceDetails)
			{

				if(detail.getTax().getId() != null && detail.getTax().isDeleted() == false)
				{

					taxOid = new ObjectId(detail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
				}

				//check  tax exists, if  deleted
				if(detail.getTax().getId() != null && detail.getTax().isDeleted() == true)
				{

					taxOid = new ObjectId(detail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
				}

				if(detail.getProduct().isDeleted() == false)  //product cant be null
				{

					prodOid = new ObjectId(detail.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
				}

				if(detail.getProduct().isDeleted() == true)
				{

					prodOid = new ObjectId(detail.getProduct().getId().toString());
					Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
					prod = prodquery.get();
					if(prod == null)
						throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
				}


				//each pod needs Id
				detail.setId(new ObjectId());
				detail.setTax(tax);
				detail.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}


			estimate.setisInvoice(true);
			if(customerChange == true)	
				estimate.setCustomer(customer);
			estimate.setInvoiceNumber(invoice.getInvoiceNumber());
			estimate.setInvoiceDate(invoice.getInvoiceDate());
			estimate.setInvoiceDueinterval(invoice.getInvoiceDueinterval());
			estimate.setInvoiceDuedate(invoice.getInvoiceDuedate());
			estimate.setInvoiceSubtotal(invoice.getInvoiceSubtotal());
			estimate.setInvoiceTaxtotal(invoice.getInvoiceTaxtotal());
			estimate.setInvoiceGrandtotal(invoice.getInvoiceGrandtotal());
			estimate.setisInvoicedeleted(false);
			estimate.setInvoiceDetails(invoiceDetails);
			estimate.setInvoiceSerialId(invoice.getInvoiceSerialId());
			estimate.setInvoiceNotes(invoice.getInvoiceNotes());


			java.math.BigDecimal invoiceAdvance = estimate.getEstimateAdvancetotal();

			estimate.setInvoiceAdvancetotal(invoiceAdvance);

			estimate.setInvoiceBalance(invoice.getInvoiceGrandtotal().subtract(invoiceAdvance));


			key = ds.merge(estimate);
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
		return estimate;

	}

	// without PO ref
	public InwardEntity createInvoice(InwardEntity invoice, String companyId, String customerId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		BusinessPlayers customer = null;
		Products prod = null;
		Tax tax = null;
		Key<InwardEntity> key = null;
		ObjectId oid = null;
		ObjectId customerOid = null;
		ObjectId prodOid = null;
		ObjectId taxOid = null;
		InwardEntity existingInvoice = null;
	



		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);



			customerOid  = new ObjectId(customerId);
			Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(customerOid).field("isCustomer").equal(true).field("isDeleted").equal(false);
			customer = bpquery.get();
			if(customer == null)
				throw new EntityException(513, "customer not found", null, null);

			//check if invoice serial number exist

			Query<InwardEntity> equery = ds.createQuery(InwardEntity.class).filter("company",cmp).field("isInvoice").equal(true).field("invoiceSerialId").equal(invoice.getInvoiceSerialId());
			existingInvoice = equery.get();
			if(existingInvoice != null)
				throw new EntityException(514, "invoice number found", null, null);

			List<OrderDetails> invoiceDetails = invoice.getInvoiceDetails();

			for(OrderDetails detail : invoiceDetails)
			{

				if(detail.getTax().getId() != null)
				{
					taxOid = new ObjectId(detail.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);
				}


				prodOid = new ObjectId(detail.getProduct().getId().toString());
				Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
				prod = prodquery.get();
				if(prod == null)
					throw new EntityException(514, "product not found", null, null);

				//each pod needs Id
				detail.setId(new ObjectId());
				detail.setTax(tax);
				detail.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}

			invoice.setInvoiceDetails(invoiceDetails);
			invoice.setCompany(cmp);
			invoice.setCustomer(customer);
			key = ds.save(invoice);
			if(key == null)
				throw new EntityException(515, "could not create", null, null);

			invoice.setId(new ObjectId(key.getId().toString()));

			System.out.println(invoiceDetails.size());

		}
		catch(EntityException e)
		{

			throw e;
		}
		catch(Exception e)
		{

			throw new EntityException(500, null , e.getMessage(), null);
		}

		return invoice;

	}
	
	public InwardEntity getInvoice(String companyId, String invoiceId) throws EntityException
	{
		Datastore ds = null;
		Company cmp = null;
		InwardEntity invoice = null;
		ObjectId oid = null;
		ObjectId invoiceOid = null;

		try
		{
			
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			//get bill from PO/ not -  bill has no unique number in db, so use internal Id
			invoiceOid = new ObjectId(invoiceId);
			Query<InwardEntity> invoiceQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true).
					filter("id", invoiceOid);

			invoice = invoiceQuery.get();
			if(invoice == null)
				throw new EntityException(512, "invoice not found", null, null);



		}
		catch(EntityException e){
			throw e;
		}
		catch(Exception e)
		{
			throw new EntityException(500, null, e.getMessage(), null);
		}

		return invoice;
	}
	
	public OrderPayments addInvoicePayment(OrderPayments payment, String invoiceNo, String companyId) throws EntityException
	{

		Datastore ds = null;
		Company cmp = null;
		InwardEntity invoice = null;
		ObjectId oid = null;
		Key<InwardEntity> key = null;
		ObjectId invoiceOid = null;

		try
		{
			ds = Morphiacxn.getInstance().getMORPHIADB("test");
			oid  = new ObjectId(companyId);
			Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
			cmp = query.get();
			if(cmp == null)
				throw new EntityException(404, "cmp not found", null, null);

			invoiceOid = new ObjectId(invoiceNo);
			Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true)
					.filter("id", invoiceOid);

			invoice = iQuery.get();
			if(invoice == null)
				throw new EntityException(512, "invoice not found", null, null);


			//if tds is null, initialise to 0

			if(payment.getTdsRate() == null)
				payment.setTdsRate(new BigDecimal(0));

			if(payment.getTdsAmount() == null)
				payment.setTdsAmount(new BigDecimal(0));

			payment.setId(new ObjectId());

			List<OrderPayments> list ;
			
			//may be adding payment for first time, initialise list

			if(invoice.getInvoicePayments() == null)
			{

				list = new ArrayList<>();
				list.add(payment);
			}
			else
			{

				list = invoice.getInvoicePayments();
				list.add(payment);
			}



			invoice.setInvoicePayments(list);
			invoice.setInvoiceAdvancetotal(invoice.getInvoiceAdvancetotal().add(payment.getPaymentAmount()));
			invoice.setInvoiceBalance(invoice.getInvoiceBalance().subtract(payment.getPaymentAmount()));

			key = ds.merge(invoice);

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

	//make changes in estimate payment list, update poadv total and biladvtotal and bill balance
		public OrderPayments updateAdvPayment(OrderPayments payment, String companyId , String invoiceNo ) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity invoice = null;
			ObjectId oid = null;
			ObjectId invoiceOid = null;


			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				//po and bill must be true
				invoiceOid = new ObjectId(invoiceNo);
				Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).filter("isInvoice", true)
						.filter("id", invoiceOid);

				invoice = iQuery.get();
				if(invoice == null)
					throw new EntityException(512, "converted invoice not found", null, null);


				//we need to sub old amt, and add new amt, so get old amt

				java.math.BigDecimal prevAmt = null;
				boolean match = false;
				
				//all deleted in between -  there are no adv payments now
				if(invoice.getEstimatePayments() == null)
					throw new EntityException(513, "payment not found", null, null);

				for(OrderPayments pay : invoice.getEstimatePayments())
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

				java.math.BigDecimal advanceTotal = invoice.getEstimateAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
				java.math.BigDecimal invoiceAdvance =invoice.getInvoiceAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());

				//as PO is converted, change only bill balance , PO balance does not get affected

				java.math.BigDecimal balance = invoice.getInvoiceBalance().add(prevAmt).subtract(payment.getPaymentAmount());


				//if null then 0 

				if(payment.getTdsRate() == null)
					payment.setTdsRate(new BigDecimal(0));

				if(payment.getTdsAmount() == null)
					payment.setTdsAmount(new BigDecimal(0));

				//search for nested payment in the list
				iQuery = ds.createQuery(InwardEntity.class).disableValidation().filter("id", invoiceOid).filter("estimatePayments.id", new ObjectId(payment.getId().toString()));

				UpdateOperations<InwardEntity> ops =  ds.createUpdateOperations(InwardEntity.class)

						.set("estimatePayments.$.paymentDate", payment.getPaymentDate())
						.set("estimatePayments.$.paymentAmount", payment.getPaymentAmount())
						.set("estimatePayments.$.purposeTitle", payment.getPurposeTitle())
						.set("estimatePayments.$.purposeDescription", payment.getPurposeDescription())
						.set("estimatePayments.$.paymentMode", payment.getPaymentMode())
						.set("estimatePayments.$.paymentAccount", payment.getPaymentAccount())
						.set("estimatePayments.$.tdsRate", payment.getTdsRate())
						.set("estimatePayments.$.tdsAmount", payment.getTdsAmount())
						.set("estimatePayments.$.isTdsReceived", payment.isTdsReceived())
						.set("estimateAdvancetotal",advanceTotal)
						.set("invoiceAdvancetotal", invoiceAdvance)
						.set("invoiceBalance", balance);


				UpdateResults result = ds.update(iQuery, ops, false);

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
		public OrderPayments updateInvoicePayment(OrderPayments payment, String invoiceNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity invoice = null;
			ObjectId oid = null;
			ObjectId invoiceOid = null;


			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);


				invoiceOid = new ObjectId(invoiceNo);
				Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true)
						.filter("id", invoiceOid);

				invoice = iQuery.get();
				if(invoice == null)
					throw new EntityException(512, " invoice not found", null, null);


				//we need to sub old amt, and add new amt, so get old amt

				java.math.BigDecimal prevAmt = null;
				boolean match = false;

				//all deleted in between
				if(invoice.getInvoicePayments() == null)
					throw new EntityException(513, "payment not found", null, null);
					
					//always fetch payment prev amt from db, it may have changed in between	
				for(OrderPayments pay : invoice.getInvoicePayments())
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
				java.math.BigDecimal invoiceAdvance = invoice.getInvoiceAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
				java.math.BigDecimal balance = invoice.getInvoiceBalance().add(prevAmt).subtract(payment.getPaymentAmount());  // or grand total  -  just set advance


				//if null then 0 

				if(payment.getTdsRate() == null)
					payment.setTdsRate(new BigDecimal(0));

				if(payment.getTdsAmount() == null)
					payment.setTdsAmount(new BigDecimal(0));

				//search for nested payment in the list
				iQuery = ds.createQuery(InwardEntity.class).disableValidation().filter("id", invoiceOid).filter("invoicePayments.id", new ObjectId(payment.getId().toString()));

				UpdateOperations<InwardEntity> ops =  ds.createUpdateOperations(InwardEntity.class)

						.set("invoicePayments.$.paymentDate", payment.getPaymentDate())
						.set("invoicePayments.$.paymentAmount", payment.getPaymentAmount())
						.set("invoicePayments.$.purposeTitle", payment.getPurposeTitle())
						.set("invoicePayments.$.purposeDescription", payment.getPurposeDescription())
						.set("invoicePayments.$.paymentMode", payment.getPaymentMode())
						.set("invoicePayments.$.paymentAccount", payment.getPaymentAccount())
						.set("invoicePayments.$.tdsRate", payment.getTdsRate())
						.set("invoicePayments.$.tdsAmount", payment.getTdsAmount())
						.set("invoicePayments.$.isTdsReceived", payment.isTdsReceived())
						.set("invoiceAdvancetotal", invoiceAdvance)
						.set("invoiceBalance", balance);


				UpdateResults result = ds.update(iQuery, ops, false);

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
		public String removeAdvPayment(String paymentId, String invoiceNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity invoice = null;
			ObjectId oid = null;
			ObjectId invoiceOid = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				invoiceOid = new ObjectId(invoiceNo);
				Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).filter("isInvoice", true)
						.filter("id", invoiceOid);

				invoice = iQuery.get();
				if(invoice == null)
					throw new EntityException(512, "converted invoice not found", null, null);

				//we need to sub old amt, and add new amt, so get old amt


				boolean match = false;
				OrderPayments pymnt = null;
				
				//all deleted in between - make sure there are adv payments to compare and update
				if(invoice.getEstimatePayments() == null)
					throw new EntityException(513, "payment not found", null, null);

				for(OrderPayments pay : invoice.getEstimatePayments())
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



				java.math.BigDecimal estimateAdvanceTotal = invoice.getEstimateAdvancetotal().subtract(pymnt.getPaymentAmount());
				java.math.BigDecimal invoiceAdvanceTotal = invoice.getInvoiceAdvancetotal().subtract(pymnt.getPaymentAmount());
				java.math.BigDecimal invoiceBalance = invoice.getInvoiceBalance().add(pymnt.getPaymentAmount());



				//get Bill, remove particular element of list after fetching it
				iQuery = ds.createQuery(InwardEntity.class).disableValidation().filter("id", invoiceOid);
				UpdateOperations<InwardEntity> ops =  ds.createUpdateOperations(InwardEntity.class)
						.removeAll("estimatePayments", pymnt)
						.set("estimateAdvancetotal",estimateAdvanceTotal)
						.set("invoiceAdvancetotal", invoiceAdvanceTotal)
						.set("invoiceBalance", invoiceBalance);

				UpdateResults result = ds.update(iQuery, ops, false);

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
			public String removeInvoicePayment(String paymentId, String invoiceNo, String companyId) throws EntityException
			{

				Datastore ds = null;
				Company cmp = null;
				InwardEntity invoice = null;
				ObjectId oid = null;
				ObjectId invoiceOid = null;

				try
				{
					ds = Morphiacxn.getInstance().getMORPHIADB("test");
					oid  = new ObjectId(companyId);
					Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
					cmp = query.get();
					if(cmp == null)
						throw new EntityException(404, "cmp not found", null, null);

					invoiceOid = new ObjectId(invoiceNo);
					Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true)
							.filter("id", invoiceOid);

					invoice = iQuery.get();
					if(invoice == null)
						throw new EntityException(512, "invoice not found", null, null);

					//we need to sub old amt, and add new amt, so get old amt


					boolean match = false;
					OrderPayments pymnt = null;
					
					//all deleted in between - make sure there are adv payments to compare and update
					if(invoice.getInvoicePayments() == null)
						throw new EntityException(513, "payment not found", null, null);

					for(OrderPayments pay : invoice.getInvoicePayments())
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


					java.math.BigDecimal invoiceAdvanceTotal = invoice.getInvoiceAdvancetotal().subtract(pymnt.getPaymentAmount());
					java.math.BigDecimal invoiceBalance = invoice.getInvoiceBalance().add(pymnt.getPaymentAmount());



					//get Bill, remove particular element of list after fetching it
					iQuery = ds.createQuery(InwardEntity.class).disableValidation().filter("id", invoiceOid);
					UpdateOperations<InwardEntity> ops =  ds.createUpdateOperations(InwardEntity.class)
							.removeAll("invoicePayments", pymnt)
							.set("invoiceAdvancetotal", invoiceAdvanceTotal)
							.set("invoiceBalance", invoiceBalance);


					UpdateResults result = ds.update(iQuery, ops, false);

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
			
			//get id from invoice, no chng in invoice number, chk fr customer chng
			public InwardEntity updateInvoice(InwardEntity entity , String companyId, String customerId) throws EntityException
			{

				Datastore ds = null;
				Company cmp = null;
				BusinessPlayers customer = null;
				Products prod = null;
				Tax tax = null;
				Key<InwardEntity> key = null;
				ObjectId oid = null;
				ObjectId customerOid = null;
				ObjectId prodOid = null;
				ObjectId taxOid = null;
				InwardEntity invoice = null;
				boolean customerChange = false;
				
				
				try
				{
					ds = Morphiacxn.getInstance().getMORPHIADB("test");
					oid  = new ObjectId(companyId);
					Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
					cmp = query.get();
					if(cmp == null)
						throw new EntityException(404, "cmp not found", null, null);
					
					

					//check if invoice exists with object id for update , fetch invoice, make changes to it
				
					Query<InwardEntity> iQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true).
							filter("id",entity.getId());

					invoice = iQuery.get();
					if(invoice == null)
						throw new EntityException(512, "invoice not found", null, null);  //cant update


					/*check if vendor changed in update, if so then check if new vendor exists
					 * if vendor hasnt changed bt was deleted - let it be
					 */

					if(!(customerId.equals(invoice.getCustomer().getId().toString())))
					{
						customerChange = true;
						customerOid  = new ObjectId(customerId);
						Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("isCustomer").equal(true).field("id").equal(customerOid).field("isDeleted").equal(false);
						customer = bpquery.get();
						if(customer == null)
							throw new EntityException(512, "customer not found", null, null);


					}
					
				
					
					/*if bill number is editable, do check if there is new /old vendor + new/old bill number combo unique
					 * if vendor hasnt changed and bill number hasnt chngd -  no prob
					 * if vendor/bill number has chngd do check
					 */
					
					
						
					// front end list cant be null
					List<OrderDetails> invoiceDetails = entity.getInvoiceDetails();

					for(OrderDetails details : invoiceDetails)
					{

						//check  tax exists, if not deleted
						if(details.getTax().getId() != null && details.getTax().isDeleted() == false)
						{

							taxOid = new ObjectId(details.getTax().getId().toString());
							Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
							tax = taxquery.get();
							if(tax == null)
								throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
						}

						//check  tax exists, if  deleted
						if(details.getTax().getId() != null && details.getTax().isDeleted() == true)
						{

							taxOid = new ObjectId(details.getTax().getId().toString());
							Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
							tax = taxquery.get();
							if(tax == null)
								throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
						}

						if(details.getProduct().isDeleted() == false)
						{

							prodOid = new ObjectId(details.getProduct().getId().toString());
							Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
							prod = prodquery.get();
							if(prod == null)
								throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
						}

						if(details.getProduct().isDeleted() == true)
						{

							prodOid = new ObjectId(details.getProduct().getId().toString());
							Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
							prod = prodquery.get();
							if(prod == null)
								throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
						}


						details.setId(new ObjectId());
						details.setTax(tax);
						details.setProduct(prod);

						tax = null;
						taxOid = null;
						prod = null;
						prodOid = null;

					}

					System.out.println("protax");
					
					invoice.setInvoiceDetails(invoiceDetails);
					invoice.setCompany(cmp);
					if(customerChange ==  true)
						invoice.setCustomer(customer);  //set only if vendor has changed
					
					
					invoice.setInvoiceDate(entity.getInvoiceDate());
					invoice.setInvoiceDate(entity.getInvoiceDate());
					invoice.setInvoiceDuedate(entity.getInvoiceDuedate());
					
					invoice.setInvoiceGrandtotal(entity.getInvoiceGrandtotal());
					invoice.setInvoiceSubtotal(entity.getInvoiceSubtotal());
					invoice.setInvoiceTaxtotal(entity.getInvoiceTaxtotal());
					java.math.BigDecimal balance = invoice.getInvoiceGrandtotal().subtract(invoice.getInvoiceAdvancetotal()); //grand total is just set, read prev advance
					invoice.setInvoiceBalance(balance);

					key = ds.merge(invoice);
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

				return invoice;
			}
			
			public List<InwardEntity> getInvoicelist( String companyId) throws EntityException
			{
				Datastore ds = null;
				Company cmp = null;
				ObjectId oid = null;
				List<InwardEntity> invoiceList = new ArrayList<>();

				try
				{
					ds = Morphiacxn.getInstance().getMORPHIADB("test");
					oid  = new ObjectId(companyId);
					Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
					cmp = query.get();
					if(cmp == null)
						throw new EntityException(404, "cmp not found", null, null);


					Query<InwardEntity> iquery = ds.find(InwardEntity.class).filter("company",cmp).filter("isInvoice", true).
							filter("isInvoicedeleted", false);

					invoiceList = iquery.asList();
					if(invoiceList.isEmpty() == true)
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

				return invoiceList;
			}



}
