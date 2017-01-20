package com.rakuten.ecommerce.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

import com.rakuten.ecommerce.dao.entities.Category;
/**
 * @author Kshitiz Garg
 */
@Repository("CategoryDao")
@Retryable(maxAttempts=5,value=Exception.class,backoff = @Backoff(delay = 3000,multiplier=2))
public class CategoryDao {
	
	public static final String FETCH_CATEGORY_BY_ID = "fetchCategoryById";

	@PersistenceContext
	private EntityManager entityManager;
	
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
			/*We can't rethrow this exception, as that would be an 
			indicator to Spring-retry module and that will retry 
			unnecessarily*/
			return null;
		}
	}

}