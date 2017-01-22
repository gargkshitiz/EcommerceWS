package com.rakuten.ecommerce.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.Price;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
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
	private CurrencyConvertor currencyConvertor;
	
	@Override
	public Product get(long productId, String desiredCurrency) throws InvalidClientRequestException, DataNotFoundException, ThirdPartyRequestFailedException {
		logger.info("Fetching product by Id: {}", productId);
		Product product = dummyProduct();
		//Product product = productDao.getBy(productId);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), desiredCurrency);
		product.setPriceInEuro(price.getPriceInEuro());
		product.setPriceInDesiredCurrency(price.getPriceInDesiredCurrency());
		return product;
	}

	private Product dummyProduct() {
		Product p = new Product();
		p.setPrice("100");
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