package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductDetails;
import com.rakuten.ecommerce.web.entities.Products;

/**
 * @author Kshitiz Garg
 */
public interface ProductService {

	void persist(ProductDetails productDetails) throws InvalidClientRequestException;

	void persist(Products products) throws InvalidClientRequestException;

	Product get(long productId, String desiredCurrency) throws InvalidClientRequestException, DataNotFoundException, ThirdPartyRequestFailedException;

}