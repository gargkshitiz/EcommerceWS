package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductFromWeb;
import com.rakuten.ecommerce.web.entities.ProductForWeb;
import com.rakuten.ecommerce.web.entities.ProductsFromWeb;

/**
 * @author Kshitiz Garg
 */
public interface ProductService {

	long createProduct(ProductFromWeb productDetails) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	void createProducts(ProductsFromWeb products) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	ProductForWeb getProductWithCategories(long productId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

}