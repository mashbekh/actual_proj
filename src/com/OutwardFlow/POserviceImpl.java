package com.OutwardFlow;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import com.ErrorHandling.EntityException;
import com.Models.OutwardEntity;
import com.Models.POBillPayments;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/po")
public class POserviceImpl {


	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPO( @QueryParam("po") String po, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OutwardEntity purchaseordr = objectMapper.readValue(po,OutwardEntity.class);


		PODaoImpl dao = new PODaoImpl();
		OutwardEntity purchaseOrder = dao.createPO(purchaseordr, companyId, vendorId);

		return Response.status(Response.Status.OK)
				.entity(purchaseOrder).build();
	}


	@POST
	@Path("/getNo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPoNumber(@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		PODaoImpl dao = new PODaoImpl();
		Long number = dao.getPoNumber(companyId);
		return Response.status(Response.Status.OK)
				.entity(number).build();
	}


	@POST
	@Path("/getPO")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchaseOrder(@QueryParam("pno") String poNumber,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		PODaoImpl dao = new PODaoImpl();
		OutwardEntity po = dao.getPurchaseOrder(companyId, poNumber);
		return Response.status(Response.Status.OK)
				.entity(po).build();
	}


	@POST
	@Path("/addPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdvPayment(@QueryParam("payment") String payment,@QueryParam("pno") String poNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		POBillPayments pymt = objectMapper.readValue(payment,POBillPayments.class);



		PODaoImpl dao = new PODaoImpl();
		POBillPayments advPayment = dao.addPayment(pymt, poNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}


	@POST
	@Path("/updatePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePayment(@QueryParam("payment") String payment,@QueryParam("pno") String poNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		POBillPayments pymt = objectMapper.readValue(payment,POBillPayments.class);



		PODaoImpl dao = new PODaoImpl();
		POBillPayments advPayment = dao.updatePayment(pymt, poNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}

	@POST
	@Path("/removePayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePayment(@QueryParam("payment") String paymentId,@QueryParam("pno") String poNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		PODaoImpl dao = new PODaoImpl();
		String advPayment = dao.removePayment(paymentId, poNo, companyId); 
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}


	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePO( @QueryParam("po") String po, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OutwardEntity purchaseordr = objectMapper.readValue(po,OutwardEntity.class);


		PODaoImpl dao = new PODaoImpl();
		OutwardEntity purchaseOrder = dao.updatePO( purchaseordr, companyId, vendorId);

		return Response.status(Response.Status.OK)
				.entity(purchaseOrder).build();
	}


	// yet to add delete PO

	@POST
	@Path("/getPolist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPOList( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		PODaoImpl dao = new PODaoImpl();
		List<OutwardEntity> poList = dao.getPolist( companyId);
		return Response.status(Response.Status.OK)
				.entity(poList).build();

	}

}





