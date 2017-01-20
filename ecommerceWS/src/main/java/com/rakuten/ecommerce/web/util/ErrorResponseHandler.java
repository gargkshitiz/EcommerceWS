package com.rakuten.ecommerce.web.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
/**
 * @author Kshitiz Garg
 */
@Provider
public class ErrorResponseHandler implements ExceptionMapper<Throwable>{

	private static final Logger logger = LoggerFactory.getLogger(ErrorResponseHandler.class);
	
	@Override
	public Response toResponse(Throwable error) {
	    Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	    if (error instanceof WebApplicationException) {
	        return ((WebApplicationException)error).getResponse();
	    } 
	    else if (error instanceof JsonProcessingException) {
	    	logger.error("Could not interpret incoming request", error);
	        return Response.status(Response.Status.BAD_REQUEST).entity(error.getMessage()).build();
	    } 
	    else{
	    	return response;
	    }
	}

}