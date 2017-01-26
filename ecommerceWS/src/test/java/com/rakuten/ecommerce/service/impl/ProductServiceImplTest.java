package com.rakuten.ecommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

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

	private static final int BULK_GET_LIMIT = 10;
	private static final String Product_DESC = "test Product desc";
	private static final long PRODUCT_ID_1 = 1;
	private static final String DESIRED_CURRENCY = "SGD";
	private static final long PRODUCT_ID_2 = 2;
	
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
	private List<Product> products = new ArrayList<>();
	private Product product2;
	
	@Before
	public void setup() throws Exception{
		productRequest = new ProductRequest();
		product = new Product();
		product2 = new Product();
		price = new Price(new BigDecimal("100"), new BigDecimal("12"));
		product.setProductDesc(Product_DESC);
		product.setProductId(PRODUCT_ID_1);
		product2.setProductDesc(Product_DESC);
		product2.setProductId(PRODUCT_ID_2);
		products.add(product);
		products.add(product2);
		productRequest.setProductDesc(Product_DESC);
		MockitoAnnotations.initMocks(this);
		productServiceImpl.setBulkGetResultsLimit(BULK_GET_LIMIT);
		Mockito.when(productDao.getProduct(PRODUCT_ID_1)).thenReturn(product);
		Mockito.when(productCategoryDao.removeByProduct(PRODUCT_ID_1)).thenReturn(true);
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EURO)).thenReturn(price);
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), DESIRED_CURRENCY)).thenReturn(price);
		Mockito.when(productDao.getBetween(PRODUCT_ID_1, PRODUCT_ID_1+BULK_GET_LIMIT)).thenReturn(products);
	}
	
	@Test
	public void getProduct() throws Exception{
		productServiceImpl.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY);
	}
	
	@Test(expected=ThirdPartyRequestFailedException.class)
	public void getProductWhenCurrencyConverterThrowsException() throws Exception{
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), DESIRED_CURRENCY)).thenThrow(new ThirdPartyRequestFailedException("err..", HttpStatus.INTERNAL_SERVER_ERROR));
		productServiceImpl.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY);
	}
	
	@Test(expected=CurrencyNotSupportedException.class)
	public void getProductWhenCurrencyConverterThrowsException2() throws Exception{
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), DESIRED_CURRENCY)).thenThrow(new CurrencyNotSupportedException("err.."));
		productServiceImpl.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY);
	}
	
	@Test
	public void getProducts() throws Exception{
		productServiceImpl.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY);
	}
	
	@Test(expected=ThirdPartyRequestFailedException.class)
	public void getProductsWhenCurrencyConverterThrowsException() throws Exception{
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), DESIRED_CURRENCY)).thenThrow(new ThirdPartyRequestFailedException("err..", HttpStatus.INTERNAL_SERVER_ERROR));
		productServiceImpl.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY);
	}
	
	@Test(expected=CurrencyNotSupportedException.class)
	public void getProductsWhenCurrencyConverterThrowsException2() throws Exception{
		Mockito.when(currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), DESIRED_CURRENCY)).thenThrow(new CurrencyNotSupportedException("err.."));
		productServiceImpl.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY);
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
