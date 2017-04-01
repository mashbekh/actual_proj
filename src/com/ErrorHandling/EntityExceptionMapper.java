package com.ErrorHandling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityExceptionMapper implements ExceptionMapper<EntityException>{

	@Override
	public Response toResponse(EntityException ex) {
		
	
		
		return Response.status(ex.getStatusCode())
			       .entity(ex.getEntity())
			       .type(MediaType.APPLICATION_JSON)
			       .build();
	}

}
