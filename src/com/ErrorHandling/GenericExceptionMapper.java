package com.ErrorHandling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable e) {
		
		System.out.println("hi" + e.getMessage());
		return Response.status(500)
				.entity(e.getMessage())
				.build();
		
	}
	
	

}
