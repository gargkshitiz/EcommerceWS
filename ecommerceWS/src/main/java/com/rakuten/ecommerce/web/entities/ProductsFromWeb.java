package com.rakuten.ecommerce.web.entities;

import java.util.List;
/**
 * @author Kshitiz Garg
 */
public class ProductsFromWeb {

	private List<ProductFromWeb> products;

	public List<ProductFromWeb> getProducts() {
		return products;
	}
	
	public void setProducts(List<ProductFromWeb> products) {
		this.products = products;
	}
	
}