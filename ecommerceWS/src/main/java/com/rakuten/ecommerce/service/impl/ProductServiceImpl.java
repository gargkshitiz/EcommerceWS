package com.rakuten.ecommerce.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.RestClient;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.RestRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductDetails;
import com.rakuten.ecommerce.web.entities.Products;
/**
 * @author Kshitiz Garg
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private RestClient restClient;

	@Override
	public Product get(long productId, String desiredCurrency) throws InvalidClientRequestException, DataNotFoundException, RestRequestFailedException {
		logger.info("Fetching product by Id: {}", productId);
		Product product = dummyProduct();
		//Product product = productDao.getBy(productId);
		String productCurrency = product.getProductCurrency();
		StringBuilder fixerUrl = new StringBuilder(restClient.getFixerBaseUri()+RestClient.SYMBOLS_Q_PARAM);
		fixerUrl.append(productCurrency);
		boolean desiredCurrencyOtherThanEuro = !StringUtils.isEmpty(desiredCurrency);
		if(desiredCurrencyOtherThanEuro){
			fixerUrl.append(",").append(desiredCurrency);
		}
		ResponseEntity<String> responseEntity = restClient.get(fixerUrl.toString());
		Map<Object, Object> exchangeRates = (Map<Object, Object>)new Gson().fromJson(responseEntity.getBody(), Map.class).get(RestClient.RATES);
		BigDecimal productCurrencyExchangeRate =  BigDecimal.valueOf((double)exchangeRates.get(productCurrency));
		BigDecimal priceValInDesiredCurrency = new BigDecimal(product.getPriceVal()).divide(productCurrencyExchangeRate, 2, RoundingMode.HALF_UP);
		if(desiredCurrencyOtherThanEuro){
			BigDecimal desiredCurrencyExchangeRate =  BigDecimal.valueOf((double)exchangeRates.get(desiredCurrency));
			priceValInDesiredCurrency = priceValInDesiredCurrency.multiply(desiredCurrencyExchangeRate, MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
		}
		product.setPriceValInDesiredCurrency(priceValInDesiredCurrency);
		return product;
	}

	private Product dummyProduct() {
		Product p = new Product();
		p.setPriceVal("100");
		p.setProductCurrency("INR");
		p.setProductId(10);
		p.setProductDesc("First product");
		return p;
	}

	@Override
	public void persist(ProductDetails productDetails) throws InvalidClientRequestException{
		// TODO
	}

	@Override
	public void persist(Products products) {
		// TODO Auto-generated method stub
		
	}

}