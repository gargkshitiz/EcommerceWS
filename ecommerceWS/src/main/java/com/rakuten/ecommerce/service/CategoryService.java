package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.Categories;
import com.rakuten.ecommerce.web.entities.CategoryDetails;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {

	Category get(long categoryId) throws InvalidClientRequestException, DataNotFoundException;

	void persist(CategoryDetails categoryDetails) throws InvalidClientRequestException;

	void persist(Categories categories) throws InvalidClientRequestException;

}