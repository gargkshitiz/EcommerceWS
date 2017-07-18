package com.testorg.ecommerce.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
@RunWith(PowerMockRunner.class)
@PrepareForTest(MDC.class)
/**
 * @author Kshitiz Garg
 */
public class RequestTrackingInterceptorTest {
	
	@Mock
	private HttpServletRequest httpServletRequest;
	
	@Mock
	private HttpServletResponse httpServletResponse;

	@InjectMocks
	private RequestTrackingInterceptor requestTrackingInterceptor = new RequestTrackingInterceptor();
	
	private static final String SAMPLE_STRING = "test";
	
	@Before
	public void setup(){
		PowerMockito.mockStatic(MDC.class);
		Mockito.when(httpServletRequest.getHeader(RequestTrackingInterceptor.REQUEST_TRACK_ID)).thenReturn(SAMPLE_STRING);
	}

	@Test
	public void preHandle() throws Exception{
		requestTrackingInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		PowerMockito.verifyStatic();
		MDC.put(RequestTrackingInterceptor.REQUEST_TRACK_ID, SAMPLE_STRING);
	}
	
	@Test
	public void preHandleWhenRequestTrackIdIsNotPresentInUpstream() throws Exception{
		Mockito.when(httpServletRequest.getHeader(RequestTrackingInterceptor.REQUEST_TRACK_ID)).thenReturn(null);
		requestTrackingInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());
		PowerMockito.verifyStatic();
		MDC.put(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void postHandle() throws Exception{
		requestTrackingInterceptor.postHandle(httpServletRequest, httpServletResponse, new Object(), new ModelAndView());
		PowerMockito.verifyStatic();
		MDC.remove(RequestTrackingInterceptor.REQUEST_TRACK_ID);
	}
	
}