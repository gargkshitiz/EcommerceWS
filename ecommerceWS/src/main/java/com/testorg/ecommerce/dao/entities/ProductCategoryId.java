package com.testorg.ecommerce.dao.entities;

import java.io.Serializable;
/**
 * @author Kshitiz Garg
 */
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (categoryId ^ (categoryId >>> 32));
		result = prime * result + (int) (productId ^ (productId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductCategoryId other = (ProductCategoryId) obj;
		if (categoryId != other.categoryId)
			return false;
		if (productId != other.productId)
			return false;
		return true;
	}

}