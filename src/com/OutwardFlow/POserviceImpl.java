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
		
		System.out.println(purchaseordr);
		
		PODaoImpl dao = new PODaoImpl();
		OutwardEntity purchaseOrder = dao.createPO(purchaseordr, companyId, vendorId);
		
		System.out.println(purchaseOrder);
		return Response.status(Response.Status.OK)
				.entity(purchaseOrder).build();
	}

}
