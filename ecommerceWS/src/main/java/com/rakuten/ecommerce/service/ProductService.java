package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.ProductDetails;

/**
 * @author Kshitiz Garg
 */
public interface ProductService {

	Product get(long productId) throws InvalidClientRequestException, DataNotFoundException;

	void persist(ProductDetails productDetails) throws InvalidClientRequestException;

}