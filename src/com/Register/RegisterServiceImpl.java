package com.Register;

import java.io.IOException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.Models.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Path("/register")
public class RegisterServiceImpl {
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User userJson) throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		String userId;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		
		
			userId = regdao.createUser(userJson);
			return Response.status(Response.Status.CREATED)
					   .entity(userId)
					   .type(MediaType.APPLICATION_JSON)
					   .build();
		
	}
	
	
	@POST
	@Path("/confirmMail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirm( @QueryParam("verifyCode") String verifyCode) throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		User user;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		System.out.println(verifyCode);
		
			user = regdao.confirmEmail(verifyCode);
			return Response.status(Response.Status.OK)
					   .entity(user).build();
		
	}
	
	
	@POST
	@Path("/sendMail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMail(@QueryParam("id") String id)throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		String userId;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		
		
			userId = regdao.resendMail(id);
			return Response.status(Response.Status.OK)
					   .entity(userId).build();
	}
	
	
	@POST
	@Path("/updateMail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMail(@QueryParam("id") String id, @QueryParam("email") String email)throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		String userId;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		
		
			userId = regdao.updateEmail(id, email);
			return Response.status(Response.Status.OK)
					   .entity(userId).build();
	}
	
	@POST
	@Path("/sendOtp")
	@Produces(MediaType.APPLICATION_JSON)    
	public Response sendOtp(@QueryParam("id") String id) throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		String userId = regdao.sendOtp(id);
		return Response.status(Response.Status.OK)
				   .entity(userId).build();
	}
	
	
	@POST
	@Path("/resendOtp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response reSendOtp(@QueryParam("id") String id) throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		String userId = regdao.resendOtp(id);  //re send , gen new Otp and update
		return Response.status(Response.Status.OK)
				   .entity(userId).build();
	}
	
	
	@POST
	@Path("/updateMobile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMobile(@QueryParam("id") String id, @QueryParam("mobile") String mobile)throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		String userId;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		
		
			userId = regdao.updateMobile(id, mobile);
			return Response.status(Response.Status.OK)
					   .entity(userId).build();
	}
	
	
	@POST
	@Path("/confirmOtp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmOtp(@QueryParam("id") String id, @QueryParam("otp") int otp)throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		String userId;
		RegisterDaoImplementation regdao = new RegisterDaoImplementation();
		
			userId = regdao.confirmOtp(id, otp);
			return Response.status(Response.Status.OK)
					   .entity(userId).build();
	}
	
	
	
	
	/*
	@GET
	@Path("/fetch")
	@Produces(MediaType.APPLICATION_JSON)
	public User fetch(@QueryParam("id") String id)
	{
		Datastore ds;
		ds = Morphiacxn.getInstance().getMORPHIADB("test");

		ObjectId a = new ObjectId(id);
		Query<User> query = ds.createQuery(User.class).field("id").equal(a);
		User user = query.get();
		System.out.println(user);
		return user;
	}
	*/
	
	
	
	//in case of google users, collect company name and mobile in step2
	/*
	@PUT
	@Path("/updateGinfo")
	public Response updateGuser(TempUser temp) throws AppException
	{
		TempUser tempUser;
		RegisterDaoImpl regdao = new RegisterDaoImpl();
		tempUser = regdao.updateGuser(temp);
		return Response.status(Response.Status.CREATED)
					   .entity(tempUser)
					   .build();
	}
	
	@GET
	@Path("/confirm")
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmEmail(@QueryParam("email") String email, @QueryParam("verifyCode") String hexcode) throws AppException, Throwable
	{
		TempUser tempUser;
		RegisterDaoImpl regdao = new RegisterDaoImpl();
		System.out.println(email + "yo" + hexcode);
		tempUser = regdao.confirmEmail(email,hexcode);
		return Response.status(Response.Status.ACCEPTED)
				   .entity(tempUser)
				   .build();
		
	}
	
	
	@GET
	@Path("/sendOtp")
	public Response sendOtp(TempUser tempUser) throws AppException,Throwable
	{
		RegisterDaoImpl regdao = new RegisterDaoImpl();
		TempUser user;
		user = regdao.sendOtp(tempUser);
		return Response.status(Response.Status.ACCEPTED)
				   .entity(user)
				   .build();
		
	}
	
	//pass temp user object along with otp
	@GET
	@Path("/verifyOtp")
	public void confirmMobile(@QueryParam("user") String user, @QueryParam("otp") int otp) throws JsonParseException, JsonMappingException, IOException, Throwable
	{
		//user has no otp set
		ObjectMapper objectMapper = new ObjectMapper();
		TempUser temp = objectMapper.readValue(user, TempUser.class);
		RegisterDaoImpl regdao = new RegisterDaoImpl();
		//regdao.confirmMobile(temp, otp);
		
		
	}
*/
}
