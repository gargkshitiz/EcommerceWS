package com.testorg.ecommerce.service.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
/**
 * @author Kshitiz Garg
 */
@Component
@Aspect
public class LogManager {

    private static final String DOTS = "..";
	private static final String DOT = ".";
	private static final String EMPTY = "";
	private static final String OP_BRACK = "(";
	private static final String COMMA = ",";
	private static final String MILLIS = " millis";
	private static final String ARROW = " -> ";
	private static final String CLO_BRACK = ")";
	private static final String PWS_WEB_SERVICES_PERF = "pws-perf";
	private final Logger logger = LoggerFactory.getLogger(PWS_WEB_SERVICES_PERF);

	/**
	 * Advice for perf logs
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("within(com.testorg.ecommerce.service..*)")
	public Object logExceutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object retVal = joinPoint.proceed();

		stopWatch.stop();

		StringBuffer logMessage = new StringBuffer();
		logMessage.append(joinPoint.getTarget().getClass().getSimpleName()).append(DOT).append(joinPoint.getSignature().getName()).append(OP_BRACK);
		Object[] args = joinPoint.getArgs();
		String prefix = EMPTY;
		for (int i = 0; i < args.length; i++) {
			logMessage.append(prefix);
			if(args[i]!=null && args[i].toString().length()>15){
				logMessage.append(args[i].toString().substring(0, 15)).append(DOTS);
			}
			else{
				logMessage.append(args[i]);
			}
			prefix = COMMA;
		}
		logMessage.append(CLO_BRACK).append(ARROW).append(stopWatch.getTotalTimeMillis()).append(MILLIS);
		getLogger().debug(logMessage.toString());
		return retVal;
	}

	// For UTCs
	Logger getLogger() {
		return logger;
	}

}