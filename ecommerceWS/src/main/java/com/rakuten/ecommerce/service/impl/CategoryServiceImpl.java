package com.rakuten.ecommerce.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rakuten.ecommerce.dao.CategoryDao;
import com.rakuten.ecommerce.dao.ProductCategoryDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryResponse;
import com.rakuten.ecommerce.web.entities.CategoryRequest;
/**
 * @author Kshitiz Garg
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public CategoryResponse getCategory(long categoryId) throws DataNotFoundException {
		logger.info("Fetching category by Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		CategoryResponse categoryResponse = new CategoryResponse();
		BeanUtils.copyProperties(category, categoryResponse);
		return categoryResponse;
	}

	@Override
	public long createCategory(CategoryRequest categoryRequest) {
		logger.info("Creating category with details: {}", categoryRequest);
		Category category = new Category();
		BeanUtils.copyProperties(categoryRequest, category);
		long currentTimeMillis = System.currentTimeMillis();
		category.setCreatedAt(new Timestamp(currentTimeMillis));
		category.setLastModifiedAt(new Timestamp(currentTimeMillis));
		return categoryDao.persist(category).getCategoryId();
	}
	
	@Override
	public void updateCategory(long categoryId, CategoryRequest categoryRequest) throws DataNotFoundException, InvalidClientRequestException {
		logger.info("Updating category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		long parentCategoryId = categoryRequest.getParentCategoryId();
		if(categoryId ==  parentCategoryId){
			throw new InvalidClientRequestException("parentCategoryId and categoryId can't be same");
		}
		if(parentCategoryId != -1){
			validateExistence(parentCategoryId);
		}
		BeanUtils.copyProperties(categoryRequest, category);
		category.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
		categoryDao.merge(category);
	}

	private Category validateExistence(long categoryId) throws DataNotFoundException {
		Category category = categoryDao.getBy(categoryId);
		if(category==null){
			throw new DataNotFoundException("No category exists against Id: "+ categoryId);
		}
		return category;
	}

	@Override
	public List<CategoryResponse> getCategories(List<Long> categoryIds) throws DataNotFoundException {
		logger.info("Fetching categories against Ids: {}", categoryIds);
		List<Category> categories = categoryDao.getBy(categoryIds);
		if(CollectionUtils.isEmpty(categories)){
			throw new DataNotFoundException("No categories exist against Ids: "+ categoryIds);
		}
		List<CategoryResponse> categoryResponses = new ArrayList<>();
		for(Category c: categories){
			CategoryResponse cfw = new CategoryResponse();
			BeanUtils.copyProperties(c, cfw);
			categoryResponses.add(cfw);
		}
		return categoryResponses;
	}

	@Transactional
	@Override
	public void deleteCategory(long categoryId) throws DataNotFoundException {
		logger.info("Deleting category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		categoryDao.remove(category);
		productCategoryDao.removeByCategory(categoryId);
	}

}