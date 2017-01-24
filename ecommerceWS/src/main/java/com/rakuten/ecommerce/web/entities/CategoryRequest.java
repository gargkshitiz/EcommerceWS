package com.rakuten.ecommerce.web.entities;
/**
 * @author Kshitiz Garg
 */
public class CategoryRequest {
	
	private String categoryDesc;

	private String categoryName;
	
	// -1 parentCategoryId is the default which indicates a top most category (parent-less)
	private long parentCategoryId = -1;
	
	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	@Override
	public String toString() {
		return "CategoryFromWeb [categoryDesc=" + categoryDesc + ", categoryName=" + categoryName
				+ ", parentCategoryId=" + parentCategoryId + "]";
	}

}
