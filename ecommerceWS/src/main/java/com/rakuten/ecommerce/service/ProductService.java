package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductResponse;
import com.rakuten.ecommerce.web.entities.BulkProductResponse;
import com.rakuten.ecommerce.web.entities.ProductRequest;

/**
 * @author Kshitiz Garg
 */
public interface ProductService {

	long createProduct(ProductRequest productRequest) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	ProductResponse getProductWithCategories(long productId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	boolean deleteProduct(long productId) throws DataNotFoundException;

	boolean updateProduct(long productId, ProductRequest productRequest) throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	BulkProductResponse getProducts(long startingProductId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException, InvalidClientRequestException;

}