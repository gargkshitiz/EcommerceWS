package com.rakuten.ecommerce.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.rakuten.ecommerce.web.entities.BulkCategoryResponse;
import com.rakuten.ecommerce.web.entities.CategoryRequest;
/**
 * @author Kshitiz Garg
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Value("${bulk-get-results-limit}")
	private long bulkGetResultsLimit;
	
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
	public boolean updateCategory(long categoryId, CategoryRequest categoryRequest) throws DataNotFoundException, InvalidClientRequestException {
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
		return true;
	}

	private Category validateExistence(long categoryId) throws DataNotFoundException {
		Category category = categoryDao.getBy(categoryId);
		if(category==null){
			throw new DataNotFoundException("No category exists against Id: "+ categoryId);
		}
		return category;
	}

	@Override
	public BulkCategoryResponse getCategories(long startingCategoryId) throws DataNotFoundException, InvalidClientRequestException {
		if(startingCategoryId<=0){
			throw new InvalidClientRequestException("startingCategoryId should be a positive integer");
		}
		logger.info("Fetching categories starting with Id: {}", startingCategoryId);
		List<Category> categories = categoryDao.getBetween(startingCategoryId, startingCategoryId+bulkGetResultsLimit);
		if(CollectionUtils.isEmpty(categories)){
			throw new DataNotFoundException("No categories exist until Id: {}"+ startingCategoryId);
		}
		List<CategoryResponse> categoryResponses = new ArrayList<>();
		int batchSize = 0;
		while(batchSize < categories.size() && batchSize < bulkGetResultsLimit){
			CategoryResponse cfw = new CategoryResponse();
			BeanUtils.copyProperties(categories.get(batchSize), cfw);
			categoryResponses.add(cfw);
			batchSize++;
		}
		BulkCategoryResponse bulkCategoryResponse = new BulkCategoryResponse();
		if(categories.size()>bulkGetResultsLimit){
			bulkCategoryResponse.setHasMore(true);
			bulkCategoryResponse.setNextCategoryId(startingCategoryId+bulkGetResultsLimit);
		}
		bulkCategoryResponse.setCount(categoryResponses.size());
		bulkCategoryResponse.setItems(categoryResponses);
		return bulkCategoryResponse;
	}

	@Transactional
	@Override
	public boolean deleteCategory(long categoryId) throws DataNotFoundException {
		logger.info("Deleting category with Id: {}", categoryId);
		Category category = validateExistence(categoryId);
		categoryDao.remove(category);
		return productCategoryDao.removeByCategory(categoryId);
	}

	public long getBulkGetResultsLimit() {
		return bulkGetResultsLimit;
	}

}