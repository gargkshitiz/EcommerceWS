package com.testorg.ecommerce.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.testorg.ecommerce.dao.ProductCategoryDao;
@Entity
@NamedQueries({
	@NamedQuery(name=ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_PROD_ID, query="DELETE FROM ProductCategory p where p.productId = :" + Product.PRODUCT_ID),
	@NamedQuery(name=ProductCategoryDao.DELETE_PROD_CAT_MAPPING_BY_CAT_ID, query="DELETE FROM ProductCategory p where p.categoryId = :" + Category.CATEGORY_ID)
})
@IdClass(ProductCategoryId.class)
@Table(name="ProductCategory")
public class ProductCategory {

	@Id
	@Column(name = Product.PRODUCT_ID_COL, unique = true, nullable = false)
	private long productId;
	
	@Id
	@Column(name = Category.CATEGORY_ID_COL, unique = true, nullable = false)
	private long categoryId;


	public ProductCategory() {
		// For JPA
	}

	
	public ProductCategory(long productId, long categoryId) {
		this.productId = productId;
		this.categoryId = categoryId;
	}

	public long getProductId() {
		return productId;
	}

	public long getCategoryId() {
		return categoryId;
	}

}