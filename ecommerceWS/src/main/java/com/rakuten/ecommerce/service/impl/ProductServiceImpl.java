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
import com.rakuten.ecommerce.web.entities.CategoryForWeb;
import com.rakuten.ecommerce.web.entities.ProductForWeb;
import com.rakuten.ecommerce.web.entities.ProductFromWeb;
import com.rakuten.ecommerce.web.entities.ProductsFromWeb;
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
		Product product = getProductEntity(productDetails);
		Price price = currencyConvertor.getPrice(product.getPrice(), product.getProductCurrency(), CurrencyConvertor.EUR);
		product.setProductCurrency(CurrencyConvertor.EUR);
		product.setPrice(price.getPriceInEuro().toString());
		long currentTimeMillis = System.currentTimeMillis();
		product.setCreatedAt(new Timestamp(currentTimeMillis));
		product.setLastModifiedAt(new Timestamp(currentTimeMillis));
		return productDao.persist(product).getProductId();
	}

	private Product getProductEntity(ProductFromWeb productDetails) {
		Product product = new Product();
		BeanUtils.copyProperties(productDetails, product);
		List<Long> categoryIds = productDetails.getCatgeoryIds();
		if(!CollectionUtils.isEmpty(categoryIds)){
			List<Category> categories = categoryDao.getBy(categoryIds);
			product.setCatgeories(new HashSet<Category>(categories));
		}
		return product;
	}

	@Override
	public void createProducts(ProductsFromWeb products) throws InvalidClientRequestException, ThirdPartyRequestFailedException, CurrencyNotSupportedException {
		for(ProductFromWeb p: products.getProducts()){
			createProduct(p);
		}
		// TODO: Batch error handling
	}

}