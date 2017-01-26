package com.rakuten.ecommerce.web.resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rakuten.ecommerce.service.ProductService;
import com.rakuten.ecommerce.service.exception.CurrencyNotSupportedException;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.service.exception.ThirdPartyRequestFailedException;
import com.rakuten.ecommerce.web.entities.ProductRequest;
import com.rakuten.ecommerce.web.entities.ProductResponse;

/**
 * @author Kshitiz Garg
 */
public class ProductResourceTest {
	 
	@InjectMocks
	private ProductResource productResource;
	
	@Mock 
	private ProductService productService;

	private ProductRequest productRequest;

	private static final long PRODUCT_ID_1 = 1;

	private static final String DESIRED_CURRENCY = "SGD";

	private ProductResponse productResponse;
	
	@Before
	public void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		productRequest = new ProductRequest();
		productResponse = new ProductResponse();
		Mockito.when(productService.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY)).thenReturn(productResponse);
	}
	
	@Test
	public void get(){
		ResponseEntity<?> response = productResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		Assert.assertEquals(productResponse, response.getBody());
	}

	@Test
	public void getWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = productResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void getWhenServiceFailsWithCNSException() throws Exception{
		whenServiceFailsWithCNSException();
		ResponseEntity<?> response = productResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void getWhenServiceFailsWithTPRFException() throws Exception{
		whenServiceFailsWithTPRFException();
		ResponseEntity<?> response = productResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCodeValue());
	}

	@Test
	public void getWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = productResource.get(PRODUCT_ID_1, DESIRED_CURRENCY);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}

	@Test
	public void put(){
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest );
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithInvalidClientRequestException() throws Exception{
		whenServiceFailsWithInvalidClientRequestException();
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest);
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}

	@Test
	public void putWhenServiceFailsWithCNSException() throws Exception{
		whenServiceFailsWithCNSException();
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest);
		Assert.assertEquals(HttpStatus.PRECONDITION_FAILED.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithTPRFException() throws Exception{
		whenServiceFailsWithTPRFException();
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest);
		Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = productResource.put(PRODUCT_ID_1, productRequest);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void delete(){
		ResponseEntity<?> response = productResource.delete(PRODUCT_ID_1);
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
	}

	@Test
	public void deleteWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = productResource.delete(PRODUCT_ID_1);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void deleteWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = productResource.delete(PRODUCT_ID_1);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	private void whenServiceFailsWithGenericException() throws Exception {
		IllegalArgumentException illegalArgumentException = new IllegalArgumentException("err....");
		Mockito.when(productService.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(illegalArgumentException);
		Mockito.when(productService.deleteProduct(PRODUCT_ID_1)).thenThrow(illegalArgumentException);
		Mockito.when(productService.updateProduct(PRODUCT_ID_1, productRequest)).thenThrow(illegalArgumentException);
	}

	private void whenServiceFailsWithInvalidClientRequestException() throws Exception {
		InvalidClientRequestException invalidClientRequestException = new InvalidClientRequestException("err....");
		Mockito.when(productService.updateProduct(PRODUCT_ID_1, productRequest)).thenThrow(invalidClientRequestException);
	}
	
	private void whenServiceFailsWithDNFException() throws Exception {
		DataNotFoundException dnfException = new DataNotFoundException("err....");
		Mockito.when(productService.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(dnfException);
		Mockito.when(productService.updateProduct(PRODUCT_ID_1, productRequest)).thenThrow(dnfException);
		Mockito.when(productService.deleteProduct(PRODUCT_ID_1)).thenThrow(dnfException);
	}

	private void whenServiceFailsWithCNSException() throws Exception {
		CurrencyNotSupportedException cnsException = new CurrencyNotSupportedException("err....");
		Mockito.when(productService.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(cnsException);
		Mockito.when(productService.updateProduct(PRODUCT_ID_1, productRequest)).thenThrow(cnsException);
	}
	
	private void whenServiceFailsWithTPRFException() throws Exception {
		ThirdPartyRequestFailedException tprfException = new ThirdPartyRequestFailedException("err....", HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(productService.getProductWithCategories(PRODUCT_ID_1, DESIRED_CURRENCY)).thenThrow(tprfException);
		Mockito.when(productService.updateProduct(PRODUCT_ID_1, productRequest)).thenThrow(tprfException);
	}

}