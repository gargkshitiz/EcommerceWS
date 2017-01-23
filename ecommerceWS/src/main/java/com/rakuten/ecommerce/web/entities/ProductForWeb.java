package com.rakuten.ecommerce.web.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public class ProductForWeb {
	
	private long productId;
	
	private String productDesc;
	
	private String productCurrency;
	
	private String price;
	
	private Set<CategoryForWeb> catgeories;
	
	private long unitsInStock;
	
	private String productCode;
	
	private String productType;
	
	private Timestamp lastModifiedAt;
	
	private Timestamp createdAt;

	private BigDecimal priceInDesiredCurrency;
	
	private BigDecimal priceInEuro;
	
	public BigDecimal getPriceInDesiredCurrency() {
		return priceInDesiredCurrency;
	}

	public void setPriceInDesiredCurrency(BigDecimal priceInDesiredCurrency) {
		this.priceInDesiredCurrency = priceInDesiredCurrency;
	}

	public BigDecimal getPriceInEuro() {
		return priceInEuro;
	}

	public void setPriceInEuro(BigDecimal priceInEuro) {
		this.priceInEuro = priceInEuro;
	}
	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

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
		this.productCurrency = productCurrency;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public long getUnitsInStock() {
		return unitsInStock;
	}

	public void setUnitsInStock(long unitsInStock) {
		this.unitsInStock = unitsInStock;
	}

	public Set<CategoryForWeb> getCatgeories() {
		return catgeories;
	}

	public void setCatgeories(Set<CategoryForWeb> catgeories) {
		this.catgeories = catgeories;
	}

	public Timestamp getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Timestamp lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

}
