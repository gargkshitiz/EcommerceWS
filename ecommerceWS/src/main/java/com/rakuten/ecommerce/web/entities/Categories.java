package com.rakuten.ecommerce.web.entities;

import java.util.List;
/**
 * @author Kshitiz Garg
 */
public class Categories {
	
	private List<CategoryDetails> categories;

	public List<CategoryDetails> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDetails> categories) {
		this.categories = categories;
	}

}
