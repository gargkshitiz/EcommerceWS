package com.rakuten.ecommerce.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.ecommerce.dao.entities.ProductCategory;

/**
 * @author Kshitiz Garg
 */
@Repository("ProductCategoryDao")
@Retryable(maxAttempts=5,value=Exception.class,backoff = @Backoff(delay = 3000,multiplier=2))
public class ProductCategoryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void persist(ProductCategory pc) {
		entityManager.persist(pc);
	}

}