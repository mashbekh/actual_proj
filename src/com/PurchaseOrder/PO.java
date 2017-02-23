package com.PurchaseOrder;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Hibernate;

import com.Models.AdvancePayment;
import com.Models.PurchaseOrder;
import com.Models.PurchaseOrderDetails;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.setup.AppEx;


@javax.ws.rs.Path("/purchase")
public class PO {
	
	
	
	/*1. To create PO - JSON - po object will have company object too, always fetch company object - populate PO, add this PO to
	   * list of PO associated with company(edit/update company types) and assign this company object to the newly created PO.
	   * no need to maintain vendor stats
	   * To update PO - nothing to do with company
	   * To delete PO - remove it from list of PO associated with company
	   *                delete all POD associated with PO, remove from company list 
	   *                then remove finally from PO table
	*/   

	@GET
	@javax.ws.rs.Path("/createpo")
	//@Produces(MediaType.APPLICATION_JSON)
	public String create_po(@QueryParam("jsondata") String jsonData) throws JsonParseException, JsonMappingException, IOException
	{
	
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		PurchaseOrder p = objectMapper.readValue(jsonData,PurchaseOrder.class);
		System.out.println(p.getOrder_date());
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
			Query query = em.createNativeQuery("SELECT max(po_id) FROM purchase_order where company_id= ? ");
			query.setParameter(1, p.getCompany_id());
			java.math.BigInteger po_id =  (BigInteger) query.getSingleResult();
		em.getTransaction().commit();
		
		String po_no=null;
		
		if(po_id !=null)
		{
			p.setPo_id(po_id.longValue() + 1);
			p.setPO_number(p.getCompany_id() + "PO"  + (po_id.longValue() + 1));
			po_no = p.getCompany_id() + "PO"  + (po_id.longValue() + 1);
		}
		else
		{
			p.setPo_id(1);
			p.setPO_number(p.getCompany_id() + "PO"  + (1));
			po_no = p.getCompany_id() + "PO"  + (1);
		}
		
		
		for(PurchaseOrderDetails  pod : p.getPod())
		{
			pod.setPo(p);
			
		}
		
		//persist both objects together
		
		em.getTransaction().begin();
		em.persist(p);
		
		for(PurchaseOrderDetails  pod : p.getPod())
		{
			em.persist(pod);
			
		}
		em.getTransaction().commit();
		em.close();
		
	return po_no;
	}
	
	@GET
	@javax.ws.rs.Path("/editpo")
	@Produces(MediaType.APPLICATION_JSON)
	public PurchaseOrder edit_po(@QueryParam("jsondata") String jsonData) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		PurchaseOrder p = objectMapper.readValue(jsonData,PurchaseOrder.class);
		System.out.println(p.toString());
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		
		//BEGIN TXN
		em.getTransaction().begin();
	   
	    PurchaseOrder porder = em.find( PurchaseOrder.class, p.getPO_number() );
	    
	    
	    //handle advance payment
	    
	    if(p.getAdv_payment().size()!=0)
	    {
	    	List<AdvancePayment> ap = p.getAdv_payment();
	    	AdvancePayment advpay =  ap.get(0); 
	    	 advpay.setPo(porder);
	    	 System.out.println(advpay.toString());
	    	 em.persist(advpay);
	    	 porder.getAdv_payment().add(advpay);
	         //double newadvance = porder.getAdvance_amount() + advpay.getPayment_amount();
	         //double newbalance = porder.getBalance() - advpay.getPayment_amount();
	         
	         //porder.setAdvance_amount(newadvance);
	         //porder.setBalance(newbalance);
	    }
	    
	    
	 
	    
	    List<PurchaseOrderDetails> existing_products = porder.getPod();
	    porder.setPod(null); //lets give new list
	    List<PurchaseOrderDetails> create_products = new ArrayList<>(); //new ones to be created
	    List<PurchaseOrderDetails> update_products = new ArrayList<>(); //ones in incoming list(same as existing)
	    List<PurchaseOrderDetails> new_products = new ArrayList<>(); //final new list
	    List<PurchaseOrderDetails> delete_products = new ArrayList<>(); //ones to be deleted
	    
	    //  seggregate incoming list as to be CREATED + UPDATED
	    
	    for(PurchaseOrderDetails pod : p.getPod())
	    {
	   	if(pod.getId()== 0)
	   		create_products.add(pod);  //these have to be created
	   	else
	   		update_products.add(pod);
	   		
	    }
	    /*
	    for(PurchaseOrderDetails pod : create_products)
	    {
	    	pod.setPo(porder);  //for all new ones, add this PO ref
	    	new_products.add(pod);//add this to newly formed list
	    }
	    */
	    
	    
	    // get to be DELETED list 
	    
	     existing_products.removeAll(update_products); //now existing products has only the ones to be deleted
	    
	     for(PurchaseOrderDetails a : existing_products)
	     {
	    	 delete_products.add(a);
	     }
	     
	    
	    //update the products
	    for(PurchaseOrderDetails x : update_products)
	    {
	    	//put in transaction and they will automatically get updated
	    //	em.getTransaction().begin();
	    		PurchaseOrderDetails y = em.find(PurchaseOrderDetails.class, x.getId());
	    		y.setAmount(x.getAmount());
	    		y.setCost(x.getCost());
	    		//y.setProduct_id(x.getProduct_id());
	    		y.setQuantity(x.getQuantity());
	    		y.setTax_amount(x.getTax_amount());
	    		y.setTax_id(x.getTax_id());
	    //	em.getTransaction().commit();
	    	//the product id, PO object will remain the same.
	    	new_products.add(y);
	    }
	    
	    
	     //delete products
	     for(PurchaseOrderDetails x : delete_products)
	     { 
	    	 //em.getTransaction().begin();
	    	 PurchaseOrderDetails y = em.find(PurchaseOrderDetails.class, x.getId());
	    	 em.remove(y);
	    	// em.getTransaction().commit();
	     }
	     
	     
	    //create products
	     
	     for(PurchaseOrderDetails pod : create_products)
		    {
	    	 //we need to persist
	    	 	//em.getTransaction().begin();
		    	pod.setPo(porder);  //for all new ones, add this PO ref
		    	em.persist(pod);
		    	//em.getTransaction().commit();
		    	new_products.add(pod);//add this to newly formed list
		    }
	     
	     //now new products has all new set of products, associate with PO object 
	     
	     //PurchaseOrder pord = em.find( PurchaseOrder.class, p.getPO_number() );
		    porder.setAmount(p.getAmount());
		    porder.setGrand_total(p.getGrand_total());
		    porder.setNote(p.getNote());
		    porder.setOrder_date(p.getOrder_date());
		   //po_id will be same
		    porder.setTax_amount(p.getTax_amount());
		    porder.setVendor_id(p.getVendor_id());
		   porder.setPod(new_products);
		   porder.setAdvance_amount(p.getAdvance_amount());
		   porder.setBalance(p.getBalance());
		   em.getTransaction().commit();
	    //em.close();
		
	    //return newly updated PO
	    em.getTransaction().begin();
	     PurchaseOrder pord = em.find( PurchaseOrder.class, p.getPO_number() );
	     Hibernate.initialize(pord.getPod());
	     Hibernate.initialize(pord.getAdv_payment());
	     em.getTransaction().commit();
	     em.close();
	    return pord;	
	}
	
	@GET
	@Path("/deletepo")
	public Response delete_po(@QueryParam("company_id") String company_id) throws AppEx
	{
		 EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
			EntityManager em = emf.createEntityManager();
			
			
			em.getTransaction().begin();
			  //delete POD , then PO
			
			PurchaseOrder p = em.find(PurchaseOrder.class, company_id);
			
			em.getTransaction().commit();
			
			if(p==null)
			{
				AppEx a =  new AppEx(Response.Status.NOT_FOUND.getStatusCode(),"not found PO");
				return Response.status(a.getStatus()).entity(a.getMsg()).build();
			}
			
			for(PurchaseOrderDetails pod : p.getPod())
			{
				
				em.remove(pod);  //delete all POD
			}
			
			for(AdvancePayment ap : p.getAdv_payment())
			{
				em.remove(ap);
			}
			
			//now delete PO
			
			em.remove(p);
			
		    em.close();
		
		//return success/failure state
		return Response.status(Response.Status.ACCEPTED.getStatusCode()).entity("deleted").build();
	}
	

   @GET
   @Path("/viewall")
   @Produces(MediaType.APPLICATION_JSON)
   public List<PurchaseOrder> view_all_po(@QueryParam("company_id") String company_id ) //or use json and get company table object
   {
	   
	  // System.out.println(company_id);
	   //this is subject to change
	   //search in company table for this ID, and get list of all PO there (bi-directional)
	   EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		Query query = em.createQuery("select g from PurchaseOrder g where company_id=:cid");
		query.setParameter("cid", company_id);
		List<PurchaseOrder> po_list  = query.getResultList();
		//System.out.println(po_list.size());
		em.getTransaction().commit();
		em.close();
	   
	    for(PurchaseOrder a : po_list)
	    {
	    	a.setPod(null);	 
	    	a.setAdv_payment(null);
	    	}
	   
	   return po_list;
   }
   
   @GET
   @Path("/viewpo")
   @Produces(MediaType.APPLICATION_JSON)
   public PurchaseOrder view_po(@QueryParam("po_num") String po_num ) //or use json and get company table object
   {
	   System.out.println(po_num);
	   EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		  PurchaseOrder a = em.find(PurchaseOrder.class, po_num);
		 Hibernate.initialize(a.getPod());
		 Hibernate.initialize(a.getAdv_payment());
		em.getTransaction().commit();
		em.close();
	   return a;
   }
   }
   
	
	
	
	
	
	
	
	


