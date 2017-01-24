package com.rakuten.ecommerce.service.impl;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.rakuten.ecommerce.dao.CategoryDao;
import com.rakuten.ecommerce.dao.ProductCategoryDao;
import com.rakuten.ecommerce.dao.ProductDao;
import com.rakuten.ecommerce.dao.entities.Category;
import com.rakuten.ecommerce.dao.entities.Product;
import com.rakuten.ecommerce.dao.entities.ProductCategory;
import com.rakuten.ecommerce.service.CurrencyConvertor;
import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.entities.Price;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.CategoryResponse;
import com.rakuten.ecommerce.web.entities.ProductResponse;
import com.rakuten.ecommerce.web.entities.ProductRequest;
/**
 * @author Kshitiz Garg
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private CurrencyConvertor currencyConvertor;
	
	@Override
	@Transactional
	public ProductResponse getProductWithCategories(long productId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		logger.info("Fetching product by Id: {}", productId);
		Product product = validateExistence(productId);
		ProductResponse productResponse = new ProductResponse();
		BeanUtils.copyProperties(product, productResponse);
		Set<CategoryResponse> categoryResponses ;
		if(!CollectionUtils.isEmpty(product.getCatgeories())){
			categoryResponses = new HashSet<>();
			for(Category cd : product.getCatgeories()){
				CategoryResponse c = new CategoryResponse();
				BeanUtils.copyProperties(cd, c);
				categoryResponses.add(c);
			}
			productResponse.setCatgeories(categoryResponses);
		}
		if(StringUtils.isEmpty(desiredCurrency)){
			desiredCurrency = CurrencyConvertor.EURO;
		}
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), desiredCurrency);
		productResponse.setPriceInEuro(price.getPriceInEuro());
		productResponse.setPriceInDesiredCurrency(price.getPriceInDesiredCurrency());
		return productResponse;
	}

	@Transactional
	@Override
	public long createProduct(ProductRequest productRequest) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		long currentTimeMillis = System.currentTimeMillis();
		Product product = getProductEntity(productRequest);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EURO);
		product.setProductCurrency(CurrencyConvertor.EURO);
		product.setPrice(price.getPriceInEuro().toString());
		product.setCreatedAt(new Timestamp(currentTimeMillis));
		product.setLastModifiedAt(new Timestamp(currentTimeMillis));
		productDao.persist(product);
		List<String> categoryIds = productRequest.getCatgeoryIds();
		persistProductCategoryMappings(product, categoryIds);
		return product.getProductId();
	}

	private void persistProductCategoryMappings(Product product, List<String> categoryIds)	throws InvalidClientRequestException {
		if(!CollectionUtils.isEmpty(categoryIds)){
			List<Long> categoryIdsLong = categoryIds.parallelStream().map(c -> Long.valueOf(c)).collect(Collectors.toList());
			List<Category> categories = categoryDao.getBy(categoryIdsLong);
			if(categories.size()!=categoryIds.size()){
				throw new InvalidClientRequestException("CategoryIds should be unique and valid (if present)");
			}
			for(String categoryId: categoryIds){
				ProductCategory pc = new ProductCategory(product.getProductId(), Long.valueOf(categoryId));
				productCategoryDao.persist(pc);
			}
		}
	}

	private Product getProductEntity(ProductRequest productRequest) {
		Product product = new Product();
		BeanUtils.copyProperties(productRequest, product);
		return product;
	}

	@Transactional
	@Override
	public void deleteProduct(long productId) throws DataNotFoundException {
		logger.info("Deleting product with Id: {}", productId);
		Product product = validateExistence(productId);
		productDao.remove(product);
		productCategoryDao.removeByProduct(productId);
	}

	private Product validateExistence(long productId) throws DataNotFoundException {
		Product product = productDao.getProduct(productId);
		if(product==null){
			throw new DataNotFoundException("No product exists against Id: "+ productId);
		}
		return product;
	}

	@Transactional
	@Override
	public void updateProduct(long productId, ProductRequest productRequest) throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		logger.info("Updating product with Id: {}", productId);
		Product product = validateExistence(productId);
		BeanUtils.copyProperties(productRequest, product);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EURO);
		product.setProductCurrency(CurrencyConvertor.EURO);
		product.setPrice(price.getPriceInEuro().toString());
		product.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
		productDao.merge(product);
		List<String> categoryIds = productRequest.getCatgeoryIds();
		if(!CollectionUtils.isEmpty(categoryIds)){
			productCategoryDao.removeByProduct(productId);
		}
		persistProductCategoryMappings(product, categoryIds);
	}

}