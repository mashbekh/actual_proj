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
import com.Models.OutwardEntity;
import com.Models.Products;
import com.Models.Tax;
import com.setup.Morphiacxn;

public class EstimateDaoImpl {
	
	public InwardEntity createEstimate(InwardEntity estimate, String companyId, String customerId) throws EntityException
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
		InwardEntity estimateObj  = null;

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
				throw new EntityException(512, "customer not found", null, null);

			//check if po with same serialNo  - converted/ not  exists within company - overall -  irrespective of prefix and suffix

			Query<InwardEntity> estimateQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).field("estimateSerialId").equal(estimate.getEstimateSerialId());
			estimateObj = estimateQuery.get();
			if(estimateObj != null)
				throw new EntityException(516, "estimate found", null, null);


			List<OrderDetails> estimateDetails = estimate.getEstimateDetails();

			for(OrderDetails eod : estimateDetails)
			{

				//search if product and tax exists
				if(eod.getTax().getId() != null)
				{
					taxOid = new ObjectId(eod.getTax().getId().toString());
					Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
					tax = taxquery.get();
					if(tax == null)
						throw new EntityException(513, "tax not found", null, null);
				}


				prodOid = new ObjectId(eod.getProduct().getId().toString());
				Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
				prod = prodquery.get();
				if(prod == null)
					throw new EntityException(514, "product not found", null, null);

				//each pod needs Id
				eod.setId(new ObjectId());
				eod.setTax(tax);
				eod.setProduct(prod);

				tax = null;
				taxOid = null;
				prod = null;
				prodOid = null;

			}

			estimate.setEstimateDetails(estimateDetails);
			estimate.setCompany(cmp);
			estimate.setCustomer(customer);
			key = ds.save(estimate);
			if(key == null)
				throw new EntityException(515, "could not create", null, null);

			estimate.setId(new ObjectId(key.getId().toString()));



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
	
	
	//amomg estimate - converted/ not converted get the largest+1
		public Long getEstimateNumber(String companyId) throws EntityException
		{
			Datastore ds = null;
			Company cmp = null;
			InwardEntity estimate = null;
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

				Query<InwardEntity> estimateQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).order("-estimateSerialId");
				estimate = estimateQuery .get();

				if(estimate == null)
					number = Long.valueOf(1);
				else
					number = estimate.getEstimateSerialId() + 1;
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
		
		//maybe converted/not  - in front end, if converted only view option
		public InwardEntity getEstimate(String companyId, String estimateNumber) throws EntityException
		{
			Datastore ds = null;
			Company cmp = null;
			InwardEntity estimate = null;
			ObjectId oid = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);


				Query<InwardEntity> eQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false).filter("estimateNumber", estimateNumber);

				estimate = eQuery.get();
				if(estimate == null)
					throw new EntityException(512, "estimate not found", null, null);



			}
			catch(EntityException e){
				throw e;
			}
			catch(Exception e)
			{
				throw new EntityException(500, null, e.getMessage(), null);
			}

			return estimate;
		}

		
		//exclusively payment for PO only -  estimate number known, its unique among all vendors
		public OrderPayments addPayment(OrderPayments payment, String estimateNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity estimate = null;
			ObjectId oid = null;
			Key<InwardEntity> key = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				Query<InwardEntity> eQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false).filter("isInvoice", false).filter("estimateNumber", estimateNo);

				estimate = eQuery.get();
				if(estimate == null)
					throw new EntityException(512, "estimate not found", null, null);

				//if tds is null, initialise to 0

				if(payment.getTdsRate() == null)
					payment.setTdsRate(new BigDecimal(0));

				if(payment.getTdsAmount() == null)
					payment.setTdsAmount(new BigDecimal(0));

				payment.setId(new ObjectId());

				List<OrderPayments> list ;
				
				//may be adding payment for first time, initialise list

				if(estimate.getEstimatePayments() == null)
				{

					list = new ArrayList<>();
					list.add(payment);
				}
				else
				{

					list = estimate.getEstimatePayments();
					list.add(payment);
				}



				estimate.setEstimatePayments(list);
				estimate.setEstimateAdvancetotal(estimate.getEstimateAdvancetotal().add(payment.getPaymentAmount()));
				estimate.setEstimateBalance(estimate.getEstimateBalance().subtract(payment.getPaymentAmount()));

				key = ds.merge(estimate);

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

		
		public OrderPayments updatePayment(OrderPayments payment, String estimateNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity estimate = null;
			ObjectId oid = null;


			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				Query<InwardEntity> eQuery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false).filter("isInvoice", false).filter("estimateNumber", estimateNo);

				estimate = eQuery.get();
				if(estimate == null)
					throw new EntityException(512, "estimate not found", null, null);

				//we need to sub old amt, and add new amt, so get old amt

				java.math.BigDecimal prevAmt = null;
				boolean match = false;
				
				//all deleted in between
				if(estimate.getEstimatePayments() == null)
					throw new EntityException(513, "payment not found", null, null);

				for(OrderPayments pay : estimate.getEstimatePayments())
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

				java.math.BigDecimal advanceTotal = estimate.getEstimateAdvancetotal().subtract(prevAmt).add(payment.getPaymentAmount());
				java.math.BigDecimal balance = estimate.getEstimateBalance().add(prevAmt).subtract(payment.getPaymentAmount());

				//if null then 0 

				if(payment.getTdsRate() == null)
					payment.setTdsRate(new BigDecimal(0));

				if(payment.getTdsAmount() == null)
					payment.setTdsAmount(new BigDecimal(0));

				eQuery = ds.createQuery(InwardEntity.class).disableValidation().filter("id", estimate.getId()).filter("estimatePayments.id", new ObjectId(payment.getId().toString()));

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
						.set("estimateBalance", balance);


				UpdateResults result = ds.update(eQuery, ops, false);

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


		public String removePayment(String paymentId, String estimateNo, String companyId) throws EntityException
		{

			Datastore ds = null;
			Company cmp = null;
			InwardEntity estimate = null;
			ObjectId oid = null;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				Query<InwardEntity> equery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false).filter("isInvoice", false).filter("estimateNumber", estimateNo);

				estimate = equery.get();
				if(estimate == null)
					throw new EntityException(512, "estimate not found", null, null);

				//we need to sub old amt, and add new amt, so get old amt


				boolean match = false;
				OrderPayments pymnt = null;
				
				//all deleted in between
				if(estimate.getEstimatePayments() == null)
					throw new EntityException(513, "payment not found", null, null);

				for(OrderPayments pay : estimate.getEstimatePayments())
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



				java.math.BigDecimal advanceTotal = estimate.getEstimateAdvancetotal().subtract(pymnt.getPaymentAmount());
				java.math.BigDecimal balance = estimate.getEstimateBalance().add(pymnt.getPaymentAmount());



				//remove particular element of list
				equery = ds.createQuery(InwardEntity.class).disableValidation().filter("id",estimate.getId());
				UpdateOperations<InwardEntity> ops =  ds.createUpdateOperations(InwardEntity.class)
						.removeAll("estimatePayments", pymnt)
						.set("estimateAdvancetotal",advanceTotal)
						.set("estimateBalance", balance);


				UpdateResults result = ds.update(equery, ops, false);

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
		public InwardEntity updateEstimate( InwardEntity entity, String companyId, String customerId) throws EntityException
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
			boolean customerChange = false;

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);

				//check if estimate exists for update only when not converted to invoice

				Query<InwardEntity> equery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false).filter("isInvoice", false).filter("estimateNumber", entity.getEstimateNumber());

				estimate = equery.get();
				if(estimate == null)
					throw new EntityException(512, "estimate not found", null, null);  //cant update


				/*check if customer changed in update, if so then check if new vendor exists
				 * if customer hasnt changed bt was deleted - let it be
				 */

				if(!(customerId.equals(estimate.getCustomer().getId().toString())))
				{
					customerChange = true;
					customerOid  = new ObjectId(customerId);
					Query<BusinessPlayers> bpquery = ds.createQuery(BusinessPlayers.class).field("company").equal(cmp).field("id").equal(customerOid).field("isCustomer").equal(true).field("isDeleted").equal(false);
					customer = bpquery.get();
					if(customer == null)
						throw new EntityException(512, "customer not found", null, null);


				}


				// front end list cant be null
				List<OrderDetails> estimateDetails = entity.getEstimateDetails();

				for(OrderDetails eod : estimateDetails)
				{

					//check  tax exists, if not deleted
					if(eod.getTax().getId() != null && eod.getTax().isDeleted() == false)
					{

						taxOid = new ObjectId(eod.getTax().getId().toString());
						Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(false);
						tax = taxquery.get();
						if(tax == null)
							throw new EntityException(513, "tax not found", null, null);  //has been deleted in due course
					}

					//check  tax exists, if  deleted
					if(eod.getTax().getId() != null && eod.getTax().isDeleted() == true)
					{

						taxOid = new ObjectId(eod.getTax().getId().toString());
						Query<Tax> taxquery = ds.createQuery(Tax.class).field("company").equal(cmp).field("id").equal(taxOid).field("isDeleted").equal(true);
						tax = taxquery.get();
						if(tax == null)
							throw new EntityException(513, "tax not found", null, null);   //tax doesn't exists / may not be deleted
					}

					if(eod.getProduct().isDeleted() == false)
					{

						prodOid = new ObjectId(eod.getProduct().getId().toString());
						Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(false);
						prod = prodquery.get();
						if(prod == null)
							throw new EntityException(514, "product not found", null, null);  //has been deleted in due course
					}

					if(eod.getProduct().isDeleted() == true)
					{

						prodOid = new ObjectId(eod.getProduct().getId().toString());
						Query<Products> prodquery = ds.createQuery(Products.class).field("company").equal(cmp).field("id").equal(prodOid).field("isDeleted").equal(true);
						prod = prodquery.get();
						if(prod == null)
							throw new EntityException(514, "product not found", null, null);  //product doesn't exists / may not be deleted
					}


					eod.setId(new ObjectId());
					eod.setTax(tax);
					eod.setProduct(prod);

					tax = null;
					taxOid = null;
					prod = null;
					prodOid = null;

				}

				estimate.setEstimateNotes(entity.getEstimateNotes());
				estimate.setEstimateDetails(estimateDetails);
				estimate.setCompany(cmp);
				if(customerChange ==  true)
					entity.setCustomer(customer);  //set only if vendor has changed
				
				estimate.setEstimateSubtotal(entity.getEstimateSubtotal());
				estimate.setEstimateGrandtotal(entity.getEstimateGrandtotal());
				estimate.setEstimateTaxtotal(entity.getEstimateTaxtotal());
				java.math.BigDecimal balance = estimate.getEstimateGrandtotal().subtract(estimate.getEstimateAdvancetotal()); //grand total is just set, read prev advance
				estimate.setEstimateBalance(balance);

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
				System.out.println(e.getMessage() + e.getClass());
				throw new EntityException(500, null , e.getMessage(), null);
			}

			return estimate;
		}
		
		// send all PO, front end if also bill then hold hyperlink
		public List<InwardEntity> getEstimatelist( String companyId) throws EntityException
		{
			Datastore ds = null;
			Company cmp = null;
			ObjectId oid = null;
			List<InwardEntity> estimateList = new ArrayList<>();

			try
			{
				ds = Morphiacxn.getInstance().getMORPHIADB("test");
				oid  = new ObjectId(companyId);
				Query<Company> query = ds.createQuery(Company.class).field("id").equal(oid);
				cmp = query.get();
				if(cmp == null)
					throw new EntityException(404, "cmp not found", null, null);


				Query<InwardEntity> equery = ds.find(InwardEntity.class).filter("company",cmp).filter("isEstimate", true).
						filter("isEstimatedeleted", false);

				 estimateList = equery.asList();
				if( estimateList.isEmpty() == true)
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

			return  estimateList;
		}

		
}
