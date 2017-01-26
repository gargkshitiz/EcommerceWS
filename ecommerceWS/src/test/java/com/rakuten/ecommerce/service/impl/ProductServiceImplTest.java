package com.rakuten.ecommerce.service.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.rakuten.ecommerce.dao.ProductCategoryDao;
import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.entities.Price;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductRequest;


/**
 * @author Kshitiz Garg
 */
public class ProductServiceImplTest {

	private static final String Product_DESC = "test Product desc";
	private static final long PRODUCT_ID_1 = 1;
	
	@Mock
	private ProductDao productDao;
	
	@Mock
	private CurrencyConvertor currencyConvertor;
	
	@Mock
	private ProductCategoryDao productCategoryDao;
	
	@InjectMocks
	private ProductServiceImpl productServiceImpl;
	private ProductRequest productRequest;
	private Product product;
	private Price price;
	
	@Before
	public void setup() throws Exception{
		productRequest = new ProductRequest();
		product = new Product();
		price = new Price(new BigDecimal("100"), new BigDecimal("12"));
		product.setProductDesc(Product_DESC);
		product.setProductId(PRODUCT_ID_1);
		productRequest.setProductDesc(Product_DESC);
		MockitoAnnotations.initMocks(this);
		Mockito.when(productDao.getProduct(PRODUCT_ID_1)).thenReturn(product);
		Mockito.when(productCategoryDao.removeByProduct(PRODUCT_ID_1)).thenReturn(true);
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EURO)).thenReturn(price);
	}
	
	@Test
	public void updateProduct() throws Exception{
		Assert.assertTrue(productServiceImpl.updateProduct(PRODUCT_ID_1, productRequest));
	}

	@Test(expected=DataNotFoundException.class)
	public void updateProductWhenProductIdIsNotFound() throws Exception{
		Mockito.when(productDao.getProduct(PRODUCT_ID_1)).thenReturn(null);
		Assert.assertTrue(productServiceImpl.updateProduct(PRODUCT_ID_1, productRequest));
	}
	
	@Test
	public void deleteProduct() throws Exception{
		Assert.assertTrue(productServiceImpl.deleteProduct(PRODUCT_ID_1));
	}
	
	@Test(expected=DataNotFoundException.class)
	public void deleteProductWhenProductIdIsNotFound() throws Exception{
		Mockito.when(productDao.getProduct(PRODUCT_ID_1)).thenReturn(null);
		Assert.assertTrue(productServiceImpl.deleteProduct(PRODUCT_ID_1));
	}
	
}
