package com.testorg.ecommerce.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.testorg.ecommerce.dao.entities.Category;
import com.testorg.ecommerce.dao.entities.Product;
import com.testorg.ecommerce.dao.entities.ProductCategory;

/**
 * @author Kshitiz Garg
 */
@Repository("ProductCategoryDao")
@Retryable(maxAttempts=5,value=Exception.class,backoff = @Backoff(delay = 2000,multiplier=2))
public class ProductCategoryDao {

	public static final String DELETE_PROD_CAT_MAPPING_BY_PROD_ID = "deleteProdCatMappingByProdId";
	public static final String DELETE_PROD_CAT_MAPPING_BY_CAT_ID = "deleteProdCatMappingByCatId";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void persist(ProductCategory pc) {
		entityManager.persist(pc);
	}

	@Transactional
	public boolean removeByProduct(long productId) {
		Query query = entityManager.createNamedQuery(DELETE_PROD_CAT_MAPPING_BY_PROD_ID);
		try{
			query.setParameter(Product.PRODUCT_ID, productId).executeUpdate();
		}
		catch(Exception e){
			return true;
		}
		return true;
	}

	@Transactional
	public boolean removeByCategory(long categoryId) {
		Query query = entityManager.createNamedQuery(DELETE_PROD_CAT_MAPPING_BY_CAT_ID);
		try{
			query.setParameter(Category.CATEGORY_ID, categoryId).executeUpdate();
		}
		catch(Exception e){
			return true;
		}
		return true;
	}

}