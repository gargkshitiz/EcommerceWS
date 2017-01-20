package com.rakuten.ecommerce.service;

import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;

/**
 * @author Kshitiz Garg
 */
public interface CategoryService {

	Category get(long categoryId) throws InvalidClientRequestException, DataNotFoundException;

}