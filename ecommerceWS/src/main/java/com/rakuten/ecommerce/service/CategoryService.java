package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.BulkCategoryResponse;
import com.rakuten.ecommerce.web.entities.CategoryRequest;
import com.rakuten.ecommerce.web.entities.CategoryResponse;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {

	CategoryResponse getCategory(long categoryId) throws DataNotFoundException;

	long createCategory(CategoryRequest categoryRequest);

	void updateCategory(long categoryId, CategoryRequest categoryRequest) throws DataNotFoundException, InvalidClientRequestException;

	void deleteCategory(long categoryId) throws DataNotFoundException;
	
	BulkCategoryResponse getCategories(long startingCategoryId)throws DataNotFoundException, InvalidClientRequestException;

}