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

import com.rakuten.ecommerce.dao.entities.Product;
/**
 * @author Kshitiz Garg
 */
@Repository("ProductDao")
@Retryable(maxAttempts=5,value=Exception.class,backoff = @Backoff(delay = 3000,multiplier=2))
public class ProductDao {
	
	public static final String FETCH_PRODUCT_BY_ID = "fetchProductById";

	public static final String FETCH_PRODUCTS_BETWEEN_IDS = "fetchProductsBetweenIds";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public Product persist(Product product) {
		entityManager.persist(product);
		return product;
	}

	public Product getProduct(long productId) {
		Query query = entityManager.createNamedQuery(FETCH_PRODUCT_BY_ID);
		try{
			return (Product)query.setParameter(Product.PRODUCT_ID, productId).getSingleResult();
		}
		catch(NoResultException e){
			/*We can't rethrow this exception, as that would be an 
			indicator to Spring-retry module and that will retry 
			unnecessarily*/
			return null;
		}
	}

	@Transactional
	public void merge(Product product) {
		entityManager.merge(product);
	}

	@Transactional
	public void remove(Product product) {
		entityManager.remove(product);
	}

	public List<Product> getBetween(long start, long end) {
		Query query = entityManager.createNamedQuery(FETCH_PRODUCTS_BETWEEN_IDS);
		try{
			return (List<Product>)query.setParameter(Product.START, start).
					setParameter(Product.END, end).getResultList();
		}
		catch(NoResultException e){
			/*We can't rethrow this exception, as that would be an 
			indicator to Spring-retry module and that will retry 
			unnecessarily*/
			return null;
		}
	}
}