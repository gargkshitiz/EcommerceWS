package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author kgarg
 */
public class ThirdPartyRequestFailedException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7659869076203386496L;
	private final HttpStatus httpStatusCode;
	
	public ThirdPartyRequestFailedException(String message, HttpStatus httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}
	
	@Override
	public HttpStatus getHttpStatusCode(){
		return httpStatusCode;
	}

}