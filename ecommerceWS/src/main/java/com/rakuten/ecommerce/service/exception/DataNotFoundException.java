package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author Kshitiz Garg
 */
public class DataNotFoundException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6127227305974284314L;
	private String message;
	
	public DataNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatusCode(){
		return HttpStatus.NOT_FOUND;
	}

}