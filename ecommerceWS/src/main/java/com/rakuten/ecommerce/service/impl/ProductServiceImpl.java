package com.rakuten.ecommerce.service.impl;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.entities.Price;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.CategoryDetails;
import com.rakuten.ecommerce.web.entities.ProductDetails;
import com.rakuten.ecommerce.web.entities.Products;
/**
 * @author Kshitiz Garg
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CurrencyConvertor currencyConvertor;
	
	@Override
	public Product getProductWithCategories(long productId, String desiredCurrency) throws InvalidClientRequestException, DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		logger.info("Fetching product by Id: {}", productId);
		Product product = productDao.getProduct(productId);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), desiredCurrency);
		product.setPriceInEuro(price.getPriceInEuro());
		product.setPriceInDesiredCurrency(price.getPriceInDesiredCurrency());
		return product;
	}

	@Override
	public long createProduct(ProductDetails productDetails) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		Product product = getProductEntity(productDetails);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EUR);
		product.setProductCurrency(CurrencyConvertor.EUR);
		product.setPrice(price.getPriceInEuro().toString());
		long currentTimeMillis = System.currentTimeMillis();
		product.setCreatedAt(new Timestamp(currentTimeMillis));
		product.setLastModifiedAt(new Timestamp(currentTimeMillis));
		if(!CollectionUtils.isEmpty(product.getCatgeories())){
			for(Category c : product.getCatgeories()){
				c.setCreatedAt(new Timestamp(currentTimeMillis));
				c.setLastModifiedAt(new Timestamp(currentTimeMillis));
			}
		}
		return productDao.persist(product).getProductId();
	}

	private Product getProductEntity(ProductDetails productDetails) {
		Product product = new Product();
		BeanUtils.copyProperties(productDetails, product);
		Set<Category> categories = null;
		if(!CollectionUtils.isEmpty(productDetails.getCatgeories())){
			categories = new HashSet<>();
			for(CategoryDetails cd : productDetails.getCatgeories()){
				Category c = new Category();
				BeanUtils.copyProperties(cd, c);
				categories.add(c);
			}
		}
		product.setCatgeories(categories);
		return product;
	}

	@Override
	public void createProducts(Products products) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		for(ProductDetails p: products.getProducts()){
			createProduct(p);
		}
		// Batch error handling
	}

}