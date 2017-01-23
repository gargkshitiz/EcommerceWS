package com.rakuten.ecommerce.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
@Entity
@IdClass(ProductCategoryId.class)
@Table(name="ProductCategory")
public class ProductCategory {

	@Id
	@Column(name = Product.PRODUCT_ID_COL, unique = true, nullable = false)
	private long productId;
	
	@Id
	@Column(name = Category.CATEGORY_ID_COL, unique = true, nullable = false)
	private long categoryId;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

}