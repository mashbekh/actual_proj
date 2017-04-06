package com.ErrorHandling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;

@Provider
public class JsonParserMapper implements ExceptionMapper<JsonParseException> {

	@Override
	public Response toResponse(JsonParseException ex) {
		
		return Response.status(400)
			       .entity("json parser exception" + ex.getMessage())
			       .build();
	}
	
	

}
