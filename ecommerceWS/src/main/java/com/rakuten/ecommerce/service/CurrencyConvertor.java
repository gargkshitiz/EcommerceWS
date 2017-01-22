package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
/**
 * @author Kshitiz Garg
 */
public interface CurrencyConvertor {

	String getBaseUri();

	Price getPrice(String price, String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException;

}