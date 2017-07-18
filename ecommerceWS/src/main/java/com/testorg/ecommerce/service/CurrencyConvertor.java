package com.testorg.ecommerce.service;

import com.testorg.ecommerce.service.entities.Price;
import com.testorg.ecommerce.service.exception.CurrencyNotSupportedException;
import com.testorg.ecommerce.service.exception.ThirdPartyRequestFailedException;
/**
 * @author Kshitiz Garg
 */
public interface CurrencyConvertor {

	String EURO = "EUR";
	
	String RATES = "rates";

	/**
	 * Creates and returns a Price entity containing the price in EURO and also in toCurrency. 
	 * @param price
	 * @param fromCurrency
	 * @param toCurrency
	 * @return Price entity containing price in EURO and price in toCurrency
	 * @throws ThirdPartyRequestFailedException
	 * @throws CurrencyNotSupportedException
	 */
	Price getPrice(String price, String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException, CurrencyNotSupportedException;

}