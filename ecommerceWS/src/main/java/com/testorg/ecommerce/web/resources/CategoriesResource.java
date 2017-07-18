package com.testorg.ecommerce.web.resources;
 
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.testorg.ecommerce.service.CategoryService;
import com.testorg.ecommerce.service.exception.DataNotFoundException;
import com.testorg.ecommerce.service.exception.InvalidClientRequestException;
import com.testorg.ecommerce.web.entities.BulkCategoryResponse;
import com.testorg.ecommerce.web.entities.CategoryRequest;
import com.testorg.ecommerce.web.swagger.ApiDocumentationConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * @author Kshitiz Garg
 */
@Api(value = ApiDocumentationConstants.CATEGORIES)
@RequestMapping(ApiDocumentationConstants.CATEGORIES_API)
@RestController
public class CategoriesResource {
 
	private static final Logger logger = LoggerFactory.getLogger(CategoriesResource.class);

	@Autowired
	private CategoryService categoryService;
	
	static final String LOCATION = ApiDocumentationConstants.CATEGORY_API + "/{id}";
	
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORIES_POST , httpMethod = ApiDocumentationConstants.POST, notes = ApiDocumentationConstants.CATEGORIES_POST_NOTES)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes= MediaType.APPLICATION_JSON)
	@ResponseStatus
	@ApiResponses(value = {
        @ApiResponse(code = 201, message = "Category created successfully"),
        @ApiResponse(code = 400, message = "Bad request")
	})
    public @ResponseBody ResponseEntity<String> create(UriComponentsBuilder uriBuilder, @RequestBody @ApiParam CategoryRequest categoryRequest){
		try {
			long categoryId = categoryService.createCategory(categoryRequest);
			UriComponents uriComponents = uriBuilder.path(LOCATION).buildAndExpand(categoryId);
			return ResponseEntity.created(uriComponents.toUri()).build();
		}
		catch (Exception e) {
			logger.error("Error creating category for details:{} ",categoryRequest, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@ResponseStatus
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Categories fetched successfully"),
		@ApiResponse(code = 400, message = "Bad request")
	})
	@ApiOperation(value =  ApiDocumentationConstants.CATEGORIES_GET , httpMethod = ApiDocumentationConstants.GET, notes = ApiDocumentationConstants.CATEGORIES_GET_NOTES)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<?> get(@RequestParam(value = "startingCategoryId", required=false) long startingCategoryId){
		try {
			return new ResponseEntity<>(categoryService.getCategories(startingCategoryId), noCacheHeaders(), HttpStatus.OK);
		}
		catch (DataNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(noCategories(), noCacheHeaders(), HttpStatus.OK);
		} 
		catch (InvalidClientRequestException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), e.getHttpStatusCode());
		} 
		catch (Exception e) {
			logger.error("Error fetching catgeories starting with id: {}", startingCategoryId, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	private BulkCategoryResponse noCategories() {
		BulkCategoryResponse bulkCategoryResponse = new BulkCategoryResponse();
		bulkCategoryResponse.setHasMore(false);
		return bulkCategoryResponse;
	}

	private HttpHeaders noCacheHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(ApiDocumentationConstants.EXPIRES_PARAM, ApiDocumentationConstants.EXPIRES_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.CACHE_CONTROL_PARAM, ApiDocumentationConstants.CACHE_CONTROL_PARAM_VALUE);
		headers.add(ApiDocumentationConstants.PRAGMA_PARAM, ApiDocumentationConstants.PRAGMA_PARAM_VALUE);
		return headers;
	}

}