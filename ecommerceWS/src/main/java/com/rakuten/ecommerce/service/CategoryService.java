package com.rakuten.ecommerce.service;

import java.util.List;

import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryResponse;
import com.rakuten.ecommerce.web.entities.CategoryRequest;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {

	CategoryResponse getCategory(long categoryId) throws DataNotFoundException;

	long createCategory(CategoryRequest categoryRequest);

	void updateCategory(long categoryId, CategoryRequest categoryRequest) throws DataNotFoundException, InvalidClientRequestException;

	void deleteCategory(long categoryId) throws DataNotFoundException;
	
	List<CategoryResponse> getCategories(List<Long> categoryIds) throws DataNotFoundException;

}