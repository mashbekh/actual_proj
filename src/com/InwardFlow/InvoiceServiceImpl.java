package com.InwardFlow;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ErrorHandling.EntityException;
import com.Models.InwardEntity;
import com.Models.OrderPayments;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/invoice")
public class InvoiceServiceImpl {
	
	@POST
	@Path("/getNo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoiceNumber(@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		Long number = dao.getInvoiceNumber(companyId);
		return Response.status(Response.Status.OK)
				.entity(number).build();
	}
	
	@POST
	@Path("/createEstimateInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEstimateInvoice( @QueryParam("invoice") String invoiceJson, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		InwardEntity invoice = objectMapper.readValue(invoiceJson,InwardEntity.class);  


		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		InwardEntity createdInvoice = dao.createEstimateInvoice(invoice, companyId, customerId);

		return Response.status(Response.Status.OK)
				.entity(createdInvoice).build();
	}
	
	
	
	@POST
	@Path("/createInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBill( @QueryParam("invoice") String invoiceJson, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		InwardEntity invoice = objectMapper.readValue(invoiceJson,InwardEntity.class);  

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		InwardEntity createdInvoice = dao.createInvoice(invoice, companyId, customerId);

		return Response.status(Response.Status.OK)
				.entity(createdInvoice).build();
	}
	
	@POST
	@Path("/getInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoice(@QueryParam("invoiceNo") String invoiceId,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		InwardEntity invoice = dao.getInvoice(companyId, invoiceId);
		return Response.status(Response.Status.OK)
				.entity(invoice).build();
	}
	
	
	@POST
	@Path("/addInvoicePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBillPayment(@QueryParam("payment") String payment,@QueryParam("invoiceNo") String invoiceNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OrderPayments pymt = objectMapper.readValue(payment,OrderPayments.class);

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		OrderPayments invoicePayment = dao.addInvoicePayment(pymt, invoiceNo, companyId);

		
		return Response.status(Response.Status.OK)
				.entity(invoicePayment).build();
	}
	
	@POST
	@Path("/updateAdvPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAdvPayment(@QueryParam("payment") String payment,@QueryParam("invoiceNo") String invoiceNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OrderPayments pymt = objectMapper.readValue(payment,OrderPayments.class);

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		OrderPayments advPayment = dao.updateAdvPayment(pymt, companyId, invoiceNo);
		
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
				
	}
	
	@POST
	@Path("/updateInvoicePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInvoicePayment(@QueryParam("payment") String payment,@QueryParam("invoiceNo") String invoiceNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OrderPayments pymt = objectMapper.readValue(payment,OrderPayments.class);

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		OrderPayments invoicePayment = dao.updateInvoicePayment(pymt,  invoiceNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(invoicePayment).build();
	}
	
	
	@POST
	@Path("/removeAdvPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePayment(@QueryParam("payment") String paymentId,@QueryParam("invoiceNo") String invoiceNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		String advPayment = dao.removeAdvPayment(paymentId, invoiceNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}
	
	
	@POST
	@Path("/removeInvoicePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeInvoicePayment(@QueryParam("payment") String paymentId,@QueryParam("invoiceNo") String invoiceNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		String invoicePayment = dao.removeInvoicePayment(paymentId, invoiceNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(invoicePayment).build();
	}
	
	
	@POST
	@Path("/updateInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInvoice(@QueryParam("invoice") String invoiceJson,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		InwardEntity entity = objectMapper.readValue(invoiceJson,InwardEntity.class);
		
		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		InwardEntity updatedInvoice = dao.updateInvoice(entity, companyId , customerId);
		return Response.status(Response.Status.OK)
				.entity(updatedInvoice).build();
	}
	

	@POST
	@Path("/getInvoicelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInvoicelist( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		InvoiceDaoImpl dao = new InvoiceDaoImpl();
		List<InwardEntity> invoiceList = dao.getInvoicelist(companyId);
		return Response.status(Response.Status.OK)
				.entity(invoiceList).build();

	}
}
