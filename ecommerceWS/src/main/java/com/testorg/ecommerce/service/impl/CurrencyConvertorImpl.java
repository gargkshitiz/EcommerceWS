package com.testorg.ecommerce.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.testorg.ecommerce.service.CurrencyConvertor;
import com.testorg.ecommerce.service.GenericRestClientJson;
import com.testorg.ecommerce.service.entities.Price;
import com.testorg.ecommerce.service.exception.CurrencyNotSupportedException;
import com.testorg.ecommerce.service.exception.ThirdPartyRequestFailedException;
@Service
/**
 * @author Kshitiz Garg
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

	@Value("${fixer-base-uri}")
	private String fixerBaseUri;
	
	@Autowired
	private GenericRestClientJson restClient;
	
	@Override
	public Price getPrice(String price, String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		if(StringUtils.isEmpty(fromCurrency)|| StringUtils.isEmpty(toCurrency)){
			throw new IllegalArgumentException("Currency symbol should not be null or empty");
		}
		final boolean fromCurrencyIsEuro = CurrencyConvertor.EURO.equalsIgnoreCase(fromCurrency);
		final boolean toCurrencyIsEuro = CurrencyConvertor.EURO.equalsIgnoreCase(toCurrency);
		if(fromCurrencyIsEuro && toCurrencyIsEuro){
			return new Price(new BigDecimal(price), new BigDecimal(price));
		}
		Map<Object, Object> rates = getRates(fromCurrency, toCurrency);
		if(fromCurrencyIsEuro){
			BigDecimal priceInEuro = new BigDecimal(price);
			return getPrice(rates, priceInEuro, toCurrency);
		}
		else if(toCurrencyIsEuro){
			BigDecimal priceInEuro = getPriceInEuro(rates, price, fromCurrency);
			return new Price(priceInEuro, priceInEuro);
		}
		else{
			BigDecimal priceInEuro = getPriceInEuro(rates, price, fromCurrency);
			return getPrice(rates, priceInEuro, toCurrency);
		}
	}

	private BigDecimal getPriceInEuro(final Map<Object, Object> rates, String price, String fromCurrency) {
		BigDecimal fromCurrencyExchangeRate =  BigDecimal.valueOf((double)rates.get(fromCurrency));
		return new BigDecimal(price).divide(fromCurrencyExchangeRate, 2, RoundingMode.HALF_UP);
	}

	private Price getPrice(final Map<Object, Object> rates, BigDecimal priceInEuro, String toCurrency) {
		BigDecimal toCurrencyExchangeRate =  BigDecimal.valueOf((double)rates.get(toCurrency));
		BigDecimal priceInToCurrency = priceInEuro.multiply(toCurrencyExchangeRate, MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
		return new Price(priceInEuro, priceInToCurrency);
	}

	private Map<Object, Object> getRates(String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		ResponseEntity<String> responseEntity = restClient.get(fixerBaseUri);
		Map<Object, Object> rates = (Map<Object, Object>)new Gson().fromJson(responseEntity.getBody(), Map.class).get(RATES);
		checkCurrencySupport(rates, fromCurrency);
		checkCurrencySupport(rates, toCurrency);
		return rates;
	}

	private void checkCurrencySupport(Map<Object, Object> rates, String currency) throws CurrencyNotSupportedException {
		if(!CurrencyConvertor.EURO.equalsIgnoreCase(currency) && !rates.keySet().contains(currency)){
			throw new CurrencyNotSupportedException(currency.concat(" is not supported by ").concat(fixerBaseUri));
		}
	}

	void setFixerBaseUri(String fixerBaseUri) {
		this.fixerBaseUri = fixerBaseUri;
	}

}