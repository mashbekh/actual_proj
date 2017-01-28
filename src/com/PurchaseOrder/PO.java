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

import com.Models.PurchaseOrder;
import com.Models.PurchaseOrderDetails;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@javax.ws.rs.Path("/purchase")
public class PO {
	

	@GET
	@javax.ws.rs.Path("/createpo")
	@Produces(MediaType.APPLICATION_JSON)
	public PurchaseOrder create_po(@QueryParam("jsondata") String jsonData) throws JsonParseException, JsonMappingException, IOException
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
		
		if(po_id !=null)
		{
			p.setPo_id(po_id.longValue() + 1);
			p.setPO_number(p.getCompany_id() + "PO"  + (po_id.longValue() + 1));
		}
		else
		{
			p.setPo_id(1);
			p.setPO_number(p.getCompany_id() + "PO"  + (1));
		}
		
		
		for(PurchaseOrderDetails  pod : p.getPod())
		{
			pod.setPo(p);
			
		}
		
		em.getTransaction().begin();
		em.persist(p);
		
		for(PurchaseOrderDetails  pod : p.getPod())
		{
			em.persist(pod);
			
		}
		em.getTransaction().commit();
		em.close();
		
	return p;
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
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
	    PurchaseOrder porder = em.find( PurchaseOrder.class, p.getPO_number() );
	    
	 
	    
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
	    	em.getTransaction().begin();
	    		PurchaseOrderDetails y = em.find(PurchaseOrderDetails.class, x.getId());
	    		y.setAmount(x.getAmount());
	    		y.setCost(x.getCost());
	    		//y.setProduct_id(x.getProduct_id());
	    		y.setQuantity(x.getQuantity());
	    		y.setTax_amount(x.getTax_amount());
	    		y.setTax_id(x.getTax_id());
	    	em.getTransaction().commit();
	    	//the product id, PO object will remain the same.
	    	new_products.add(y);
	    }
	    
	    
	     //delete products
	     for(PurchaseOrderDetails x : delete_products)
	     { 
	    	 em.getTransaction().begin();
	    	 PurchaseOrderDetails y = em.find(PurchaseOrderDetails.class, x.getId());
	    	 em.remove(y);
	    	 em.getTransaction().commit();
	     }
	     
	     
	    //create products
	     
	     for(PurchaseOrderDetails pod : create_products)
		    {
	    	 //we need to persist
	    	 	em.getTransaction().begin();
		    	pod.setPo(porder);  //for all new ones, add this PO ref
		    	em.persist(pod);
		    	em.getTransaction().commit();
		    	new_products.add(pod);//add this to newly formed list
		    }
	     
	     //now new products has all new set of products, associate with PO object 
	     em.getTransaction().begin();
	     PurchaseOrder pord = em.find( PurchaseOrder.class, p.getPO_number() );
		    pord.setAmount(p.getAmount());
		    pord.setGrand_total(p.getGrand_total());
		    pord.setNote(p.getNote());
		    pord.setOrder_date(p.getOrder_date());
		   //po_id will be same
		    pord.setTax_amount(p.getTax_amount());
		    pord.setVendor_id(p.getVendor_id());
		   pord.setPod(new_products);
		   em.getTransaction().commit();
	    
		
	    return porder;	
	}
	
	
	}
	


