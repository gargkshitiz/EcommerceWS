package com.rakuten.ecommerce.web.resources;

import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.BulkProductResponse;
import com.rakuten.ecommerce.web.entities.ProductRequest;

/**
 * @author Kshitiz Garg
 */
public class ProductsResourceTest {
	 
	@InjectMocks
	private ProductsResource productsResource;
	
	@Mock
	private UriComponentsBuilder uriBuilder;
	
	@Mock 
	private UriComponents uriComponents;
	
	@Mock 
	private ProductService productService;

	private ProductRequest productRequest;

	private static final long PRODUCT_ID_1 = 1;

	private static final String DESIRED_CURRENCY = "SGD";

	private BulkProductResponse bulkProductResponse;

	private URI uri ;
	
	@Before
	public void setup() throws Exception{
		uri = new URI("/test/products");
		MockitoAnnotations.initMocks(this);
		productRequest = new ProductRequest();
		bulkProductResponse = new BulkProductResponse();
		Mockito.when(productService.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY)).thenReturn(bulkProductResponse);
		Mockito.when(productService.createProduct(productRequest)).thenReturn(PRODUCT_ID_1);
		Mockito.when(uriBuilder.path(ProductsResource.LOCATION)).thenReturn(uriBuilder);
		Mockito.when(uriBuilder.buildAndExpand(PRODUCT_ID_1)).thenReturn(uriComponents);
		Mockito.when(uriComponents.toUri()).thenReturn(uri);
	}
	
	@Test
	public void get(){
		ResponseEntity<?> response = productsResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		Assert.assertEquals(bulkProductResponse, response.getBody());
	}

	@Test
	public void getWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = productsResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		BulkProductResponse body = (BulkProductResponse) response.getBody();
		Assert.assertEquals(false, body.isHasMore());
		Assert.assertEquals(0, body.getCount());
		Assert.assertEquals(-1, body.getNextProductId());
		Assert.assertNull(body.getItems());
	}
	
	@Test
	public void getWhenServiceFailsWithCNSException() throws Exception{
		whenServiceFailsWithCNSException();
		ResponseEntity<?> response = productsResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void getWhenServiceFailsWithTPRFException() throws Exception{
		whenServiceFailsWithTPRFException();
		ResponseEntity<?> response = productsResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCodeValue());
	}

	@Test
	public void getWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = productsResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}

	@Test
	public void create(){
		ResponseEntity<?> response = productsResource.create(uriBuilder, productRequest );
		Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createWhenServiceFailsWithInvalidClientRequestException() throws Exception{
		whenServiceFailsWithInvalidClientRequestException();
		ResponseEntity<?> response = productsResource.create(uriBuilder, productRequest);
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createWhenServiceFailsWithCNSException() throws Exception{
		whenServiceFailsWithCNSException();
		ResponseEntity<?> response = productsResource.create(uriBuilder, productRequest);
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createWhenServiceFailsWithTPRFException() throws Exception{
		whenServiceFailsWithTPRFException();
		ResponseEntity<?> response = productsResource.create(uriBuilder, productRequest);
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void createWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = productsResource.create(uriBuilder, productRequest);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	private void whenServiceFailsWithGenericException() throws Exception {
		IllegalArgumentException illegalArgumentException = new IllegalArgumentException("err....");
		Mockito.when(productService.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(illegalArgumentException);
		Mockito.when(productService.createProduct(productRequest)).thenThrow(illegalArgumentException);
	}

	private void whenServiceFailsWithInvalidClientRequestException() throws Exception {
		InvalidClientRequestException invalidClientRequestException = new InvalidClientRequestException("err....");
		Mockito.when(productService.createProduct(productRequest)).thenThrow(invalidClientRequestException);
	}
	
	private void whenServiceFailsWithDNFException() throws Exception {
		DataNotFoundException dnfException = new DataNotFoundException("err....");
		Mockito.when(productService.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(dnfException);
	}

	private void whenServiceFailsWithCNSException() throws Exception {
		CurrencyNotSupportedException cnsException = new CurrencyNotSupportedException("err....");
		Mockito.when(productService.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(cnsException);
		Mockito.when(productService.createProduct(productRequest)).thenThrow(cnsException);
	}
	
	private void whenServiceFailsWithTPRFException() throws Exception {
		ThirdPartyRequestFailedException tprfException = new ThirdPartyRequestFailedException("err....", HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(productService.getProducts(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(tprfException);
		Mockito.when(productService.createProduct(productRequest)).thenThrow(tprfException);
	}

}