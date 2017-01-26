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

import com.rakuten.ecommerce.service.CategoryService;
import com.rakuten.ecommerce.service.exception.DataNotFoundException;
import com.rakuten.ecommerce.service.exception.InvalidClientRequestException;
import com.rakuten.ecommerce.web.entities.CategoryRequest;
import com.rakuten.ecommerce.web.entities.CategoryResponse;

/**
 * @author Kshitiz Garg
 */
public class CategoryResourceTest {
	 
	@InjectMocks
	private CategoryResource categoryResource;
	
	@Mock 
	private CategoryService categoryService;

	private CategoryRequest categoryRequest;

	private static final long CATEGORY_ID_1 = 1;

	private CategoryResponse categoryResponse;
	
	@Before
	public void setup() throws Exception{
		MockitoAnnotations.initMocks(this);
		categoryRequest = new CategoryRequest();
		categoryResponse = new CategoryResponse();
		Mockito.when(categoryService.getCategory(CATEGORY_ID_1)).thenReturn(categoryResponse);
	}
	
	@Test
	public void get(){
		ResponseEntity<?> response = categoryResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		Assert.assertEquals(categoryResponse, response.getBody());
	}

	@Test
	public void getWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = categoryResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void getWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = categoryResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}

	@Test
	public void put(){
		ResponseEntity<?> response = categoryResource.put(CATEGORY_ID_1, categoryRequest );
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithInvalidClientRequestException() throws Exception{
		whenServiceFailsWithInvalidClientRequestException();
		ResponseEntity<?> response = categoryResource.put(CATEGORY_ID_1, categoryRequest);
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = categoryResource.put(CATEGORY_ID_1, categoryRequest);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void putWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = categoryResource.put(CATEGORY_ID_1, categoryRequest);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void delete(){
		ResponseEntity<?> response = categoryResource.delete(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCodeValue());
	}

	@Test
	public void deleteWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = categoryResource.delete(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
	}
	
	@Test
	public void deleteWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = categoryResource.delete(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	private void whenServiceFailsWithGenericException() throws Exception {
		IllegalArgumentException illegalArgumentException = new IllegalArgumentException("err....");
		Mockito.when(categoryService.getCategory(CATEGORY_ID_1)).thenThrow(illegalArgumentException);
		Mockito.when(categoryService.deleteCategory(CATEGORY_ID_1)).thenThrow(illegalArgumentException);
		Mockito.when(categoryService.updateCategory(CATEGORY_ID_1, categoryRequest)).thenThrow(illegalArgumentException);
	}

	private void whenServiceFailsWithInvalidClientRequestException() throws Exception {
		InvalidClientRequestException invalidClientRequestException = new InvalidClientRequestException("err....");
		Mockito.when(categoryService.updateCategory(CATEGORY_ID_1, categoryRequest)).thenThrow(invalidClientRequestException);
	}
	
	private void whenServiceFailsWithDNFException() throws Exception {
		DataNotFoundException dnfException = new DataNotFoundException("err....");
		Mockito.when(categoryService.getCategory(CATEGORY_ID_1)).thenThrow(dnfException);
		Mockito.when(categoryService.updateCategory(CATEGORY_ID_1, categoryRequest)).thenThrow(dnfException);
		Mockito.when(categoryService.deleteCategory(CATEGORY_ID_1)).thenThrow(dnfException);
	}

}
