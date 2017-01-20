package com.rakuten.ecommerce.web.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;

/**
 * @author Kshitiz Garg
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MDC.class)
public class RequestTrackingFilterTest {
	
	private static final String EMPTY = "";

	private static final String REQUEST_BODY = "{\"forumName\": \"sampleForum\",\"programWebsiteIdList\":[\"hr\"]}";

	@Spy
	private RequestTrackingFilter requestTrackingFilter = new RequestTrackingFilter();
	
	private static final String SAMPLE_STRING = "test";
	
	private static final InputStream requestStream = new ByteArrayInputStream(REQUEST_BODY.getBytes(StandardCharsets.UTF_8));
	
	@Captor
	private ArgumentCaptor<InputStream> is;
	
	@Mock
	private ContainerRequestContext requestContext;

	@Mock
	private ContainerResponseContext responseContext;

	@Before
	public void setup(){
		PowerMockito.mockStatic(MDC.class);
		Mockito.when(requestTrackingFilter.isTraceEnabled()).thenReturn(true);
		Mockito.when(requestContext.getEntityStream()).thenReturn(requestStream);
		Mockito.when(requestContext.getHeaderString(RequestTrackingFilter.REQUEST_TRACK_ID)).thenReturn(SAMPLE_STRING);
	}

	@Test
	public void filter() throws Exception{
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingFilter.REQUEST_TRACK_ID, SAMPLE_STRING);
		Mockito.verify(requestContext, Mockito.times(1)).setEntityStream(is.capture());
		Assert.assertEquals(REQUEST_BODY, getStringFromInputStream(is.getValue()));
	}
	
	@Test
	public void filterWhenTraceLogLevelIsDisabled() throws Exception{
		Mockito.when(requestTrackingFilter.isTraceEnabled()).thenReturn(false);
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingFilter.REQUEST_TRACK_ID, SAMPLE_STRING);
		Mockito.verify(requestContext, Mockito.times(0)).getEntityStream();
		Mockito.verify(requestContext, Mockito.times(0)).setEntityStream(is.capture());
	}
	
	@Test
	public void filterWhenRequestEntityStreamIsNull() throws Exception{
		Mockito.when(requestContext.getEntityStream()).thenReturn(null);
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingFilter.REQUEST_TRACK_ID, SAMPLE_STRING);
		Mockito.verify(requestContext, Mockito.times(0)).setEntityStream(is.capture());
	}
	
	@Test
	public void filterWhenRequestEntityStreamIsEmpty() throws Exception{
		Mockito.when(requestContext.getEntityStream()).thenReturn(new ByteArrayInputStream(EMPTY.getBytes(StandardCharsets.UTF_8)));
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingFilter.REQUEST_TRACK_ID, SAMPLE_STRING);
		Mockito.verify(requestContext, Mockito.times(1)).setEntityStream(is.capture());
		Assert.assertEquals(EMPTY, getStringFromInputStream(is.getValue()));
	}
	
	@Test
	public void filterWhenRequestEntityStreamFetchingThrowsException() throws Exception{
		Mockito.when(requestContext.getEntityStream()).thenThrow(new IllegalArgumentException("err..."));
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingFilter.REQUEST_TRACK_ID, SAMPLE_STRING);
		Mockito.verify(requestContext, Mockito.times(0)).setEntityStream(is.capture());
	}
	
	@Test
	public void filterWhenRequestTrackIdAndClientIdAreNotPresentInUpstream() throws Exception{
		Mockito.when(requestContext.getHeaderString(RequestTrackingFilter.REQUEST_TRACK_ID)).thenReturn(null);
		requestTrackingFilter.filter(requestContext);
		PowerMockito.verifyStatic();
		MDC.put(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void responseFilter() throws Exception{
		requestTrackingFilter.filter(requestContext, responseContext);
		PowerMockito.verifyStatic();
		MDC.remove(RequestTrackingFilter.REQUEST_TRACK_ID);
	}
	
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} 
		catch (IOException e) {
		} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		return sb.toString();

	}
	
}