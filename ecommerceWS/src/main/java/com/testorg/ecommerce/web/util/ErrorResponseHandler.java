package com.testorg.ecommerce.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
/**
 * @author Kshitiz Garg
 */
@ControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler{

	private static final Logger log = LoggerFactory.getLogger(ErrorResponseHandler.class);
	
	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<Object> toResponse(RuntimeException ex, WebRequest request) {
    	log.error("Could not interpret incoming request", request);
    	return callSuper(ex, request, HttpStatus.BAD_REQUEST);
	}

	ResponseEntity<Object> callSuper(RuntimeException ex, WebRequest request, HttpStatus httpStatus) {
		return handleExceptionInternal(ex, "", new HttpHeaders(), httpStatus, request);
	}

}