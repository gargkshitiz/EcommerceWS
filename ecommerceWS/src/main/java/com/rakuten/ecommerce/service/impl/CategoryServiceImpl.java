package com.rakuten.ecommerce.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rakuten.ecommerce.dao.CategoryDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryForWeb;
import com.rakuten.ecommerce.web.entities.CategoryFromWeb;
/**
 * @author Kshitiz Garg
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	public CategoryForWeb get(long categoryId) throws InvalidClientRequestException, DataNotFoundException {
		logger.info("Fetching category by Id: {}", categoryId);
		Category category = categoryDao.getBy(categoryId);
		if(category==null){
			throw new DataNotFoundException("No category exist against Id: "+ categoryId);
		}
		CategoryForWeb categoryForWeb = new CategoryForWeb();
		BeanUtils.copyProperties(category, categoryForWeb);
		return categoryForWeb;
	}

	@Override
	public long createCategory(CategoryFromWeb categoryFromWeb) throws InvalidClientRequestException {
		logger.info("Creating category with details: {}", categoryFromWeb);
		Category category = new Category();
		BeanUtils.copyProperties(categoryFromWeb, category);
		long currentTimeMillis = System.currentTimeMillis();
		category.setCreatedAt(new Timestamp(currentTimeMillis));
		category.setLastModifiedAt(new Timestamp(currentTimeMillis));
		return categoryDao.persist(category).getCategoryId();
	}
	
	@Override
	public void updateCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws InvalidClientRequestException {
		logger.info("Updating category with Id: {}", categoryId);
		Category category = new Category();
		BeanUtils.copyProperties(categoryFromWeb, category);
		category.setCategoryId(categoryId);
		category.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
		categoryDao.merge(category);
	}

	@Override
	public List<CategoryForWeb> get(List<Long> categoryIds) throws InvalidClientRequestException, DataNotFoundException {
		logger.info("Fetching categories by Ids: {}", categoryIds);
		List<Category> categories = categoryDao.getBy(categoryIds);
		if(CollectionUtils.isEmpty(categories)){
			throw new DataNotFoundException("No categories exist against Ids: "+ categoryIds);
		}
		List<CategoryForWeb> categoriesForWeb = new ArrayList<>();
		for(Category c: categories){
			CategoryForWeb cfw = new CategoryForWeb();
			BeanUtils.copyProperties(c, cfw);
			categoriesForWeb.add(cfw);
		}
		return categoriesForWeb;
	}

	@Override
	public void delete(long categoryId) throws DataNotFoundException {
		Category c = new Category();
		// TODO: delete all refs
	}

}