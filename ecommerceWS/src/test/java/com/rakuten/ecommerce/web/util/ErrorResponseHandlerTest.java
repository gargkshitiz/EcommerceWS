package com.rakuten.ecommerce.web.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
/**
 * @author Kshitiz Garg
 */
public class ErrorResponseHandlerTest {

	@Captor
	private ArgumentCaptor<HttpStatus> httpStatusCaptor;

	@Captor
	private ArgumentCaptor<RuntimeException> runtimeExceptionCaptor;
	
	@Captor
	private ArgumentCaptor<WebRequest> webRequestCaptor;
	
	@InjectMocks
	@Spy
	private ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();
	
	@Mock
	private RuntimeException runTimeException;
	
	@Mock
	private WebRequest webRequest;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		runTimeException = new IllegalArgumentException("err....");
	}

	@Test
	public void toResponse(){
		errorResponseHandler.toResponse(runTimeException, webRequest);
		Mockito.verify(errorResponseHandler).callSuper(runtimeExceptionCaptor.capture(), webRequestCaptor.capture(), httpStatusCaptor.capture());
		Assert.assertEquals(HttpStatus.BAD_REQUEST, httpStatusCaptor.getValue());
	}

}