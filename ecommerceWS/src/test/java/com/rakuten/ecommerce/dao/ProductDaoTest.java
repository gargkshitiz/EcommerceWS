package com.rakuten.ecommerce.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.rakuten.ecommerce.dao.entities.Product;
/**
 * @author Kshitiz Garg
 */
public class ProductDaoTest {

	private static final long PRODUCT_ID_1 = 1;

	private static final long PRODUCT_ID_2 = 2;

	private static final long START = 1;

	private static final long END = 8;
	
	private List<Product> productList = new ArrayList<>();
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private Query namedQuery;
	
	@InjectMocks
	private ProductDao productDao;
	
	List<Long> productIds = new ArrayList<>();

	private Product product1;

	private Product product2;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		productIds.add(PRODUCT_ID_1);
		productIds.add(PRODUCT_ID_2);
		product1 = new Product();
		product2 = new Product();
		productList.add(product1);
		productList.add(product2);
		Mockito.when(entityManager.createNamedQuery(Mockito.anyString())).thenReturn(namedQuery);
		Mockito.when(namedQuery.setParameter(Mockito.anyString(), Mockito.anyString())).thenReturn(namedQuery);
	}
	
	@Test
	public void get() {
		Mockito.when(namedQuery.getSingleResult()).thenReturn(product1);
		Product product = productDao.getProduct(PRODUCT_ID_1);
		Assert.assertEquals(product1, product);
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductDao.FETCH_PRODUCT_BY_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.PRODUCT_ID, PRODUCT_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).getSingleResult();
	}
	
	@Test
	public void getWhenException() {
		Mockito.when(namedQuery.getResultList()).thenThrow(new NoResultException("err..."));
		Assert.assertNull(productDao.getProduct(PRODUCT_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductDao.FETCH_PRODUCT_BY_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.PRODUCT_ID, PRODUCT_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).getSingleResult();
	}	

	@Test
	public void getBetween() {
		Mockito.when(namedQuery.getResultList()).thenReturn(productList);
		List<Product> productList = productDao.getBetween(START, END);
		Assert.assertEquals(product1, productList.get(0));
		Assert.assertEquals(product2, productList.get(1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductDao.FETCH_PRODUCTS_BETWEEN_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.START, START);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.END, END);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void getBetweenWhenException() {
		Mockito.when(namedQuery.getResultList()).thenThrow(new NoResultException("err..."));
		Assert.assertNull(productDao.getBetween(START, END));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(ProductDao.FETCH_PRODUCTS_BETWEEN_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.START, START);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Product.END, END);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void persist() {
		productDao.persist(product1);
		Mockito.verify(entityManager, Mockito.only()).persist(product1);
	}
	
	@Test
	public void merge() {
		productDao.merge(product1);
		Mockito.verify(entityManager, Mockito.only()).merge(product1);
	}
	
	@Test
	public void remove() {
		productDao.remove(product1);
		Mockito.verify(entityManager, Mockito.only()).remove(product1);
	}

}