package com.testorg.ecommerce.service;

import com.testorg.ecommerce.service.exception.CurrencyNotSupportedException;
import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.service.exception.InvalidClientRequestException;
import com.testorg.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.testorg.ecommerce.web.entities.ProductResponse;
import com.testorg.ecommerce.web.entities.BulkProductResponse;
import com.testorg.ecommerce.web.entities.ProductRequest;

/**
 * @author Kshitiz Garg
 */
public interface ProductService {

	/**
	 * createProduct
	 * Creates product entity in the system using productRequest which also includes creation of category-product associations if categoryIds are 
	 * passed within productRequest
	 * Created product's id is returned
	 * @param ProductRequest
	 * @return long
	 * @throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException
	 */
	long createProduct(ProductRequest productRequest) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	/**
	 * getProductWithCategories
	 * Gets the product with an id equal to the product id as passed in the argument. It also includes the categories that this product belongs to.
	 * These categories are lazily loaded just before this method finishes
	 * price in both EURO and desiredCurrency would be present in the final output
	 * @param productId
	 * @param desiredCurrency
	 * @return ProductResponse
	 * @throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException
	 */
	ProductResponse getProductWithCategories(long productId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	/**
	 * deleteProduct
	 * Deletes the product with an id equal to the product id as passed in the argument 
	 * @param productId
	 * @return boolean
	 * @throws DataNotFoundException
	 */
	boolean deleteProduct(long productId) throws DataNotFoundException;

	/**
	 * updateProduct
	 * Updates the product with an id equal to the product id as passed in the argument using the passed productRequest.
	 * Passed categoryIds would be used to 'replace' the existing product-categories mappings for this product 
	 * @param productId
	 * @param productRequest
	 * @return boolean
	 * @throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException
	 */
	boolean updateProduct(long productId, ProductRequest productRequest) throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException;

	/**
	 * getProducts
	 * Results are fetched up to a pre-defined limit starting from startingProductId (inclusive). 
	 * If hasMore is true, then there would be as many items as the count attribute. Otherwise, there is no data available in the system.
	 * prices of the products in both EURO and desiredCurrency would be present in the final output. It also includes the categories that a particular product belongs to.
	 * These categories are lazily loaded just before this method finishes
	 * @param startingProductId
	 * @param desiredCurrency
	 * @return BulkProductResponse
	 * @throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException
	 */
	BulkProductResponse getProducts(long startingProductId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException, InvalidClientRequestException;

}