package com.Customer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.AccountDetails;
import com.Models.BusinessDetails;
import com.Models.BusinessPlayers;
import com.Models.ContactDetails;
import com.Models.TaxDetails;

@Path("/customer")
public class CustomerServiceImpl {
	

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createVendor(@FormDataParam("business_name") String businessName, @FormDataParam("contact_name") String contactName, 
			@FormDataParam("addr_line1") String addrLine1,@FormDataParam("addr_line2") String addrLine2,@FormDataParam("state") String state,@FormDataParam("city") String city,@FormDataParam("locality") String locality,@FormDataParam("email") String email,@FormDataParam("website") String website,@FormDataParam("mobile") String mobile,@FormDataParam("code") String code,@FormDataParam("landline") String landline,
			@FormDataParam("pan_no") String panNo,@FormDataParam("aadhar_no") String aadharNo,@FormDataParam("tin_no") String tinNo, @FormDataParam("strn_no") String strnNo,@FormDataParam("tan_no") String tanNo, @FormDataParam("gst_no") String gstNo,
			@FormDataParam("acc_num") String accNo,@FormDataParam("type_acc") String accType,@FormDataParam("bank") String bank,@FormDataParam("ifsc_code") String ifsc_code,
			@QueryParam("cid") String companyId, @QueryParam("id") String userId ,@QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{

		BusinessDetails bd = new BusinessDetails(businessName, false, contactName, false, null, false, null, false, null);

		ContactDetails cd = new ContactDetails(addrLine1, addrLine2, state, city, locality, false, email, false, website, mobile, false, code + landline, aadharNo);

		TaxDetails td =  new TaxDetails(panNo, false, null, false, tinNo, false, null, false, strnNo, false, null, false, tanNo, false, null, false, gstNo, false, null, false, null, false, null, false);

		AccountDetails ad   = new AccountDetails(accNo, accType, bank, ifsc_code);

		List<AccountDetails> adList =  new ArrayList<>();
		adList.add(ad);

		BusinessPlayers bplayer = new BusinessPlayers( bd, cd, td, adList , false, true, false, false , new BigDecimal("0") , new BigDecimal("0"), null, null, false);
		CustomerDaoImpl customerdao = new CustomerDaoImpl();
		BusinessPlayers bp = customerdao.createCustomer(bplayer, companyId);

		return Response.status(Response.Status.OK)
				.entity(bp).build();

	}
	
	
	
	@POST
	@Path("/getList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerlist( @QueryParam("cid") String companyId, @QueryParam("id") String userId, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		CustomerDaoImpl customerdao = new CustomerDaoImpl();
		List<BusinessPlayers> customers = customerdao.getCustomers(companyId);
		return Response.status(Response.Status.OK)
				.entity(customers).build();

	}

}
