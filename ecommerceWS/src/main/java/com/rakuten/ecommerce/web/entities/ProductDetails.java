package com.rakuten.ecommerce.web.entities;

import java.util.Currency;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.rakuten.ecommerce.service.CurrencyConvertor;

import io.swagger.annotations.ApiModel;

/**
 * @author Kshitiz Garg
 */
@ApiModel
public class ProductDetails {

	private String productDesc;
	private String productCurrency;
	private String price;
    private Set<CategoryDetails> catgeories;
	private long unitsInStock;
    private String productCode;
    private String productType;
    
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductCurrency() {
		return productCurrency;
	}
	public void setProductCurrency(String productCurrency) {
		if(StringUtils.isEmpty(productCurrency)){
			this.productCurrency = CurrencyConvertor.EUR;
		}
		validate(productCurrency);
		this.productCurrency = productCurrency;
	}
	
	private void validate(String productCurrency) {
		Currency.getInstance(productCurrency);
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Set<CategoryDetails> getCatgeories() {
		return catgeories;
	}
	public void setCatgeories(Set<CategoryDetails> catgeories) {
		this.catgeories = catgeories;
	}
	public long getUnitsInStock() {
		return unitsInStock;
	}
	public void setUnitsInStock(long unitsInStock) {
		this.unitsInStock = unitsInStock;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}