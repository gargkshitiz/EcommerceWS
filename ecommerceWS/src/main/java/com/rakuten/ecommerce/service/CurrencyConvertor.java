package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.service.entities.Price;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
/**
 * @author Kshitiz Garg
 */
public interface CurrencyConvertor {

	public String EURO = "EUR";

	Price getPrice(String price, String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException, CurrencyNotSupportedException;

}