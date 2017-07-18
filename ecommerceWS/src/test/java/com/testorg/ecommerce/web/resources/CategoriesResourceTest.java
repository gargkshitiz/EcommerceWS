package com.testorg.ecommerce.web.resources;

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

import com.testorg.ecommerce.service.CategoryService;
import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.web.entities.BulkCategoryResponse;
import com.testorg.ecommerce.web.entities.CategoryRequest;

/**
 * @author Kshitiz Garg
 */
public class CategoriesResourceTest {
	 
	@InjectMocks
	private CategoriesResource categoriesResource;
	
	@Mock
	private UriComponentsBuilder uriBuilder;
	
	@Mock 
	private UriComponents uriComponents;
	
	@Mock 
	private CategoryService categoryService;

	private CategoryRequest categoryRequest;

	private static final long CATEGORY_ID_1 = 1;

	private BulkCategoryResponse bulkProductResponse;

	private URI uri ;
	
	@Before
	public void setup() throws Exception{
		uri = new URI("/test/products");
		MockitoAnnotations.initMocks(this);
		categoryRequest = new CategoryRequest();
		bulkProductResponse = new BulkCategoryResponse();
		Mockito.when(categoryService.getCategories(CATEGORY_ID_1)).thenReturn(bulkProductResponse);
		Mockito.when(categoryService.createCategory(categoryRequest)).thenReturn(CATEGORY_ID_1);
		Mockito.when(uriBuilder.path(CategoriesResource.LOCATION)).thenReturn(uriBuilder);
		Mockito.when(uriBuilder.buildAndExpand(CATEGORY_ID_1)).thenReturn(uriComponents);
		Mockito.when(uriComponents.toUri()).thenReturn(uri);
	}
	
	@Test
	public void get(){
		ResponseEntity<?> response = categoriesResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		Assert.assertEquals(bulkProductResponse, response.getBody());
	}

	@Test
	public void getWhenServiceFailsWithDNFException() throws Exception{
		whenServiceFailsWithDNFException();
		ResponseEntity<?> response = categoriesResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		BulkCategoryResponse body = (BulkCategoryResponse) response.getBody();
		Assert.assertEquals(false, body.isHasMore());
		Assert.assertEquals(0, body.getCount());
		Assert.assertEquals(-1, body.getNextCategoryId());
		Assert.assertNull(body.getItems());
	}
	
	@Test
	public void getWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = categoriesResource.get(CATEGORY_ID_1);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}

	@Test
	public void create(){
		ResponseEntity<?> response = categoriesResource.create(uriBuilder, categoryRequest );
		Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
	}

	@Test
	public void createWhenServiceFailsWithGenericException() throws Exception{
		whenServiceFailsWithGenericException();
		ResponseEntity<?> response = categoriesResource.create(uriBuilder, categoryRequest);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
	}
	
	private void whenServiceFailsWithGenericException() throws Exception {
		IllegalArgumentException illegalArgumentException = new IllegalArgumentException("err....");
		Mockito.when(categoryService.getCategories(CATEGORY_ID_1)).thenThrow(illegalArgumentException);
		Mockito.when(categoryService.createCategory(categoryRequest)).thenThrow(illegalArgumentException);
	}

	private void whenServiceFailsWithDNFException() throws Exception {
		DataNotFoundException dnfException = new DataNotFoundException("err....");
		Mockito.when(categoryService.getCategories(CATEGORY_ID_1)).thenThrow(dnfException);
	}

}