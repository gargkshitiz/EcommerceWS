package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public class DataConflictException extends ServiceException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8249485655352089962L;
	private String message;

	public DataConflictException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.CONFLICT;
	}

}