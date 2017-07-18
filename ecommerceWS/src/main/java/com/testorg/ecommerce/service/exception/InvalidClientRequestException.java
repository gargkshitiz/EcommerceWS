package com.testorg.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public class InvalidClientRequestException extends ServiceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1330851943578521588L;

	public InvalidClientRequestException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.BAD_REQUEST;
	}

}