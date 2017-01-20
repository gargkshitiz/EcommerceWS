package com.rakuten.ecommerce.service.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
import org.slf4j.Logger;

import com.rakuten.ecommerce.service.util.LogManager;
/**
 * @author Kshitiz Garg
 */
public class LogManagerTest {

	private static final String NULL = "null";

	private static final String SAMPLE_STRING = "test";

	private String[] args = {"arg1", "args2"};
	
	@Spy @InjectMocks
	private LogManager logManager = new LogManager();
	
	@Mock 
	private ProceedingJoinPoint joinPoint;
	
	@Mock 
	private Signature signature;

	@Mock 
	private Logger logger;

	@Captor
	private ArgumentCaptor<String> capturedLog;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(joinPoint.getTarget()).thenReturn(new LogManager());
		Mockito.when(joinPoint.getSignature()).thenReturn(signature);
		Mockito.when(signature.getName()).thenReturn(SAMPLE_STRING);
		Mockito.when(joinPoint.getArgs()).thenReturn(args);
		Mockito.when(logManager.getLogger()).thenReturn(logger);
	}
	
	@Test
	public void logExceutionTime() throws Throwable{
		logManager.logExceutionTime(joinPoint);
		Mockito.verify(logger, Mockito.times(1)).debug(capturedLog.capture());
		Assert.assertTrue(capturedLog.getValue().contains(SAMPLE_STRING));
		Assert.assertTrue(capturedLog.getValue().contains(LogManager.class.getSimpleName()));
		Assert.assertTrue(capturedLog.getValue().contains(args[0]));
		Assert.assertTrue(capturedLog.getValue().contains(args[1]));
	}

	@Test
	public void logExceutionTimeWhenAnArgsIsVeryLong() throws Throwable{
		whenAnArgIsVeryLong();
		logManager.logExceutionTime(joinPoint);
		Mockito.verify(logger, Mockito.times(1)).debug(capturedLog.capture());
		Assert.assertTrue(capturedLog.getValue().contains(SAMPLE_STRING));
		Assert.assertTrue(capturedLog.getValue().contains(LogManager.class.getSimpleName()));
		Assert.assertTrue(capturedLog.getValue().contains(args[0]));
		Assert.assertTrue(capturedLog.getValue().contains(args[1].substring(0,10)));
	}
	
	@Test
	public void logExceutionTimeWhenAnArgsIsNull() throws Throwable{
		whenAnArgIsNull();
		logManager.logExceutionTime(joinPoint);
		Mockito.verify(logger, Mockito.times(1)).debug(capturedLog.capture());
		Assert.assertTrue(capturedLog.getValue().contains(SAMPLE_STRING));
		Assert.assertTrue(capturedLog.getValue().contains(LogManager.class.getSimpleName()));
		Assert.assertTrue(capturedLog.getValue().contains(args[0]));
		Assert.assertTrue(capturedLog.getValue().contains(NULL));
	}

	private void whenAnArgIsNull() {
		args[1] = null;
	}
	
	private void whenAnArgIsVeryLong() {
		args[1] = "args111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
	}
}