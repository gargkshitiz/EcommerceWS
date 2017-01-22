package com.rakuten.ecommerce.service.exception;

import org.springframework.http.HttpStatus;
/**
 * @author kgarg
 */
public class RestRequestFailedException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 652262094764676889L;
	private String message;
	private HttpStatus httpStatusCode;
	
	public RestRequestFailedException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}
	
	@Override
	public HttpStatus getHttpStatusCode(){
		return httpStatusCode;
	}

}