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

import com.ErrorHandling.EntityException;
import com.Models.OutwardEntity;
import com.Models.POBillPayments;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/bill")
public class BillServiceImpl {
	
	@POST
	@Path("/createPOBill")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPOBill( @QueryParam("bill") String bill, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OutwardEntity purchaseordr = objectMapper.readValue(bill,OutwardEntity.class);  


		BillDaoImpl dao   = new BillDaoImpl();
		OutwardEntity purchaseOrder = dao.createPoBill(purchaseordr, companyId, vendorId);

		return Response.status(Response.Status.OK)
				.entity(purchaseOrder).build();
	}
	
	
	
	@POST
	@Path("/createBill")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBill( @QueryParam("bill") String bill, @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		OutwardEntity purchaseordr = objectMapper.readValue(bill,OutwardEntity.class);  


		BillDaoImpl dao   = new BillDaoImpl();
		OutwardEntity purchaseOrder = dao.createBill(purchaseordr, companyId, vendorId);

		return Response.status(Response.Status.OK)
				.entity(purchaseOrder).build();
	}
	
	

	@POST
	@Path("/getBill")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBill(@QueryParam("billNo") String billId,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		BillDaoImpl dao = new BillDaoImpl();
		OutwardEntity bill = dao.getBill(companyId, billId);
		return Response.status(Response.Status.OK)
				.entity(bill).build();
	}
	
	

	@POST
	@Path("/addBillPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addBillPayment(@QueryParam("payment") String payment,@QueryParam("billNo") String billNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		POBillPayments pymt = objectMapper.readValue(payment,POBillPayments.class);

		BillDaoImpl dao = new BillDaoImpl();
		POBillPayments billPayment = dao.addBillpayment(pymt, billNo, companyId);

		
		return Response.status(Response.Status.OK)
				.entity(billPayment).build();
	}
	
	@POST
	@Path("/updateAdvPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAdvPayment(@QueryParam("payment") String payment,@QueryParam("billNo") String billNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		System.out.println("pp");
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		POBillPayments pymt = objectMapper.readValue(payment,POBillPayments.class);

		BillDaoImpl dao = new BillDaoImpl();
		POBillPayments billPayment = dao.updateAdvPayment(pymt, companyId, billNo);
		
		return Response.status(Response.Status.OK)
				.entity(billPayment).build();
				
	}
	
	@POST
	@Path("/updateBillPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBillPayment(@QueryParam("payment") String payment,@QueryParam("billNo") String billNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{
		ObjectMapper objectMapper = new ObjectMapper();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		objectMapper.setDateFormat(df);
		POBillPayments pymt = objectMapper.readValue(payment,POBillPayments.class);

		BillDaoImpl dao = new BillDaoImpl();
		POBillPayments billPayment = dao.updateBillPayment(pymt, billNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(billPayment).build();
	}
	
	
	@POST
	@Path("/removeAdvPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removePayment(@QueryParam("payment") String paymentId,@QueryParam("billNo") String billNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		BillDaoImpl dao = new BillDaoImpl();
		String advPayment = dao.removeAdvPayment(paymentId, billNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(advPayment).build();
	}
	
	
	@POST
	@Path("/removeBillPayment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeBillPayment(@QueryParam("payment") String paymentId,@QueryParam("billNo") String billNo,@QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token,  @QueryParam("vendorId") String vendorId ) throws JsonMappingException , JsonParseException, IOException, EntityException, Exception, Throwable
	{

		BillDaoImpl dao = new BillDaoImpl();
		String billPayment = dao.removeBillPayment(paymentId, billNo, companyId);
		return Response.status(Response.Status.OK)
				.entity(billPayment).build();
	}
	

}
