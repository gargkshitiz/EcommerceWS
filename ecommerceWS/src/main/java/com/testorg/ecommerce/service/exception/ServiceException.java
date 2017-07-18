package com.testorg.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public abstract class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2717826210867309345L;

	public ServiceException(String message) {
		super(message);
	}

	public abstract HttpStatus getHttpStatusCode() ;

}