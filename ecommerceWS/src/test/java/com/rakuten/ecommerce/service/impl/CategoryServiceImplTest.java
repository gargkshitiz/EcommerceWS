package com.rakuten.ecommerce.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.rakuten.ecommerce.dao.CategoryDao;
import com.rakuten.ecommerce.dao.ProductCategoryDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryRequest;


/**
 * @author Kshitiz Garg
 */
public class CategoryServiceImplTest {

	private static final String CATEGORY_NAME = "testCategory";
	private static final String CATEGORY_DESC = "test category desc";
	private static final long CATEGORY_ID_1 = 1;
	
	@Mock
	private CategoryDao categoryDao;
	
	@Mock
	private ProductCategoryDao productCategoryDao;
	
	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;
	private CategoryRequest categoryRequest;
	private Category category;
	
	@Before
	public void setup(){
		categoryRequest = new CategoryRequest();
		category = new Category();
		category.setCategoryDesc(CATEGORY_DESC);
		category.setCategoryName(CATEGORY_NAME);
		category.setCategoryId(CATEGORY_ID_1);
		categoryRequest.setCategoryDesc(CATEGORY_DESC);
		categoryRequest.setCategoryName(CATEGORY_NAME);
		categoryRequest.setParentCategoryId(-1);
		MockitoAnnotations.initMocks(this);
		Mockito.when(categoryDao.getBy(CATEGORY_ID_1)).thenReturn(category);
		Mockito.when(productCategoryDao.removeByCategory(CATEGORY_ID_1)).thenReturn(true);
	}
	
	@Test
	public void updateCategory() throws DataNotFoundException, InvalidClientRequestException{
		Assert.assertTrue(categoryServiceImpl.updateCategory(CATEGORY_ID_1, categoryRequest));
	}
	
	@Test(expected=InvalidClientRequestException.class)
	public void updateCategoryWhenParentCategoryIdIsInvalid() throws DataNotFoundException, InvalidClientRequestException{
		categoryRequest.setParentCategoryId(-2);
		Assert.assertTrue(categoryServiceImpl.updateCategory(CATEGORY_ID_1, categoryRequest));
	}
	
	@Test(expected=InvalidClientRequestException.class)
	public void updateCategoryWhenParentCategoryIdIsInvalid2() throws DataNotFoundException, InvalidClientRequestException{
		categoryRequest.setParentCategoryId(CATEGORY_ID_1);
		Assert.assertTrue(categoryServiceImpl.updateCategory(CATEGORY_ID_1, categoryRequest));
	}
	
	@Test(expected=DataNotFoundException.class)
	public void updateCategoryWhenCategoryIdIsNotFound() throws DataNotFoundException, InvalidClientRequestException{
		Mockito.when(categoryDao.getBy(CATEGORY_ID_1)).thenReturn(null);
		Assert.assertTrue(categoryServiceImpl.updateCategory(CATEGORY_ID_1, categoryRequest));
	}
	
	@Test
	public void deleteCategory() throws DataNotFoundException, InvalidClientRequestException{
		Assert.assertTrue(categoryServiceImpl.deleteCategory(CATEGORY_ID_1));
	}
	
	@Test(expected=DataNotFoundException.class)
	public void deleteCategoryWhenCategoryIdIsNotFound() throws DataNotFoundException, InvalidClientRequestException{
		Mockito.when(categoryDao.getBy(CATEGORY_ID_1)).thenReturn(null);
		Assert.assertTrue(categoryServiceImpl.deleteCategory(CATEGORY_ID_1));
	}
	
}
