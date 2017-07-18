package com.testorg.ecommerce.service;

import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.service.exception.InvalidClientRequestException;
import com.testorg.ecommerce.web.entities.BulkCategoryResponse;
import com.testorg.ecommerce.web.entities.CategoryRequest;
import com.testorg.ecommerce.web.entities.CategoryResponse;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {
	
	/**
	 * createCategory
	 * Creates category entity in the system using productRequest. Created category's id is returned
	 * @param CategoryRequest
	 * @return long
	 */
	long createCategory(CategoryRequest categoryRequest);

	/**
	 * getCategory
	 * Gets the category with an id equal to the category id as passed in the argument. 
	 * @param categoryId
	 * @return CategoryResponse
	 * @throws DataNotFoundException
	 */
	CategoryResponse getCategory(long categoryId) throws DataNotFoundException;

	/**
	 * updateCategory
	 * Updates the category entity with an id equal to the category id as passed in the argument using the passed categoryRequest. 
	 * @param categoryId
	 * @param CategoryRequest
	 * @return boolean
	 * @throws DataNotFoundException, InvalidClientRequestException
	 */
	boolean updateCategory(long categoryId, CategoryRequest categoryRequest) throws DataNotFoundException, InvalidClientRequestException;

	/**
	 * deleteCategory
	 * Deletes the category entity with an id equal to the category id as passed in the argument. 
	 * @param categoryId
	 * @return boolean
	 * @throws DataNotFoundException
	 */
	boolean deleteCategory(long categoryId) throws DataNotFoundException;
	
	/**
	 * getCategories
	 * Results are fetched up to a pre-defined limit starting from startingCategoryId (inclusive). 
	 * If hasMore is true, then there would be as many items as the count attribute. Otherwise, there is no data available in the system.
	 * @param startingCategoryId
	 * @return BulkCategoryResponse
	 * @throws DataNotFoundException, InvalidClientRequestException
	 */
	BulkCategoryResponse getCategories(long startingCategoryId) throws DataNotFoundException, InvalidClientRequestException;

}