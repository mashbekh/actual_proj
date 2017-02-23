package com.setup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.Models.FileUpload;






@Path("/file")
public class fileUpload {

	@Path("/upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@FormDataParam("logo") InputStream  is) throws IOException
	{
		
		System.out.println("hiii");
		byte[] a = IOUtils.toByteArray(is);
		
		System.out.println(a);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		FileUpload a1 = new FileUpload(a, 11);
		
		em.getTransaction().begin();
		
		em.persist(a1);
		
		em.getTransaction().commit();
		
		
		
	    return Response.status(200).entity("success").build();
	}
	
	
	@Path("/getpic")
	@GET
	public String getimage()
	{
		
		FileUpload a1 = null;
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Demo");
		EntityManager em = emf.createEntityManager();
		
		
		
		em.getTransaction().begin();
		
		a1 = em.find(FileUpload.class, 11);
		
		em.getTransaction().commit();
		
		
		String base64Encoded = Base64.getEncoder().encodeToString(a1.getFile());
		return base64Encoded;
		
		
		
	}
	
}
