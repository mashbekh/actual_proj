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

@Path("/estimate")
public class EstimateServiceImpl {
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEstimate( @QueryParam("estimate") String estimateJson, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		InwardEntity estimate = objectMapper.readValue(estimateJson,InwardEntity.class);


		EstimateDaoImpl dao =  new EstimateDaoImpl();
		InwardEntity createdEstimate = dao.createEstimate(estimate, companyId, customerId);

		return Response.status(Response.Status.OK)
				.entity(createdEstimate).build();
	}
	
	
	
	@POST
	@Path("/getEstimateNo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstimateNumber(@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		EstimateDaoImpl dao =  new EstimateDaoImpl();
		Long number = dao.getEstimateNumber(companyId);
		return Response.status(Response.Status.OK)
				.entity(number).build();
	}
	
	@POST
	@Path("/getEstimate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchaseOrder(@QueryParam("eNo") String estimateNumber,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		EstimateDaoImpl dao =  new EstimateDaoImpl();
		InwardEntity estimate = dao.getEstimate(companyId, estimateNumber);
		return Response.status(Response.Status.OK)
				.entity(estimate).build();
	}
	
	@POST
	@Path("/addPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdvPayment(@QueryParam("payment") String payment,@QueryParam("eNo") String estimateNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OrderPayments pymt = objectMapper.readValue(payment,OrderPayments.class);

		EstimateDaoImpl dao =  new EstimateDaoImpl();
		OrderPayments advPayment = dao.addPayment(pymt, estimateNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}

	@POST
	@Path("/updatePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePayment(@QueryParam("payment") String payment,@QueryParam("eNo") String estimateNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OrderPayments pymt = objectMapper.readValue(payment,OrderPayments.class);

		EstimateDaoImpl dao =  new EstimateDaoImpl();
		OrderPayments advPayment = dao.updatePayment(pymt, estimateNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}

	@POST
	@Path("/removePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePayment(@QueryParam("payment") String paymentId,@QueryParam("eNo") String estimateNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		EstimateDaoImpl dao =  new EstimateDaoImpl();
		String advPayment = dao.removePayment(paymentId, estimateNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}
	
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEstimate( @QueryParam("estimate") String estimateJson, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("customerId") String customerId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		InwardEntity entity = objectMapper.readValue(estimateJson,InwardEntity.class);


		EstimateDaoImpl dao =  new EstimateDaoImpl();
		InwardEntity estimate = dao.updateEstimate(entity, companyId, customerId);

		return Response.status(Response.Status.OK)
				.entity(estimate).build();
	}
	
	@POST
	@Path("/getEstimatelist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEstimateList( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		EstimateDaoImpl dao =  new EstimateDaoImpl();
		List<InwardEntity> estimateList = dao.getEstimatelist( companyId);
		return Response.status(Response.Status.OK)
				.entity(estimateList).build();

	}

}
