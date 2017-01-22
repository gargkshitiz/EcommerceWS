package com.rakuten.ecommerce.service.impl;

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
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.Price;
import com.rakuten.ecommerce.service.RestClient;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
@Service
/**
 * @author Kshitiz Garg
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

	@Value("${fixer-base-uri}")
	private String fixerBaseUri;
	
	@Autowired
	private RestClient restClient;
	
	@Override
	public String getBaseUri() {
		return fixerBaseUri;
	}
	
	@Override
	public Price getPrice(String price, String fromCurrency, String toCurrency) throws ThirdPartyRequestFailedException{
		StringBuilder fixerUrl = new StringBuilder(fixerBaseUri+RestClient.SYMBOLS_Q_PARAM);
		fixerUrl.append(fromCurrency);
		boolean desiredCurrencyIsEuro = StringUtils.isEmpty(toCurrency) || "EUR".equalsIgnoreCase(toCurrency);
		if(!desiredCurrencyIsEuro){
			fixerUrl.append(",").append(toCurrency);
		}
		ResponseEntity<String> responseEntity = restClient.get(fixerUrl.toString());
		Map<Object, Object> exchangeRates = (Map<Object, Object>)new Gson().fromJson(responseEntity.getBody(), Map.class).get(RestClient.RATES);
		BigDecimal productCurrencyExchangeRate =  BigDecimal.valueOf((double)exchangeRates.get(fromCurrency));
		BigDecimal priceInEuro = new BigDecimal(price).divide(productCurrencyExchangeRate, 2, RoundingMode.HALF_UP);
		BigDecimal priceInDesiredCurrency ;
		if(desiredCurrencyIsEuro){
			priceInDesiredCurrency = priceInEuro;
		}
		else{
			BigDecimal desiredCurrencyExchangeRate =  BigDecimal.valueOf((double)exchangeRates.get(toCurrency));
			priceInDesiredCurrency = priceInEuro.multiply(desiredCurrencyExchangeRate, MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
		}
		return new Price(priceInEuro, priceInDesiredCurrency);
	}
}
