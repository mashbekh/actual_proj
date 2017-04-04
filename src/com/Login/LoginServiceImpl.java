package com.Login;

import java.io.IOException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.ErrorHandling.AppException;
import com.ErrorHandling.EntityException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Path("/login")
public class LoginServiceImpl {
	
	@POST
	@Path("/verify")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyUser(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("type") int type) throws  EntityException, JsonMappingException , JsonParseException, IOException, AppException, Exception, Throwable
	{
		LoginDaoImpl logindao = new LoginDaoImpl();
		String data = logindao.verifyUser(email, password, type);
		return Response.status(Response.Status.OK)
				   .entity(data).build();
		
	}
	

}
