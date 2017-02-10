package com.setup;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.nio.file.Path;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
//import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.Models.Employee;
import com.Models.PO;
import com.Models.PODetailsFrontEnd;
import com.Models.POfrontEnd;
import com.Models.PurchaseOrder;
import com.Models.PurchaseOrderDetails;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@javax.ws.rs.Path("/jack")
public class TestjsonObject {
	
	@GET
	@javax.ws.rs.Path("/main")
	public  String jsonObj(@QueryParam("jsondata") String jsonData) throws JsonParseException, JsonMappingException, IOException
	{
	
		ObjectMapper objectMapper = new ObjectMapper();
		Employee emp = objectMapper.readValue(jsonData, Employee.class);
		return emp.toString();
	}

	@GET
	@javax.ws.rs.Path("/actualPO")
	@Produces(MediaType.APPLICATION_JSON)
	public PurchaseOrder actual_po(@QueryParam("jsondata") String jsonData) throws JsonParseException, JsonMappingException, IOException
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
	
	
	
	
	
	

}
