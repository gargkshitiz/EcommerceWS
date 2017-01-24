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

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Override
	public CategoryForWeb getCategory(long categoryId) throws DataNotFoundException {
		logger.info("Fetching category by Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		CategoryForWeb categoryForWeb = new CategoryForWeb();
		BeanUtils.copyProperties(category, categoryForWeb);
		return categoryForWeb;
	}

	@Override
	public long createCategory(CategoryFromWeb categoryFromWeb) {
		logger.info("Creating category with details: {}", categoryFromWeb);
		Category category = new Category();
		BeanUtils.copyProperties(categoryFromWeb, category);
		long currentTimeMillis = System.currentTimeMillis();
		category.setCreatedAt(new Timestamp(currentTimeMillis));
		category.setLastModifiedAt(new Timestamp(currentTimeMillis));
		return categoryDao.persist(category).getCategoryId();
	}
	
	@Override
	public void updateCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws DataNotFoundException {
		logger.info("Updating category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		long parentCategoryId = categoryFromWeb.getParentCategoryId();
		if(parentCategoryId != -1){
			validateExistence(parentCategoryId);
		}
		BeanUtils.copyProperties(categoryFromWeb, category);
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
	public List<CategoryForWeb> getCategories(List<Long> categoryIds) throws DataNotFoundException {
		logger.info("Fetching categories against Ids: {}", categoryIds);
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

	@Transactional
	@Override
	public void deleteCategory(long categoryId) throws DataNotFoundException {
		logger.info("Deleting category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		categoryDao.remove(category);
		productCategoryDao.removeByCategory(categoryId);
	}

	@Override
	public void patchCategory(long categoryId, CategoryFromWeb categoryFromWeb) throws DataNotFoundException {
		logger.info("Patching category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		// TODO Auto-generated method stub
	}

}