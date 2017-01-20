package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public class InvalidClientRequestException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3358143584066412867L;
	private String message;
	
	public InvalidClientRequestException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.BAD_REQUEST;
	}

}