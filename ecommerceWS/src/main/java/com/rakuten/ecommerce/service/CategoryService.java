package com.rakuten.ecommerce.service;

import java.util.List;

import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryForWeb;
import com.rakuten.ecommerce.web.entities.CategoryFromWeb;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {

	CategoryForWeb getCategory(long categoryId) throws DataNotFoundException;

	long createCategory(CategoryFromWeb categoryDetails) throws InvalidClientRequestException;

	List<CategoryForWeb> getCategories(List<Long> categoryIds) throws InvalidClientRequestException, DataNotFoundException;

	void updateCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws DataNotFoundException;

	void deleteCategory(long categoryId) throws DataNotFoundException;
	
	void patchCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws DataNotFoundException;

}