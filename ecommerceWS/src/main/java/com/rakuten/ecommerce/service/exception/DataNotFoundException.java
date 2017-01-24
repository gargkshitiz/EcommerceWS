package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Kshitiz Garg
 */
public class DataNotFoundException extends ServiceException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6220520325216132821L;

	public DataNotFoundException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.NOT_FOUND;
	}

}