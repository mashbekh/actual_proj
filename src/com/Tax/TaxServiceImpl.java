package com.Tax;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.Tax;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/tax")
public class TaxServiceImpl {
	
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTax(@QueryParam("tax") String taxObj, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		//check user, and get company he is associated with, check if cid one of them -> continue
		//if cid not one of them, login again
		// for now just get company this user is part of and continue
		ObjectMapper objectMapper = new ObjectMapper();
		Tax tax = objectMapper.readValue(taxObj, Tax.class);
		TaxDaoImpl taxdao = new TaxDaoImpl();
		Tax t = taxdao.addTax(tax, companyId);
		
		return Response.status(Response.Status.OK)
				   .entity(t).build();
	}
	
	@POST
	@Path("/getTaxlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTaxlist( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		TaxDaoImpl taxdao = new TaxDaoImpl();
		List<Tax> taxlist = taxdao.getTaxlist(companyId);
		return Response.status(Response.Status.OK)
				   .entity(taxlist).build();
		
		
	}
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlltaxes( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		TaxDaoImpl taxdao = new TaxDaoImpl();
		List<Tax> taxlist = taxdao.getAllTaxes(companyId);
		return Response.status(Response.Status.OK)
				   .entity(taxlist).build();
		
		
	}
	
	
	

	@POST
	@Path("/updateTax")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTax(@QueryParam("tax") String taxObj,  @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		Tax tax = objectMapper.readValue(taxObj, Tax.class);
		TaxDaoImpl taxdao = new TaxDaoImpl();
		
		Tax tx = taxdao.updateTax(tax, companyId);
		return Response.status(Response.Status.OK)
				   .entity(tx).build();
		
		
	}
	
	
	@POST
	@Path("/deleteTax")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTax(@QueryParam("tid") String taxId,  @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		TaxDaoImpl taxdao = new TaxDaoImpl();
		String msg = taxdao.deleteTax(taxId, companyId);
		return Response.status(Response.Status.OK)
				   .entity(msg).build();
		
		
	}
	
	
	

}
