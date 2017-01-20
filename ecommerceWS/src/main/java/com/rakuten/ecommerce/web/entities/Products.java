package com.rakuten.ecommerce.web.entities;

import java.util.List;
/**
 * @author Kshitiz Garg
 */
public class Products {

	private List<ProductDetails> products;

	public List<ProductDetails> getProducts() {
		return products;
	}
	
	public void setProducts(List<ProductDetails> products) {
		this.products = products;
	}
	
}