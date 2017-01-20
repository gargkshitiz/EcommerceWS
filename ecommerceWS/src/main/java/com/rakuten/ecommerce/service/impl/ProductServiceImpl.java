package com.rakuten.ecommerce.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.RestClient;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.RequestFailedException;
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
	public Product get(long productId) throws InvalidClientRequestException, DataNotFoundException {
		logger.info("Fetching product by Id: {}", productId);
		return productDao.getBy(productId);
	}

	@Override
	public void persist(ProductDetails productDetails) throws InvalidClientRequestException{
		try {
			ResponseEntity<String> responseEntity = restClient.get("/latest");
			logger.info(responseEntity.getBody());
			return;
		} 
		catch (RequestFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void persist(Products products) {
		// TODO Auto-generated method stub
		
	}

}