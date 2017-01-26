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

import com.rakuten.ecommerce.dao.entities.Category;
/**
 * @author Kshitiz Garg
 */
public class CategoryDaoTest {

	private static final long CATEGORY_ID_1 = 1;

	private static final long CATEGORY_ID_2 = 2;
	
	private List<Category> categoryList = new ArrayList<>();
	
	@Mock
	private EntityManager entityManager;
	
	@Mock
	private Query namedQuery;
	
	@InjectMocks
	private CategoryDao categoryDao;
	
	List<Long> categoryIds = new ArrayList<>();

	private Category cat1;

	private Category cat2;
	
	private static final long START = 1;

	private static final long END = 8;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		categoryIds.add(CATEGORY_ID_1);
		categoryIds.add(CATEGORY_ID_2);
		cat1 = new Category();
		cat2 = new Category();
		categoryList.add(cat1);
		categoryList.add(cat2);
		Mockito.when(entityManager.createNamedQuery(Mockito.anyString())).thenReturn(namedQuery);
		Mockito.when(namedQuery.setParameter(Mockito.anyString(), Mockito.anyString())).thenReturn(namedQuery);
	}
	
	@Test
	public void get() {
		Mockito.when(namedQuery.getSingleResult()).thenReturn(cat1);
		Category category = categoryDao.getBy(CATEGORY_ID_1);
		Assert.assertEquals(cat1, category);
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORY_BY_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_ID, CATEGORY_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).getSingleResult();
	}
	
	@Test
	public void getWhenException() {
		Mockito.when(namedQuery.getResultList()).thenThrow(new NoResultException("err..."));
		Assert.assertNull(categoryDao.getBy(CATEGORY_ID_1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORY_BY_ID);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_ID, CATEGORY_ID_1);
		Mockito.verify(namedQuery, Mockito.times(1)).getSingleResult();
	}	

	@Test
	public void getByList() {
		Mockito.when(namedQuery.getResultList()).thenReturn(categoryList);
		List<Category> categoryList = categoryDao.getBy(categoryIds);
		Assert.assertEquals(cat1, categoryList.get(0));
		Assert.assertEquals(cat2, categoryList.get(1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORIES_BY_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_IDS, categoryIds);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void getByListWhenException() {
		Mockito.when(namedQuery.getResultList()).thenThrow(new NoResultException("err..."));
		Assert.assertNull(categoryDao.getBy(categoryIds));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORIES_BY_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.CATEGORY_IDS, categoryIds);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void getBetween() {
		Mockito.when(namedQuery.getResultList()).thenReturn(categoryList);
		List<Category> categoryList = categoryDao.getBetween(START, END);
		Assert.assertEquals(cat1, categoryList.get(0));
		Assert.assertEquals(cat2, categoryList.get(1));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORIES_BETWEEN_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.START, START);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.END, END);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void getBetweenWhenException() {
		Mockito.when(namedQuery.getResultList()).thenThrow(new NoResultException("err..."));
		Assert.assertNull(categoryDao.getBetween(START, END));
		Mockito.verify(entityManager, Mockito.only()).createNamedQuery(CategoryDao.FETCH_CATEGORIES_BETWEEN_IDS);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.START, START);
		Mockito.verify(namedQuery, Mockito.times(1)).setParameter(Category.END, END);
		Mockito.verify(namedQuery, Mockito.times(1)).getResultList();
	}
	
	@Test
	public void persist() {
		categoryDao.persist(cat1);
		Mockito.verify(entityManager, Mockito.only()).persist(cat1);
	}
	
	@Test
	public void merge() {
		categoryDao.merge(cat1);
		Mockito.verify(entityManager, Mockito.only()).merge(cat1);
	}
	
	@Test
	public void remove() {
		categoryDao.remove(cat1);
		Mockito.verify(entityManager, Mockito.only()).remove(cat1);
	}

}