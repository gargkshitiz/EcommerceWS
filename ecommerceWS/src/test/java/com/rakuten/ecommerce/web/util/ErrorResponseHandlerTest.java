/*package com.rakuten.ecommerce.web.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
*//**
 * @author Kshitiz Garg
 *//*
public class ErrorResponseHandlerTest {

	private static final String BAD_REQUEST_MSG = "Bad request";

	@InjectMocks
	private ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();
	
	@Mock
	private Throwable throwable;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		throwable = new IllegalArgumentException("err....");
	}

	@Test
	public void toResponse(){
		Response response = errorResponseHandler.toResponse(throwable);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
	}

	@Test
	public void toResponseWhenRequestIsBad(){
		whenRequestIsBad();
		Response response = errorResponseHandler.toResponse(throwable);
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	public void toResponseWhenWebApplicationExceptionOccurs() throws Exception{
		whenWebApplicationExceptionOccurs();
		Response response = errorResponseHandler.toResponse(throwable);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
	}
	
	@Test
	public void toResponseWhenWebApplicationExceptionOccursButRecoveryEmailFails() throws Exception{
		whenWebApplicationExceptionOccurs();
		Response response = errorResponseHandler.toResponse(throwable);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
	}

	private ErrorResponseHandlerTest whenWebApplicationExceptionOccurs() {
		throwable = new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		return this;
	}

	private void whenRequestIsBad() {
		throwable = new UnrecognizedPropertyException(BAD_REQUEST_MSG, null, null, null, null);
	}
	
}*/