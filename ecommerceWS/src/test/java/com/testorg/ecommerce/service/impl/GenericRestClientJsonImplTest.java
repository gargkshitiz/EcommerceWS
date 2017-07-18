package com.testorg.ecommerce.service.impl;

import java.util.Arrays;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.testorg.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.testorg.ecommerce.service.impl.GenericRestClientJsonImpl;
/**
 * @author kgarg
 */
public class GenericRestClientJsonImplTest {
	
	private static final String RESOURCE_PATH = "http://ecommerce.com/API";
	
	private static final String REQUEST_BODY = "{\"Name\":\"request\"}";

	@InjectMocks
	@Spy
	private GenericRestClientJsonImpl genericRestClientJsonImpl;
	
	@Captor
	private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;
	
	@Captor
	private ArgumentCaptor<HttpMethod> httpMethodCaptor;
	
	@Captor
	private ArgumentCaptor<String> urlCaptor;
	
	@Mock
	private RestTemplate restTemplate;
	
	private ResponseEntity<String> response = null;

	@SuppressWarnings("unchecked")
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		genericRestClientJsonImpl.setContentTypeHeaders();
		response = new ResponseEntity<String>("Success", HttpStatus.OK);
		Mockito.when(restTemplate.exchange(urlCaptor.capture(), httpMethodCaptor.capture(), httpEntityCaptor.capture(), Mockito.any(Class.class))).thenReturn(response);
	}
	
	@Test
	public void verifyHttpHeaders() throws ThirdPartyRequestFailedException{
		genericRestClientJsonImpl.get(RESOURCE_PATH);
		Assert.assertEquals(MediaType.APPLICATION_JSON_UTF8, httpEntityCaptor.getValue().getHeaders().getContentType());
		Assert.assertEquals(Arrays.asList(MediaType.APPLICATION_JSON_UTF8), httpEntityCaptor.getValue().getHeaders().getAccept());
	}
	
	@Test
	public void get() throws ThirdPartyRequestFailedException{
		ResponseEntity<String> responseEntity = genericRestClientJsonImpl.get(RESOURCE_PATH);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(RESOURCE_PATH, urlCaptor.getValue());
		Assert.assertEquals(HttpMethod.GET, httpMethodCaptor.getValue());
	}
	
	@Test
	public void delete() throws ThirdPartyRequestFailedException{
		ResponseEntity<String> responseEntity = genericRestClientJsonImpl.delete(RESOURCE_PATH);
		Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(RESOURCE_PATH, urlCaptor.getValue());
		Assert.assertEquals(HttpMethod.DELETE, httpMethodCaptor.getValue());
	}
	
	@Test
	public void post() throws ThirdPartyRequestFailedException{
		ResponseEntity<String> responseEntity = genericRestClientJsonImpl.post(RESOURCE_PATH, REQUEST_BODY);
		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(RESOURCE_PATH, urlCaptor.getValue());
		Assert.assertEquals(HttpMethod.POST, httpMethodCaptor.getValue());
		Assert.assertEquals(REQUEST_BODY, httpEntityCaptor.getValue().getBody());
	}
	
	@Test
	public void put() throws ThirdPartyRequestFailedException{
		ResponseEntity<String> responseEntity = genericRestClientJsonImpl.put(RESOURCE_PATH, REQUEST_BODY);
		Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Assert.assertEquals(RESOURCE_PATH, urlCaptor.getValue());
		Assert.assertEquals(HttpMethod.PUT, httpMethodCaptor.getValue());
		Assert.assertEquals(REQUEST_BODY, httpEntityCaptor.getValue().getBody());
	}
	
	@Test
	public void getWhenServerSends400(){
		whenServerSends400();
		try {
			response = genericRestClientJsonImpl.get(RESOURCE_PATH);
		} 
		catch (ThirdPartyRequestFailedException e) {
			Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),  Mockito.any(Class.class));
			Assert.assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatusCode());
		}
	}
	
	@Test
	public void getWhenRestClientExceptionOccurs(){
		whenRestClientExceptionOccurs();
		try{
			genericRestClientJsonImpl.get(RESOURCE_PATH);
		}
		catch (ThirdPartyRequestFailedException e) {
			Mockito.verify(restTemplate, Mockito.times(3)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),  Mockito.any(Class.class));
		}
	}
	
	@Test
	public void getWhenGenericExceptionOccurs() {
		whenGenericExceptionOccurs();
		try{
			genericRestClientJsonImpl.get(RESOURCE_PATH);
		}
		catch (ThirdPartyRequestFailedException e) {
			Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),  Mockito.any(Class.class));
		}
	}
	
	@Test
	public void postWhenServerSends500AndRetriesAreNotEnabled(){
		whenServerSends500();
		try {
			response = genericRestClientJsonImpl.post(RESOURCE_PATH, REQUEST_BODY, false);
		} 
		catch (ThirdPartyRequestFailedException e) {
			Mockito.verify(restTemplate, Mockito.times(1)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),  Mockito.any(Class.class));
			Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}
	}
	
	@Test
	public void getWhenServerSends500(){
		whenServerSends500();
		try {
			response = genericRestClientJsonImpl.get(RESOURCE_PATH);
		} 
		catch (ThirdPartyRequestFailedException e) {
			Mockito.verify(restTemplate, Mockito.times(3)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class),  Mockito.any(Class.class));
			Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		}
	}

	@Test(expected=ThirdPartyRequestFailedException.class)
	public void getWhenThreadSleepThrowsInterruptedException() throws ThirdPartyRequestFailedException, InterruptedException{
		whenRestClientExceptionOccurs().whenThreadSleepThrowsInterruptedException();
		response = genericRestClientJsonImpl.get(RESOURCE_PATH);
	}
	
	private void whenThreadSleepThrowsInterruptedException() throws InterruptedException {
		Mockito.doThrow(new InterruptedException("err...")).when(genericRestClientJsonImpl).sleep();
	}

	@SuppressWarnings("unchecked")
	private void whenGenericExceptionOccurs() {
		Mockito.when(restTemplate.exchange(urlCaptor.capture(), httpMethodCaptor.capture(), httpEntityCaptor.capture(), Mockito.any(Class.class))).thenThrow(new IllegalArgumentException("err..."));
	}
	
	@SuppressWarnings("unchecked")
	private GenericRestClientJsonImplTest whenRestClientExceptionOccurs() {
		Mockito.when(restTemplate.exchange(urlCaptor.capture(), httpMethodCaptor.capture(), httpEntityCaptor.capture(), Mockito.any(Class.class))).thenThrow(new RestClientException("err..."));
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void whenServerSends500() {
		response = new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(restTemplate.exchange(urlCaptor.capture(), httpMethodCaptor.capture(), httpEntityCaptor.capture(), Mockito.any(Class.class))).thenReturn(response);
	}

	@SuppressWarnings("unchecked")
	private void whenServerSends400() {
		Mockito.when(restTemplate.exchange(urlCaptor.capture(), httpMethodCaptor.capture(), httpEntityCaptor.capture(), Mockito.any(Class.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "err..."));
	}

}