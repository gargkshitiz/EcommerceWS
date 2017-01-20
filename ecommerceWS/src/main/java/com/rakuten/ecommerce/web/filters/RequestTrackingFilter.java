package com.rakuten.ecommerce.web.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.ReaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author Kshitiz Garg
 */
@Provider
public class RequestTrackingFilter implements ContainerRequestFilter, ContainerResponseFilter{
	
	public static final String REQUEST_TRACK_ID = "requestTrackId";
	
	private static final Logger logger = LoggerFactory.getLogger(RequestTrackingFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext)	throws IOException {
		String requestTrackId = requestContext.getHeaderString(REQUEST_TRACK_ID); 
		if(requestTrackId == null){
			requestTrackId = UUID.randomUUID().toString();
			MDC.put(REQUEST_TRACK_ID, requestTrackId);
			logger.trace("Generated fresh requestTrackId: [{}] as it was not sent from upstream system", requestTrackId);
		}
		else{
			MDC.put(REQUEST_TRACK_ID, requestTrackId);
		}
		try{
			traceIncomingRequest(requestContext);
		}
		catch(Exception e){
			logger.trace("Even though trace log level is enabled, incoming HTTP request could not be logged", e);
		}
	}

	private void traceIncomingRequest(ContainerRequestContext requestContext) {
		if(isTraceEnabled()){
	        String entityBody = getEntityBody(requestContext);
	        if(!entityBody.isEmpty()){
	        	logger.trace("Incoming HTTP request body.....\n{}", entityBody);
	        }
		}
	}

	private String getEntityBody(ContainerRequestContext requestContext) {
		InputStream in = requestContext.getEntityStream();
		if(in==null){
			return "";
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final StringBuilder b = new StringBuilder();
		try {
			ReaderWriter.writeTo(in, out);
			byte[] requestEntity = out.toByteArray();
			if (requestEntity.length == 0) {
				b.append("").append("\n");
			} 
			else {
				b.append(new String(requestEntity)).append("\n");
			}
			requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
		} 
		catch (IOException ex) {
			logger.trace("Even though trace log level is enabled, incoming HTTP request could not be logged", ex);
		}
		return b.toString();
	}

	boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		MDC.remove(REQUEST_TRACK_ID);
	}

}