package com.rakuten.ecommerce.web.entities;

import java.util.Currency;

/**
 * @author Kshitiz Garg
 */
public class ProductDetails {

	private String productDesc;
	private String currencySymbol;
	
	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		Currency.getInstance(currencySymbol);
		this.currencySymbol = currencySymbol;
	}
	
}
