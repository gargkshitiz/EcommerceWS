package com.testorg.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public class DataConflictException extends ServiceException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8371540354163112166L;


	public DataConflictException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.CONFLICT;
	}

}