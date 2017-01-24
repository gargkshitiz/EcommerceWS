package com.rakuten.ecommerce.service.impl;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.rakuten.ecommerce.web.entities.CategoryForWeb;
import com.rakuten.ecommerce.web.entities.ProductForWeb;
import com.rakuten.ecommerce.web.entities.ProductFromWeb;
/**
 * @author Kshitiz Garg
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private CurrencyConvertor currencyConvertor;
	
	@Override
	@Transactional
	public ProductForWeb getProductWithCategories(long productId, String desiredCurrency) throws DataNotFoundException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		logger.info("Fetching product by Id: {}", productId);
		Product product = productDao.getProduct(productId);
		ProductForWeb productForWeb = new ProductForWeb();
		BeanUtils.copyProperties(product, productForWeb);
		Set<CategoryForWeb> categories ;
		if(!CollectionUtils.isEmpty(product.getCatgeories())){
			categories = new HashSet<>();
			for(Category cd : product.getCatgeories()){
				CategoryForWeb c = new CategoryForWeb();
				BeanUtils.copyProperties(cd, c);
				categories.add(c);
			}
			productForWeb.setCatgeories(categories);
		}
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), desiredCurrency);
		productForWeb.setPriceInEuro(price.getPriceInEuro());
		productForWeb.setPriceInDesiredCurrency(price.getPriceInDesiredCurrency());
		return productForWeb;
	}

	@Override
	public long createProduct(ProductFromWeb productDetails) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException{
		long currentTimeMillis = System.currentTimeMillis();
		Product product = getProductEntity(productDetails);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EUR);
		product.setProductCurrency(CurrencyConvertor.EUR);
		product.setPrice(price.getPriceInEuro().toString());
		product.setCreatedAt(new Timestamp(currentTimeMillis));
		product.setLastModifiedAt(new Timestamp(currentTimeMillis));
		productDao.persist(product);
		List<String> categoryIds = productDetails.getCatgeoryIds();
		if(!CollectionUtils.isEmpty(categoryIds)){
			for(String categoryId: categoryIds){
				long catgeory;
				try{
					catgeory = Long.parseLong(categoryId);
				}
				catch(Exception e){
					throw new InvalidClientRequestException("CategoryIds should be valid (if present)");
				}
				ProductCategory pc = new ProductCategory(product.getProductId(), catgeory);
				productCategoryDao.persist(pc);
			}
		}
		return product.getProductId();
	}

	private Product getProductEntity(ProductFromWeb productDetails) {
		Product product = new Product();
		BeanUtils.copyProperties(productDetails, product);
		return product;
	}

	@Override
	public void updateProduct(long productId, ProductFromWeb productFromWeb) throws DataNotFoundException {
		logger.info("Updating product with Id: {}", productId);
		Product product = validateExistence(productId);
		BeanUtils.copyProperties(productFromWeb, product);
		product.setLastModifiedAt(new Timestamp(System.currentTimeMillis()));
		productDao.merge(product);
		
	}

	@Override
	public void deleteProduct(long productId) throws DataNotFoundException {
		logger.info("Deleting product with Id: {}", productId);
		Product product = validateExistence(productId);
		productDao.remove(product);
	}

	private Product validateExistence(long productId) throws DataNotFoundException {
		Product product = productDao.getProduct(productId);
		if(product==null){
			throw new DataNotFoundException("No product exists against Id: "+ productId);
		}
		return product;
	}

	@Override
	public void patchProduct(long productId) throws DataNotFoundException {
		logger.info("Patching product with Id: {}", productId);
		Product product = validateExistence(productId);
		// TODO
	}

}