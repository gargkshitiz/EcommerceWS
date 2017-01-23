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

	CategoryForWeb get(long categoryId) throws InvalidClientRequestException, DataNotFoundException;

	long createCategory(CategoryFromWeb categoryDetails) throws InvalidClientRequestException;

	List<CategoryForWeb> get(List<Long> categoryIds) throws InvalidClientRequestException, DataNotFoundException;

	void updateCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws InvalidClientRequestException;

	void delete(long categoryId) throws DataNotFoundException;

}