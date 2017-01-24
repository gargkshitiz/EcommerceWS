package com.rakuten.ecommerce.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.ecommerce.dao.entities.Category;
/**
 * @author Kshitiz Garg
 */
@Repository("CategoryDao")
@Retryable(maxAttempts=5,value=Exception.class,backoff = @Backoff(delay = 3000,multiplier=2))
public class CategoryDao {
	
	public static final String FETCH_CATEGORY_BY_ID = "fetchCategoryById";
	public static final String FETCH_CATEGORIES_BY_IDS = "fetchCategoriesByIds";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Category persist(Category category) {
		entityManager.persist(category);
		return category;
	}

	public Category getBy(long categoryId) {
		Query query = entityManager.createNamedQuery(FETCH_CATEGORY_BY_ID);
		try{
			return (Category)query.setParameter(Category.CATEGORY_ID, categoryId).getSingleResult();
		}
		catch(NoResultException e){
			/*Don't rethrow this exception, as that would be an 
			indicator to Spring-retry module and that will retry 
			unnecessarily*/
			return null;
		}
	}

	public List<Category> getBy(List<Long> categoryIds) {
		Query query = entityManager.createNamedQuery(FETCH_CATEGORIES_BY_IDS);
		try{
			return (List<Category>)query.setParameter(Category.CATEGORY_IDS, categoryIds).getResultList();
		}
		catch(NoResultException e){
			/*We can't rethrow this exception, as that would be an 
			indicator to Spring-retry module and that will retry 
			unnecessarily*/
			return null;
		}
	}

	@Transactional
	public void merge(Category category) {
		entityManager.merge(category);
	}
	
	@Transactional
	public void remove(Category category) {
		entityManager.remove(category);
	}

}