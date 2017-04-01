package com.ErrorHandling;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IOExceptionMapper implements ExceptionMapper<IOException>{

	@Override
	public Response toResponse(IOException arg0) {
		
		return Response.status(400)
			       .entity("json parse-io exception")
			       .build();
	}
	

}
