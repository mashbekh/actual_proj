package com.CompanyProfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.AccountDetails;
import com.Models.BusinessDetails;
import com.Models.ColumnSettings;
import com.Models.Company;
import com.Models.ContactDetails;
import com.Models.EstimateSettings;
import com.Models.InvoiceSettings;
import com.Models.POSettings;
import com.Models.TaxDetails;

@Path("/company")
public class CompanyServiceImpl {
	
	@POST
	@Path("/add")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCompany(
			@FormDataParam("business_name") String businessName, @FormDataParam("contact_name") String contactName, @FormDataParam("type_business") String businessType,@FormDataParam("business_sector") String businessSector,
			@FormDataParam("addr_line1") String addrLine1,@FormDataParam("addr_line2") String addrLine2,@FormDataParam("state") String state,@FormDataParam("city") String city,@FormDataParam("locality") String locality,@FormDataParam("email") String email,@FormDataParam("website") String website,@FormDataParam("mobile") String mobile,@FormDataParam("code") String code,@FormDataParam("landline") String landline,
			@FormDataParam("pan_no") String panNo,@FormDataParam("tin_no") String tinNo, @FormDataParam("strn_no") String strnNo,@FormDataParam("tan_no") String tanNo, @FormDataParam("gst_no") String gstNo, @FormDataParam("cin_no") String cinNo,
			@FormDataParam("acc_num") String accNo,@FormDataParam("type_acc") String accType,@FormDataParam("bank") String bank,@FormDataParam("ifsc_code") String ifsc_code,
			@FormDataParam("upload_pan") InputStream panDoc, @FormDataParam("upload_tin") InputStream tinDoc, @FormDataParam("upload_cin") InputStream cinDoc, @FormDataParam("upload_strn") InputStream strnDoc,@FormDataParam("upload_gst") InputStream gstDoc,@FormDataParam("upload_tan") InputStream tanDoc,
			@FormDataParam("logo") InputStream logoDoc, @QueryParam("id") String id, @QueryParam("token") String token) throws IOException, AppException, Exception, Throwable, EntityException, AppException
	{
		
		byte[] logo = IOUtils.toByteArray(logoDoc);
		
		BusinessDetails bd = new BusinessDetails(businessName, false, contactName, false, businessSector, false, businessType, false, logo);
		
		ContactDetails cd = new ContactDetails(addrLine1, addrLine2, state, city, locality, false, email, false, website, mobile, false, code + landline);
		
		TaxDetails td =  new TaxDetails(panNo, false, null, false, tinNo, false, null, false, strnNo, false, null, false, tanNo, false, null, false, gstNo, false, null, false, cinNo, false, null, false);
		
		AccountDetails ad   = new AccountDetails(accNo, accType, bank, ifsc_code);
		
		List<AccountDetails> adList =  new ArrayList<>();
		adList.add(ad);
		
		POSettings po = new POSettings(null,null,null,null,Long.valueOf(0));
		EstimateSettings estimate = new EstimateSettings(null,null,Long.valueOf(0),null,null);
		InvoiceSettings invoice = new InvoiceSettings(null,null,Long.valueOf(0),null,null);
		ColumnSettings column = new ColumnSettings("Products", "Quantity", "Price");
		
		CompanyDaoImpl companydao = new CompanyDaoImpl();
		Company company = new Company(bd,cd,td,adList,po,estimate,invoice,column,null);
		companydao.createCompany(company,id);
		return null;
	}
	

}
