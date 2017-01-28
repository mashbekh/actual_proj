package com.Settings;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.print.attribute.standard.Media;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.Models.Taxes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/tax")
public class Tax_settings {
	
	@Path("/create_tax")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Taxes create_tax(@QueryParam("json") String json) throws JsonParseException, JsonMappingException, IOException 
	{
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		Taxes t = objectMapper.readValue(json,Taxes.class);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
			Query query = em.createNativeQuery("SELECT max(id) FROM tax_settings where company_id= ? ");
			query.setParameter(1, t.getCompany_id());
			java.math.BigInteger id =  (BigInteger) query.getSingleResult();
		em.getTransaction().commit();
		
		//System.out.println(id.longValue());
		//System.out.println(id.longValue() + 1);
		
		if(id !=null)
		{
			t.setId(id.longValue()+1);
			t.setTax_id((t.getCompany_id() + "TAXNO"  + (id.longValue() + 1)));
		}
		else
		{
			//System.out.println(t.getCompany_id() + "TAXNO"  + (1));
			t.setId(1);
			t.setTax_id(t.getCompany_id() + "TAXNO"  + (1));
			
		}
		
		em.getTransaction().begin();
		em.persist(t);
		
		em.getTransaction().commit();
		em.close();
		
		return t;
		
	}

}
