package com.rakuten.ecommerce.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.dao.entities.ProductCategory;
/**
 * @author Kshitiz Garg
 */
public class ProductCategoryDaoTest {

	private static final long CATEGORY_ID_1 = 1;

	private static final long PRODUCT_ID_1 = 1;
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private Query namedQuery;
	
	@InjectMocks
	private ProductCategoryDao productCategoryDao;

	private ProductCategory productCategory = new ProductCategory();
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(entityManager.createNamedQuery(Mockito.anyString())).thenReturn(namedQuery);
		Mockito.when(namedQuery.setParameter(Mockito.anyString(), Mockito.anyString())).thenReturn(namedQuery);
	}
	
	@Test
	public void removeByCategory() {
		Assert.assertTrue(productCategoryDao.removeByCategory(CATEGORY_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_CAT_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_ID, CATEGORY_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).executeUpdate();
	}
	
	@Test
	public void removeByCategoryWhenException() {
		Mockito.when(namedQuery.executeUpdate()).thenThrow(new PersistenceException());
		Assert.assertTrue(productCategoryDao.removeByCategory(CATEGORY_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_CAT_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_ID, CATEGORY_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).executeUpdate();
	}
	
	@Test
	public void removeByProduct() {
		Assert.assertTrue(productCategoryDao.removeByProduct(PRODUCT_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_PROD_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.PRODUCT_ID, PRODUCT_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).executeUpdate();
	}

	@Test
	public void removeByProductWhenException() {
		Mockito.when(namedQuery.executeUpdate()).thenThrow(new PersistenceException());
		Assert.assertTrue(productCategoryDao.removeByProduct(PRODUCT_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_PROD_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.PRODUCT_ID, PRODUCT_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).executeUpdate();
	}
	
	@Test
	public void persist() {
		productCategoryDao.persist(productCategory );
		Mockito.verify(entityManager, Mockito.only()).persist(productCategory);
	}
	
}