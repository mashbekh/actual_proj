package com.ErrorHandling;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

	@Override
	public Response toResponse(AppException ex) {
		
		String msg  =ex.getDevMessage();
		
		if(ex.getExceptionMsg() != null)
			msg += ex.getExceptionMsg();
		if(ex.getTrace() != null)
			msg += ex.getTrace();
		
		
		return Response.status(ex.getstatusCode())
				       .entity( msg + ex.getStackTrace())
				       .build();
		
	}

}
