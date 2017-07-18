package com.testorg.ecommerce.service.exception;

public class CurrencyNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -429003931395542482L;

	public CurrencyNotSupportedException(String msg) {
		super(msg);
	}

}
