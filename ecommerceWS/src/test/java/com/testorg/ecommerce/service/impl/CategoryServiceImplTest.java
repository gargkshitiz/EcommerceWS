package com.testorg.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.testorg.ecommerce.dao.CategoryDao;
import com.testorg.ecommerce.dao.ProductCategoryDao;
import com.testorg.ecommerce.dao.entities.Category;
import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.service.exception.InvalidClientRequestException;
import com.testorg.ecommerce.web.entities.CategoryRequest;


/**
 * @author Kshitiz Garg
 */
public class CategoryServiceImplTest {

	private static final String CATEGORY_NAME = "testCategory";
	private static final String CATEGORY_DESC = "test category desc";
	private static final long CATEGORY_ID_1 = 1;
	private static final long CATEGORY_ID_2 = 2;
	
	@Mock
	private CategoryDao categoryDao;
	
	@Mock
	private ProductCategoryDao productCategoryDao;
	
	@InjectMocks
	private CategoryServiceImpl categoryServiceImpl;
	private CategoryRequest categoryRequest;
	private Category category;
	private Category category2;
	private List<Category> categories = new ArrayList<>();
	private static final int BULK_GET_LIMIT = 10;
	
	@Before
	public void setup(){
		categoryRequest = new CategoryRequest();
		category = new Category();
		category2 = new Category();
		category.setCategoryDesc(CATEGORY_DESC);
		category.setCategoryName(CATEGORY_NAME);
		category2.setCategoryId(CATEGORY_ID_2);
		category2.setCategoryDesc(CATEGORY_DESC);
		category2.setCategoryName(CATEGORY_NAME);
		categories.add(category);
		categories.add(category2);
		category.setCategoryId(CATEGORY_ID_1);
		categoryRequest.setCategoryDesc(CATEGORY_DESC);
		categoryRequest.setCategoryName(CATEGORY_NAME);
		categoryRequest.setParentCategoryId(-1);
		MockitoAnnotations.initMocks(this);
		categoryServiceImpl.setBulkGetResultsLimit(BULK_GET_LIMIT);
		Mockito.when(categoryDao.getBy(CATEGORY_ID_1)).thenReturn(category);
		Mockito.when(productCategoryDao.removeByCategory(CATEGORY_ID_1)).thenReturn(true);
		Mockito.when(categoryDao.getBetween(CATEGORY_ID_1, CATEGORY_ID_1+BULK_GET_LIMIT)).thenReturn(categories);
	}
	
	@Test
	public void getCategory() throws Exception{
		categoryServiceImpl.getCategory(CATEGORY_ID_1);
	}
	
	@Test(expected=DataNotFoundException.class)
	public void getCategoryWhenDataNotFound() throws Exception{
		Mockito.when(categoryDao.getBy(CATEGORY_ID_1)).thenReturn(null);
		categoryServiceImpl.getCategory(CATEGORY_ID_1);
	}
	
	@Test
	public void getCategories() throws Exception{
		categoryServiceImpl.getCategories(CATEGORY_ID_1);
	}
	
	@Test(expected=DataNotFoundException.class)
	public void getCategoriesWhenDataNotFound() throws Exception{
		Mockito.when(categoryDao.getBetween(CATEGORY_ID_1, CATEGORY_ID_1+BULK_GET_LIMIT)).thenReturn(null);
		categoryServiceImpl.getCategories(CATEGORY_ID_1);
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
