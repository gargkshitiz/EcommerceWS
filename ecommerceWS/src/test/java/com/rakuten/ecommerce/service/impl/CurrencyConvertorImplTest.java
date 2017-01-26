package com.rakuten.ecommerce.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.GenericRestClientJson;
import com.rakuten.ecommerce.service.entities.Price;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
/**
 * @author Kshitiz Garg
 */
public class CurrencyConvertorImplTest {

	private static final String THOUSAND = "1000";

	private static final String FIXER_BASE_URI = "http://api.fixer.io/latest";

	private static final String FIXER_RESPONSE_JSON = "fixerResponse.json";

	private static final String INR = "INR";

	private static final String SGD = "SGD";
	
	@Mock
	private GenericRestClientJson genericRestClientJson;
	
	@Mock
	ResponseEntity<String> responseEntity;
	
	private String fixerResponseJson;
	
	@InjectMocks
	private CurrencyConvertorImpl convertorImpl;

	private Map<Object, Object> rates;
	
	@Before
	public void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		convertorImpl.setFixerBaseUri(FIXER_BASE_URI);
		fixerResponseJson = readFileFrom(FIXER_RESPONSE_JSON);
		rates = (Map<Object, Object>)new Gson().fromJson(fixerResponseJson, Map.class).get(CurrencyConvertor.RATES);
		Mockito.when(responseEntity.getBody()).thenReturn(fixerResponseJson);
		Mockito.when(genericRestClientJson.get(FIXER_BASE_URI)).thenReturn(responseEntity);
	}
	
	@Test
	public void getPrice() throws ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		final BigDecimal fromCurrencyExchangeRate =  BigDecimal.valueOf((double)rates.get(INR));
		final BigDecimal toCurrencyExchangeRate =  BigDecimal.valueOf((double)rates.get(SGD));
		final BigDecimal priceInEuro = new BigDecimal(THOUSAND).divide(fromCurrencyExchangeRate, 2, RoundingMode.HALF_UP);
		final BigDecimal priceInToCurrency = priceInEuro.multiply(toCurrencyExchangeRate, MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
		Price price = convertorImpl.getPrice(THOUSAND, INR, SGD);
		Assert.assertEquals(priceInEuro, price.getPriceInEuro());
		Assert.assertEquals(priceInToCurrency, price.getPriceInDesiredCurrency());
	}

	@Test
	public void getPriceWhenFromCurrencyIsEuro() throws ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		final BigDecimal toCurrencyExchangeRate =  BigDecimal.valueOf((double)rates.get(SGD));
		Price price = convertorImpl.getPrice(THOUSAND, CurrencyConvertor.EURO, SGD);
		final BigDecimal priceInToCurrency = new BigDecimal(THOUSAND).multiply(toCurrencyExchangeRate, MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
		Assert.assertEquals(THOUSAND, price.getPriceInEuro().toString());
		Assert.assertEquals(priceInToCurrency, price.getPriceInDesiredCurrency());
	}
	
	private String readFileFrom(String path) throws Exception{
		return new String(Files.readAllBytes(new File(Thread.currentThread().getContextClassLoader().getResource(path).toURI()).toPath()));
	}
	

}