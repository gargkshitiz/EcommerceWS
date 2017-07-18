package com.testorg.ecommerce.service.impl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.testorg.ecommerce.dao.CategoryDao;
import com.testorg.ecommerce.dao.ProductCategoryDao;
import com.testorg.ecommerce.dao.ProductDao;
import com.testorg.ecommerce.dao.entities.Category;
import com.testorg.ecommerce.dao.entities.Product;
import com.testorg.ecommerce.dao.entities.ProductCategory;
import com.testorg.ecommerce.service.CurrencyConvertor;
import com.testorg.ecommerce.service.ProductService;
import com.testorg.ecommerce.service.entities.Price;
import com.testorg.ecommerce.service.exception.CurrencyNotSupportedException;
import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.service.exception.InvalidClientRequestException;
import com.testorg.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.testorg.ecommerce.web.entities.BulkProductResponse;
import com.testorg.ecommerce.web.entities.CategoryResponse;
import com.testorg.ecommerce.web.entities.ProductResponse;
import com.testorg.ecommerce.web.entities.ProductRequest;
/**
 * @author Kshitiz Garg
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Value("${bulk-get-results-limit}")
	private long bulkGetResultsLimit;
	
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
		return prepareProductResponse(desiredCurrency, product);
	}

	private ProductResponse prepareProductResponse(String desiredCurrency, Product product)	throws ThirdPartyRequestFailedException, CurrencyNotSupportedException {
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
	public boolean deleteProduct(long productId) throws DataNotFoundException {
		logger.info("Deleting product with Id: {}", productId);
		Product product = validateExistence(productId);
		productDao.remove(product);
		return productCategoryDao.removeByProduct(productId);
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
	public boolean updateProduct(long productId, ProductRequest productRequest) throws DataNotFoundException, InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
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
		return true;
	}
	
	@Transactional
	@Override
	public BulkProductResponse getProducts(long startingProductId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException, InvalidClientRequestException {
		if(startingProductId<=0){
			throw new InvalidClientRequestException("startingProductId should be a positive integer");
		}
		logger.info("Fetching products starting with Id: {}", startingProductId);
		List<Product> products = productDao.getBetween(startingProductId, startingProductId+bulkGetResultsLimit);
		if(CollectionUtils.isEmpty(products)){
			throw new DataNotFoundException("No products exist until Id: {}"+ startingProductId);
		}
		List<ProductResponse> productResponses = new ArrayList<>();
		int batchSize = 0;
		while(batchSize < products.size() && batchSize < bulkGetResultsLimit){
			ProductResponse productResponse = prepareProductResponse(desiredCurrency, products.get(batchSize));
			productResponses.add(productResponse);
			batchSize++;
		}
		BulkProductResponse bulkProductResponse = new BulkProductResponse();
		if(products.size()>bulkGetResultsLimit){
			bulkProductResponse.setHasMore(true);
			bulkProductResponse.setNextProductId(startingProductId+bulkGetResultsLimit);
		}
		bulkProductResponse.setCount(productResponses.size());
		bulkProductResponse.setItems(productResponses);
		return bulkProductResponse;
	}

	void setBulkGetResultsLimit(long bulkGetResultsLimit) {
		this.bulkGetResultsLimit = bulkGetResultsLimit;
	}

}