package com.testorg.ecommerce.service.entities;

import java.math.BigDecimal;
/**
 * @author Kshitiz Garg
 */
public class Price {
	
	private BigDecimal priceInDesiredCurrency;
	private BigDecimal priceInEuro;
	
	public Price(BigDecimal priceInEuro, BigDecimal priceInDesiredCurrency) {
		this.priceInEuro = priceInEuro;
		this.priceInDesiredCurrency = priceInDesiredCurrency;
	}
	
	public BigDecimal getPriceInDesiredCurrency() {
		return priceInDesiredCurrency;
	}

	public BigDecimal getPriceInEuro() {
		return priceInEuro;
	}

}