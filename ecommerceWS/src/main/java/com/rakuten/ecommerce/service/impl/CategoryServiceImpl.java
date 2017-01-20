package com.rakuten.ecommerce.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakuten.ecommerce.dao.CategoryDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
/**
 * @author Kshitiz Garg
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	public Category get(long categoryId) throws InvalidClientRequestException, DataNotFoundException {
		logger.info("Fetching category by Id: {}", categoryId);
		return categoryDao.getBy(categoryId);
	}

}