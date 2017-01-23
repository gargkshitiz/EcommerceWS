package com.rakuten.ecommerce.dao.entities;

import java.io.Serializable;

public class ProductCategoryId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5718529113866172344L;

	private long productId;
	
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
